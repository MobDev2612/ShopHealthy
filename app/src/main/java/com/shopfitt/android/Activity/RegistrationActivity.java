package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.CustomVolleyRequest;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener {

    private TextInputEditText name_edt_text, email_edt_text, username_edt_text, pwd_edt_text, confirm_pwd_edt_text, phone_edt_text, otp_edt_text;
    private TextInputLayout name_edt_text_layout, email_edt_text_layout, username_edt_text_layout, pwd_edt_text_layout, confirm_pwd_edt_text_layout, phone_edt_text_layout, otp_edt_text_layout;
    private Button get_otp_button, verify_otp_button, register_button;

    private String name_text, email_text, username_text, pwd_text, confirm_pwd_text, phone_text;
    private int requestID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialiseComponents();
    }

    private void initialiseComponents() {
        name_edt_text = (TextInputEditText) findViewById(R.id.name);
        email_edt_text = (TextInputEditText) findViewById(R.id.email);
        username_edt_text = (TextInputEditText) findViewById(R.id.username);
        pwd_edt_text = (TextInputEditText) findViewById(R.id.password);
        confirm_pwd_edt_text = (TextInputEditText) findViewById(R.id.confirm_pwd);
        phone_edt_text = (TextInputEditText) findViewById(R.id.phone);
        otp_edt_text = (TextInputEditText) findViewById(R.id.otp_verify_text);

        name_edt_text_layout = (TextInputLayout) findViewById(R.id.name_layout);
        email_edt_text_layout = (TextInputLayout) findViewById(R.id.email_layout);
        username_edt_text_layout = (TextInputLayout) findViewById(R.id.username_layout);
        pwd_edt_text_layout = (TextInputLayout) findViewById(R.id.pwd_layout);
        confirm_pwd_edt_text_layout = (TextInputLayout) findViewById(R.id.confirm_pwd_layout);
        phone_edt_text_layout = (TextInputLayout) findViewById(R.id.phone_layout);
        otp_edt_text_layout = (TextInputLayout) findViewById(R.id.otp_verify_text_layout);

        get_otp_button = (Button) findViewById(R.id.get_otp);
        verify_otp_button = (Button) findViewById(R.id.verify_otp_button);
        register_button = (Button) findViewById(R.id.register);

        name_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        email_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        pwd_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        username_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        confirm_pwd_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        phone_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        otp_edt_text.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));

        name_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        email_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        username_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        pwd_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        confirm_pwd_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        phone_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        otp_edt_text_layout.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));

        get_otp_button.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        verify_otp_button.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));
        register_button.setTypeface(Font.getTypeface(this,Font.FONT_AWESOME));

        get_otp_button.setOnClickListener(this);
        verify_otp_button.setOnClickListener(this);
        register_button.setOnClickListener(this);
    }

    private void registerUser() {
        name_edt_text_layout.setError(null);
        email_edt_text_layout.setError(null);
        username_edt_text_layout.setError(null);
        pwd_edt_text_layout.setError(null);
        confirm_pwd_edt_text_layout.setError(null);
        phone_edt_text_layout.setError(null);
        otp_edt_text_layout.setError(null);

        name_text = name_edt_text.getText().toString();
        email_text = email_edt_text.getText().toString();
        username_text = username_edt_text.getText().toString();
        pwd_text = pwd_edt_text.getText().toString();
        confirm_pwd_text = confirm_pwd_edt_text.getText().toString();
        phone_text = phone_edt_text.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(name_text)) {
            name_edt_text_layout.setError(getString(R.string.error_invalid_password));
            focusView = name_edt_text;
            cancel = true;
        }

        if (TextUtils.isEmpty(email_text)) {
            email_edt_text_layout.setError(getString(R.string.error_field_required));
            focusView = email_edt_text;
            cancel = true;
        }

        if (TextUtils.isEmpty(username_text)) {
            username_edt_text_layout.setError(getString(R.string.error_field_required));
            focusView = username_edt_text;
            cancel = true;
        }

        if (TextUtils.isEmpty(pwd_text)) {
            pwd_edt_text_layout.setError(getString(R.string.error_field_required));
            focusView = pwd_edt_text;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirm_pwd_text)) {
            confirm_pwd_edt_text_layout.setError(getString(R.string.error_field_required));
            focusView = confirm_pwd_edt_text;
            cancel = true;
        }

        if (!pwd_text.equals(confirm_pwd_text)) {
            confirm_pwd_edt_text_layout.setError(getString(R.string.error_field_required));
            focusView = confirm_pwd_edt_text;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone_text)) {
            phone_edt_text_layout.setError(getString(R.string.error_field_required));
            focusView = phone_edt_text;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Email", email_text);
                jsonObject.put("Mobile",phone_text);
                jsonObject.put("Name",name_text);
                jsonObject.put("Password",pwd_text);
                jsonObject.put("Username",username_text);
                performRegistration(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        String number = phone_edt_text.getText().toString();
        if (v == get_otp_button) {
            if(!number.isEmpty()){
                requestOTP(number);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.verify_otp_layout);
                linearLayout.setVisibility(View.VISIBLE);
//                register_button.setVisibility(View.VISIBLE);
//                get_otp_button.setVisibility(View.GONE);
            } else {
                phone_edt_text_layout.setError(getString(R.string.error_field_required));
            }
        } else if (v == verify_otp_button){
            String otp = otp_edt_text.getText().toString();
            if(!otp.isEmpty()){
                verifyOTP(number, otp);
            } else {
                otp_edt_text_layout.setError(getString(R.string.error_field_required));
            }
        } else if( v == register_button){
            registerUser();
        }

    }

    private void verifyOTP(String number, String otp) {
        requestID =2;
        CommonMethods.showProgress(true,this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", number);
            jsonObject.put("OTP", otp);
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/confirmMobile/",String.class,jsonObject,
                    this,this);
            volleyRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 30000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 0;
                }

                @Override
                public void retry(VolleyError volleyError) throws VolleyError {

                }
            });
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "verifyotpapi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestOTP(String number) {
        requestID =1;
        CommonMethods.showProgress(true,this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", number);
            VolleyRequest<String> volleyRequest = new VolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SendConfirmationOTP/",String.class,null,
                    this,this,jsonObject);
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "sendotpapi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void performRegistration(JSONObject jsonObject) {
        requestID =3;
        CommonMethods.showProgress(true,this);
        try {
            VolleyRequest<JSONObject> request = new VolleyRequest<JSONObject>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveCustomer/",JSONObject.class,null,
                    this,this,jsonObject);
            Shopfitt.getInstance().addToRequestQueue(request, "registerapi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, this);
        if(requestID != 1) {
            Toast.makeText(this, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(Object o) {
        CommonMethods.showProgress(false,this);
        if(requestID == 2){
            String result = (String) o;
            if(result.equalsIgnoreCase("1")) {
                register_button.setVisibility(View.VISIBLE);
            } else if (result.equalsIgnoreCase("0")){
                    otp_edt_text_layout.setError("Wrong OTP");
            }
        }
        if(requestID == 3){
            String result = (String) o;
            if(result.equalsIgnoreCase("1")) {
                SharedPreferences sharedPreferences = new SharedPreferences(this);
                sharedPreferences.setLoginID(username_text);
                sharedPreferences.setPassword(pwd_text);
                Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else if (result.equalsIgnoreCase("0")){
                Toast.makeText(this,"Something went wrong.. please try later",Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    public void onStop() {
        CommonMethods.showProgress(false,this);
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
