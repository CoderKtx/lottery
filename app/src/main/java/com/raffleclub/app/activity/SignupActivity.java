package com.raffleclub.app.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.R;
import com.raffleclub.app.utils.Utills;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private Context context;

    public Button signupBt;
    public EditText nameEt;
    public CountryCodePicker cntrNoEt;
    public EditText mobileNoEt;
    public EditText passwordEt;
    public EditText confirmPasswordEt;
    public EditText referEt;
    public TextView textInputError;
    public TextView errorMobile;
    public TextView privacyPolicyTv;

    public TextInputLayout inputPassword;
    public TextInputLayout inputPasswordConfirm;

    private LinearLayout layoutError,lMin , lUpperCase , lLowerCase , lNumber , lSymbol ;
    private ImageView  ic_CheckErrorMin ,icCheckUpperCase,icCheckLowerCase,icCheckNumber,icCheckSymbol;


    private int year;
    private int month;
    private int day;
    public EditText dateOfBirthEt;

    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        context = SignupActivity.this;

        ic_CheckErrorMin = findViewById(R.id.icErrorMin);
        icCheckUpperCase = findViewById(R.id.icCheckUpperCase);
        icCheckLowerCase = findViewById(R.id.icCheckLowerCase);
        icCheckNumber = findViewById(R.id.icCheckNumber);
        icCheckSymbol = findViewById(R.id.icCheckSymbol);

        layoutError = findViewById(R.id.layoutError);
        lMin = findViewById(R.id.errorMin);
        lUpperCase = findViewById(R.id.ErrorUpperCase);
        lLowerCase = findViewById(R.id.ErrorLowerCase);
        lNumber = findViewById(R.id.ErrorNumber);
        lSymbol = findViewById(R.id.ErrorSymbol);

        nameEt = findViewById(R.id.name);
        cntrNoEt = findViewById(R.id.cntrno);
        errorMobile = findViewById(R.id.errorMobile);
        inputPassword = findViewById(R.id.inputPassword);
        inputPasswordConfirm = findViewById(R.id.inputConfirmPassword);

        mobileNoEt = findViewById(R.id.mobileno);
        passwordEt = findViewById(R.id.password);
        confirmPasswordEt = findViewById(R.id.confirmPassword);
        referEt = findViewById(R.id.refer);
        textInputError = findViewById(R.id.textinput_error);
        privacyPolicyTv = findViewById(R.id.privacyPolicy);
        signupBt = findViewById(R.id.signup);

        dateOfBirthEt = findViewById(R.id.dateofbirth);
        dateOfBirthEt.setOnClickListener(v -> setDateOfBirth());


        signupBt.setOnClickListener(v -> {
            try {
                InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }



            if (mobileNoEt.getText().toString().equals("")){
                errorMobile.setText("Please enter your registered mobile number");
                errorMobile.setVisibility(View.VISIBLE);
                return;
            }else if (!mobileNoEt.getText().toString().equals("") && (
                    mobileNoEt.getText().toString().charAt(0) != '6'&&
                            mobileNoEt.getText().toString().charAt(0) != '7'&&
                            mobileNoEt.getText().toString().charAt(0) != '8'&&
                            mobileNoEt.getText().toString().charAt(0) != '9'
            )
            ){
                errorMobile.setVisibility(View.VISIBLE);
                errorMobile.setText("should start with 6,7,8,9");
                return;
            }else if(mobileNoEt.getText().toString().length()<10){
                errorMobile.setVisibility(View.VISIBLE);
                errorMobile.setText("enter 10 correct digit number");
                return;
            }

//            if (!Utills.newInstance().isValidPassword(passwordEt.getText().toString())){
//                inputPassword.setError("password should be 8 digit and password should contain alphabet, number and symbol, etc");
//                return;
//            }
            if (!validationPass){
                layoutError.setVisibility(View.VISIBLE);
                Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!confirmPasswordEt.getText().toString().equals(passwordEt.getText().toString())) {
                inputPasswordConfirm.setError("Password not matching");
                return;
            }




            if (mobileNoEt.getText().toString().equals("") && passwordEt.getText().toString().equals("") && confirmPasswordEt.getText().toString().equals("")) {
                textInputError.setText("");
                textInputError.setText("All fields are mandatory");
            } else if (mobileNoEt.getText().toString().equals("")) {
                textInputError.setText("");
                textInputError.setText("Please enter your mobile number");
            }  else if (mobileNoEt.getText().length() < 6) {
                textInputError.setText("");
                textInputError.setText("Please enter valid mobile number");
            } else if (passwordEt.getText().toString().equals("")) {
                textInputError.setText("");
                textInputError.setText("Please enter password");
            }
            else if (passwordEt.getText().toString().length() < 8) {
                textInputError.setText("");
                textInputError.setText("Password must be 8 characters");
            /*} else if (!passwordEt.getText().toString().equals(confirmPasswordEt.getText().toString())) {
                textInputError.setText("");
                textInputError.setText("Password mismatch");*/
            } else if (dateOfBirthEt.getText().toString().equals("")) {
                dateOfBirthEt.setText("");
                textInputError.setText("Please enter date");
            } else {
                textInputError.setText("");
                AppConstant.COUNTRY_CODE = "+"+cntrNoEt.getSelectedCountryCode().trim();
                Preferences.getInstance(context).setString(Preferences.KEY_FULL_NAME, nameEt.getText().toString());
                Preferences.getInstance(context).setString(Preferences.KEY_MOBILE, mobileNoEt.getText().toString());
                Preferences.getInstance(context).setString(Preferences.KEY_PASSWORD, passwordEt.getText().toString());
                Preferences.getInstance(context).setString(Preferences.KEY_REFER_CODE, referEt.getText().toString());
                Preferences.getInstance(context).setString(Preferences.KEY_DOB, dateOfBirthEt.getText().toString());
                Log.i("testSignUp", "signUp: "+dateOfBirthEt.getText().toString());

                Function.fireIntent(context, SignupOTPActivity.class);
            }
        });

        privacyPolicyTv.setOnClickListener(v -> Function.fireIntent(context, TermsConditionActivity.class));

        validation();
    }



    public void setDateOfBirth() {
        final Calendar c = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        c.set(Calendar.YEAR, 1990);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(SignupActivity.this,
                (view, year2, monthOfYear, dayOfMonth) -> {

                    if (calendar.get(Calendar.YEAR) - year2>18) {
                        year = year2;
                        month = monthOfYear;
                        day = dayOfMonth;
//                    dateOfBirthEt.setText(new StringBuilder().append(pad(month + 1)).append("-").append(pad(day)).append("-").append(year));
                        dateOfBirthEt.setText(new StringBuilder().append(year).append("-").append(pad(month + 1)).append("-").append(pad(day)));
                    }else{
                        new AlertDialog.Builder(SignupActivity.this)
                                .setMessage("You must be +18 to use RaffleClub")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                }, year, month, day);

        picker.getDatePicker().setMaxDate(new Date().getTime());
        picker.show();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }

    public void validation(){

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
                            errorMobile.setVisibility(View.VISIBLE);
                            errorMobile.setText("Please enter correct 10 digit phone number");
                        }else{
                            errorMobile.setText("");
                            errorMobile.setVisibility(View.GONE);
                        }

                    }
                    else {
                        errorMobile.setVisibility(View.VISIBLE);
                        errorMobile.setText("Please enter correct 10 digit phone number starting with 6,7,8 or 9");
                    }
                else{
                    errorMobile.setText("");
                    errorMobile.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passwordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length()==0)
                    layoutError.setVisibility(View.GONE);
                else
                layoutError.setVisibility(View.VISIBLE);

                if (!Utills.newInstance().isValidLength(charSequence.toString())){
                    validationPass = false;
                    ic_CheckErrorMin.setImageResource(R.drawable.ic_baseline_close_24);
                    ic_CheckErrorMin.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.red_color));
                }else{
                    validationPass = true;
                    ic_CheckErrorMin.setImageResource(R.drawable.ic_sharp_check_24);
                    ic_CheckErrorMin.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.primary_green));
                }

                if (!Utills.newInstance().isValidUpperCase(charSequence.toString())){
                    validationPass = false;
                    icCheckUpperCase.setImageResource(R.drawable.ic_baseline_close_24);
                    icCheckUpperCase.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.red_color));
                }else{
                    icCheckUpperCase.setImageResource(R.drawable.ic_sharp_check_24);
                    icCheckUpperCase.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.primary_green));
                    validationPass = true;
                }
                if (!Utills.newInstance().isValidlowerCase(charSequence.toString())){
                    validationPass = false;
                    icCheckLowerCase.setImageResource(R.drawable.ic_baseline_close_24);
                    icCheckLowerCase.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.red_color));
                }else{
                    icCheckLowerCase.setImageResource(R.drawable.ic_sharp_check_24);
                    icCheckLowerCase.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.primary_green));
                    validationPass = true;
                }
                if (!Utills.newInstance().isValidNumber(charSequence.toString())){
                    validationPass = false;
                    icCheckNumber.setImageResource(R.drawable.ic_baseline_close_24);
                    icCheckNumber.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.red_color));
                }else{
                    icCheckNumber.setImageResource(R.drawable.ic_sharp_check_24);
                    icCheckNumber.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.primary_green));
                    validationPass = true;
                }
                if (!Utills.newInstance().isValidSymbol(charSequence.toString())){
                    validationPass = false;
                    icCheckSymbol.setImageResource(R.drawable.ic_baseline_close_24);
                    icCheckSymbol.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.red_color));
                }else{
                    icCheckSymbol.setImageResource(R.drawable.ic_sharp_check_24);
                    icCheckSymbol.setColorFilter(ContextCompat.getColor(SignupActivity.this,R.color.primary_green));
                    validationPass = true;
                }

//                if (!Utills.newInstance().isValidPassword(charSequence.toString())){
//                    inputPassword.setError("password should be 8 digit and password should contain alphabet, number and symbol, etc");
//                }else{
//                    inputPassword.setErrorEnabled(false);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        confirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!Utills.newInstance().isValidPassword(charSequence.toString())){
//                    inputPasswordConfirm.setError("password should be 8 digit and password should contain alphabet, number and symbol, etc");
//                }else{
//                    inputPasswordConfirm.setErrorEnabled(false);
//                }


                if (!charSequence.toString().equals(passwordEt.getText().toString())) {
                    inputPasswordConfirm.setError("Password not matching");
                }else{
                    inputPasswordConfirm.setErrorEnabled(false);
                }


                if (charSequence.toString().equals(""))
                    inputPasswordConfirm.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private Boolean validationPass =false;

}
