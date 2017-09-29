package bd.com.madmind.rentmaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bd.com.madmind.rentmaster.models.GetFlatsData;
import bd.com.madmind.rentmaster.models.ManagementData;

public class ManagementActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private CoordinatorLayout layout;
    private  DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference totalExpenseRef ;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private int counter = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        progressDialog = new ProgressDialog(ManagementActivity.this);
        progressDialog.setTitle("Loading ...");
        progressDialog.show();


        String projectName = getIntent().getStringExtra("project");


        auth = FirebaseAuth.getInstance();
        recyclerView = (RecyclerView) findViewById(R.id.amRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        layout = (CoordinatorLayout) findViewById(R.id.linearLayout);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Income overview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        totalExpenseRef = ref.child("users").child(auth.getCurrentUser().getUid().toString())
                .child("projects").child(projectName).child("flats");

        recyclerAdapter(totalExpenseRef);



        totalE();



    }


    public void recyclerAdapter(final DatabaseReference reference) {

        final FirebaseRecyclerAdapter<ManagementData, ManagementActivity.PostViewHolder> fra =
                new FirebaseRecyclerAdapter<ManagementData, ManagementActivity.PostViewHolder>(
                        ManagementData.class,
                        R.layout.management_recycler_layout,
                        ManagementActivity.PostViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(final ManagementActivity.PostViewHolder viewHolder, final ManagementData model, final int position) {
                        if(viewHolder !=null) {
                            progressDialog.dismiss();

                            viewHolder.setTitle(model.getTitle());


                            reference.child(model.getTitle()).child("months").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    long sum;
                                    int count = 0;

                                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                                        if(ds.child("status").getValue().toString().contains("true")){
                                            count++;

                                        }
                                    }

                                    long due = Long.parseLong(model.getDue());
                                    long rent = Long.parseLong(model.getRent());
                                    sum = (rent*count)-due;


                                    viewHolder.setIncomeTitle(sum+"");


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });






                           // Log.d("nnnnn" ,counter+"" );
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
            TextView textView = (TextView) mView.findViewById(R.id.projectTitle);
            textView.setText(title);
        }

        public void setIncomeTitle(String title) {
            TextView textView = (TextView) mView.findViewById(R.id.incomeView);
            textView.setText(title+" ৳");
        }
    }






    public void totalE(){

//        try {
//
//            totalExpenseRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    long sum = 0;
//
//
//                    for(DataSnapshot ds : dataSnapshot.getChildren()){
//                        long rent = Long.parseLong(ds.child("rent").getValue().toString());
//                        long totalM = 1;
//                        if(totalM == 0) totalM = 1;
//                        else totalM= ds.child("months").getChildrenCount();
//                        long due = Long.parseLong(ds.child("due").getValue().toString());
//
//
//                        long total = (rent*totalM)-due;
//
//                        sum = total+sum;
//
//                    }
//
//                    Snackbar snackbar = Snackbar.make(layout ,"Total income so far "+sum+" ৳" , Snackbar.LENGTH_INDEFINITE);
//                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                    snackbar.show();
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        totalExpenseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for (DataSnapshot ds1 : ds.child("months").getChildren()){
                        String status = ds1.child("status").getValue().toString();
                        Log.d("sum" ,status);
                        if(status.contains("true")) counter++;

                    }

                    long rent = Long.parseLong(ds.child("rent").getValue().toString());

                    long due = Long.parseLong(ds.child("due").getValue().toString());


                    long total = (rent*counter)-due;

                    sum = total+sum;



                    counter = 0;

                }

                Snackbar snackbar = Snackbar.make(layout ,"Total income so far "+sum+" ৳" , Snackbar.LENGTH_INDEFINITE);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



}
