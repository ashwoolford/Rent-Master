package bd.com.madmind.rentmaster;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bd.com.madmind.rentmaster.fragments.AlertDetails;
import bd.com.madmind.rentmaster.fragments.PaymentFragment;
import bd.com.madmind.rentmaster.fragments.PropertiesFragment;
import bd.com.madmind.rentmaster.fragments.RentReceiveFragment;
import bd.com.madmind.rentmaster.fragments.Report;
import bd.com.madmind.rentmaster.fragments.TenantsListFragment;

public class DetailsActivity extends AppCompatActivity {

    private String title;
    private int id;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = getIntent().getStringExtra("title");
        id = getIntent().getIntExtra("id" , -1);
        fragmentManager = getFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference();


        list = new ArrayList<>();

        adapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, list);

        switch (id){
            case 1:
                fragmentManager.beginTransaction().replace(R.id.container, new PropertiesFragment()).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.container , new TenantsListFragment()).commit();
                break;
            case 3:
               //fragmentManager.beginTransaction().replace(R.id.container , new RentReceiveFragment()).commit();
                //startActivity(new Intent(getApplicationContext() , RentReceiveActv.class));
                showDialog(DetailsActivity.this , "");
                break;
            case 4:
                fragmentManager.beginTransaction().replace(R.id.container , new PaymentFragment()).commit();
                break;
            case 5:
                fragmentManager.beginTransaction().replace(R.id.container , new AlertDetails()).commit();
                break;
            case 6:
                fragmentManager.beginTransaction().replace(R.id.container , new Report()).commit();
                break;
            case 7:

                finish();
//                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//                homeIntent.addCategory( Intent.CATEGORY_HOME );
//                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(homeIntent);
                System.exit(0);
                break;
            default:


        }



    }



        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_layout);

            final Spinner spinner = (Spinner) dialog.findViewById(R.id.rentReceiveSpinner);

            DatabaseReference ref = database.getReference().child("users").child(auth.getCurrentUser().getUid())
                    .child("projects");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        list.add(ds.getKey());
                        spinner.setAdapter(adapter);
                        Log.d("key" , ds.getKey());
                        //  notifyAll();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.black));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });




            Button button = (Button) dialog.findViewById(R.id.processBtn);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String item = spinner.getSelectedItem().toString().trim();

                    if(!item.isEmpty())  startActivity(new Intent(getApplicationContext() , RentReceiveActv.class)
                            .putExtra("title" , item));
                    else Toast.makeText(getApplicationContext() , "Didn't found any project" , Toast.LENGTH_SHORT).show();



                    finish();

                }
            });

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);
            dialog.setCancelable(true);

            dialog.show();
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });


        }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}
