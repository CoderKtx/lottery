package com.raffleclub.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.ConfigurationModel;
import com.raffleclub.app.R;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private ProgressBarHelper progressBarHelper;
    private ApiCalling api;
    public Context context;

    private WebView contentTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        context = PrivacyPolicyActivity.this;
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);

        contentTv = findViewById(R.id.contentTv);


        getSupportActionBar().setTitle(getString(R.string.menu_policy));
        getPrivacyPolicy();


    }

    private void getPrivacyPolicy() {
        progressBarHelper.showProgressDialog();

        Call<ConfigurationModel> call = api.getPrivacyPolicy(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<ConfigurationModel>() {
            @Override
            public void onResponse(@NonNull Call<ConfigurationModel> call, @NonNull Response<ConfigurationModel> response) {
                progressBarHelper.hideProgressDialog();


                if (response.isSuccessful()) {
                    ConfigurationModel legalData = response.body();
                    List<ConfigurationModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        Log.i("testestes", "onResponse: "+res.get(0).getPrivacy());

                        if (res.get(0).getSuccess() == 1) {
                            contentTv.loadDataWithBaseURL(null, res.get(0).getPrivacy(), "text/html", "UTF-8", null);
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

