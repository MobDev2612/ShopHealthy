package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.LoginObject;

import org.json.JSONArray;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONArray> {

    private EditText mEmailView;
    private EditText mPasswordView;
    private TextInputLayout mailInputLayout, pwdInputLayout;
    private String userName, password;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mailInputLayout = (TextInputLayout) findViewById(R.id.email_layout);
        pwdInputLayout = (TextInputLayout) findViewById(R.id.password_layout);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registrationButton = (Button) findViewById(R.id.email_sign_up_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        sharedPreferences = new SharedPreferences(this);
        String username = sharedPreferences.getLoginID("");
        String password = sharedPreferences.getPassword("");
        if(username.length()>0) {
            mEmailView.setText(username);
            mPasswordView.setText(password);
            attemptLogin();
//            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
        }
    }

    private void attemptLogin() {
        mailInputLayout.setError(null);
        pwdInputLayout.setError(null);

        userName = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            pwdInputLayout.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mailInputLayout.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            CommonMethods.showProgress(true, this);
            performLogin(userName);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
//    private void showProgress(final boolean show) {
//        CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance();
//        if (show) {
//            customProgressDialog.showProgress("Logging in", this);
//        } else {
//            customProgressDialog.dismissProgress();
//        }
//    }

    private void performLogin(String userName) {
        JsonArrayRequest performLogin = new JsonArrayRequest(
                "http://json.shopfitt.in/Details.aspx?username=" + userName,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(performLogin, "loginapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,this);
        Toast.makeText(this, "Unable to login..please try later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONArray s) {
        CommonMethods.showProgress(false, this);
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            LoginObject loginObject = gson.fromJson(s.get(0).toString(), LoginObject.class);
            if (loginObject.getUsername().equalsIgnoreCase(userName) && loginObject.getPassword().equals(password)) {
                loginSuccess(loginObject);
            } else {
                loginFailure();
            }
        } catch (Exception e){
//            Toast.makeText(this,"Error in login..",Toast.LENGTH_LONG).show();
            loginFailure();
        }
    }

    private void loginSuccess(LoginObject loginObject) {
        sharedPreferences.setLoginID(userName);
        sharedPreferences.setPassword(password);
        Config.customerID = loginObject.getID();
        sharedPreferences.setCustomerID(loginObject.getID());
        sharedPreferences.setPhoneNumber(loginObject.getMobile());
        Config.loginDone = true;
//        showProgress(false);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginFailure() {
        pwdInputLayout.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }
}

