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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.LocationAdapter;
import com.shopfitt.android.datamodels.LocationObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {

    private View view;
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

    private void initialiseComponents() {
        EditText searchArea = (EditText) view.findViewById(R.id.search_bar);
        areaList = (ListView) view.findViewById(R.id.location_list);
        areaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                showOutlets((String) textView.getText());
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
                if (locationAdapter != null) {
                    locationAdapter.filter(search);
                }
            }
        });
        getLocations();
    }

    private void showOutlets(String areaName) {
        SharedPreferences sharedPreferences = new SharedPreferences(getActivity());
        sharedPreferences.setLocation(areaName);
        Bundle bundle = new Bundle();
        bundle.putString("area",areaName);
        Fragment fragment = new OutletFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void getLocations() {
        CommonMethods.showProgress(true,getActivity());
        JsonArrayRequest fetchLocations = new JsonArrayRequest("http://json.wiing.org/Details.aspx?area=all",
                this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchLocations, "locationapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,getActivity());
        Toast.makeText(getActivity(), "Unable to fetch location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        CommonMethods.showProgress(false,getActivity());
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<LocationObject> posts = new ArrayList<LocationObject>();
            posts = Arrays.asList(gson.fromJson(jsonArray.toString(), LocationObject[].class));
            setList(posts);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Erroring in fetching location", Toast.LENGTH_SHORT).show();
        }
    }


    private void setList(List<LocationObject> areas) {
        locationAdapter = new LocationAdapter(getActivity(), android.R.layout.simple_list_item_1, areas);
        areaList.setAdapter(locationAdapter);
    }
}
