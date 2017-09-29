package bd.com.madmind.rentmaster.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bd.com.madmind.rentmaster.R;

import static bd.com.madmind.rentmaster.R.id.fpMaintCost;

public class PaymentFragment extends Fragment {

    private EditText rent , water , gas , maint , materials , elec , others;
    private Button add , sum;
    int billWater , billGas , billMaint , billMaterial , totalBills , electricityB , othersB ;
    private TextView sumTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Spinner spinner ;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database  = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());

        progressDialog = new ProgressDialog(getActivity());

        spinner = (Spinner) view.findViewById(R.id.fpSpinner);

        water = (EditText) view.findViewById(R.id.fpWater);
        gas = (EditText) view.findViewById(R.id.fpMaintGas);
        maint = (EditText) view.findViewById(fpMaintCost);
        materials = (EditText) view.findViewById(R.id.fpMaintMaterial);
        elec = (EditText) view.findViewById(R.id.fpElectricity);
        others = (EditText) view.findViewById(R.id.fpOthers);

        sumTextView = (TextView) view.findViewById(R.id.fpSumView);

        add = (Button) view.findViewById(R.id.fpAddBtn);
        sum = (Button) view.findViewById(R.id.fpSumBtn);


        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, list);


        DatabaseReference ref = database.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid())
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










        add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!water.getText().toString().isEmpty() && !gas.getText().toString().isEmpty()
                            && !maint.getText().toString().isEmpty() && !materials.getText().toString().isEmpty()
                            && !elec.getText().toString().isEmpty() && !elec.getText().toString().isEmpty()){
                        billGas = Integer.parseInt(gas.getText().toString().trim());
                        billWater = Integer.parseInt(water.getText().toString().trim());
                        billMaint = Integer.parseInt(maint.getText().toString().trim());
                        billMaterial = Integer.parseInt(materials.getText().toString().trim());
                        electricityB = Integer.parseInt(elec.getText().toString().trim());
                        othersB = Integer.parseInt(others.getText().toString().trim());

                        addData();
                    }

                    else Toast.makeText(getActivity() , "Input valid value" , Toast.LENGTH_SHORT).show();




                }
            });

            sum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!water.getText().toString().isEmpty() && !gas.getText().toString().isEmpty()
                            && !maint.getText().toString().isEmpty() && !materials.getText().toString().isEmpty()
                            && !elec.getText().toString().isEmpty() && !elec.getText().toString().isEmpty()){
                        billGas = Integer.parseInt(gas.getText().toString().trim());
                        billWater = Integer.parseInt(water.getText().toString().trim());
                        billMaint = Integer.parseInt(maint.getText().toString().trim());
                        billMaterial = Integer.parseInt(materials.getText().toString().trim());
                        electricityB = Integer.parseInt(elec.getText().toString().trim());
                        othersB = Integer.parseInt(others.getText().toString().trim());

                        totalBills = billGas+billMaterial+billMaint+billWater+electricityB+othersB;
                        sumTextView.setText(totalBills+"");
                    }


                    else Toast.makeText(getActivity() , "Input valid value" , Toast.LENGTH_SHORT).show();



                }
            });










        return view;
    }

    public void addData(){

        Calendar calendar = Calendar.getInstance();
        String date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1);

        DatabaseReference ref = databaseReference.child("payments").child(date).child(spinner.getSelectedItem().toString());
        ref.child("total").setValue(totalBills+"");
        ref.child("date").setValue(date);

        getActivity().onBackPressed();
    }



}
