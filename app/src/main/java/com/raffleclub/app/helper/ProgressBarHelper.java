package com.raffleclub.app.helper;

import android.app.ProgressDialog;
import android.content.Context;
import com.raffleclub.app.listner.ProgressListener;


public class ProgressBarHelper implements ProgressListener {
    private final ProgressDialog dialog;

    public ProgressBarHelper(Context context, boolean isCancelable) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
        dialog.setMessage("Please wait...");
    }

    @Override
    public void showProgressDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
