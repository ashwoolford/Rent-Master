package bd.com.madmind.rentmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

public class TenantsDetailsActivity extends AppCompatActivity {

    private static final int GALLERYINTENT = 100;
    private Spinner pSpinner;
    private Spinner fSpinner;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private ArrayList<String> list;
    private ArrayList<String> list1;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;
    private EditText name , address , phone , telephone , email , nid;
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private String uid;
    private ImageView upload , showImage;
    private Uri uri;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenants_details);

        dialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        uid = auth.getCurrentUser().getUid();

        upload = (ImageView) findViewById(R.id.atImageUpload);
        showImage = (ImageView) findViewById(R.id.atImageView);

        pSpinner = (Spinner) findViewById(R.id.atSp1);
        fSpinner = (Spinner) findViewById(R.id.atSp2);

        name = (EditText) findViewById(R.id.atName);
        address = (EditText) findViewById(R.id.atAddress);
        phone = (EditText) findViewById(R.id.atPhone);
        telephone = (EditText) findViewById(R.id.atPhone);
        email = (EditText) findViewById(R.id.atEmail);
        nid = (EditText) findViewById(R.id.atNID);
        toolbar = (Toolbar) findViewById(R.id.atToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a tenants");

//        List<String> spinnerArray =  new ArrayList<String>();
//        spinnerArray.add("item1");
//        spinnerArray.add("item2");

        list = new ArrayList<>();
        list1 = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list);
        adapter1 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, list1);


//        list.add("X");
//        list.add("Y");
//        list.add("Z");



        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        DatabaseReference ref = databaseReference.child("users").child(uid).child("projects");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    list.add(ds.getKey());
                    pSpinner.setAdapter(adapter);
                    Log.d("key" , ds.getKey());
                  //  notifyAll();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //handle click event of the upload button

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*") , GALLERYINTENT);
            }
        });



        //adapter.notifyDataSetChanged();

        pSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list1.clear();
                Toast.makeText(getApplicationContext() , ""+pSpinner.getSelectedItem() , Toast.LENGTH_SHORT).show();
                DatabaseReference ref = database.getReference().child("users").child(uid).child("projects").child(""+pSpinner.getSelectedItem()).child("av_flats");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            list1.add(ds.getKey());
                            fSpinner.setAdapter(adapter1);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        insertData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tenantsmenu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.atmAdd:
                insertData();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERYINTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri = result.getUri();
                showImage.setImageURI(uri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    public void insertData(){
        dialog.setTitle("Uploading...");
        dialog.show();

        storage.getReference().child("profileimages").child(uri.getLastPathSegment()).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        String flat  = fSpinner.getSelectedItem().toString();
                        String projects = pSpinner.getSelectedItem().toString();
                        DatabaseReference tenantsRef = databaseReference.child("users").child(uid).child("tenants").push();
                        tenantsRef.child("name").setValue(name.getText().toString().trim());
                        tenantsRef.child("address").setValue(address.getText().toString().trim());
                        tenantsRef.child("phone").setValue(phone.getText().toString().trim());
                        tenantsRef.child("telephone").setValue(telephone.getText().toString().trim());
                        tenantsRef.child("email").setValue(email.getText().toString().trim());
                        tenantsRef.child("nid").setValue(nid.getText().toString().trim());
                        tenantsRef.child("project").setValue(projects);
                        tenantsRef.child("flat").setValue(flat);
                        tenantsRef.child("profileuri").setValue(taskSnapshot.getDownloadUrl().toString());

                        DatabaseReference delRef = databaseReference.child("users").child(uid).child("projects").child(projects)
                                .child("av_flats").child(flat);
                        delRef.removeValue();

                        dialog.dismiss();
                        finish();


                    }
                });



    }
}
