package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.OrderHistoryAdapter;
import com.shopfitt.android.datamodels.OrderHistoryObject;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray>  {

    private View view;
    private ListView orderHistoryList;
    private Context mContext;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_history, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseComponents();
    }

    private void initialiseComponents() {
        orderHistoryList = (ListView) view.findViewById(R.id.order_history_list);
        getOrderHistory();
    }

    private void getOrderHistory() {
        CommonMethods.showProgress(true, mContext);
        JsonArrayRequest fetchOrderHistory = new JsonArrayRequest("http://23.91.69.85:61090/ProductService.svc/GetTop10Orders/" + Config.customerID, this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchOrderHistory, "orderHistory");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        Toast.makeText(mContext, "Unable to fetch order history", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            CommonMethods.showProgress(false, mContext);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<OrderHistoryObject> orderHistoryObjects = Arrays.asList(gson.fromJson(jsonArray.toString(), OrderHistoryObject[].class));
            setList(orderHistoryObjects);
        } catch (Exception e) {
            Toast.makeText(mContext, "Error in fetching order history", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<OrderHistoryObject> orderHistoryObjects) {
        OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(mContext, R.layout.order_history_list, orderHistoryObjects);
        orderHistoryList.setAdapter(orderHistoryAdapter);
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false, mContext);
        super.onStop();
    }

}
