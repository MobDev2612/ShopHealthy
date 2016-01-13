package com.shopfitt.android.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.SubCategoryAdapter;
import com.shopfitt.android.datamodels.SubCategoryObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategoryFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {
    private View view;
    private ListView subCategoryList;
    List<SubCategoryObject> subcategories;


    public SubCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        initialiseComponents(arguments);
    }

    private void initialiseComponents(Bundle arguments) {
        subcategories = new ArrayList<SubCategoryObject>();
        subCategoryList = (ListView) view.findViewById(R.id.subcategory_list);
        subCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                CategoryObject categoryObject = subcategories.get(position); // TODO: 13-Jan-16
//                showSubCategories(categoryObject.getID()+"");
            }
        });
        getCategories(arguments.getString("category"));
    }
//
//    private void showSubCategories(String categoryID) {
//        Bundle bundle = new Bundle();
//        bundle.putString("category",categoryID);
//        Fragment fragment = new OutletFragment();
//        fragment.setArguments(bundle);
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
//    }

    private void getCategories(String id) {
        JsonArrayRequest fetchOutlets = new JsonArrayRequest("http://json.wiing.org/Details.aspx?subcategory="+id,this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "categoryapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), "Unable to fetch categories", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            subcategories = Arrays.asList(gson.fromJson(jsonArray.toString(), SubCategoryObject[].class));
            setList(subcategories);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Erroring in fetching sub categories", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<SubCategoryObject> outlets){
        SubCategoryAdapter outletAdapter = new SubCategoryAdapter(getActivity(), android.R.layout.simple_list_item_1, outlets);
        subCategoryList.setAdapter(outletAdapter);
    }

}
