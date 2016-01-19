package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.shopfitt.android.Network.CustomVolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeliveryActivity extends AppCompatActivity implements  Response.ErrorListener, Response.Listener{

    private Button completeOrder;
    private EditText address;
    int requestID;
    String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialiseComponents();
    }

    private void initialiseComponents() {
        final SharedPreferences sharedPreferences = new SharedPreferences(this);

        completeOrder = (Button)findViewById(R.id.delivery_order_complete);
        address = (EditText) findViewById(R.id.delivery_address);
//        mobile = (EditText) findViewById(R.id.delivery_phone_number);

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String mobileNumber = sharedPreferences.getPhoneNumber();
                    String addressText = address.getText().toString();
                    if(Config.orderId == null) {
                        if (addressText.isEmpty()) {
                            ((TextInputLayout) findViewById(R.id.delivery_address_layout)).setError(getResources().getString(R.string.error_field_required));
                        }
                        if (!addressText.isEmpty()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("mobile", mobileNumber);
                            jsonObject.put("address", addressText);
                            jsonObject.put("total", Config.cartTotalAmount);
                            getOrderID(jsonObject);
                        }
                    } else {
                        sendOrder();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getOrderID(JSONObject jsonObject) {
        requestID =1;
        CommonMethods.showProgress(true, this);
        try {
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveOrder/",String.class,jsonObject,
                    this,this);
            volleyRequest.setRetryPolicy(new RetryPolicy() {
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
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "orderid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOrder(){
        requestID =2;
        CommonMethods.showProgress(true, this);
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i=0;i< Config.addToCart.size();i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("OrderID",Config.orderId);
                jsonObject.put("Product",Config.addToCart.get(i).getProduct_name());
                jsonObject.put("Quantity",Config.addToCart.get(i).getQtyBought());
                jsonArray.put(jsonObject);
            }
            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveOrderLine/",String.class,jsonArray,
                    this,this);
            volleyRequest.setRetryPolicy(new RetryPolicy() {
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
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "order");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,this);
        Toast.makeText(this, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object o) {
        CommonMethods.showProgress(false,this);
        if(requestID == 1){
            String result = (String) o;
            orderId = result;
            Config.orderId = result;
            sendOrder();
        }
        if(requestID == 2){
            Toast.makeText(this,"Thanks for order",Toast.LENGTH_LONG).show();
            Config.addToCart.clear();
            Intent intent = new Intent(this,CrunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
