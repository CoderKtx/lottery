package com.raffleclub.app.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.raffleclub.app.R;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.helper.Preferences;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReferActivity extends AppCompatActivity {

    private Context context;

    public TextView referTv;
    public TextView prizeTv;
    public TextView shareTv;

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Refer & Earn");

        context = ReferActivity.this;

        referTv = findViewById(R.id.refer);
        prizeTv = findViewById(R.id.prizeTv);
        shareTv = findViewById(R.id.share);

        prizeTv.setText(String.format("For every friend that you refer, you and your friend will get %s%d as a Bonus reward by using your Referral code.", AppConstant.CURRENCY_SIGN, AppConstant.APP_SHARE_PRIZE, AppConstant.CURRENCY_SIGN, AppConstant.APP_DOWNLOAD_PRIZE));

        String[] split = Preferences.getInstance(context).getString(Preferences.KEY_EMAIL).split("@");
        String username = split[0];

        referTv.setText(username);
        referTv.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Referral code Copied", username);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(), "Referral code Copied", Toast.LENGTH_SHORT).show();
        });

        shareTv.setOnClickListener(v -> Function.shareApp(context, referTv.getText().toString()));
    }

}
