package com.shopfitt.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.CustomerAddress;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressHistoryFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {

    private View view;
    private ListView listView;
    private Context mContext;
    private OnFragmentInteractionListener mListener;
    List<CustomerAddress> customerAddresses;

    public AddressHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_address_history, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) view.findViewById(R.id.address_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onButtonPressed(customerAddresses.get(position));
            }
        });
        getAddresses();
    }

    public void onButtonPressed(CustomerAddress customerAddress) {
        if (mListener != null) {
            mListener.onFragmentInteraction(customerAddress);
        }
    }

    private void getAddresses() {
        CommonMethods.showProgress(true, mContext);
        JsonArrayRequest fetchAddress = new JsonArrayRequest("http://23.91.69.85:61090/ProductService.svc/GetCustAddresses/" + Config.customerID, this, this);
        fetchAddress.setRetryPolicy(new RetryPolicy() {
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
        Shopfitt.getInstance().addToRequestQueue(fetchAddress, "address");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false, mContext);
        Toast.makeText(mContext, "Unable to fetch addresses", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            CommonMethods.showProgress(false, mContext);
            if(jsonArray.length() > 0) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                customerAddresses = Arrays.asList(gson.fromJson(jsonArray.toString(), CustomerAddress[].class));
                setList(customerAddresses);
            } else{
                Toast.makeText(mContext, "No Previous found!!!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "Error in fetching addresses", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<CustomerAddress> customerAddresses) {
        String[] countries;
        List<String> stringList = new ArrayList<>(customerAddresses.size());
        for (CustomerAddress customerAddress : customerAddresses) {
            stringList.add(customerAddress.toString());
        }
        countries = stringList.toArray(new String[stringList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.address_list, countries);
        listView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false, mContext);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(CustomerAddress customerAddress);
    }
}
