package bd.com.madmind.rentmaster.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
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

import bd.com.madmind.rentmaster.AddingProperties;
import bd.com.madmind.rentmaster.FlatActivity;
import bd.com.madmind.rentmaster.R;
import bd.com.madmind.rentmaster.models.PropertiesData;

public class PropertiesFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Downloading....");
        progressDialog.show();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("projects");
        fab = (FloatingActionButton) view.findViewById(R.id.propertiesFab);
        recyclerView = (RecyclerView) view.findViewById(R.id.propertiesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddingProperties.class);
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



        return view;
    }

    public void recyclerAdapter() {

        final FirebaseRecyclerAdapter<PropertiesData, PostViewHolder> fra = new FirebaseRecyclerAdapter<PropertiesData, PostViewHolder>(
                PropertiesData.class,
                R.layout.propertieslistlayout,
                PostViewHolder.class,
                ref
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, PropertiesData model, final int position) {
                if(viewHolder !=null) {
                    progressDialog.dismiss();
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setUrl(model.getUrl());
                    viewHolder.setAddress(model.getAddress());
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String key = getRef(position).getKey();
                        startActivity(new Intent(getActivity() , FlatActivity.class).putExtra("key" , key));
                        Toast.makeText(getActivity() , ""+key , Toast.LENGTH_SHORT).show();

                    }
                });



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
           TextView titleView = (TextView) mView.findViewById(R.id.Ptitle);
            titleView.setText(title);

        }

        public void setAddress(String address) {
            TextView addressView = (TextView) mView.findViewById(R.id.Paddress);
            addressView.setText(address);
        }

        public void setUrl(String url) {
            ImageView poster = (ImageView) mView.findViewById(R.id.PimageView);
            Glide.with(mView.getContext()).load(url).into(poster);

        }

    }





}
