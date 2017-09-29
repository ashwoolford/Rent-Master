package bd.com.madmind.rentmaster.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import bd.com.madmind.rentmaster.ManagementActivity;
import bd.com.madmind.rentmaster.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Report extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;

    private Spinner spinner;
    private Button submitBtn;
    private String msg = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report, container, false);


        spinner = (Spinner) v.findViewById(R.id.spinnerReport);
        submitBtn = (Button) v.findViewById(R.id.totalIncomebtn);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        list = new ArrayList<>();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);

        handlingButtonClick();


        return v;
    }



    public void handlingButtonClick(){
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
                ((TextView) spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.green));
                msg = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = spinner.getSelectedItem().toString().trim();
                if(!item.isEmpty()){
                    startActivity(new Intent(getActivity() , ManagementActivity.class).putExtra("project" ,item));
                }
                else Toast.makeText(getActivity() , "Didn't found any project !!" , Toast.LENGTH_SHORT).show();




            }
        });



    }

}
