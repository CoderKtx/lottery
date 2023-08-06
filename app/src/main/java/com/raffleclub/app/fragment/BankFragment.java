package com.raffleclub.app.fragment;



import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.R;
import com.raffleclub.app.activity.BankAccountActivity;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Constants;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.helper.PicModeSelectDialogFragment;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.BankModel;
import com.raffleclub.app.model.Contest;
import com.raffleclub.app.utils.NoSpaceInputFilter;
import com.raffleclub.app.utils.Utills;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankFragment extends Fragment {

    public static final String ERROR_MSG = "error_msg";
    public static final String ERROR = "error";


    private int status = 0;
    private String uriFile;

    private ProgressBarHelper progressBarHelper;
    private ApiCalling api;

    public TextInputEditText accNameTv;
    public TextView accNoTv;
    public TextView ifscCodeTv;
    public TextView panNoTv;
//    public ImageView proofCopyTv;
    public ImageView addImageIv;
//    public CardView proofCopyCardView;
    public TextView submitTv;
    public TextView textInputError;
    public TextView tErrorBankAccount;
    public TextView tErrorIFSC;
    public TextView tErrorPan;
    private EditText eAccNumberRenter;

    private Utills utills;

    String a;
    int keyDel;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(getContext(), false);

        utills = new Utills();
        accNameTv = view.findViewById(R.id.acc_name);
        accNoTv = view.findViewById(R.id.acc_no);
        tErrorPan = view.findViewById(R.id.tErrorPan);
        ifscCodeTv = view.findViewById(R.id.ifsc_code);
        panNoTv = view.findViewById(R.id.pan_no);
        addImageIv = view.findViewById(R.id.addImage);

//        proofCopyTv = view.findViewById(R.id.proof_copy);
//        proofCopyCardView = view.findViewById(R.id.proofCopyCardView);
        submitTv = view.findViewById(R.id.submit);
        textInputError = view.findViewById(R.id.textinput_error);
        tErrorBankAccount = view.findViewById(R.id.tErrorBankAccount);
        tErrorIFSC = view.findViewById(R.id.tErrorIFSC);
        eAccNumberRenter = view.findViewById(R.id.eAccNumberRenter);

        accNameTv.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ifscCodeTv.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(11),new NoSpaceInputFilter()});

        eAccNumberRenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equals(accNoTv.getText().toString())){
                    tErrorBankAccount.setVisibility(View.GONE);
                }else{
                    tErrorBankAccount.setVisibility(View.VISIBLE);
                }

                if (charSequence.toString().isEmpty())
                    tErrorBankAccount.setVisibility(View.GONE);


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ifscCodeTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



                if (ifscCodeTv.getText().toString().length()==11 &&
//                        utills.isValidlowerCase(charSequence.toString()) &&
                        utills.isValidUpperCase(charSequence.toString()))
                    tErrorIFSC.setVisibility(View.GONE);
                else
                    tErrorIFSC.setVisibility(View.VISIBLE);

                if (charSequence.toString().isEmpty())
                    tErrorIFSC.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        panNoTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()==10){
                    tErrorPan.setVisibility(View.GONE);
                }else
                    tErrorPan.setVisibility(View.VISIBLE);

                if (charSequence.toString().isEmpty())
                    tErrorPan.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


//        accNoTv.setTransformationMethod(new MyPasswordTransformationMethod());


        if (Function.checkNetworkConnection(getContext())) {
            getBankInfo();
        }

        submitTv.setOnClickListener(v -> {
                    Toast.makeText(getContext(), accNoTv.getText().toString() + "", Toast.LENGTH_SHORT).show();
                    updateBankInfo();
                }
        );

    }



    private void getBankInfo() {
        progressBarHelper.showProgressDialog();

        Call<BankModel> call = api.getBankInfo(AppConstant.PURCHASE_KEY, Preferences.getInstance(getContext()).getString(Preferences.KEY_USER_ID));
        call.enqueue(new Callback<BankModel>() {
            @Override
            public void onResponse(@NonNull Call<BankModel> call, @NonNull Response<BankModel> response) {
                progressBarHelper.hideProgressDialog();

                if (response.isSuccessful()) {
                    BankModel legalData = response.body();
                    List<BankModel.Result> res;
                    if (legalData != null) {
                        res = legalData.getResult();
                        if (res.get(0).getSuccess() == 1) {
                            accNameTv.setText(res.get(0).getAcc_name());
                            accNoTv.setText(res.get(0).getAcc_no());
                            ifscCodeTv.setText(res.get(0).getIfsc_code());
                            panNoTv.setText(res.get(0).getPan_no());
                            if (res.get(0).getProof_copy() != null) {
//                                proofCopyCardView.setVisibility(View.GONE);

//                                Glide.with(getContext()).load(AppConstant.FILE_URL + res.get(0).getProof_copy())
//                                        .apply(new RequestOptions().override(720, 540))
//                                        .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
//                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                                        .apply(RequestOptions.skipMemoryCacheOf(true))
//                                        .into(proofCopyTv);
                            } else {
//                                proofCopyCardView.setVisibility(View.VISIBLE);
//                                proofCopyCardView2.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BankModel> call, @NonNull Throwable t) {
                progressBarHelper.hideProgressDialog();
            }
        });
    }

    private void addBankDetail() {
//        if (status == 1) {
//            progressBarHelper.showProgressDialog();
//
//            Call<List<Contest>> call = api.updateBankInfo(AppConstant.PURCHASE_KEY, Preferences.getInstance(getContext()).getString(Preferences.KEY_USER_ID), accNameTv.getText().toString(), accNoTv.getText().toString(), ifscCodeTv.getText().toString(), panNoTv.getText().toString(), uriFile, "0");
//            call.enqueue(new Callback<List<Contest>>() {
//                @Override
//                public void onResponse(@NonNull Call<List<Contest>> call, @NonNull Response<List<Contest>> response) {
//                    progressBarHelper.hideProgressDialog();
//                    if (response.isSuccessful()) {
//                        List<Contest> legalData = response.body();
//                        if (legalData != null) {
//                            Function.showToast(getContext(), legalData.get(0).getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<List<Contest>> call, @NonNull Throwable t) {
//                    progressBarHelper.hideProgressDialog();
//                }
//            });
//        } else {
//            progressBarHelper.showProgressDialog();
//
//            Call<List<Contest>> call = api.updateBankInfoWithoutProofCopy(AppConstant.PURCHASE_KEY, Preferences.getInstance(getContext()).getString(Preferences.KEY_USER_ID), accNameTv.getText().toString(), accNoTv.getText().toString(), ifscCodeTv.getText().toString(), panNoTv.getText().toString(), "0");
//            call.enqueue(new Callback<List<Contest>>() {
//                @Override
//                public void onResponse(@NonNull Call<List<Contest>> call, @NonNull Response<List<Contest>> response) {
//                    progressBarHelper.hideProgressDialog();
//                    if (response.isSuccessful()) {
//                        List<Contest> legalData = response.body();
//                        if (legalData != null) {
//                            Function.showToast(getContext(), legalData.get(0).getMsg());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<List<Contest>> call, @NonNull Throwable t) {
//                    progressBarHelper.hideProgressDialog();
//                }
//            });
//        }
    }

    @SuppressLint("SetTextI18n")
    public void updateBankInfo() {
        if (accNameTv.getText().toString().equals("") && accNoTv.getText().toString().equals("") && ifscCodeTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("All fields are mandatory");
        } else if (accNameTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("Please enter account name");
        } else if (accNoTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("Please enter account no");
        } else if (ifscCodeTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("Please enter ifsc code");
        } else if (status == 0) {
            textInputError.setText("");
            textInputError.setText("Attach any bank account proof for verification");
        } else {
            addBankDetail();
        }
    }




    public void errorValidation() {
        Intent intent = new Intent();
        intent.putExtra(ERROR, true);
        intent.putExtra(ERROR_MSG, "Error while opening the image file. Please try again.");
        getActivity().finish();
    }





}
