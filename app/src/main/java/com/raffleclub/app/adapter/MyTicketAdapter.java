package com.raffleclub.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raffleclub.app.activity.TicketDetailActivity;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.model.Contest;
import com.raffleclub.app.R;
import com.raffleclub.app.utils.Utills;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyTicketAdapter extends RecyclerView.Adapter<MyTicketAdapter.ViewHolder> {

    Context context;
    List<Contest> dataArrayList;

    public MyTicketAdapter(Context applicationContext, List<Contest> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_ticket, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.dateTv.setText("Date Of Bought\n"+dataArrayList.get(position).getDate());
        holder.feeTv.setText(AppConstant.CURRENCY_SIGN +""+dataArrayList.get(position).getPrice());
        holder.prizeTv.setText(AppConstant.CURRENCY_SIGN +""+dataArrayList.get(position).getTotal_prize());
        holder.totalBoughtTv.setText(dataArrayList.get(position).getNo_of_bought()+" Bought");
        holder.totalPrizeTv.setText(dataArrayList.get(position).getNo_of_winners()+" Winners");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TicketDetailActivity.class);
            intent.putExtra("FEES_ID", dataArrayList.get(position).getId());
            intent.putExtra("ENTREE_FEES", dataArrayList.get(position).getPrice());
            intent.putExtra("TOTAL_PRIZE", dataArrayList.get(position).getTotal_prize());
            intent.putExtra("TOTAL_TICKET", dataArrayList.get(position).getNo_of_tickets());
            intent.putExtra("TOTAL_WINNERS", dataArrayList.get(position).getNo_of_winners());
            intent.putExtra("TOTAL_SOLD", dataArrayList.get(position).getNo_of_sold());
            intent.putExtra("TOTAL_BOUGHT", dataArrayList.get(position).getNo_of_bought());
            intent.putExtra("STATUS", dataArrayList.get(position).getStatus());
            intent.putExtra("TAG", "0");
            Preferences.getInstance(context).setString(Preferences.KEY_FEE_ID, dataArrayList.get(position).getId());
            Function.fireIntentWithData(context, intent);
        });

        holder.totalBoughtTv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.totalBoughtIv , context.getString(R.string.number_tickets));
        });
        holder.totalBoughtIv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.totalBoughtIv,context.getString(R.string.number_tickets));
        });
        holder.totalPrizeIv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.totalPrizeIv,context.getString(R.string.total_winner));
        });
        holder.totalPrizeTv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.totalPrizeIv,context.getString(R.string.total_winner));
        });
        holder.checkedIv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.checkedIv,context.getString(R.string.guaranteed_place));
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        TextView prizeTv;
        TextView feeTv;
        TextView firstPrizeTv;
        TextView totalPrizeTv;
        TextView totalBoughtTv;
        ImageView totalPrizeIv;
        ImageView totalBoughtIv;
        ImageView checkedIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTv = itemView.findViewById(R.id.dateTv);
            prizeTv = itemView.findViewById(R.id.prizeTv);
            feeTv = itemView.findViewById(R.id.feeTv);
            firstPrizeTv = itemView.findViewById(R.id.firstPrizeTv);
            totalPrizeTv = itemView.findViewById(R.id.totalPrizeTv);
            totalBoughtTv = itemView.findViewById(R.id.totalBoughtTv);
            totalPrizeIv = itemView.findViewById(R.id.totalPrizeIv);
            totalBoughtIv = itemView.findViewById(R.id.totalBoughtIv);
            checkedIv = itemView.findViewById(R.id.checkedIv);
        }
    }

}

