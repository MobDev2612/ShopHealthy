package com.shopfitt.android.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;

import com.shopfitt.android.R;

public class CustomProgressDialog {

    private static CustomProgressDialog instance = new CustomProgressDialog();
    private android.app.ProgressDialog mProgressDialog;

    private CustomProgressDialog() {
    }

    /**
     * Returns instance of class
     *
     * @return CustomProgressDialog
     */
    public static CustomProgressDialog getInstance() {
        return instance;
    }

    /**
     * Dismiss progress dialog
     */
    public void dismissProgress() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    /**
     * Shows progress dialog
     *
     * @param msg     message to be shown on progress dialog while loading
     * @param context Context of the activity
     */
    public void showProgress(String msg, Context context) {

        String title = context.getResources().getString(R.string.app_name);
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            dismissProgress();
        }
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(Html.fromHtml(title));
        mProgressDialog.setMessage(msg);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
}
