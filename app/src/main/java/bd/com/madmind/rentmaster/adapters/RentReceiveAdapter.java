package bd.com.madmind.rentmaster.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;

import java.util.ArrayList;

import bd.com.madmind.rentmaster.R;
import bd.com.madmind.rentmaster.models.RentReceiveData;

/**
 * Created by ash on 8/17/2017.
 */

public class RentReceiveAdapter extends RecyclerView.Adapter<RentReceiveAdapter.ViewHolder>{

    private ArrayList<RentReceiveData> dataArrayList;
    private Context context;
    private String[] mDataset;

    public RentReceiveAdapter(ArrayList<RentReceiveData> dataArrayList, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
        mDataset = new String[10];
        Log.d("size" , ""+this.dataArrayList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rent_recv_adapter_layout , parent , false);
        return new ViewHolder(view, new MyCustomEditText());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.currentMrentV.setText(""+dataArrayList.get(position).getCurntMonth());
        holder.customEditText.updatePosition(holder.getAdapterPosition());
        holder.additonalBillsV.setText(""+dataArrayList.get(position).getAdditionalB());
        holder.dueV.setText(""+dataArrayList.get(position).getDue());
        holder.sumV.setText("546778");
        holder.loadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , mDataset[position] , Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView currentMrentV , dueV , additonalBillsV , sumV;
        MyCustomEditText customEditText;
        EditText getText;
        LoadingButton loadingButton;



        public ViewHolder(View itemView , MyCustomEditText customEditText) {
            super(itemView);

            currentMrentV = (TextView) itemView.findViewById(R.id.showRentTextView);
            dueV = (TextView) itemView.findViewById(R.id.showDueTextView);
            additonalBillsV = (TextView) itemView.findViewById(R.id.showBillsTextView);
            sumV = (TextView) itemView.findViewById(R.id.showTotalTextView);
            getText = (EditText) itemView.findViewById(R.id.editTextDue);
            loadingButton = (LoadingButton) itemView.findViewById(R.id.loading_button);


            this.customEditText = customEditText;
            this.getText.addTextChangedListener(customEditText);
        }
    }


    public class MyCustomEditText implements TextWatcher {

        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mDataset[position] = s.toString();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }



}
