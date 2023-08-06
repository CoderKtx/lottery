package com.raffleclub.app.fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.raffleclub.app.R;

public class BankAndDrawalActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialButton btnBank , btnWithDrawal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_drawal_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnBank = findViewById(R.id.btnBank);
        btnWithDrawal = findViewById(R.id.btnDrawal);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new BankFragment()).commit();

        btnBank.setOnClickListener(this);
        btnWithDrawal.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBank:
                btnBank.setBackgroundColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.colorPrimary));
                btnBank.setTextColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.white));

                btnWithDrawal.setBackgroundColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.white));
                btnWithDrawal.setTextColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.colorPrimary));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new BankFragment()).commit();
                break;
            case R.id.btnDrawal:
                btnBank.setBackgroundColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.white));
                btnBank.setTextColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.colorPrimary));

                btnWithDrawal.setBackgroundColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.colorPrimary));
                btnWithDrawal.setTextColor(ContextCompat.getColor(BankAndDrawalActivity.this,R.color.white));

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new WithdrawalFragment()).commit();
                break;
        }
    }


}
