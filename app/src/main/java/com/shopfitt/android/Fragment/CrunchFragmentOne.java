package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
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
public class CrunchFragmentOne extends Fragment implements Response.ErrorListener, Response.Listener {

    private View view;
    private ListView listView;
    int requestId=0;
    List<String> list;


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
        listView = (ListView) view.findViewById(R.id.crunch_options_layout);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doCrunchMatch(list.get(position));
            }
        });
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
//            JsonArray jsonArray = (JsonArray) o;
            String response = (String) o;
            String[] items = response.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
            list = Arrays.asList(items);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
            listView.setAdapter(arrayAdapter);
        }
        if (requestId == 2) {
            String response = (String) o;
            if(Config.customerID.equalsIgnoreCase(response)){
                Config.crunchWon= true;
            } else {
                Config.crunchWon = false;
            }
            showThankyou();
        }
    }

    private void showThankyou() {
        Fragment fragment = new CrunchFragmentTwo();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
