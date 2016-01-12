package com.shopfitt.android.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.shopfitt.android.Activity.LoginActivity;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.LocationAdapter;
import com.shopfitt.android.datamodels.OutletObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class OutletFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {

    private View view;
    private ListView outletList;

    public OutletFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_outlet, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        initialiseComponents(arguments);
    }

    private void initialiseComponents(Bundle arguments) {
        outletList = (ListView) view.findViewById(R.id.outlet_list);
        outletList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String outlet = (String) textView.getText();
                goToMenu(outlet);
            }
        });
        getOutlets(arguments.getString("area"));
    }

    private void goToMenu(String outlet) {
        SharedPreferences sharedPreferences = new SharedPreferences(getActivity());
        sharedPreferences.setOutlet(outlet);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void getOutlets(String area) {
        JsonArrayRequest fetchOutlets = new JsonArrayRequest("http://json.wiing.org/Details.aspx?store="+area,this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "outletapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), "Unable to fetch outlets", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            String[] outlets = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                outlets[i] = ((OutletObject) jsonArray.get(i)).getStore_name();
            }
            setList(outlets);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Erroring in fetching outlets", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(String[] outlets){
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(outlets));
        LocationAdapter outletAdapter = new LocationAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        outletList.setAdapter(outletAdapter);
    }

}
