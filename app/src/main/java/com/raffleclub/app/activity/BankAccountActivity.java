package com.raffleclub.app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.raffleclub.app.api.ApiCalling;
import com.raffleclub.app.helper.AppConstant;
import com.raffleclub.app.helper.Constants;
import com.raffleclub.app.helper.Function;
import com.raffleclub.app.MyApplication;
import com.raffleclub.app.helper.PicModeSelectDialogFragment;
import com.raffleclub.app.helper.Preferences;
import com.raffleclub.app.helper.ProgressBarHelper;
import com.raffleclub.app.model.BankModel;
import com.raffleclub.app.model.Contest;
import com.raffleclub.app.R;
import com.raffleclub.app.utils.MyPasswordTransformationMethod;
import com.raffleclub.app.utils.MyPasswordTransformationMethod4;
import com.raffleclub.app.utils.NoSpaceInputFilter;
import com.raffleclub.app.utils.Utills;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BankAccountActivity extends AppCompatActivity implements PicModeSelectDialogFragment.IPicModeSelectListener {

    public static final String ERROR_MSG = "error_msg";
    public static final String ERROR = "error";

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 218;
    public static final int REQUEST_CODE_PICK_GALLERY = 0x1;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;

    private int status = 0;
    private int statusPAC = 0;
    private String uriFile;
    private String uriFilePAN;

    private ProgressBarHelper progressBarHelper;
    private ApiCalling api;
    private Context context;

    public EditText accNameTv;
    public TextView accNoTv;
    public TextView ifscCodeTv;
    public TextView panNoTv;
    public ImageView proofCopyTv,proof_copy_Final,pan_card_Final;
    public ImageView addImageIv,addImagePAN,imgPAN;
    public CardView proofCopyCardView,cardViewPDA;
    public TextView submitTv;
    public TextView textInputError;
    public TextView tErrorBankAccount;
    public TextView tErrorIFSC;
    public TextView tErrorPan;
    private EditText eAccNumberRenter;
    private Spinner spinnerBank;

    private Utills utills;
     String bankName="";


    String a;
    int keyDel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Bank Account");

        context = BankAccountActivity.this;
        api = MyApplication.getRetrofit().create(ApiCalling.class);
        progressBarHelper = new ProgressBarHelper(context, false);

        utills = new Utills();
        pan_card_Final = findViewById(R.id.pan_card_Final);
        cardViewPDA = findViewById(R.id.cardViewPDA);
        proof_copy_Final = findViewById(R.id.proof_copy_Final);
        spinnerBank = findViewById(R.id.spinnerBank);
        accNameTv = findViewById(R.id.acc_name);
        accNoTv = findViewById(R.id.acc_no);
        tErrorPan = findViewById(R.id.tErrorPan);
        ifscCodeTv = findViewById(R.id.ifsc_code);
        panNoTv = findViewById(R.id.pan_no);
        addImageIv = findViewById(R.id.addImage);
        imgPAN = findViewById(R.id.imgPAN);
        addImagePAN = findViewById(R.id.addImagePAN);
        proofCopyTv = findViewById(R.id.proof_copy);
        proofCopyCardView = findViewById(R.id.proofCopyCardView);
        submitTv = findViewById(R.id.submit);
        textInputError = findViewById(R.id.textinput_error);
        tErrorBankAccount = findViewById(R.id.tErrorBankAccount);
        tErrorIFSC = findViewById(R.id.tErrorIFSC);
        eAccNumberRenter = findViewById(R.id.eAccNumberRenter);

//        accNameTv.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        ifscCodeTv.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(11),new NoSpaceInputFilter()});
        panNoTv.setFilters(new InputFilter[]{new InputFilter.AllCaps(),new InputFilter.LengthFilter(10), new NoSpaceInputFilter()});

        accNameTv.setFilters(new InputFilter[] {new InputFilter.AllCaps(),
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }
                        if(src.toString().matches("[a-zA-Z ]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });

        accNoTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(" ")){
                    return;
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals(" ")){
                    return;
                }

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

                if (!Utills.newInstance().isValidAlphaNumeric(charSequence.toString()) ||
                        charSequence.toString().length() < 10){
                    tErrorPan.setVisibility(View.VISIBLE);
                }else{
                    tErrorPan.setVisibility(View.GONE);
                }

//                if (charSequence.toString().length() < 10) {
//                    tErrorPan.setVisibility(View.VISIBLE);
//                } else
//                    tErrorPan.setVisibility(View.GONE);

                if (charSequence.toString().isEmpty())
                    tErrorPan.setVisibility(View.GONE);

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


        eAccNumberRenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().equals(accNoTv.getText().toString())) {
                    tErrorBankAccount.setVisibility(View.GONE);
                } else {
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

                if (charSequence.toString().equals(" ")){
                    return;
                }


                if (ifscCodeTv.getText().toString().length() == 11 &&
//                        utills.isValidlowerCase(charSequence.toString()) &&
                        utills.isValidNumber(charSequence.toString()) &&
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



//        accNoTv.setTransformationMethod(new MyPasswordTransformationMethod4());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.banks));
        arrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerBank.setAdapter(arrayAdapter);
        spinnerBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                bankName = (String) adapterView.getItemAtPosition(position);
                textInputError.setText("");
//                Toast.makeText(context, bankName+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        if (Function.checkNetworkConnection(context)) {
            getBankInfo();
        }

        submitTv.setOnClickListener(v -> {

                    updateBankInfo();
                }
        );


        proofCopyCardView.setOnClickListener(v -> proofCopyCardView());

        addImageIv.setOnClickListener(v -> addImage());
//        addImage1Iv.setOnClickListener(v -> addImage1());
        addImagePAN.setOnClickListener(v -> addImagePAN());
    }

    private void getBankInfo() {
        progressBarHelper.showProgressDialog();

        Call<BankModel> call = api.getBankInfo(AppConstant.PURCHASE_KEY, Preferences.getInstance(context).getString(Preferences.KEY_USER_ID));
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

//                            if (res.get(0).getAcc_status().equals("2"))
//                                showDialog("under review", "2");
//                            else if (res.get(0).getAcc_status().equals("0"))
//                                showDialog("Rejected", "0");

                            Log.i("3636Bank", "onResponse: " +res.get(0).getProof_copy());
                            Log.i("3636Bank", "onResponse: " +res.get(0).getPan_card());

                            accNameTv.setText(res.get(0).getAcc_name());
                            accNoTv.setText(res.get(0).getAcc_no());
                            ifscCodeTv.setText(res.get(0).getIfsc_code());
                            panNoTv.setText(res.get(0).getPan_no());
                            if (res.get(0).getProof_copy() != null) {
                                status = 1;
                                proofCopyCardView.setVisibility(View.GONE);

                                proof_copy_Final.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load(AppConstant.FILE_IMAGE_URL + res.get(0).getProof_copy())
                                        .apply(new RequestOptions().override(720, 540))
                                        .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                        .apply(RequestOptions.skipMemoryCacheOf(true))
                                        .into(proof_copy_Final);
                            } else {
                                proof_copy_Final.setVisibility(View.GONE);
                                proofCopyCardView.setVisibility(View.VISIBLE);
                            }
                            if (res.get(0).getPan_card() != null) {
                                cardViewPDA.setVisibility(View.GONE);
                                statusPAC = 1;

                                pan_card_Final.setVisibility(View.VISIBLE);
                                Glide.with(getApplicationContext()).load(AppConstant.FILE_IMAGE_URL + res.get(0).getPan_card())
                                        .apply(new RequestOptions().override(720, 540))
                                        .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                        .apply(RequestOptions.skipMemoryCacheOf(true))
                                        .into(pan_card_Final);
                            } else {
                                cardViewPDA.setVisibility(View.VISIBLE);
                                pan_card_Final.setVisibility(View.GONE);
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

     void showDialog(String message, String sts) {
        new AlertDialog.Builder(context, R.style.AlertDialog)

                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    if (sts.equals("2"))
                        finish();
                    else if (sts.equals("0"))
                        dialogInterface.dismiss();
                }).show();
    }

    private void addBankDetail() {
        if (status == 1) {
            progressBarHelper.showProgressDialog();

            Call<List<Contest>> call = api.updateBankInfo(
                    AppConstant.PURCHASE_KEY,
                    Preferences.getInstance(context).getString(Preferences.KEY_USER_ID),
                    accNameTv.getText().toString(),
                    accNoTv.getText().toString(),
                    ifscCodeTv.getText().toString(),
                    panNoTv.getText().toString(),
                    uriFilePAN,
                    uriFile,
                    bankName,
                    "0");
            call.enqueue(new Callback<List<Contest>>() {
                @Override
                public void onResponse(@NonNull Call<List<Contest>> call, @NonNull Response<List<Contest>> response) {

                    Log.i("testBank", "onResponse: "+response.isSuccessful());
                    Log.i("testBank", "onResponse: "+response.code());

                    progressBarHelper.hideProgressDialog();
                    if (response.isSuccessful()) {
                        List<Contest> legalData = response.body();
                        if (legalData != null) {
                            Function.showToast(context, legalData.get(0).getMsg());
                            setResult(101);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Contest>> call, @NonNull Throwable t) {

                    Log.e("testBank", "onResponse: "+t.getMessage());
                    progressBarHelper.hideProgressDialog();
                }
            });
        } else {
            progressBarHelper.showProgressDialog();

            Call<List<Contest>> call = api.updateBankInfoWithoutProofCopy(AppConstant.PURCHASE_KEY,
                    Preferences.getInstance(context).getString(Preferences.KEY_USER_ID),
                    accNameTv.getText().toString(),
                    accNoTv.getText().toString(),
                    ifscCodeTv.getText().toString(),
                    panNoTv.getText().toString(),
                    bankName,
                    "0");
            call.enqueue(new Callback<List<Contest>>() {
                @Override
                public void onResponse(@NonNull Call<List<Contest>> call, @NonNull Response<List<Contest>> response) {
                    progressBarHelper.hideProgressDialog();
                    if (response.isSuccessful()) {
                        List<Contest> legalData = response.body();
                        if (legalData != null) {
                            Function.showToast(context, legalData.get(0).getMsg());
                            setResult(101);
                            finish();
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

    @SuppressLint("SetTextI18n")
    public void updateBankInfo() {

        Log.i("3636Validation", "updateBankInfo: "+status);
        Log.i("3636Validation", "updateBankInfo: "+statusPAC);


        if (accNameTv.getText().toString().equals("") && accNoTv.getText().toString().equals("") && ifscCodeTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("# All fields are mandatory");
        } else if (accNameTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("Please enter account name");
        } else if (bankName.toString().equals("Select your Bank")) {
            textInputError.setText("Please select your bank");
        } else if (accNoTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("Please enter account no");
        }else if (!eAccNumberRenter.getText().toString().equals(accNoTv.getText().toString())) {
            textInputError.setText("# All fields are mandatory");
            tErrorBankAccount.setVisibility(View.VISIBLE);
//            tErrorBankAccount.setVisibility(View.GONE);
        } else if (ifscCodeTv.getText().toString().equals("")) {
            textInputError.setText("");
            textInputError.setText("Please enter ifsc code");
        } else if (status == 0) {
            textInputError.setText("");
            textInputError.setText("Attach any bank account proof for verification");
        } else if (statusPAC == 0) {
            textInputError.setText("");
            textInputError.setText("You have not uploaded any PAN card");
        }else if (!Utills.newInstance().isValidAlphaNumeric(panNoTv.getText().toString())) {
            textInputError.setText("# All fields are mandatory");
        }
        else {
            addBankDetail();
        }
    }

    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAddProfilePicDialog();
            }
        }
    }

    private void pickImage() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_PICK_GALLERY);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void takePic() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bmp != null) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == REQUEST_CODE_TAKE_PICTURE) {
            try {
                onCaptureImageResultInstrument(result);
            } catch (Exception ex) {
                errorValidation();
            }
            textInputError.setText("");

        } else if (requestCode == REQUEST_CODE_PICK_GALLERY) {
            if (resultCode == RESULT_CANCELED) {
                userCancelled();
            } else if (resultCode == RESULT_OK && result != null && result.getData() != null) {
                try {
                    onGalleryImageResultInstrument(result);
                } catch (Exception e) {
                    errorValidation();
                }
            } else {
                errorValidation();
            }
            textInputError.setText("");

        }
    }

    public void userCancelled() {

    }

    public void errorValidation() {
        Intent intent = new Intent();
        intent.putExtra(ERROR, true);
        intent.putExtra(ERROR_MSG, "Error while opening the image file. Please try again.");
        finish();
    }

    @Override
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(Constants.PicModes.CAMERA) ? Constants.IntentExtras.ACTION_CAMERA : Constants.IntentExtras.ACTION_GALLERY;
        if (action.equals(Constants.IntentExtras.ACTION_CAMERA)) {
            takePic();
        } else {
            pickImage();
        }
    }

    String typeImage = "";
    private void onCaptureImageResultInstrument(Intent data) {
        //Getting the Bitmap from Gallery
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        //Setting the Bitmap to ImageView
        if (typeImage.equals("PAN")){
            uriFilePAN = getStringImage(bitmap);

            imgPAN.setImageBitmap(bitmap);
            imgPAN.setVisibility(View.VISIBLE);
            statusPAC = 1;

        }else {
            uriFile = getStringImage(bitmap);
            status = 1;

            proofCopyTv.setVisibility(View.VISIBLE);
            proofCopyTv.setImageBitmap(bitmap);
        }


    }

    private void onGalleryImageResultInstrument(Intent data) {
        final Uri saveUri = data.getData();

        try {
            //Getting the Bitmap from Gallery
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), saveUri);

            if (typeImage.equals("PAN")){
                uriFilePAN = getStringImage(bitmap);

                imgPAN.setImageBitmap(bitmap);
                imgPAN.setVisibility(View.VISIBLE);
                statusPAC = 1;

            }else {
                uriFile = getStringImage(bitmap);
                status = 1;

                proofCopyTv.setVisibility(View.VISIBLE);
                proofCopyTv.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addImage() {
        typeImage = "";
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                showAddProfilePicDialog();
            }
        } else {
            showAddProfilePicDialog();
        }
    }

    public void proofCopyCardView() {
        typeImage = "";

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                showAddProfilePicDialog();
            }
        } else {
            showAddProfilePicDialog();
        }
    }

    public void addImagePAN() {
        typeImage = "PAN";
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                showAddProfilePicDialog();
            }
        } else {
            showAddProfilePicDialog();
        }
    }

}
