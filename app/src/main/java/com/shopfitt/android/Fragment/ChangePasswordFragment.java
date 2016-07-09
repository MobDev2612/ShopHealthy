package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    private View view;
    private Context mContext;
    private TextInputEditText newPassword, newConfirmPassword;
    private Button changePassword;
    private TextInputLayout newPasswordLayout, newConfirmPasswordLayout;
    private String password, confirmPassword;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseComponents();
    }

    private void initialiseComponents() {
        changePassword = (Button) view.findViewById(R.id.change_password_button);
        newPassword = (TextInputEditText) view.findViewById(R.id.password);
        newConfirmPassword = (TextInputEditText) view.findViewById(R.id.confirm_pwd);
        newPasswordLayout = (TextInputLayout) view.findViewById(R.id.pwd_layout);
        newConfirmPasswordLayout = (TextInputLayout) view.findViewById(R.id.confirm_pwd_layout);

        changePassword.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        newConfirmPassword.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        newPassword.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        newPasswordLayout.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        newConfirmPasswordLayout.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        newPasswordLayout.setError(null);
        newConfirmPasswordLayout.setError(null);

        password = newPassword.getText().toString();
        confirmPassword = newConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            newPasswordLayout.setError(getString(R.string.error_field_required));
            focusView = newPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            newConfirmPasswordLayout.setError(getString(R.string.error_field_required));
            focusView = newConfirmPassword;
            cancel = true;
        }

        if (!password.equals(confirmPassword)) {
            newConfirmPasswordLayout.setError(getString(R.string.error_field_required));
            focusView = newConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ID", Config.customerID);
                jsonObject.put("changepassword", password);
                performWebservice(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void performWebservice(JSONObject jsonObject) {
        CommonMethods.showProgress(true, mContext);
        try {
            VolleyRequest<String> request = new VolleyRequest<String>(Request.Method.POST, "http://23.91.69.85:61090/ProductService.svc/ChangePassword/", String.class, null,
                    this, this, jsonObject);
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
        if (s.contains("1")) {
            SharedPreferences sharedPreferences = new SharedPreferences(mContext);
            sharedPreferences.setPassword(password);
            showHome();
        } else if (s.contains("0")) {
            Toast.makeText(mContext, "Something went wong. Please try later", Toast.LENGTH_LONG).show();
        }
    }

    private void showHome(){
        Toast.makeText(mContext, "Password Changed Successfully", Toast.LENGTH_LONG).show();
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
