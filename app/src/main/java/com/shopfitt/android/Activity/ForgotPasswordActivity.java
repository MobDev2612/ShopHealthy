package com.shopfitt.android.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.Shopfitt;

public class ForgotPasswordActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String> {

    private String email;
    private EditText emailView;
    private TextInputLayout emailViewLayout;
    private Button resetPassword;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseComponents();
    }

    private void initialiseComponents() {
        mContext = this;
        emailView = (EditText) findViewById(R.id.forgot_password_email);
        emailViewLayout = (TextInputLayout) findViewById(R.id.forgot_password_email_layout);
        resetPassword = (Button) findViewById(R.id.reset_password);

        emailView.setTypeface(Font.getTypeface(this, Font.FONTAWESOME));
        emailViewLayout.setTypeface(Font.getTypeface(this, Font.FONTAWESOME));
        resetPassword.setTypeface(Font.getTypeface(this, Font.FONTAWESOME));

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailView.getText().toString();
                if (email.isEmpty()) {
                    emailViewLayout.setError(getResources().getString(R.string.error_field_required));
                } else {
                    emailViewLayout.setError(null);
                    resetPassword();
                }
            }
        });
    }

    private void resetPassword() {
        CommonMethods.showProgress(true, mContext);
        try {
            VolleyRequest<String> request = new VolleyRequest<String>(Request.Method.GET, "http://23.91.69.85:61090/ProductService.svc/ForgotPassword/" + email, String.class, null,
                    this, this);
            Shopfitt.getInstance().addToRequestQueue(request, "registerapi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false, mContext);
        super.onStop();
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        Toast.makeText(mContext, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String s) {
        CommonMethods.showProgress(false, mContext);
//        if (s.contains("1")) {
            ForgotPasswordActivity.this.finish();
//        } else if (s.contains("0")) {
//            Toast.makeText(mContext, "Something went wong. Please try later", Toast.LENGTH_LONG).show();
//        }
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
