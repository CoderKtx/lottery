package com.raffleclub.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.raffleclub.app.R;
import com.raffleclub.app.model.BtnTO;

import java.util.ArrayList;
import java.util.List;

public class BtnAdapter extends RecyclerView.Adapter<BtnAdapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<BtnTO> list;
    private int positionSelected = 0;

    public BtnAdapter(Context context){
        list = new ArrayList();
        this.layoutInflater = LayoutInflater.from(context);
    }
    public void updateAdapter(List<BtnTO> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public void selected(int positionSelected){
        this.positionSelected = positionSelected;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.adapter_btn,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.initModel(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (list!=null)
        return list.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout constraintLayout;
        private MaterialTextView title,tr1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.const_btn1);
            title = itemView.findViewById(R.id.tickets1);
            tr1 = itemView.findViewById(R.id.tr1);
        }
        public void initModel(BtnTO btnTO,int position){
            String[] str = btnTO.getTitle().split(" ");

            if (positionSelected==position)
                constraintLayout.setBackgroundResource(R.drawable.border_btn_active);
                else
                constraintLayout.setBackgroundResource(R.drawable.border_btn_non_active);


            if (str.length > 0  && str[0] != null)
                tr1.setText(str[0]);

            if (str.length > 1  && str[1] != null)
                title.setText(str[1]);


            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickAdapter!=null)
                        onClickAdapter.onClickAdapter(position);
                    positionSelected = position;
                    notifyDataSetChanged();
                }
            });

//            Log.i("testAdapter", "initModel: "+str[0]+" "+str[1]);
//            title.setText(str[0] +" "+ str[1]);
        }
    }

    private OnClickAdapter onClickAdapter;

    public void setOnClickAdapter(OnClickAdapter onClickAdapter){
        this.onClickAdapter = onClickAdapter;
    }

    public interface OnClickAdapter{
        public void onClickAdapter(int position);
    }

}
