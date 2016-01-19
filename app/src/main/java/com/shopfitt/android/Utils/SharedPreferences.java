package com.shopfitt.android.Utils;

import android.content.Context;

public class SharedPreferences {

    protected static final String UTF8 = "utf-8";
    private static final String ALGO = "AES";

    private static String TAG = "SharedPrefs";
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
        String encryptedLogin = sharedPreferences.getString(SHARED_PREFERENCES_LOGIN_ID, defaultValue);
//        return decryptCredentials(encryptedLogin, key);
        return  encryptedLogin;
    }

    public void setPassword(String value) {
//        String encryptPassword = encryptCredentials(value, key);
        editor.putString(SHARED_PREFERENCES_PASSWORD, value);
        editor.commit();
    }

    public String getPassword(String defaultValue) {
        String encryptedPwd = sharedPreferences.getString(SHARED_PREFERENCES_PASSWORD, defaultValue);
//        return decryptCredentials(encryptedPwd, key);
        return  encryptedPwd;
    }

    public void setShowYoutubeVideo(boolean show){
        editor.putBoolean(SHARED_PREFERENCES_YOUTUBE_VIEW, show);
        editor.commit();
    }

    public boolean getShowYoutubeVideo(){
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_YOUTUBE_VIEW, true);
    }

    public void setLocation(String location){
        editor.putString(SHARED_PREFERENCES_LOCATION_PREFERENCE, location);
        editor.commit();
    }

    public String getLocation(){
        return sharedPreferences.getString(SHARED_PREFERENCES_LOCATION_PREFERENCE, "");
    }

    public void setOutlet(String outlet){
        editor.putString(SHARED_PREFERENCES_STORE_PREF, outlet);
        editor.commit();
    }

    public String getOutlet() {
        return sharedPreferences.getString(SHARED_PREFERENCES_STORE_PREF, "");
    }

    public void setCustomerID(String outlet){
        editor.putString(SHARED_PREFERENCES_USER_ID, outlet);
        editor.commit();
    }

    public String getCustomerId(){
        return sharedPreferences.getString(SHARED_PREFERENCES_USER_ID, "");
    }

    public void setPhoneNumber(String outlet){
        editor.putString(SHARED_PREFERENCES_PHONE, outlet);
        editor.commit();
    }

    public String getPhoneNumber(){
        return sharedPreferences.getString(SHARED_PREFERENCES_PHONE, "");
    }

    public void performLogin(boolean outlet){
        editor.putBoolean(SHARED_PREFERENCES_PERFORM_LOGIN, outlet);
        editor.commit();
    }

    public boolean performLogin(){
        return sharedPreferences.getBoolean(SHARED_PREFERENCES_PERFORM_LOGIN, true);
    }


}
