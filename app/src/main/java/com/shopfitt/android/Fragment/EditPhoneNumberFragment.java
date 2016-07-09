package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPhoneNumberFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener {
    private int requestID=0;
    private View view;
    private Button getOtpButton;
    private Button verifyOtpButton;
    private Button registerButton;
    private TextInputEditText phoneEdtText, otpEdtText;
    TextInputLayout phoneEdtTextLayout, otpEdtTextLayout;
    SharedPreferences sharedPreferences;
    private Context mContext;
    private FontView emailView;
    private FontView phoneView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public EditPhoneNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_phone_number, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedPreferences = new SharedPreferences(mContext);
        initialiseComponents();
    }

    private void initialiseComponents() {
        FontView nameView = (FontView) view.findViewById(R.id.profile_name);
        emailView = (FontView) view.findViewById(R.id.profile_email);
        phoneView = (FontView) view.findViewById(R.id.profile_number);

        phoneView.setText("Phone: "+sharedPreferences.getPhoneNumber());
        nameView.setText("Name: " + sharedPreferences.getName());
        emailView.setText("Email: "+sharedPreferences.getEmail());

        phoneEdtText = (TextInputEditText) view.findViewById(R.id.phone);
        otpEdtText = (TextInputEditText) view.findViewById(R.id.otp_verify_text);
        phoneEdtTextLayout = (TextInputLayout) view.findViewById(R.id.phone_layout);
        otpEdtTextLayout = (TextInputLayout) view.findViewById(R.id.otp_verify_text_layout);

        getOtpButton = (Button) view.findViewById(R.id.get_otp);
        verifyOtpButton = (Button) view.findViewById(R.id.verify_otp_button);
        registerButton = (Button) view.findViewById(R.id.register);
        Button changeNumberButton = (Button) view.findViewById(R.id.change_number_button);

        phoneEdtText.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        otpEdtText.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        phoneEdtTextLayout.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        otpEdtTextLayout.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        getOtpButton.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        verifyOtpButton.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        registerButton.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
        changeNumberButton.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));

        changeNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneEdtText.setVisibility(View.VISIBLE);
                getOtpButton.setVisibility(View.VISIBLE);
            }
        });

        getOtpButton.setOnClickListener(this);
        verifyOtpButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String number = phoneEdtText.getText().toString();
        String oldNumber = sharedPreferences.getPhoneNumber();
        if (v == getOtpButton) {
            if(!number.isEmpty()){
                requestOTP(number);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.verify_otp_layout);
                linearLayout.setVisibility(View.VISIBLE);
//                registerButton.setVisibility(View.VISIBLE);
//                getOtpButton.setVisibility(View.GONE);
            } else {
                phoneEdtTextLayout.setError(getString(R.string.error_field_required));
            }
        } else if (v == verifyOtpButton){
            String otp = otpEdtText.getText().toString();
            if(!otp.isEmpty()){
                verifyOTP(number, otp);
            } else {
                otpEdtTextLayout.setError(getString(R.string.error_field_required));
            }
        } else if( v == registerButton){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("newnumber", number);
                jsonObject.put("oldnumber", oldNumber);
                saveNumber(jsonObject);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    private void verifyOTP(String number, String otp) {
        requestID =2;
        CommonMethods.showProgress(true, mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", number);
            jsonObject.put("OTP", otp);
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/confirmMobile/",String.class,jsonObject,
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
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "verifyOtp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestOTP(String number) {
        requestID =1;
        CommonMethods.showProgress(true,mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", number);
            VolleyRequest<String> volleyRequest = new VolleyRequest<>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SendConfirmationOTP/",String.class,null,
                    this,this,jsonObject);
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "sendOtp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveNumber(JSONObject jsonObject) {
        requestID =3;
        CommonMethods.showProgress(true,mContext);
        try {
            VolleyRequest<JSONObject> request = new VolleyRequest<>(Request.Method.POST," http://23.91.69.85:61090/ProductService.svc/updateMobileNumber/",JSONObject.class,null,
                    this,this,jsonObject);
            Shopfitt.getInstance().addToRequestQueue(request, "register");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        if(requestID != 1) {
            Toast.makeText(mContext, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(Object o) {
        CommonMethods.showProgress(false,mContext);
        if(requestID == 2){
            String result = (String) o;
            if(result.equalsIgnoreCase("1")) {
                registerButton.setVisibility(View.VISIBLE);
            } else if (result.equalsIgnoreCase("0")){
                otpEdtTextLayout.setError("Wrong OTP");
            }
        }
        if(requestID == 3){
            String result = (String) o;
            if(result.equalsIgnoreCase("1")) {
                sharedPreferences.setPhoneNumber(phoneEdtText.getText().toString());
                Fragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
            } else if (result.equalsIgnoreCase("0")){
                Toast.makeText(mContext, "Something went wrong.. please try again", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }

}
