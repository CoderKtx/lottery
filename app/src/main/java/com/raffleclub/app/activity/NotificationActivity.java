package com.raffleclub.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.raffleclub.app.adapter.NotificationAdapter;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.NotificationModel;
import com.raffleclub.app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.CallBackAdapterNotifi {

    private ProgressBarHelper progressBarHelper;
    private ApiCalling api;
    private Context context;

    private SwipeRefreshLayout swipeRefresh;
    private NotificationAdapter dataAdapter;
    private NotificationModel notificationModel;
    private Preferences preferences ;

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        preferences = new Preferences(this);
        context = NotificationActivity.this;
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);



        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataAdapter = new NotificationAdapter(getApplicationContext());
        dataAdapter.setCallBack(NotificationActivity.this);
        recyclerView.setAdapter(dataAdapter);


        if(Function.checkNetworkConnection(context)) {
            getNotification();
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNotification();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getNotification() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        progressBarHelper.showProgressDialog();

        Call<List<NotificationModel>> call = api.getNotifications(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<List<NotificationModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<NotificationModel>> call, @NonNull Response<List<NotificationModel>> response) {
                progressBarHelper.hideProgressDialog();
                Log.i("testNotif", "onResponse: seen : " +response.isSuccessful());
                if (response.isSuccessful()) {
                    List<NotificationModel> legalData = response.body();
                    if (legalData != null && legalData.size() > 0) {
                        preferences.setInt(Preferences.COUNT_NOTIFICATION,legalData.size());


                        for (NotificationModel notificationModel1 : legalData) {
                            Log.i("testNotif", "onResponse: seen : " +notificationModel1.getSeen());

                        }
//                        List<NotificationModel> dataArrayList = new ArrayList<>();
//                        for (NotificationModel notificationModel1 : legalData) {
//                            Log.i("testNotifi", "onResponse: "+notificationModel1.getId());
//                            notificationModel = new NotificationModel();
//                            notificationModel.setTitle(notificationModel1.getTitle());
//                            notificationModel.setMessage(notificationModel1.getMessage());
//                            notificationModel.setImage(notificationModel1.getImage());
//                            notificationModel.setCreated(findDifference(notificationModel1.getCreated(), currentDateAndTime));
//                            notificationModel.setImage(notificationModel1.getImage());
//                            notificationModel.setUrl(notificationModel1.getUrl());
//                            dataArrayList.add(notificationModel);
//                        }
//                        if (!dataArrayList.isEmpty()) {
//                            dataAdapter.updateNotif(legalData);
//                        }
                        dataAdapter.updateNotif(legalData);

                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<NotificationModel>> call, @NonNull Throwable t) {
                Log.e("testNotif", "exception : " +t.getMessage());
                progressBarHelper.hideProgressDialog();
            }
        });
    }

    public String findDifference(String start_date, String end_date) {
        // SimpleDateFormat converts the
        // string format to date object
        String ans = "";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");

        // Try Class
        try {
            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            // Calucalte time difference
            // in milliseconds
            long difference_In_Time = Objects.requireNonNull(d2).getTime() - Objects.requireNonNull(d1).getTime();

            // Calucalte time difference in seconds,
            // minutes, hours, years, and days
            long difference_In_Seconds
                    = TimeUnit.MILLISECONDS
                    .toSeconds(difference_In_Time)
                    % 60;

            long difference_In_Minutes = TimeUnit.MILLISECONDS.toMinutes(difference_In_Time);

            long difference_In_Hours
                    = TimeUnit
                    .MILLISECONDS
                    .toHours(difference_In_Time)
                    % 24;

            long difference_In_Days
                    = TimeUnit
                    .MILLISECONDS
                    .toDays(difference_In_Time)
                    % 365;

            if (difference_In_Minutes == 0) {
                ans = difference_In_Seconds + " seconds";
            } else if (difference_In_Hours == 0) {
                ans = difference_In_Minutes + " minutes";
            } else if (difference_In_Days == 0) {
                ans = difference_In_Hours + " hours";
            } else if (difference_In_Days > 0 && difference_In_Days < 7) {
                ans = difference_In_Days + " days";
            } else if (difference_In_Days > 6) {
                ans = start_date;
            }

            return ans;
        } catch (ParseException e) {
            return "";
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==5){
            getNotification();
        }
        
    }

    @Override
    public void notif(NotificationModel notificationModel) {
        Intent intent = new Intent(context, NotificationDetailsActivity.class);
        intent.putExtra(AppConstant.NOTIFICATION_ID, notificationModel.getId());
        intent.putExtra("title", notificationModel.getTitle());
        intent.putExtra("description", notificationModel.getMessage());
        intent.putExtra("image", notificationModel.getImage());
        intent.putExtra("url", notificationModel.getUrl());
        intent.putExtra("created", notificationModel.getCreated());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,5);
    }
}
