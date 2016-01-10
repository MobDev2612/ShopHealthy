package com.shopfitt.android.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Activity.LoginActivity;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.LocationAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class OutletFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {

    private View view;
    private ListView outletList;
    private LocationAdapter outletAdapter;

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
        initialiseComponents();
    }

    private void initialiseComponents() {
        outletList = (ListView) view.findViewById(R.id.outlet_list);
        outletList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToMenu();
            }
        });
//        getOutlets();
        setList();
    }

    private void goToMenu() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void getOutlets() {
        VolleyRequest<String> fetchOutlets = new VolleyRequest<String>(Request.Method.GET,
                "https://faasos.0x10.info/api/faasos?type=json&query=api_hits",
                String.class, null,
                this, this); //TODO
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "outletapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(String s) {
        //TODO
     setList();
    }

    private void setList(){
        String[] outlets = new String[]{"Big Bazzar", "More Megastore", "Reliance Fresh", "D-Mart"};
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(outlets));
        outletAdapter = new LocationAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        outletList.setAdapter(outletAdapter);
    }

}
