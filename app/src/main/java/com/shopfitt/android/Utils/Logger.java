package com.shopfitt.android.Utils;

import android.util.Log;

public class Logger {

    private static String TAG = "ShopFitt ";

    private Logger() {
    }

    public static void e(String tag, String message, Throwable tr) {
        if (message != null) {
            Log.e(TAG + tag, message, tr);
        }
    }

    public static void w(String tag, String message) {
        if (message != null) {
            Log.w(TAG + tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (Config.DEBUG && message != null) {
            Log.i(TAG + tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (Config.DEBUG && message != null) {
            Log.d(TAG + tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (Config.DEBUG && message != null) {
            Log.v(TAG + tag, message);
        }
    }
}
