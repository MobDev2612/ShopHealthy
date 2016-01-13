package com.shopfitt.android.Utils;

import android.content.Context;

/**
 * Created by Hari Haran on 13-Jan-16.
 */
public class CommonMethods {
    public static void showProgress(final boolean show,Context context) {
        CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance();
        if (show) {
            customProgressDialog.showProgress("Please wait", context);
        } else {
            customProgressDialog.dismissProgress();
        }
    }
}
