package com.raffleclub.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

import com.raffleclub.app.MyApplication;
import com.raffleclub.app.R;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.ConfigurationModel;

import java.util.List;
import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    private ProgressBarHelper progressBarHelper;
    private ApiCalling api;
    public Context context;

    private WebView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.menu_contact));

        context = ContactUsActivity.this;
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);

        contentTv = findViewById(R.id.contentTv);

        getContactUs();

    }

    private void getContactUs() {
        progressBarHelper.showProgressDialog();

        Call<ConfigurationModel> call = api.getContactUs(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBarHelper.hideProgressDialog();

                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            contentTv.loadDataWithBaseURL(null, res.get(0).getContact(), "text/html", "UTF-8", null);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ConfigurationModel> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });

    }
}