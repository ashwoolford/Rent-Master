package bd.com.madmind.rentmaster.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bd.com.madmind.rentmaster.R;
import bd.com.madmind.rentmaster.models.GetRentReceiveData;
import bd.com.madmind.rentmaster.models.RentReceiveData;

/**
 * A simple {@link Fragment} subclass.
 */
public class RentReceiveFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<String> list;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;
    private int bills = 0;
    private long total = 0;
    private ArrayList<Long> dueList;
    DatabaseReference paymentRef;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("assss" , "OnCreate");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rent_receive, container, false);





        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Wait a while...");
        progressDialog.show();

        spinner = (Spinner) view.findViewById(R.id.rentReceiveSpinner);


        list = new ArrayList<>();
        dueList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, list);

        //init the firebase instance variables

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


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


         recyclerView = (RecyclerView) view.findViewById(R.id.rrpRecylerView);
         recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                //list.clear();
                dueList.clear();


                String ss = spinner.getSelectedItem().toString();

                reference = database.getReference().child("users").child(auth.getCurrentUser().getUid())
                        .child("projects").child(ss).child("flats");

                paymentRef = database.getReference().child("users").child(auth.getCurrentUser().getUid())
                        .child("payments").child("2017-9").child(ss);

                recyclerAdapter();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        return view;
    }


    public void recyclerAdapter() {

        final FirebaseRecyclerAdapter<GetRentReceiveData, RentReceiveFragment.PostViewHolder> fra = new FirebaseRecyclerAdapter<GetRentReceiveData, RentReceiveFragment.PostViewHolder>(
                GetRentReceiveData.class,
                R.layout.rent_recv_adapter_layout,
                RentReceiveFragment.PostViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(final RentReceiveFragment.PostViewHolder viewHolder,
                                              final GetRentReceiveData model, final int position) {
                if(viewHolder !=null) {
                    progressDialog.dismiss();

                    final int rent = Integer.parseInt(model.getRent());
                    final int due = Integer.parseInt(model.getDue());

                    //Log.d("hhh" , ""+dueList);

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setRent(model.getRent());
                    viewHolder.setDue(model.getDue());

                    reference.child(model.getTitle()).child("months").child("2017-9").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                //Log.d("hhhhhh" ,  ds.getValue()+"");

                                if(ds.getValue().equals("true"))
                                    viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.green_s));
                                else
                                    viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.yellow_s));
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    paymentRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            bills = Integer.valueOf(""+dataSnapshot.child("total").getValue());
                            viewHolder.setBills(""+bills);

                            total = rent + bills + due;

                            viewHolder.setTotal(""+total);
                            dueList.add(total);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });



                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getActivity() , viewHolder.getText() , Toast.LENGTH_SHORT).show();

                    }
                });

                viewHolder.loadingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        viewHolder.loadingButton.startLoading();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity() , viewHolder.getText() , Toast.LENGTH_SHORT).show();

                                if(!(viewHolder.getText().toString().trim()).isEmpty()){
                                    long cTotal= Long.parseLong(viewHolder.getText().toString().trim());
                                    DatabaseReference mRef = reference.child(model.getTitle()).child("due");

                                    Log.d("asssss" , dueList+"");
                                    //DatabaseReference mRef1 = reference.child(model.getTitle()).child("status");
                                    if(dueList.get(position) >= cTotal){
                                        mRef.setValue(""+(dueList.get(position) - cTotal));
                                        //mRef1.setValue(true);

                                        reference.child(model.getTitle()).child("months").child("2017-9")
                                                .child("status").setValue("true");

                                        list.clear();


                                    }
                                    viewHolder.loadingButton.loadingSuccessful();
                                }
                                else {
                                    Toast.makeText(getActivity() , "Input valid data" , Toast.LENGTH_SHORT).show();
                                    viewHolder.loadingButton.loadingSuccessful();
                                }


                            }
                        }, 500);
                    }
                });




            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };

        if(fra.getItemCount()>0) progressDialog.dismiss();

        recyclerView.setAdapter(fra);



    }







    public static class PostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        LoadingButton loadingButton;
        ImageView imageView;
        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            loadingButton = (LoadingButton) itemView.findViewById(R.id.loading_button);
            imageView = (ImageView) mView.findViewById(R.id.flatBillsSta);
        }

        public void setRent(String rent) {

            TextView rentV = (TextView) mView.findViewById(R.id.showRentTextView);
            rentV.setText(rent);

        }

        public void setTitle(String title) {
            TextView titleV = (TextView) mView.findViewById(R.id.flatTitle);
            titleV.setText(title);

        }

        public void setBills(String bills){
            TextView billsV = (TextView) mView.findViewById(R.id.showBillsTextView);
            billsV.setText(bills);
        }

        public String getText(){
            EditText editText = (EditText) mView.findViewById(R.id.editTextDue);
            return editText.getText().toString().trim();
        }

        public void setTotal(String total){
            TextView textView = (TextView) mView.findViewById(R.id.showTotalTextView);
            textView.setText(total);
        }

        public void setDue(String due){
            TextView dueV = (TextView) mView.findViewById(R.id.showDueTextView);
            dueV.setText(due);

        }

       // LoadingButton loadingButton = (LoadingButton) mView.findViewById(R.id.loading_button);




    }

}
