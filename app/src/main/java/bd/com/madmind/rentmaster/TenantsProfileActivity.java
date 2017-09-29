package bd.com.madmind.rentmaster;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import bd.com.madmind.rentmaster.models.GetTenantData;

public class TenantsProfileActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String uid , key;
    private ImageView profileImg;
    private Toolbar toolbar;
    String nameXX , imageUriXX;
    private TextView name , email , phone , flat , project , address , nid;

    private int DATE_PICKER = 00;
    private int month_x , year_x , day_x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenants_profile);

        profileImg = (ImageView) findViewById(R.id.atpProfileImage);

        toolbar = (Toolbar) findViewById(R.id.atptoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile View");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.atpName);
        email = (TextView) findViewById(R.id.atpEmail);
        phone = (TextView) findViewById(R.id.atpPhone);
        flat = (TextView) findViewById(R.id.atpFlatNo);
        project = (TextView) findViewById(R.id.atpProject);
        address = (TextView) findViewById(R.id.atpAddress);
        nid = (TextView) findViewById(R.id.atpNid);
        //init firebase instance variables
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        key = getIntent().getStringExtra("key");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("users").child(uid).child("tenants").child(key);


        //calender object

        Calendar calendar = Calendar.getInstance();
        day_x = calendar.get(Calendar.DAY_OF_MONTH);
        month_x = calendar.get(Calendar.MONTH);
        year_x = calendar.get(Calendar.YEAR
        );



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GetTenantData data = dataSnapshot.getValue(GetTenantData.class);
                name.setText(data.getName());
                nameXX = data.getName();
                email.setText(data.getEmail());
                phone.setText(data.getPhone());
                flat.setText(data.getFlat());
                project.setText(data.getProject());
                address.setText(data.getAddress());
                nid.setText(data.getNid());
                imageUriXX = data.getProfileuri();
                Glide.with(getApplicationContext()).load(data.getProfileuri()).into(profileImg);
                    //Log.d("data" , data.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.tenant_profile_view , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.leaveFlat:

                showDialog(DATE_PICKER);

            break;
        }


        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    protected Dialog onCreateDialog(int id) {
            if(id == DATE_PICKER)
                return new DatePickerDialog(this , datePicker , year_x , month_x , day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            year_x = year;
            month_x = month;
            day_x = dayOfMonth;

            String d_m_y = day_x+"/"+(month_x+1)+"/"+year_x;

            Toast.makeText(getApplicationContext() , "Successfully set to "+d_m_y, Toast.LENGTH_SHORT).show();

            DatabaseReference reference = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("alert")
                    .push();
            reference.child("period").setValue(d_m_y);
            reference.child("msg").setValue(nameXX);
            reference.child("uri").setValue(imageUriXX);

        }
    };
}
