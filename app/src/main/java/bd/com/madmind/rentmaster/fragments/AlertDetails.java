package bd.com.madmind.rentmaster.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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
import bd.com.madmind.rentmaster.TenantsProfileActivity;
import bd.com.madmind.rentmaster.models.GetAletsData;
import bd.com.madmind.rentmaster.models.TenantsList;

public class AlertDetails extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_alert_details, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        //init the firebase instance variables
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        reference = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("alert");

        //init the view widgets

        recyclerView = (RecyclerView) v.findViewById(R.id.fadRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reference.addValueEventListener(new ValueEventListener() {
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

        final FirebaseRecyclerAdapter<GetAletsData, AlertDetails.PostViewHolder> fra = new FirebaseRecyclerAdapter<GetAletsData, AlertDetails.PostViewHolder>(
                GetAletsData.class,
                R.layout.alert_details_adapter_layout,
                AlertDetails.PostViewHolder.class,
                reference
        ) {
            @Override
            protected void populateViewHolder(AlertDetails.PostViewHolder viewHolder, GetAletsData model, final int position) {
                if(viewHolder !=null) {
                    progressDialog.dismiss();
                    viewHolder.setMsg(model.getMsg() , model.getPeriod());
                    viewHolder.setUri(model.getUri());

                    Log.d("nnnnn" , model.getMsg());
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

        public void setMsg(String msg , String period) {
            TextView titleView = (TextView) mView.findViewById(R.id.tname);
            StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
            String spanText = "want to leave this flat in ";
            int spanText_len = spanText.length();
            int msg_len = msg.length();
            int period_len = period.length();

            SpannableStringBuilder sb = new SpannableStringBuilder(msg+" "+spanText+period);
           // sb.setSpan(boldStyle, 0, msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(boldStyle , spanText_len+msg_len , spanText_len+msg_len+period_len+1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //textView.setText(sb);
            titleView.setText(sb);
        }


        public void setUri(String uri) {

            ImageView poster = (ImageView) mView.findViewById(R.id.tthumbnail);
            Glide.with(mView.getContext()).load(uri).into(poster);

        }

    }



}
