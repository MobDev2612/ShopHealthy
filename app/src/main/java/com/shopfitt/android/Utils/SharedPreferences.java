package com.shopfitt.android.Utils;

import android.content.Context;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

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
    public static final String SHARED_PREFERENCES_LOGIN_TOKEN = "Key3";//"LoginToken";
    public static final String SHARED_PREFERENCES_GCM_REGISTRATION_ID = "Key4";//"GCMRegistrationID";
    public static final String SHARED_PREFERENCES_YOUTUBE_VIEW = "Key5";//"youtubeview";
    public static final String SHARED_PREFERENCES_LOCATION_PREFERENCE = "Key6";
    public static final String SHARED_PREFERENCES_STORE_PREF = "Key7";

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

    public void setLoginToken(String value) {
        editor.putString(SHARED_PREFERENCES_LOGIN_TOKEN, value);
        editor.commit();
    }

    public String getLoginToken(String defaultValue) {
        return sharedPreferences.getString(SHARED_PREFERENCES_LOGIN_TOKEN, defaultValue);
    }


    public void setGCMRegistrationID(String registrationID) {
        editor.putString(SHARED_PREFERENCES_GCM_REGISTRATION_ID, registrationID);
        editor.commit();
    }

    public String getGCMRegistrationID(String defaultValue) {
        return sharedPreferences.getString(SHARED_PREFERENCES_GCM_REGISTRATION_ID, defaultValue);
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

    public String getOutlet(){
        return sharedPreferences.getString(SHARED_PREFERENCES_STORE_PREF,"");
    }



    protected String encryptCredentials(String value, SecretKey key) {
        if (key != null) {
            try {
                final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
                Cipher ulCipher = Cipher.getInstance(ALGO);
                ulCipher.init(Cipher.ENCRYPT_MODE, key);

                return new String(Base64.encode(ulCipher.doFinal(bytes), Base64.NO_WRAP), UTF8);

            } catch (Exception e) {
                Logger.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return new String();
    }

    protected String decryptCredentials(String value, SecretKey key) {
        try {
            final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            Cipher ulCipher = Cipher.getInstance(ALGO);
            ulCipher.init(Cipher.DECRYPT_MODE, key);
            return new String(ulCipher.doFinal(bytes), UTF8);
        } catch (Exception e) {
            Logger.e(TAG, e.getLocalizedMessage(), e);
            return "";
        }
    }

}
