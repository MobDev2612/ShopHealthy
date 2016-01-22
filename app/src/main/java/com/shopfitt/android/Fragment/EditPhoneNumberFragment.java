package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.CustomVolleyRequest;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
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
    private Button get_otp_button,verify_otp_button,register_button;
    private EditText phone_edt_text,otp_edt_text;
    TextInputLayout phone_edt_text_layout,otp_edt_text_layout;
    SharedPreferences sharedPreferences;
    private Context mContext;

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
        phone_edt_text = (EditText) view.findViewById(R.id.phone);
        otp_edt_text = (EditText) view.findViewById(R.id.otp_verify_text);
        phone_edt_text_layout = (TextInputLayout) view.findViewById(R.id.phone_layout);
        otp_edt_text_layout = (TextInputLayout) view.findViewById(R.id.otp_verify_text_layout);

        get_otp_button = (Button) view.findViewById(R.id.get_otp);
        verify_otp_button = (Button) view.findViewById(R.id.verify_otp_button);
        register_button = (Button) view.findViewById(R.id.register);

        get_otp_button.setOnClickListener(this);
        verify_otp_button.setOnClickListener(this);
        register_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String number = phone_edt_text.getText().toString();
        String oldnumber = sharedPreferences.getPhoneNumber();
        if (v == get_otp_button) {
            if(!number.isEmpty()){
                requestOTP(number);
//                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.verify_otp_layout);
//                linearLayout.setVisibility(View.VISIBLE);
                register_button.setVisibility(View.VISIBLE);
                get_otp_button.setVisibility(View.GONE);
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
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("newnumber", number);
                jsonObject.put("oldnumber", oldnumber);
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
        CommonMethods.showProgress(true,mContext);
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

    private void saveNumber(JSONObject jsonObject) {
        requestID =3;
        CommonMethods.showProgress(true,mContext);
        try {
            VolleyRequest<JSONObject> request = new VolleyRequest<JSONObject>(Request.Method.POST," http://23.91.69.85:61090/ProductService.svc/updateMobileNumber/",JSONObject.class,null,
                    this,this,jsonObject);
            Shopfitt.getInstance().addToRequestQueue(request, "registerapi");
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
                register_button.setVisibility(View.VISIBLE);
            } else if (result.equalsIgnoreCase("0")){
                otp_edt_text_layout.setError("Wrong OTP");
            }
        }
        if(requestID == 3){
            String result = (String) o;
            if(result.equalsIgnoreCase("1")) {
                sharedPreferences.setPhoneNumber(phone_edt_text.getText().toString());
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
        Log.e("test", "test1");
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }

}
