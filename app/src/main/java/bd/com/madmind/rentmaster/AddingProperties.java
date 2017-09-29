package bd.com.madmind.rentmaster;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import bd.com.madmind.rentmaster.loopers.PhotoSizeReduceHelper;

public class AddingProperties extends AppCompatActivity {

    private static final int GALLERY_INTENT = 100;
    private Spinner sp1 ,sp2;
    private EditText title , address;
    private ImageView imageView;
    private FloatingActionButton fab;
    private FirebaseDatabase dataBase;
    private DatabaseReference ref;
    private StorageReference storage;
    private Button uploadBtn;
    private PhotoSizeReduceHelper photoSizeReduceHelper;
    private  Uri uri;
    private FirebaseAuth auth;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_properties);

        //init the firebase instance variables
        auth= FirebaseAuth.getInstance();

        dataBase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance().getReference();

        pDialog = new ProgressDialog(this);


        sp1 = (Spinner) findViewById(R.id.apSp1);
        sp2 = (Spinner) findViewById(R.id.apSp2);
        title = (EditText) findViewById(R.id.apTitleView);
        address = (EditText) findViewById(R.id.apAddressView);
        imageView = (ImageView) findViewById(R.id.header);
        fab = (FloatingActionButton) findViewById(R.id.apFab);
        uploadBtn = (Button) findViewById(R.id.apUploadButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent , GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == GALLERY_INTENT){
              uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                photoSizeReduceHelper = new PhotoSizeReduceHelper(AddingProperties.this ,this, imageView ,bitmap);
                photoSizeReduceHelper.execute();
                //imageView.setImageBitmap(photoSizeReduceHelper.saveToInternalStorage());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void insetPropertiesData(String imageUri){
        ref = dataBase.getReference().child("users").child(auth.getCurrentUser().getUid())
                .child("projects").child(title.getText().toString().trim());
        ref.child("title").setValue(title.getText().toString().trim());
        ref.child("address").setValue(address.getText().toString().trim());
        ref.child("propertiesType").setValue(sp1.getSelectedItem());
        ref.child("unitType").setValue(sp2.getSelectedItem());
        ref.child("url").setValue(imageUri);
    }

    public void getBitmap(final Bitmap bitmap1){
        Log.d("bitmap" , ""+bitmap1);
        imageView.setImageBitmap(bitmap1);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  pDialog.setTitle("Uploading...");
                  pDialog.show();
                  storage.child("images").child(uri.getLastPathSegment())
                          .putFile(getImageUri(bitmap1,uri.getLastPathSegment().toString()))
                          .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            insetPropertiesData(taskSnapshot.getDownloadUrl().toString());
                            finish();
                            pDialog.dismiss();
                        }
                    });
            }
        });
    }

    public Uri getImageUri(Bitmap inImage , String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage,title, null);
        return Uri.parse(path);
    }
}
