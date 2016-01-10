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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CustomProgressDialog;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.Utils.Validation;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    private EditText mEmailView;
    private EditText mPasswordView;
    private TextInputLayout mailInputLayout,pwdInputLayout;

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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mailInputLayout.setError(null);
        pwdInputLayout.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !Validation.isValidPassword(password)) {
            pwdInputLayout.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mailInputLayout.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Validation.isValidEmailPattern(email)) {
            mailInputLayout.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            performLogin();
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

    private void performLogin() {
        VolleyRequest<String> performLogin = new VolleyRequest<String>(Request.Method.GET,
                "https://faasos.0x10.info/api/faasos?type=json&query=api_hits",
                String.class, null,
                this, this); //TODO
        Shopfitt.getInstance().addToRequestQueue(performLogin, "loginapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        showProgress(false);
        pwdInputLayout.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }

    @Override
    public void onResponse(String s) {
        showProgress(false);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}

