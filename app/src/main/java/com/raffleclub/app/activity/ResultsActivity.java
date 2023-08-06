package com.raffleclub.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.raffleclub.app.adapter.MyResultAdapter;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.Contest;
import com.raffleclub.app.R;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsActivity extends AppCompatActivity {

    private Context context;
    private ApiCalling api;
    private ProgressBarHelper progressBarHelper;

    private MyResultAdapter contestAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Results");

        context = ResultsActivity.this;
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);

        recyclerView = findViewById(R.id.recyclerView);

        if(Function.checkNetworkConnection(context)) {
            getMyResults();
        }
    }

    private void getMyResults() {
        progressBarHelper.showProgressDialog();

        Call<List<Contest>> call = api.getMyResults(AppConstant.PURCHASE_KEY, Preferences.getInstance(context).getString(Preferences.KEY_USER_ID), Preferences.getInstance(context).getString(Preferences.KEY_CONSTANT_ID));
        call.enqueue(new Callback<List<Contest>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<Contest>> call, @NonNull Response<List<Contest>> response) {
                progressBarHelper.hideProgressDialog();

                if (response.isSuccessful()) {
                    List<Contest> legalData = response.body();
                    if (legalData != null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        contestAdapter = new MyResultAdapter(context, legalData);
                        contestAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(contestAdapter);
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Contest>> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });
    }

}

