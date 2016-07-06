package com.shopfitt.android.Utils;

import android.content.Context;

public class SharedPreferences {

    ObscuredSharedPreferences sharedPreferences;
    ObscuredSharedPreferences.Editor editor;
    Context mContext;

    public static final String SHARED_PREFERENCES_KEY = "SF_SharedPreferences";
    public static final String SHARED_PREFERENCES_LOGIN_ID = "Key1";//"Login_id";
    public static final String SHARED_PREFERENCES_PASSWORD = "Key2";//"Password";
    public static final String SHARED_PREFERENCES_YOUTUBE_VIEW = "Key5";//"youtubeview";
    public static final String SHARED_PREFERENCES_LOCATION_PREFERENCE = "Key6";
    public static final String SHARED_PREFERENCES_STORE_PREF = "Key7";
    public static final String SHARED_PREFERENCES_USER_ID = "Key8";
    public static final String SHARED_PREFERENCES_PHONE = "Key9";
    public static final String SHARED_PREFERENCES_PERFORM_LOGIN = "Key10";
    public static final String SHARED_PREFERENCES_NAME = "Key11";
    public static final String SHARED_PREFERENCES_EMAIL = "Key12";

    public SharedPreferences(Context context) {
        mContext = context;
        sharedPreferences = ObscuredSharedPreferences.getPrefs(context, SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginID(String value) {
//        String encryptLoginID = encryptCredentials(value, key);
        editor.putString(SHARED_PREFERENCES_LOGIN_ID, value);
        editor.commit();
    }

    public String getLoginID(String defaultValue) {
        //        return decryptCredentials(encryptedLogin, key);
        return sharedPreferences.getString(SHARED_PREFERENCES_LOGIN_ID, defaultValue);
    }

    public void setPassword(String value) {
//        String encryptPassword = encryptCredentials(value, key);
        editor.putString(SHARED_PREFERENCES_PASSWORD, value);
        editor.commit();
    }

    public String getPassword(String defaultValue) {
        //        return decryptCredentials(encryptedPwd, key);
        return sharedPreferences.getString(SHARED_PREFERENCES_PASSWORD, defaultValue);
    }

    public void setShowYoutubeVideo(boolean show) {
        editor.putBoolean(SHARED_PREFERENCES_YOUTUBE_VIEW, show);
        editor.commit();
    }

    public boolean getShowYoutubeVideo() {
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_YOUTUBE_VIEW, true);
    }

    public void setLocation(String location) {
        editor.putString(SHARED_PREFERENCES_LOCATION_PREFERENCE, location);
        editor.commit();
    }

    public String getLocation() {
        return sharedPreferences.getString(SHARED_PREFERENCES_LOCATION_PREFERENCE, "");
    }

    public void setName(String location) {
        editor.putString(SHARED_PREFERENCES_NAME, location);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(SHARED_PREFERENCES_NAME, "");
    }

    public void setEmail(String location) {
        editor.putString(SHARED_PREFERENCES_EMAIL, location);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, "");
    }

    public void setOutlet(String outlet) {
        editor.putString(SHARED_PREFERENCES_STORE_PREF, outlet);
        editor.commit();
    }

    public String getOutlet() {
        return sharedPreferences.getString(SHARED_PREFERENCES_STORE_PREF, "");
    }

    public void setCustomerID(String outlet) {
        editor.putString(SHARED_PREFERENCES_USER_ID, outlet);
        editor.commit();
    }

    public String getCustomerId() {
        return sharedPreferences.getString(SHARED_PREFERENCES_USER_ID, "");
    }

    public void setPhoneNumber(String outlet) {
        editor.putString(SHARED_PREFERENCES_PHONE, outlet);
        editor.commit();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(SHARED_PREFERENCES_PHONE, "");
    }

    public void clearAll() {
        editor.putString(SHARED_PREFERENCES_PHONE, null);
        editor.putString(SHARED_PREFERENCES_USER_ID, null);
        editor.putString(SHARED_PREFERENCES_STORE_PREF, null);
        editor.putString(SHARED_PREFERENCES_EMAIL, null);
        editor.putString(SHARED_PREFERENCES_LOGIN_ID, null);
        editor.putString(SHARED_PREFERENCES_PASSWORD, null);
        editor.putString(SHARED_PREFERENCES_NAME, null);
        editor.putString(SHARED_PREFERENCES_LOCATION_PREFERENCE, null);
        editor.commit();
    }


}
