package com.raffleclub.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ViewHolder> {

    Context context;
    List<Contest> dataArrayList;

    public ContestAdapter(Context applicationContext, List<Contest> dataArrayList) {
        this.context = applicationContext;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.feeTv.setText(AppConstant.CURRENCY_SIGN +""+dataArrayList.get(position).getPrice());
        holder.prizeTv.setText(AppConstant.CURRENCY_SIGN +""+dataArrayList.get(position).getTotal_prize());
        holder.firstPrizeTv.setText(AppConstant.CURRENCY_SIGN +""+dataArrayList.get(position).getFirst_prize());
        holder.totalPrizeTv.setText(dataArrayList.get(position).getNo_of_winners()+" Winners");
        holder.roomStatusTv.setText(Integer.parseInt(dataArrayList.get(position).getNo_of_tickets())-Integer.parseInt(dataArrayList.get(position).getNo_of_sold())+" Spots Left");
        holder.roomSizeTv.setText(dataArrayList.get(position).getNo_of_tickets()+"  Spots");

        holder.progressBar.setMax(Integer.parseInt(dataArrayList.get(position).getNo_of_tickets()));
        holder.progressBar.setProgress(Integer.parseInt(dataArrayList.get(position).getNo_of_sold()));

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, TicketDetailActivity.class);
            intent.putExtra("FEES_ID", dataArrayList.get(position).getId());
            intent.putExtra("ENTREE_FEES", dataArrayList.get(position).getPrice());
            intent.putExtra("TOTAL_PRIZE", dataArrayList.get(position).getTotal_prize());
            intent.putExtra("FIRST_PRIZE", dataArrayList.get(position).getFirst_prize());
            intent.putExtra("TOTAL_TICKET", dataArrayList.get(position).getNo_of_tickets());
            intent.putExtra("TOTAL_WINNERS", dataArrayList.get(position).getNo_of_winners());
            intent.putExtra("TOTAL_SOLD", dataArrayList.get(position).getNo_of_sold());
            intent.putExtra("TAG", "1");
            Preferences.getInstance(context).setString(Preferences.KEY_FEE_ID, dataArrayList.get(position).getId());
            Function.fireIntentWithData(context, intent);
        });


        holder.firstPrizeTv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.firstPrizeIv , context.getString(R.string.first_price));
        });
        holder.firstPrizeIv.setOnClickListener(view -> {
            Utills.newInstance().setGuid(holder.firstPrizeIv,context.getString(R.string.first_price));
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
        TextView prizeTv;
        TextView feeTv;
        TextView roomStatusTv;
        TextView roomSizeTv;
        TextView firstPrizeTv;
        ImageView firstPrizeIv;
        ImageView totalPrizeIv;
        ImageView checkedIv;
        TextView totalPrizeTv;
        ProgressBar progressBar;
        RelativeLayout realtiveUnder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prizeTv = itemView.findViewById(R.id.prizeTv);
            feeTv = itemView.findViewById(R.id.feeTv);
            roomStatusTv = itemView.findViewById(R.id.roomStatusTv);
            roomSizeTv = itemView.findViewById(R.id.roomSizeTv);
            firstPrizeIv = itemView.findViewById(R.id.firstPrizeIv);
            firstPrizeTv = itemView.findViewById(R.id.firstPrizeTv);
            checkedIv = itemView.findViewById(R.id.checkedIv);
            totalPrizeTv = itemView.findViewById(R.id.totalPrizeTv);
            totalPrizeIv = itemView.findViewById(R.id.totalPrizeIv);
            progressBar = itemView.findViewById(R.id.progressBar);
            realtiveUnder = itemView.findViewById(R.id.RealtiveUnder);
        }
    }

}
