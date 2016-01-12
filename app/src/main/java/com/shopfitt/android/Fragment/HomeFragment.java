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
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.CategoryAdapter;
import com.shopfitt.android.datamodels.CategoryObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {
    private View view;
    private ListView categoryList;

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
        categoryList = (ListView) view.findViewById(R.id.home_list);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView textView = (TextView) view.findViewById(android.R.id.text1);// TODO: 12-Jan-16
//                String outlet = (String) textView.getText();
//                goToMenu(outlet);
            }
        });
        getCategories();
    }

    private void getCategories() {
        JsonArrayRequest fetchOutlets = new JsonArrayRequest("http://json.wiing.org/Details.aspx?category=all",this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "categoryapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), "Unable to fetch categories", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            JSONArray jsonArray1 = jsonArray.getJSONArray(0);
            CategoryObject[] outlets = new CategoryObject[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                outlets[i] = ((CategoryObject) jsonArray.get(i));
            }
            setList(outlets);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Erroring in fetching outlets", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(CategoryObject[] outlets){
        ArrayList<CategoryObject> arrayList = new ArrayList<CategoryObject>(Arrays.asList(outlets));
        CategoryAdapter outletAdapter = new CategoryAdapter(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        categoryList.setAdapter(outletAdapter);
    }
}
