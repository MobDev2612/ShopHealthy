package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CustomProgressDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
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
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mailInputLayout = (TextInputLayout) findViewById(R.id.email_layout);
        pwdInputLayout = (TextInputLayout) findViewById(R.id.password_layout);
    }

    private void attemptLogin() {
        mailInputLayout.setError(null);
        pwdInputLayout.setError(null);

        userName = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password)) {
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
            showProgress(true);
            performLogin(userName);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance();
        if (show) {
            customProgressDialog.showProgress("Logging in", this);
        } else {
            customProgressDialog.dismissProgress();
        }
    }

    private void performLogin(String userName) {
        JsonArrayRequest performLogin = new JsonArrayRequest(
                "http://json.wiing.org/Details.aspx?username=" + userName,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(performLogin, "loginapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(this, "Unable to login..please try later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(JSONArray s) {
        try {
            if (((LoginObject) s.get(0)).getUsername().equalsIgnoreCase(userName) && ((LoginObject) s.get(0)).getPassword().equals(password)) {
                loginSuccess();
            } else {
                loginFailure();
            }
        } catch (Exception e){
            Toast.makeText(this,"Erroring in login..",Toast.LENGTH_LONG).show();
        }
    }

    private void loginSuccess() {
        showProgress(false);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginFailure() {
        showProgress(false);
        pwdInputLayout.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }
}

