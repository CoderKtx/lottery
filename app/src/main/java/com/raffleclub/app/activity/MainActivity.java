package com.raffleclub.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.raffleclub.app.adapter.BtnAdapter;
import com.raffleclub.app.adapter.ViewPagerAdapter;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Constants;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.BtnTO;
import com.raffleclub.app.model.CustomerModel;
import com.raffleclub.app.R;
import com.raffleclub.app.model.NotificationModel;
import com.raffleclub.app.model.Packages;
import com.raffleclub.app.ui.tickets.TicketsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;
import io.customerly.Customerly;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TicketsFragment.RefreshContest, BtnAdapter.OnClickAdapter {

    public DrawerLayout drawer;
    public NavigationView navigationView;
    public Toolbar toolbar;
    public ViewPager viewPager;
    public MaterialTextView timer;
    public MaterialTextView contest;

    public ActionBarDrawerToggle mDrawerToggle;
    private ProgressBarHelper progressBarHelper;
    private ApiCalling api;
    private Context context;

    public TextView navUsername, emailId, imageText, countNotif;
    private CircleImageView imageView;
    public FloatingActionButton chatBt;

    public SwitchCompat switchNoti;
    public MyApplication MyApp;
    public FirebaseAnalytics mFirebaseAnalytics;

    private long mHours = 0;
    private long mMinutes = 0;
    private long mSeconds = 0;
    private long mMilliSeconds = 0;

    public TimerListener mListener;
    private CountDownTimer mCountDownTimer;

    private long backPressed;


    public interface TimerListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }

    private ViewPagerAdapter adapter;
    private ConstraintLayout layoutNotif;

    private RecyclerView recyclerBtn;
    private BtnAdapter btnAdapter;
    private Timer timerContest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        MyApp = MyApplication.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);


        recyclerBtn = findViewById(R.id.recycler);
        btnAdapter = new BtnAdapter(this);
        btnAdapter.setOnClickAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerBtn.setLayoutManager(linearLayoutManager);
        recyclerBtn.setAdapter(btnAdapter);


        layoutNotif = findViewById(R.id.layoutNotif);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        timer = findViewById(R.id.timer);
        contest = findViewById(R.id.contest);
        countNotif = findViewById(R.id.countNotif);


        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            drawer.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
        }

        mDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.white));


        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        navUsername = headerView.findViewById(R.id.username);
//        navUsername.setText(Preferences.getInstance(context).getString(Preferences.KEY_FULL_NAME));
        navUsername.setText(Preferences.getInstance(context).getString(Preferences.KEY_USER_NAME));

        emailId = headerView.findViewById(R.id.emailId);
//        emailId.setText(Preferences.getInstance(context).getString(Preferences.KEY_EMAIL));
        emailId.setText(Preferences.getInstance(context).getString(Preferences.KEY_FULL_NAME));

        imageView = headerView.findViewById(R.id.imageView);
        imageText = headerView.findViewById(R.id.imageText);

        if (Preferences.getInstance(context).getString(Preferences.KEY_RESORT_IMAGE).equals("") || Preferences.getInstance(context).getString(Preferences.KEY_RESORT_IMAGE).equals(AppConstant.FILE_URL + "")) {
            imageText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            try {
                imageText.setText(Preferences.getInstance(context).getString(Preferences.KEY_USER_NAME));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageText.setVisibility(View.GONE);
            try {


//                Glide
//                        .with(this)
//                        .load(AppConstant.FILE_IMAGE_URL + Preferences.getInstance(context).getString(Preferences.KEY_RESORT_IMAGE))
//                        .apply(new RequestOptions().override(512,512))
//                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
//                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                        .apply(RequestOptions.skipMemoryCacheOf(false))
//                        .into(imageView);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        chatBt = findViewById(R.id.chatBt);
        chatBt.setOnClickListener(v -> Customerly.openSupport(MainActivity.this));

        switchNoti = findViewById(R.id.switch_noti);
        switchNoti.setChecked(MyApp.getNotification());

        switchNoti.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MyApp.saveIsNotification(isChecked);
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.TOPIC_GLOBAL);
        });

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                btnAdapter.selected(position);
                recyclerBtn.scrollToPosition(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        timerContest = new Timer();

        runTimerLive();


        if (Function.checkNetworkConnection(context)) {
            getLiveContest();
        }


        layoutNotif = findViewById(R.id.layoutNotif);
        layoutNotif.setOnClickListener(view -> Function.fireIntent(MainActivity.this, NotificationActivity.class));
    }

    private void runTimerLive() {
        timerContest.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("timerRun", "run: Timer");

                        if (Function.checkNetworkConnection(context)) {
                            live();
                        }
                    }
                }, 100);
            }
        }, 1000, 3000);
    }

    private int countNotifINt;

    private void getNotification() {

        progressBarHelper.showProgressDialog();
        countNotifINt = 0;

        Call<List<NotificationModel>> call = api.getNotifications(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<List<NotificationModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<NotificationModel>> call, @NonNull Response<List<NotificationModel>> response) {
                progressBarHelper.hideProgressDialog();
                if (response.isSuccessful()) {
                    List<NotificationModel> legalData = response.body();
                    for (NotificationModel notf : legalData) {
                        if (notf.seen == 0) {
                            countNotifINt++;
                        }
                    }
                    if (countNotifINt > 0) {
                        countNotif.setText(String.valueOf(countNotifINt));
                        countNotif.setVisibility(View.VISIBLE);
                    } else
                        countNotif.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<NotificationModel>> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });
    }


    public TicketsFragment addPage(String id, String title) {
//        Log.i("testDeposit", "onResponse: "+title);

        Bundle bundle = new Bundle();
        bundle.putString("PKG_ID", id);
        bundle.putString("PKG_NAME", title);
        TicketsFragment dynamicFragment = new TicketsFragment();
        dynamicFragment.setRefresh(this);
        dynamicFragment.setArguments(bundle);

        Preferences.getInstance(context).setString(Preferences.KEY_PKG_ID, id);

//        adapter.addFrag(dynamicFragment , title);
//        adapter.notifyDataSetChanged();
//        if (adapter.getCount() > 0) tabLayout.setupWithViewPager(viewPager);

//        viewPager.setCurrentItem(adapter.getCount() - 1);
        return dynamicFragment;
    }

    private void setDynamicFragmentToTabLayout() {
//        progressBarHelper.showProgressDialog();
        Call<List<Packages>> call = api.getPackages(AppConstant.PURCHASE_KEY);

        call.enqueue(new Callback<List<Packages>>() {
            @Override
            public void onResponse(@NonNull Call<List<Packages>> call, @NonNull Response<List<Packages>> response) {
                progressBarHelper.hideProgressDialog();


                if (response.isSuccessful()) {
                    List<Packages> legalData = response.body();
                    if (legalData != null) {
                        if (legalData.size() > 0) {
                            ArrayList<TicketsFragment> ticketsFragments = new ArrayList<>();
                            List<BtnTO> btnTOList = new ArrayList<>();
                            for (int i = 0; i < legalData.size(); i++) {

                                String id = legalData.get(i).getId();
                                String title = legalData.get(i).getPkg_name();
                                ticketsFragments.add(addPage(id, title));
//                                viewPager.setCurrentItem(0);
                                btnTOList.add(new BtnTO(title, id));
                            }
                            adapter.updateAdapter(ticketsFragments);

                            btnAdapter.updateAdapter(btnTOList);
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Packages>> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

//            case R.id.action_notification:
//                Function.fireIntent(this, NotificationActivity.class);
//                return true;
            case R.id.menu_profile:
                Function.fireIntent(this, ProfileActivity.class);
                return false;
            case R.id.action_wallet:
            case R.id.menu_wallet:
                Function.fireIntent(this, WalletActivity.class);
                return true;
            case R.id.menu_my_ticket:
                Function.fireIntent(this, MyTicketActivity.class);
                return true;
            case R.id.menu_history:
                Function.fireIntent(this, HistoryActivity.class);
                return true;
            case R.id.menu_refer_earn:
                Function.fireIntent(this, ReferActivity.class);
                return true;
            case R.id.menu_policy:
                Function.fireIntent(this, PrivacyPolicyActivity.class);
                return true;
            case R.id.menu_terms:
                Function.fireIntent(this, TermsConditionActivity.class);
                return true;
            case R.id.menu_contact:
                Function.fireIntent(this, ContactUsActivity.class);
                return true;
            case R.id.menu_about:
                Function.fireIntent(this, AboutUsActivity.class);
                return true;
            case R.id.logout:
                alertDialog().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AlertDialog alertDialog() {
        return new AlertDialog.Builder(this)
                // set dialog icon
                .setIcon(R.drawable.logout)
                // Set Dialog Message
                .setMessage("Are you sure you want to Logout?")
                // positive button
                .setPositiveButton("Confirm", (dialog, which) -> Preferences.getInstance(MainActivity.this).setlogout())
                // negative button
                .setNegativeButton("Cancel", (dialog, which) -> {
                }).create();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                Function.fireIntent(this, ProfileActivity.class);
                break;
            case R.id.menu_wallet:
                Function.fireIntent(this, WalletActivity.class);
                break;
            case R.id.menu_my_ticket:
                Function.fireIntent(this, MyTicketActivity.class);
                break;
            case R.id.menu_history:
                Function.fireIntent(this, HistoryActivity.class);
                break;
            case R.id.menu_refer_earn:
                Function.fireIntent(this, ReferActivity.class);
                break;
            case R.id.menu_policy:
                Function.fireIntent(this, PrivacyPolicyActivity.class);
                break;
            case R.id.menu_terms:
                Function.fireIntent(this, TermsConditionActivity.class);
                break;
            case R.id.menu_contact:
                Function.fireIntent(this, ContactUsActivity.class);
                break;
            case R.id.menu_about:
                Function.fireIntent(this, AboutUsActivity.class);
                break;
            case R.id.menu_faq:
                Intent intent = new Intent(this, FAQActivity.class);
                intent.putExtra("faq", "faq");
                startActivity(intent);
                break;
            case R.id.menu_refund_policy:
                Intent intentRefund = new Intent(this, FAQActivity.class);
                intentRefund.putExtra("faq", "refund");
                startActivity(intentRefund);
                break;
            case R.id.logout:
                alertDialog().show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Call<CustomerModel> callLive;

    private void live() {
//        if (isProgress)
//        progressBarHelper.showProgressDialog();

        callLive = api.getLiveContest(AppConstant.PURCHASE_KEY);
        callLive.enqueue(new Callback<CustomerModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {

//                Log.i("testLive", "     -----------------live----------------       ");
//                Log.i("testLive", "getLive_contest: "+ response.body().getResult().get(0).getLive_contest());
//                Log.i("testLive", "getContent: "+ response.body().getResult().get(0).getContent());
//                Log.i("testLive", "getStatus: "+ response.body().getResult().get(0).getStatus());
//                Log.i("testLive", "getSuccess: "+ response.body().getResult().get(0).getSuccess());
//                Log.i("testLive", "getUpcoming_contest: "+ response.body().getResult().get(0).getUpcoming_contest());
//                Log.i("testLive", "     ---------------------------------       ");

                if (response.isSuccessful()) {
                    CustomerModel legalData = response.body();
                    List<CustomerModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();


                        if (res.get(0).getSuccess() == Preferences.getInstance(MainActivity.this).getInt(AppConstant.CONTEST_TIMER)) {
                            Log.i("testContaxt", " no change ");
//                            if(Function.checkNetworkConnection(context))
//                                if (res.get(0).getLive_contest()!=0)
//                                    setDynamicFragmentToTabLayout();
                        } else {
                            Log.i("testContaxt", " change ");

                            Preferences.getInstance(MainActivity.this).setInt(AppConstant.CONTEST_TIMER, res.get(0).getSuccess());
//                            getLiveContest();
                            refreshActivity();
                        }

                        if (res.get(0).getSuccess() == 1) {

                            Log.i("testLive", "     -----------------live----------------       ");
                            Log.i("testLive", "live : getSuccess 1");

                            Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);

                            int time = Integer.parseInt(res.get(0).getEnd_time()) - Integer.parseInt(res.get(0).getCurrent_time());

                            if (time > 0) {
                                contest.setText("Contest ends in: ");
                            } else {
                                contest.setText("Result will be announced soon");
                            }
                        } else {
                            getUpcomingContest();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });

    }


    private void getLiveContest() {

        progressBarHelper.showProgressDialog();

        Call<CustomerModel> call = api.getLiveContest(AppConstant.PURCHASE_KEY);
        call.enqueue(new Callback<CustomerModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
                progressBarHelper.hideProgressDialog();

                if (response.isSuccessful()) {
                    CustomerModel legalData = response.body();
                    List<CustomerModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {

                            if (res.get(0).getEnd_time() != null && res.get(0).getCurrent_time() != null) {
                                Preferences.getInstance(context).setString(Preferences.KEY_CONTST_ID, res.get(0).getId());
                                int time = Integer.parseInt(res.get(0).getEnd_time()) - Integer.parseInt(res.get(0).getCurrent_time());

                                if (time > 0) {
                                    setTime(time * 1000L);
                                    startCountDown();
                                    contest.setText("Contest ends in: ");
                                } else {
                                    progressBarHelper.hideProgressDialog();
                                    if (mCountDownTimer != null)
                                        mCountDownTimer.cancel();
//                                    timer.setText("");
                                    contest.setText("Result will be announced soon");
                                }
                            }
                        } else {
                            getUpcomingContest();
                        }
                    }

                    if (Function.checkNetworkConnection(context)) {
                        setDynamicFragmentToTabLayout();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });

    }

    private Call<CustomerModel> callUpComing;

    private void getUpcomingContest() {
        callUpComing = api.getUpcomingContest(AppConstant.PURCHASE_KEY);

        callUpComing.enqueue(new Callback<CustomerModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CustomerModel> call, @NonNull Response<CustomerModel> response) {
//                Log.i("testLive", "     -----------------getUpcomingContest----------------       ");
//                Log.i("testLive", "Success: "+ response.body().getResult().get(0).getSuccess());
//                Log.i("testLive", "onResponse: "+
//                        Preferences.getInstance(MainActivity.this).getInt(AppConstant.ON_COMING_TIME));


                if (response.isSuccessful()) {
                    CustomerModel legalData = response.body();
                    List<CustomerModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            if (res.get(0).getStart_time() != null && res.get(0).getCurrent_time() != null) {


                                contest.setText("Next contest starts in: ");
                                int time = Integer.parseInt(res.get(0).getStart_time()) - Integer.parseInt(res.get(0).getCurrent_time());

                                if (time > 0) {
//                                  Log.i("testLive", "onComing 2 : "+
//                                            (Preferences.getInstance(MainActivity.this).getInt(AppConstant.ON_COMING_TIME)==1));

                                    if (Preferences.getInstance(MainActivity.this).getInt(AppConstant.ON_COMING_TIME) == 1) {

                                    } else {
                                        setTime(time * 1000L);
                                        startCountDown();
                                        Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 1);
//                                        Log.i("testLive", "onComing else 2 : "+ response.body().getResult().get(0).getSuccess());
                                    }
//                                    contest.setText("Contest ends in: ");
                                } else {
                                    Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);
                                    progressBarHelper.hideProgressDialog();
                                    if (mCountDownTimer != null)
                                        mCountDownTimer.cancel();
//                                    timer.setText("");
                                    contest.setText("Contest will start soon");
                                }
                            }
                        } else {
                            Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);

                            if (mCountDownTimer != null)
                                mCountDownTimer.cancel();
//                            timer.setText("");
                            contest.setText("No Upcoming Contest");
                        }
                    } else {
                        Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);
                        if (mCountDownTimer != null)
                            mCountDownTimer.cancel();
//                        timer.setText("");
                        contest.setText("No Upcoming Contest");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomerModel> call, @NonNull Throwable t) {
            }
        });

    }

    private void initCounter() {
//        if (mCountDownTimer==null)
        mCountDownTimer = new CountDownTimer(mMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                calculateTime(millisUntilFinished, timer);

                if (mListener != null) {
                    mListener.onTick(millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                calculateTime(0, timer);
                if (mListener != null) {
                    mListener.onFinish();
                }
                refreshActivity();
//                Function.fireIntent(context,MainActivity.class);
//                getLiveContest();

            }
        };
    }

    public void startCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.start();
        }
    }

    public void setTime(long milliSeconds) {
        mMilliSeconds = milliSeconds;
        initCounter();
        calculateTime(milliSeconds, timer);
    }

    private void calculateTime(long milliSeconds, TextView timeText) {
        mSeconds = (milliSeconds / 1000) % 60;
        mMinutes = (milliSeconds / (1000 * 60)) % 60;
        mHours = (milliSeconds / (1000 * 60 * 60));
//        Log.i("testTimer", "initCounter: "+timeText.getText()+" "+mSeconds+" / " + mMinutes+" / " + mHours);

        displayText(timeText);
    }

    private void displayText(TextView timeText) {
        try {
            String stringBuilder = getTwoDigitNumber(mHours) + ":" + getTwoDigitNumber(mMinutes) + ":" + getTwoDigitNumber(mSeconds) + "";
            timeText.setText(stringBuilder);


        } catch (NullPointerException e) {

            e.printStackTrace();
        }
    }

    private String getTwoDigitNumber(long number) {
        if (number >= 0 && number < 10) {
            return "0" + number;
        }

        return String.valueOf(number);
    }


    @Override
    public void runRefresh() {
//        getLiveContest();
//        if(Function.checkNetworkConnection(context)){
//            setDynamicFragmentToTabLayout();
//        }
//        Function.fireIntent(context,MainActivity.class);

        if (timerContest != null)
            timerContest.cancel();
        if (callUpComing != null)
            callUpComing.cancel();
        if (callLive != null)
            callLive.cancel();

        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
            mCountDownTimer.cancel();
            mCountDownTimer = null;
            Log.i("testTimer", "--------------clear timer----------- ");
        }


        Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);
        refreshActivity();
//        getLiveContest();

    }

    public void refreshActivity() {
        if (timerContest != null)
            timerContest.cancel();
        Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);
        Function.fireIntent(context, MainActivity.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerContest != null)
            timerContest.cancel();

    }

    @Override
    public void onBackPressed() {
        if (backPressed + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit.", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Preferences.getInstance(MainActivity.this).setInt(AppConstant.ON_COMING_TIME, 0);

        getID();
        getNotification();

        if (Preferences.getInstance(context).getString(Preferences.KEY_RESORT_IMAGE).equals("") || Preferences.getInstance(context).getString(Preferences.KEY_RESORT_IMAGE).equals(AppConstant.FILE_URL + "")) {
            imageText.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            try {
                String a = String.valueOf(Preferences.getInstance(context).getString(Preferences.KEY_USER_NAME).charAt(0));
                imageText.setText(a);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            imageView.setVisibility(View.VISIBLE);
            imageText.setVisibility(View.GONE);
            try {

                Glide.with(this)
                        .asBitmap()
                        .load(AppConstant.FILE_IMAGE_URL +
                                Preferences.getInstance(context).getString(Preferences.KEY_RESORT_IMAGE)
                        )
                        .apply(new RequestOptions().override(512,512))
                        .apply(new RequestOptions().placeholder(R.drawable.app_icon).error(R.drawable.app_icon))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.skipMemoryCacheOf(false))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });



            } catch (IllegalArgumentException e) {
                Log.e("adapterNotif", "exception: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClickAdapter(int position) {

        viewPager.setCurrentItem(position);

    }

    public void getID(){

        api.getUserProfile(
                AppConstant.PURCHASE_KEY,
                Preferences.getInstance(context).getString(Preferences.KEY_USER_ID)
                ).enqueue(
                        new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.body().getResult().get(0).getStatus()==0){
                    new android.app.AlertDialog.Builder(MainActivity.this)
                            .setMessage("Your account is under review")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    dialogInterface.dismiss();
                                }
                            })
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            })
                            .setCancelable(false)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                Log.e("testActive", "onResponse: "+t.getMessage());

            }
        }
        );

    }

}
