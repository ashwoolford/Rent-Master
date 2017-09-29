package bd.com.madmind.rentmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bd.com.madmind.rentmaster.DetailsActivity;
import bd.com.madmind.rentmaster.R;
import bd.com.madmind.rentmaster.models.MenuData;

/**
 * Created by ash on 8/6/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    private ArrayList<MenuData> list;
    private Context context;
    private long nofCounter;

    public HomeAdapter(ArrayList<MenuData> list, Context context , long nofCounter) {
        this.list = list;
        this.context = context;
        this.nofCounter = nofCounter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homeadaperlayout , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if(position == 4){
            holder.notfView.setVisibility(View.VISIBLE);
            holder.notfView.setText(" "+nofCounter+" ");
        }

        else holder.notfView.setVisibility(View.INVISIBLE);
        Glide.with(context)
                .load(list.get(position).getImageUrl())
                .into(holder.imageView);
        final String getTitle = list.get(position).getTitle();
        holder.title.setText(""+getTitle);
        final int getId = list.get(position).getId();
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DetailsActivity.class);
                intent.putExtra("title" , getTitle);
                intent.putExtra("id" , getId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        CardView cardView;
        TextView notfView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.iconView);
            title = (TextView) itemView.findViewById(R.id.titleView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewLayout);
            notfView = (TextView) itemView.findViewById(R.id.noticationView);
        }
    }
}
