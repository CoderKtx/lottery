package com.raffleclub.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.raffleclub.app.activity.NotificationDetailsActivity;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.model.NotificationModel;
import com.raffleclub.app.R;
import com.raffleclub.app.utils.Utills;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<NotificationModel> dataArrayList;
    private Typeface typeface;

    public NotificationAdapter(Context applicationContext) {
        typeface = ResourcesCompat.getFont(applicationContext, R.font.poppins_regular);
        this.context = applicationContext;
        this.dataArrayList = new ArrayList<>();
    }

    public void updateNotif( List<NotificationModel> dataArrayList){
        this.dataArrayList = dataArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTv.setText(dataArrayList.get(position).getTitle());
//
//        Log.i("adapterNotif", "onBindViewHolder: "+position + " "
//                +dataArrayList.get(position).title+" "
//                +dataArrayList.get(position).getImage()
//        );

        if(dataArrayList.get(position).getImage() != null) {
            Picasso.get().load(AppConstant.FILE_URL + dataArrayList.get(position).getImage())
                    .placeholder(R.drawable.app_icon)
                    .into(holder.iconTv);
        }else{
            holder.iconTv.setImageResource(R.drawable.app_icon);
        }

        holder.itemView.setOnClickListener(v -> {
            if (callBackAdapterNotifi!=null)
                callBackAdapterNotifi.notif(dataArrayList.get(position));
        });

        if (dataArrayList.get(position).getSeen()==1) {
            holder.titleTv.setTextColor(ContextCompat.getColor(context, R.color.gray_e));
            holder.titleTv.setTypeface(typeface, Typeface.NORMAL);
            holder.timeTv.setTextColor(ContextCompat.getColor(context, R.color.gray_e));
            holder.timeTv.setTypeface(typeface,Typeface.NORMAL);
        }else{
            holder.titleTv.setTextColor(ContextCompat.getColor(context, R.color.black_notif));
            holder.titleTv.setTypeface(typeface, Typeface.BOLD);
            holder.timeTv.setTextColor(ContextCompat.getColor(context, R.color.black_notif));
            holder.timeTv.setTypeface(typeface,Typeface.BOLD);
        }

//        long input = dataArrayList.get(position).getElapsed_time();
//        holder.timeTv.setText(Utills.newInstance().formatDate(dataArrayList.get(position).getCreated()));

        holder.timeTv.setText(
                dataArrayList.get(position).getCreated() +" • "+dataArrayList.get(position).elapsed_time);
//        holder.timeTv.setText(dataArrayList.get(position).getCreated()+" "+dataArrayList.get(position).elapsed_time);




    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconTv;
        TextView titleTv;
        TextView timeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconTv = itemView.findViewById(R.id.iconTv);
            titleTv = itemView.findViewById(R.id.titleTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }

    public interface CallBackAdapterNotifi{
        void notif(NotificationModel notificationModel);
    }

    private CallBackAdapterNotifi callBackAdapterNotifi;

    public void setCallBack(CallBackAdapterNotifi callBack){
        this.callBackAdapterNotifi = callBack;
    }

}
