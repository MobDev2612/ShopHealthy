package com.shopfitt.android.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.Activity.LoginActivity;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.OutletAdapter;
import com.shopfitt.android.datamodels.OutletObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//        String username = sharedPreferences.getLoginID("");
        if(Config.loginDone) {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void getOutlets(String area) {
        CommonMethods.showProgress(true,getActivity());
        JsonArrayRequest fetchOutlets = new JsonArrayRequest("http://json.wiing.org/Details.aspx?store="+area,this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "outletapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,getActivity());
        Toast.makeText(getActivity(), "Unable to fetch categories", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            CommonMethods.showProgress(false, getActivity());
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<OutletObject> posts = new ArrayList<OutletObject>();
            posts = Arrays.asList(gson.fromJson(jsonArray.toString(), OutletObject[].class));
            setList(posts);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Erroring in fetching categories", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<OutletObject> outlets){
        OutletAdapter outletAdapter = new OutletAdapter(getActivity(), android.R.layout.simple_list_item_1, outlets);
        outletList.setAdapter(outletAdapter);
    }

}
