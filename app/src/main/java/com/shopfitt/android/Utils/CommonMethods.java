package com.shopfitt.android.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

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

    public static void setActionBarProperties(Context context, Activity activity, int drawableResourceId) {
        try {
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Logger.e("test", e.getLocalizedMessage(), e);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((AppCompatActivity) activity).getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(drawableResourceId, null));
        } else {
            ((AppCompatActivity) activity).getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(drawableResourceId));
        }
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
