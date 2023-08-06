package com.raffleclub.app.ui.tickets;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raffleclub.app.adapter.ContestAdapter;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.R;
import com.raffleclub.app.model.CustomerModel;

import java.io.IOException;
import java.util.List;

public class TicketsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private Context context;
    private ApiCalling api;
    private TicketsViewModel mViewModel;
    public ProgressBarHelper progressBarHelper;
    private ContestAdapter contestAdapter;
    private ImageView imgBackRace;

    private SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    TextView textContest;

    public Bundle bundle;
    String id, title;
    private MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(TicketsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tickets, container, false);
        mediaPlayer=MediaPlayer.create(getContext(), R.raw.coin);

        api = MyApplication.getRetrofit().create(ApiCalling.class);
        context = getActivity();
        progressBarHelper = new ProgressBarHelper(context, false);

        swipeRefreshLayout = root.findViewById(R.id.swipeRefresh);
        recyclerView = root.findViewById(R.id.recyclerView);
        textContest = root.findViewById(R.id.textContest);
        imgBackRace = root.findViewById(R.id.imgBackRace);

        bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("PKG_ID");
            title = bundle.getString("PKG_NAME");
        }

        if (Function.checkNetworkConnection(context)) {
            getContestStatus();
        } else {
            Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_LONG).show();
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        return root;
    }


    private void getContestStatus() {
        //progressBarHelper.showProgressDialog();

        Call<CustomerModel> call = api.getContestStatus(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<CustomerModel>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                //progressBarHelper.hideProgressDialog();

                if (response.isSuccessful()) {
                    CustomerModel legalData = response.body();
                    List<CustomerModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        Log.i("testDeposit", "getSuccess: "+res.get(0).getSuccess() + " "+id);


                        if (res.get(0).getSuccess() == 1) {
                            if (res.get(0).getLive_contest() == 1) {
                                textContest.setVisibility(View.GONE);
                                imgBackRace.setImageResource(R.drawable.background_home);
                                recyclerView.setVisibility(View.VISIBLE);

                                mViewModel.init(Preferences.getInstance(context).getString(Preferences.KEY_CONTST_ID), id);
                                mViewModel.getDomesticList().observe(getViewLifecycleOwner(), invoiceModels -> {
                                    if (invoiceModels != null) {
                                        if (invoiceModels.size() > 0) {
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                            recyclerView.setLayoutManager(linearLayoutManager);
                                            contestAdapter = new ContestAdapter(context, invoiceModels);
                                            contestAdapter.notifyDataSetChanged();
                                            recyclerView.setAdapter(contestAdapter);
                                        }
                                    }
                                });
                            } else if (res.get(0).getUpcoming_contest() == 1) {
//                                textContest.setText("Upcoming contest");
//                                textContest.setVisibility(View.VISIBLE);
                                imgBackRace.setImageResource(R.drawable.background_no_race);
                                recyclerView.setVisibility(View.GONE);
                            } else if (res.get(0).getUpcoming_contest() == 0 && res.get(0).getLive_contest() == 0) {
//                                textContest.setText("No Upcoming Contest");
//                                textContest.setVisibility(View.VISIBLE);
                                imgBackRace.setImageResource(R.drawable.background_no_race);
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
//                            textContest.setVisibility(View.VISIBLE);
                            imgBackRace.setImageResource(R.drawable.background_no_race);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
                //progressBarHelper.hideProgressDialog();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("testFrag", "onResume: "+id);

    }

    @Override
    public void onRefresh() {
        if (Function.checkNetworkConnection(context)) {
            if (refreshContest!=null)
                refreshContest.runRefresh();
        } else {
            Toast.makeText(context, "Please check your internet connectivity...", Toast.LENGTH_LONG).show();
        }

        if (mediaPlayer.isPlaying()){
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(getContext(), R.raw.coin);
            }
            mediaPlayer.start();
        }else
        mediaPlayer.start();
        swipeRefreshLayout.setRefreshing(false);
    }


    public interface RefreshContest{
        void runRefresh();
    }
    private RefreshContest refreshContest;
    public void setRefresh(RefreshContest refreshContest){
        this.refreshContest = refreshContest;
    }
}
