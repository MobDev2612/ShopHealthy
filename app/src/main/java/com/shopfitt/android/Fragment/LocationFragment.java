package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.LocationAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class LocationFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {

    private View view;
    private EditText searchArea;
    private ListView areaList;
    private LocationAdapter locationAdapter;

    public LocationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseComponents();
    }

    private void initialiseComponents(){
        searchArea = (EditText) view.findViewById(R.id.search_bar);
        areaList = (ListView) view.findViewById(R.id.location_list);
        areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOutlets();
            }
        });
        searchArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search = s.toString();
                locationAdapter.filter(search);
            }
        });
//        getLocations(); // TODO
        setList();
    }

    private void showOutlets(){
        Fragment fragment = new OutletFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void getLocations(){
        VolleyRequest<String> fetchLocations = new VolleyRequest<String>(Request.Method.GET,
                "https://faasos.0x10.info/api/faasos?type=json&query=api_hits",
                String.class, null,
                this, this); //TODO
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
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
        String[] areas = new String[]{"Maratahalli", "Kundalahalli Gate", "Bellandur", "Silk Board", "BTM Layout", "Tin Factory", "Koramangala", "WhiteField"};
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(areas));
        locationAdapter = new LocationAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        areaList.setAdapter(locationAdapter);
    }
}
