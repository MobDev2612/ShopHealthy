package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shopfitt.android.Network.CustomVolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener {

    private EditText name_edt_text, email_edt_text, username_edt_text, pwd_edt_text, confirm_pwd_edt_text, phone_edt_text, otp_edt_text;
    private TextInputLayout name_edt_text_layout, email_edt_text_layout, username_edt_text_layout, pwd_edt_text_layout, confirm_pwd_edt_text_layout, phone_edt_text_layout, otp_edt_text_layout;
    private Button get_otp_button, verify_otp_button, register_button;

    private String name_text, email_text, username_text, pwd_text, confirm_pwd_text, phone_text;
    private int requestID=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initialiseComponents();
    }

    private void initialiseComponents() {
        name_edt_text = (EditText) findViewById(R.id.name);
        email_edt_text = (EditText) findViewById(R.id.email);
        username_edt_text = (EditText) findViewById(R.id.username);
        pwd_edt_text = (EditText) findViewById(R.id.password);
        confirm_pwd_edt_text = (EditText) findViewById(R.id.confirm_pwd);
        phone_edt_text = (EditText) findViewById(R.id.phone);
        otp_edt_text = (EditText) findViewById(R.id.otp_verify_text);

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
            CommonMethods.showProgress(true, this);
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
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SendConfirmationOTP/",String.class,jsonObject,
                    this,this);
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "sendotpapi");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void performRegistration(JSONObject jsonObject) {
        requestID =3;
        CommonMethods.showProgress(true,this);
        try {
            Request<JSONObject> request = new JsonObjectRequest(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveCustomer/",jsonObject,
                    this,this);
            Shopfitt.getInstance().addToRequestQueue(request, "registerapi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,this);
        Toast.makeText(this,"Unable to connect. Please try later",Toast.LENGTH_LONG).show();
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
            SharedPreferences sharedPreferences = new SharedPreferences(this);
            sharedPreferences.setLoginID(username_text);
            sharedPreferences.setPassword(pwd_text);
            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
