package bd.com.madmind.rentmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dx.dxloadingbutton.lib.LoadingButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import bd.com.madmind.rentmaster.adapters.FlatAdapter;
import bd.com.madmind.rentmaster.fragments.AlertDetails;
import bd.com.madmind.rentmaster.fragments.PropertiesFragment;
import bd.com.madmind.rentmaster.models.GetAletsData;
import bd.com.madmind.rentmaster.models.GetFlatsData;
import bd.com.madmind.rentmaster.models.GetRentReceiveData;
import bd.com.madmind.rentmaster.models.PropertiesData;

public class FlatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FloatingActionButton fab;
    private String key;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private Toolbar toolbar;
    private ArrayList<GetFlatsData> list;
    private ArrayAdapter<String> adapter;
    private ProgressDialog progressDialog;
    DatabaseReference reference1;
    ArrayList<String> spinnerList;
    private boolean isEmpty = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat);


        key = getIntent().getStringExtra("key");
        progressDialog = new ProgressDialog(FlatActivity.this);
        progressDialog.setTitle("Loading ...");
        progressDialog.show();





        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
       // databaseReference = database.getReference();

        reference1 = database.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid())
                .child("projects").child(key);

        spinner = (Spinner) findViewById(R.id.spinnerAF);



        list = new ArrayList<>();
        spinnerList = new ArrayList<>();
        spinnerList.add("All flats");
        spinnerList.add("Available flats");

        adapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_spinner_item, spinnerList);



        //listData = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.AFtoolbar);
        //spinner = (Spinner) findViewById(R.id.spinnerxx);

        spinner.setAdapter(adapter);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Flat list of "+key);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.baaaaal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.getRecycledViewPool().clear();




        reference1.child("flats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) {
                    isEmpty = true;
                    Log.d("zero" , "greater than 1");
                }
                else {
                    progressDialog.dismiss();
                    Log.d("zero" , "less than 1");
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

                switch (position){
                    case 0:
                        if(isEmpty) recyclerAdapter(reference1.child("flats"));
                        else progressDialog.dismiss();
                        break;
                    case 1:
                        if (isEmpty) recyclerAdapter(reference1.child("av_flats"));
                        else progressDialog.dismiss();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        fab = (FloatingActionButton) findViewById(R.id.AFfab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlatActivity.this , EditFlatActivity.class)
                .putExtra("key" , key));
            }
        });




    }



    public void recyclerAdapter(DatabaseReference reference) {

        final FirebaseRecyclerAdapter<GetFlatsData, FlatActivity.PostViewHolder> fra =
                new FirebaseRecyclerAdapter<GetFlatsData, FlatActivity.PostViewHolder>(
                GetFlatsData.class,
                R.layout.flat_list_layout,
                FlatActivity.PostViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(FlatActivity.PostViewHolder viewHolder, GetFlatsData model, final int position) {
                if(viewHolder !=null) {
                    progressDialog.dismiss();

                    viewHolder.setTitle(model.getTitle());


                    //Log.d("nnnnn" , model.getMsg());
                }

            }
        };


        recyclerView.setAdapter(fra);



    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title) {
           TextView textView = (TextView) mView.findViewById(R.id.ftxx);
            textView.setText(title);
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
