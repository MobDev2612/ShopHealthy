package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.CategoryAdapter;
import com.shopfitt.android.datamodels.CategoryObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {
    private View view;
    private ListView categoryList;
    List<CategoryObject> categories;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        initialiseComponents(arguments);
    }

    private void initialiseComponents(Bundle arguments) {
        categories = new ArrayList<CategoryObject>();
        categoryList = (ListView) view.findViewById(R.id.home_list);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryObject categoryObject = categories.get(position);
                showSubCategories(categoryObject.getID()+"");
            }
        });
        getCategories();
    }

    private void showSubCategories(String categoryID) {
        Bundle bundle = new Bundle();
        bundle.putString("category",categoryID);
        Fragment fragment = new SubCategoryFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    private void getCategories() {
        CommonMethods.showProgress(true,getActivity());
        JsonArrayRequest fetchOutlets = new JsonArrayRequest("http://json.wiing.org/Details.aspx?category=all",this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "categoryapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,getActivity());
        Toast.makeText(getActivity(), "Unable to fetch categories", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        CommonMethods.showProgress(false,getActivity());
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            categories = Arrays.asList(gson.fromJson(jsonArray.toString(), CategoryObject[].class));
            setList(categories);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Error in fetching categories", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<CategoryObject> outlets){
        CategoryAdapter outletAdapter = new CategoryAdapter(getActivity(), android.R.layout.simple_list_item_1, outlets);
        categoryList.setAdapter(outletAdapter);
    }
}
