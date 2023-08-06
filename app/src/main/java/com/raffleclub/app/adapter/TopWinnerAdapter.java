package com.raffleclub.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.model.Contest;
import com.raffleclub.app.R;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopWinnerAdapter extends RecyclerView.Adapter<TopWinnerAdapter.ViewHolder> {

    Context context;
    List<Contest> dataArrayList;

    public TopWinnerAdapter(Context applicationContext, List<Contest> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_top_winner, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.winningPrice.setText(AppConstant.CURRENCY_SIGN +""+dataArrayList.get(position).getWin_prize());
        holder.username.setText(dataArrayList.get(position).getUsername());

        if (dataArrayList.get(position).getName()!=null)
            holder.name.setText(dataArrayList.get(position).getName());
        else
            holder.name.setText("");

        try {
            holder.userImage.setText(dataArrayList.get(position).getUsername().charAt(1)+"");
        }catch (Exception e){
            e.printStackTrace();
        }
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.userImage.setBackgroundColor(randomAndroidColor);
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView userImage;
        TextView winningPrice,name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            userImage = itemView.findViewById(R.id.userImage);
            winningPrice = itemView.findViewById(R.id.prize);
        }
    }

}

