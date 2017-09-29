package bd.com.madmind.rentmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bd.com.madmind.rentmaster.adapters.HomeAdapter;
import bd.com.madmind.rentmaster.models.MenuData;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<MenuData> list;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    private long Counter = 0;


    private int images [] = {R.drawable.home ,R.drawable.tenants, R.drawable.rent_receive,
            R.drawable.payment , R.drawable.alert ,R.drawable.report, R.drawable.exit};
    private String title[] = {"Properties","Tenants","Rent Receive","Payments","Alerts","Report","Exit"};
    private int [] id = {1,2,3,4,5,6,7,8,9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        toolbar = (Toolbar) findViewById(R.id.homeToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");


        //Log.d("ggggg" , getNcounter()+"");

        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.homeRecyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext() , 2));

        for(int i = 0 ; i < images.length ; i++){
            list.add(new MenuData(id[i], images[i] , title[i]));

        }

        getNcounter();


    }

    public void getNcounter(){

        database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("alert").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                recyclerView.setAdapter(new HomeAdapter(list , getApplicationContext() , dataSnapshot.getChildrenCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.signout:
                signout();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void signout(){
        auth.signOut();
        startActivity(new Intent(HomeActivity.this , LogInActivity.class));
        finish();
    }


}
