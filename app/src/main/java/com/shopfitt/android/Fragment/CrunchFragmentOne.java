package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
//    private ListView listView;
    int requestId=0;
    List<String> list;
    private ImageButton imageButton1,imageButton2,imageButton3,imageButton4;


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
        initialiseComponents();
        getOptions();
    }

    private void initialiseComponents() {
        imageButton1 = (ImageButton) view.findViewById(R.id.crunch_match_1);
        imageButton2 = (ImageButton) view.findViewById(R.id.crunch_match_2);
        imageButton3 = (ImageButton) view.findViewById(R.id.crunch_match_3);
        imageButton4 = (ImageButton) view.findViewById(R.id.crunch_match_4);
        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
//        listView = (ListView) view.findViewById(R.id.crunch_options_layout);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                doCrunchMatch(list.get(position));
//            }
//        });
    }

    private void getOptions() {
        requestId = 1;
        CommonMethods.showProgress(true, getActivity());
        StringRequest fetchLocations = new StringRequest("http://23.91.69.85:61090/ProductService.svc/getCustomertoCompare/" + Config.customerID,
                this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    private void doCrunchMatch(String comparercustid) {
        requestId =2;
        CommonMethods.showProgress(true,getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("comparercustid", comparercustid);
            jsonObject.put("corderID", "");
            jsonObject.put("orderID", Config.orderId);
            jsonObject.put("custid", Config.customerID);
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/match/",String.class,jsonObject,
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

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, getActivity());
        Toast.makeText(getActivity(), "Unable to fetch details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Object o) {
        CommonMethods.showProgress(false, getActivity());
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
            showThankyou();
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

    private void showThankyou() {
        Fragment fragment = new CrunchFragmentTwo();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v == imageButton1){
            doCrunchMatch(list.get(0));
        } else  if(v == imageButton2){
            doCrunchMatch(list.get(1));
        } else  if(v == imageButton3){
            doCrunchMatch(list.get(2));
        } else  if(v == imageButton4){
            doCrunchMatch(list.get(3));
        }

    }
}
