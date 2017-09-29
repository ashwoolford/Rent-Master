package bd.com.madmind.rentmaster.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import bd.com.madmind.rentmaster.R;
import bd.com.madmind.rentmaster.TenantsDetailsActivity;
import bd.com.madmind.rentmaster.TenantsProfileActivity;
import bd.com.madmind.rentmaster.models.TenantsList;

import static android.R.attr.key;


/**
 * A simple {@link Fragment} subclass.
 */
public class TenantsListFragment extends Fragment {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private ProgressDialog progressDialog;

    private String projectN  = "", flatT = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_tenants_list, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Downloading....");
        progressDialog.show();
        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("tenants");

        fab = (FloatingActionButton) v.findViewById(R.id.ftFab);
        recyclerView = (RecyclerView) v.findViewById(R.id.ftRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , TenantsDetailsActivity.class);
                startActivity(intent);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0) recyclerAdapter();
                else progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //recyclerAdapter();


        return v;
    }




    public void recyclerAdapter() {

        final FirebaseRecyclerAdapter<TenantsList, TenantsListFragment.PostViewHolder> fra = new FirebaseRecyclerAdapter<TenantsList, TenantsListFragment.PostViewHolder>(
                TenantsList.class,
                R.layout.tanentslistlayout,
                TenantsListFragment.PostViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(TenantsListFragment.PostViewHolder viewHolder, TenantsList model, final int position) {
                if(viewHolder !=null) {
                    progressDialog.dismiss();
                   viewHolder.setName(model.getName());
                    viewHolder.setAddress(model.getAddress());
                    viewHolder.setProfileuri(model.getProfileuri());
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = getRef(position).getKey();
                        startActivity(new Intent(getActivity() , TenantsProfileActivity.class).putExtra("key" , key));
                        Toast.makeText(getActivity() , ""+key , Toast.LENGTH_SHORT).show();

                    }
                });



                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        String key = getRef(position).getKey();
                        Toast.makeText(getActivity() , ""+key , Toast.LENGTH_SHORT).show();
                        AlertDialog(key);
                        return true;
                    }
                });



            }
        };

        if(fra.getItemCount()>0) progressDialog.dismiss();

        recyclerView.setAdapter(fra);



    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public PostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String name) {
            TextView titleView = (TextView) mView.findViewById(R.id.tname);
            titleView.setText(name);
        }


        public void setAddress(String address) {
            TextView addressView = (TextView) mView.findViewById(R.id.taddress);
            addressView.setText(address);
        }

        public void setProfileuri(String profileuri) {
            ImageView poster = (ImageView) mView.findViewById(R.id.tthumbnail);
            Glide.with(mView.getContext()).load(profileuri).into(poster);
        }

    }


    public void AlertDialog(final String key){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Do you want to Delete ? ");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d("debug" , "  ");

                        addNewChild(key);
                        //
                        removePerson(key);

                        dialog.cancel();


                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void addNewChild(final String key){


        if(!key.isEmpty()){


            DatabaseReference reference = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("tenants").child(key);


            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        projectN = dataSnapshot.child("project").getValue().toString();
                        flatT = dataSnapshot.child("flat").getValue().toString();

                        DatabaseReference flatAddRef = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("projects")
                                .child(projectN).child("av_flats");
                        flatAddRef.child(flatT).child("title").setValue(flatT);

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

    }


    public void removePerson(String keyx){
        ref.child(keyx).removeValue();

    }


}
