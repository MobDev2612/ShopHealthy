package com.shopfitt.android.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.Activity.ItemPopActivity;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.ProductAdapter;
import com.shopfitt.android.datamodels.ProductObject;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {
    private View view;
    private ListView productList;
    List<ProductObject> productObjects;


    public ProductListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        initialiseComponents(arguments);
    }

    private void initialiseComponents(Bundle arguments) {
        if(Config.addToCart==null){
            Config.addToCart = new ArrayList<ProductObject>();
        }
        productObjects = new ArrayList<ProductObject>();
        productList = (ListView) view.findViewById(R.id.product_list);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),"Added to cart",Toast.LENGTH_LONG).show();
                ProductObject productObject = productObjects.get(position);
                productObject.setQtyBought(1);
                Bundle bundle = new Bundle();
                bundle.putParcelable("product", productObject);
                Intent intent = new Intent(getActivity(), ItemPopActivity.class);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        getProducts(arguments.getString("subcategory"));
//        getProductsFromAssets();
    }

    private void getProductsFromAssets(){
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = getResources().getAssets().open("product.json");
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            parseResponse(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getProducts(String id) {
        CommonMethods.showProgress(true,getActivity());
        JsonArrayRequest fetchOutlets = new JsonArrayRequest("http://json.wiing.org/Details.aspx?subcatproducts="+id,this, this);
        fetchOutlets.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 300000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError volleyError) throws VolleyError {

            }
        });
        Shopfitt.getInstance().addToRequestQueue(fetchOutlets, "productapi");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,getActivity());
        Toast.makeText(getActivity(), "Unable to fetch products", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        CommonMethods.showProgress(false, getActivity());
        parseResponse(jsonArray.toString());
    }

    private void parseResponse(String response){
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            productObjects = Arrays.asList(gson.fromJson(response, ProductObject[].class));
            setList(productObjects);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Error in fetching products", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<ProductObject> outlets){
        ProductAdapter outletAdapter = new ProductAdapter(getActivity(), R.layout.product_list_item, outlets);
        productList.setAdapter(outletAdapter);
    }


}
