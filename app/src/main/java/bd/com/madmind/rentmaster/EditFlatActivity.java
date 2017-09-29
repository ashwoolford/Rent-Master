package bd.com.madmind.rentmaster;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditFlatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String uid;
    private EditText title , rent;
    String getKey;
    Toolbar toolbar;

    Calendar calendar = Calendar.getInstance();
    String date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flat);

        toolbar = (Toolbar) findViewById(R.id.EFtoolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add a flat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //inti firebase instance variables

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        getKey = intent.getStringExtra("key");


        title = (EditText) findViewById(R.id.EFtitle);
        rent = (EditText) findViewById(R.id.EFrent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_flat , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.done:
                saveData();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void saveData(){

        String title1 = title.getText().toString().trim();
        String rent1 = rent.getText().toString().trim();

        if(!title1.isEmpty() && !rent1.isEmpty()){


            databaseReference = database.getReference().child("users").child(uid).child("projects")
                    .child(getKey).child("flats").child(title1);
            databaseReference.child("due").setValue("00");
            databaseReference.child("rent").setValue(rent1);
            databaseReference.child("title").setValue(title1);
            databaseReference.child("months").child(date).child("status").setValue("false");
            databaseReference = database.getReference().child("users").child(uid).child("projects").child(getKey).child("av_flats");
            databaseReference.child(title1).child("title").setValue(title1);



        }
    }
}
