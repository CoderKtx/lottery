package com.raffleclub.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.CustomerModel;
import com.raffleclub.app.R;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends Activity {

    private Context context;
    private ApiCalling api;
    private ProgressBarHelper progressBarHelper;

    public EditText mobileNoEt;
    public TextView textInputError;
    public Button nextBt;
    private MaterialTextView tError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        context = ForgotActivity.this;
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);

        mobileNoEt = findViewById(R.id.mobileno);
        textInputError = findViewById(R.id.textinput_error);
        nextBt = findViewById(R.id.next);
        tError = findViewById(R.id.tError);

        nextBt.setOnClickListener(v -> {
            try {
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mobileNoEt.getText().toString().equals("")) {
                textInputError.setText("");
                textInputError.setText("Please enter your mobile number.");
            } else if(mobileNoEt.getText().length() < 6){
                textInputError.setText("");
                textInputError.setText("Please enter valid mobile number.");
            }else {
                textInputError.setText("");
                verifyUserMobile();
            }
        });



        mobileNoEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();

                if (str.length() > 0)
                    if (str.charAt(0) == '9' || str.charAt(0)== '8' || str.charAt(0)== '7' || str.charAt(0)== '6' ){

                        if (str.length()<10) {
                            tError.setVisibility(View.VISIBLE);
                            tError.setText("Please enter correct 10 digit phone number");
                        }else{
                            tError.setText("");
                            tError.setVisibility(View.GONE);
                        }

                    }
                    else {
                        tError.setVisibility(View.VISIBLE);
                        tError.setText("Please enter correct 10 digit phone number starting with 6,7,8 or 9");
                    }
                else{
                    tError.setText("");
                    tError.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void verifyUserMobile() {
        progressBarHelper.showProgressDialog();

        Call<CustomerModel> call = api.verifyUserMobile(AppConstant.PURCHASE_KEY, mobileNoEt.getText().toString());
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
                            Preferences.getInstance(context).setString(Preferences.KEY_MOBILE, mobileNoEt.getText().toString());
                            Function.fireIntent(context, ForgotOTPActivity.class);
                        }else {
                            textInputError.setText("");
                            textInputError.setText("Please check your mobile number.");
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

}
