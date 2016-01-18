package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.CustomDatePicker;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDateFragment extends Fragment implements Response.Listener<String>, Response.ErrorListener {

    private View view;
    private EditText editText;
    private Button submit;
    SharedPreferences sharedPreferences;
    public DeliveryDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delivery_date, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseComponents();
    }

    private void initialiseComponents() {
        sharedPreferences = new SharedPreferences(getActivity());
        editText = (EditText) view.findViewById(R.id.delivery_date);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDatePicker.openDatePicker(getFragmentManager(), editText,getContext());
            }
        });
        submit = (Button) view.findViewById(R.id.delivery_date_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getCustomerId().length()>0) {
                    showThankyou();
                } else {
                    getCustomerId();
                }
            }
        });
    }

    private void getCustomerId() {
        SharedPreferences sharedPreferences = new SharedPreferences(getActivity());
        String userName = sharedPreferences.getLoginID("");
        CommonMethods.showProgress(true, getActivity());
        StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/getCustomerID/" + userName,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, getActivity());
        Toast.makeText(getActivity(), "Unable to fetch details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(String s) {
        CommonMethods.showProgress(false, getActivity());

        sharedPreferences.setCustomerID(s);
        Config.customerID = s;
        showThankyou();
    }

    private void showThankyou() {
        Fragment fragment = new QuestionFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
