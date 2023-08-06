package com.raffleclub.app.activity;


import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.R;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.NotificationModel;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationDetailsActivity extends AppCompatActivity {

    public TextView titleTv, dateTv,tDescription;
    public WebView webView;
    public Button viewMoreBt;
    public ImageView imageIv,imageNotif;
    private ApiCalling api;

    private ProgressBarHelper progressBarHelper;
    public String notificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notification Details");
        progressBarHelper = new ProgressBarHelper(this, false);

        api = MyApplication.getRetrofit().create(ApiCalling.class);


        tDescription = findViewById(R.id.tDescription);
        titleTv = findViewById(R.id.titleTv);
        dateTv = findViewById(R.id.dateTv);
        viewMoreBt = findViewById(R.id.viewMoreBt);
        imageIv = findViewById(R.id.imageIv);
        imageNotif = findViewById(R.id.imageNotification);
        webView = findViewById(R.id.webView);

        if (getIntent().getExtras() != null) {
//            title = getIntent().getExtras().getString("title");
//            description = getIntent().getExtras().getString("description");
            String image = getIntent().getExtras().getString("image");
            Log.i("serviceFireBase", "getIntent image : " + image);

//            url = getIntent().getExtras().getString("url");
//            created = getIntent().getExtras().getString("created");
            notificationID = getIntent().getExtras().getString(AppConstant.NOTIFICATION_ID);
            Log.i("serviceFireBase", "getIntent notificationID : " + notificationID);

            getNotification();
            readNotification();
        }

    }

    private void getNotification() {

        progressBarHelper.showProgressDialog();

        Call<NotificationModel> call = api.getNotification(AppConstant.PURCHASE_KEY,notificationID);
        call.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {

                progressBarHelper.hideProgressDialog();
                Log.i("testNotif", "onResponse: seen : " +response.isSuccessful());
                Log.i("serviceFireBase", "onResponse: seen : " + response.body().url);
                Log.i("serviceFireBase", "image : " + response.body().image);

                if (response.isSuccessful()) {
                    NotificationModel notificationModel1 = response.body();


                titleTv.setText(notificationModel1.getTitle());
                dateTv.setText(notificationModel1.getCreated());


                    tDescription.setText(notificationModel1.getMessage().trim());


                    dateTv.setText(notificationModel1.getCreated() +" • "+notificationModel1.elapsed_time);

                    Log.i("serviceFireBase", "title : " + notificationModel1.title);
                    Log.i("serviceFireBase", "message : " + notificationModel1.getMessage());

        try {
            if (notificationModel1.image !=null) {

                Picasso.get().load(AppConstant.FILE_URL + notificationModel1.image).placeholder(R.drawable.app_icon)
                                .into(imageNotif);
//                Glide.with(getApplicationContext()).load(AppConstant.FILE_URL + notificationModel1.image)
//                        .apply(new RequestOptions().override(512,512))
//                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                        .apply(RequestOptions.skipMemoryCacheOf(true))
//                        .into(imageNotif);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        try {
            if (!notificationModel1.url.isEmpty()) {
                if (!notificationModel1.url.equals("false")) {
                    viewMoreBt.setVisibility(View.VISIBLE);
                }
                else {
                    viewMoreBt.setVisibility(View.GONE);
                }
            }
            else {
                viewMoreBt.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            viewMoreBt.setVisibility(View.GONE);
        }


        viewMoreBt.setOnClickListener(v ->
                webView.loadUrl(notificationModel1.url)

//                    openWebPage(notificationModel1.url)
        );


                }

            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                Log.e("testNotif", "exception : " +t.getMessage());
                progressBarHelper.hideProgressDialog();
            }
        });

    }


    public void readNotification(){
        api.updateNotification(AppConstant.PURCHASE_KEY,notificationID)
                .enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                        }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {

                    }
                });
    }

    public void openWebPage(String url) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install link web browser or check your URL.",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}