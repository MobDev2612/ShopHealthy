package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.shopfitt.android.Network.CustomVolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrunchFragmentOne extends Fragment implements Response.ErrorListener, Response.Listener, View.OnClickListener {

    private View view;
    int requestId=0;
    List<String> list;
    private Button imageButton1,imageButton2,imageButton3,imageButton4;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public CrunchFragmentOne() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_crunch_fragment_one, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Crunchathon");
        initialiseComponents();
        getOptions();
    }

    private void initialiseComponents() {
        imageButton1 = (Button) view.findViewById(R.id.crunch_match_1);
        imageButton2 = (Button) view.findViewById(R.id.crunch_match_2);
        imageButton3 = (Button) view.findViewById(R.id.crunch_match_3);
        imageButton4 = (Button) view.findViewById(R.id.crunch_match_4);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
    }

    private void getOptions() {
        requestId = 1;
        CommonMethods.showProgress(true, mContext);
        StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/getCustomertoCompare/" + Config.customerID,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "getOptions");
    }

    private void getOpponentOrderID(String customerId) {
        requestId = 3;
        customerId= customerId.replaceAll("\"","");
        CommonMethods.showProgress(true, mContext);
        StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/getMatchingOrder/" + customerId,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "getOpponentOrderId");
    }

    private void doCrunchMatch(String opponentOrderId) {
        requestId =2;
        CommonMethods.showProgress(true, mContext);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comparercustid", Config.comparerID.replaceAll("\"",""));
            jsonObject.put("corderID", opponentOrderId);
            jsonObject.put("orderID", Config.orderId);
            jsonObject.put("custid", Config.customerID);
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/match/",String.class,jsonObject,
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
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "doCrunchMatch");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        Toast.makeText(mContext, "Unable to fetch details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object o) {
        CommonMethods.showProgress(false, mContext);
        if (requestId == 1) {
            String response = (String) o;
            String[] items = response.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
            list = Arrays.asList(items);
            showBalls(list.size());
        }
        if (requestId == 2) {
            String response = (String) o;
            if(response.equalsIgnoreCase("1")) {
                Config.crunchWon =true;
            } else if (response.equalsIgnoreCase("0")){
                Config.crunchWon =false;
            }
            showNextScreen();
        } if (requestId == 3){
            String response = (String) o;
            Config.comparerOrderID = response;
            doCrunchMatch(response);
        }
    }

    private void showBalls(int number){
        if(number >= 1){
            imageButton1.setVisibility(View.VISIBLE);
        }
        if(number >= 2){
            imageButton2.setVisibility(View.VISIBLE);
        }
        if(number >= 3){
            imageButton3.setVisibility(View.VISIBLE);
        }
        if(number >= 4){
            imageButton4.setVisibility(View.VISIBLE);
        }

    }

    private void showNextScreen() {
        Fragment fragment = new CrunchathonTableFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v == imageButton1){
            Config.comparerID = list.get(0);
        } else  if(v == imageButton2){
            Config.comparerID = list.get(1);
        } else  if(v == imageButton3){
            Config.comparerID = list.get(2);
        } else  if(v == imageButton4){
            Config.comparerID = list.get(3);
        }
        getOpponentOrderID(Config.comparerID);
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }
}
