package bd.com.madmind.rentmaster.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bd.com.madmind.rentmaster.FlatActivity;
import bd.com.madmind.rentmaster.R;
import bd.com.madmind.rentmaster.models.GetFlatsData;

/**
 * Created by ash on 8/25/2017.
 */

public class FlatAdapter extends RecyclerView.Adapter<FlatAdapter.ViewHolder>{

    ArrayList<GetFlatsData> list;
    Context context;

    ProgressDialog progressDialog;


    public FlatAdapter(ArrayList<GetFlatsData> list, Context context , Context context1) {
        this.list = list;
        this.context = context;

        progressDialog = new ProgressDialog(context1);
        progressDialog.setTitle("Retrieving Data....  ");
        progressDialog.show();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flat_list_layout , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(list.get(position).getTitle());

        if(holder != null) progressDialog.dismiss();

    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.ftxx);
        }
    }
}
