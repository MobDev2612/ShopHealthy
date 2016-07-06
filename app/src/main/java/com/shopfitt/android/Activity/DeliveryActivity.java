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
import com.android.volley.toolbox.StringRequest;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.Logger;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;

import org.json.JSONException;
import org.json.JSONObject;

public class DeliveryActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener {

    private Button completeOrder;
    private EditText address1, address2, area, city, landmark, pincode;
    int requestID;
    String orderId;
    boolean executed;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialiseComponents();
    }

    @Override
    public void setTitle(CharSequence title) {
        FontView title1 = (FontView) toolbar.findViewById(R.id.app_bar_title);
        title1.setText(title);
    }

    private void initialiseComponents() {
        sharedPreferences = new SharedPreferences(this);
        setTitle(getResources().getString(R.string.title_activity_delivery));
        completeOrder = (Button) findViewById(R.id.delivery_order_complete);
        address1 = (EditText) findViewById(R.id.delivery_address_line_1);
        address2 = (EditText) findViewById(R.id.delivery_address_line_2);
        area = (EditText) findViewById(R.id.delivery_area);
        city = (EditText) findViewById(R.id.delivery_city);
        landmark = (EditText) findViewById(R.id.delivery_land_mark);
        pincode = (EditText) findViewById(R.id.delivery_pin_code);

        completeOrder.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        address1.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        address2.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        area.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        city.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        landmark.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        pincode.setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));

        ((TextInputLayout) findViewById(R.id.delivery_area_layout)).setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        ((TextInputLayout) findViewById(R.id.delivery_address_line_1_layout)).setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        ((TextInputLayout) findViewById(R.id.delivery_address_line_2_layout)).setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        ((TextInputLayout) findViewById(R.id.delivery_city_layout)).setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        ((TextInputLayout) findViewById(R.id.delivery_land_mark_layout)).setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));
        ((TextInputLayout) findViewById(R.id.delivery_pin_code_layout)).setTypeface(Font.getTypeface(this, Font.FONT_AWESOME));

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean all = true;
                try {
                    String mobileNumber = sharedPreferences.getPhoneNumber();
                    String areaName = area.getText().toString();
                    String addressText1 = address1.getText().toString();
                    String addressText2 = address2.getText().toString();
                    String cityName = city.getText().toString();
                    String landMark = landmark.getText().toString();
                    String pinCode = pincode.getText().toString();

                    if (areaName.isEmpty()) {
                        ((TextInputLayout) findViewById(R.id.delivery_area_layout)).setError(getResources().getString(R.string.error_field_required));
                        all = false;
                    }
                    if (addressText1.isEmpty()) {
                        all = false;
                        ((TextInputLayout) findViewById(R.id.delivery_address_line_1_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (addressText2.isEmpty()) {
                        all = false;
                        ((TextInputLayout) findViewById(R.id.delivery_address_line_2_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (cityName.isEmpty()) {
                        all = false;
                        ((TextInputLayout) findViewById(R.id.delivery_city_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (landMark.isEmpty()) {
                        all = false;
                        ((TextInputLayout) findViewById(R.id.delivery_land_mark_layout)).setError(getResources().getString(R.string.error_field_required));
                    }
                    if (pinCode.isEmpty()) {
                        all = false;
                        ((TextInputLayout) findViewById(R.id.delivery_pin_code_layout)).setError(getResources().getString(R.string.error_field_required));
                    }

                    if (all) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("mobile", mobileNumber);
                        jsonObject.put("address", addressText1);
                        jsonObject.put("total", Config.cartTotalAmount + "");
                        getOrderID(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getOrderID(JSONObject jsonObject) {
        requestID = 1;
        CommonMethods.showProgress(true, this);
        try {
            VolleyRequest<String> volleyRequest = new VolleyRequest<>(Request.Method.POST, "http://23.91.69.85:61090/ProductService.svc/SaveOrder/", String.class, null,
                    this, this, jsonObject);
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

//    private void sendOrder(){
//        requestID =2;
//        CommonMethods.showProgress(true, this);
//        try {
//            JSONArray jsonArray = new JSONArray();
//            for (int i=0;i< Config.addToCart.size();i++){
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("OrderID",Config.orderId);
//                jsonObject.put("Product",Config.addToCart.get(i).getProduct_name());
//                jsonObject.put("Quantity",Config.addToCart.get(i).getQtyBought());
//                jsonArray.put(jsonObject);
//            }
//            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveOrderLine/",String.class,jsonArray,
//                    this,this);
//            volleyRequest.setRetryPolicy(new RetryPolicy() {
//                @Override
//                public int getCurrentTimeout() {
//                    return 30000;
//                }
//
//                @Override
//                public int getCurrentRetryCount() {
//                    return 0;
//                }
//
//                @Override
//                public void retry(VolleyError volleyError) throws VolleyError {
//
//                }
//            });
//            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "order");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void sendOrder() {
        requestID = 2;
//        CommonMethods.showProgress(true, this);
        try {
//            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < Config.addToCart.size(); i++) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("OrderID",Config.orderId);
//                jsonObject.put("Product",Config.addToCart.get(i).getProduct_name());
//                jsonObject.put("Quantity",Config.addToCart.get(i).getQtyBought());
//                jsonArray.put(jsonObject);
//            }
                String url = "http://23.91.69.85:61090/ProductService.svc/SaveOrderLine2/" + Config.orderId + "/" + Config.addToCart.get(i).getProduct_name()
                        + "/" + Config.addToCart.get(i).getQtyBought() + "/";
                url = url.replace(" ", "%20");
                StringRequest volleyRequest = new StringRequest(Request.Method.GET,
                        url,
                        this, this);
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
                if (i == Config.addToCart.size() - 1) {
                    requestID = 10;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if (requestID == 1) {
            CommonMethods.showProgress(false, this);
        }
        Toast.makeText(this, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(Object o) {
        if (requestID == 1) {
            CommonMethods.showProgress(false, this);
            String result = "0";
            if (o instanceof String) {
                result = (String) o;
            } else if (o instanceof Integer) {
                result = String.valueOf(o);
            }
            if (result.equalsIgnoreCase("0")) {
                Toast.makeText(this, "Unable to get order ID", Toast.LENGTH_SHORT).show();
            } else {
                orderId = result;
                Config.orderId = result;
                Logger.i("OrderId", result);
                sendOrder();
            }
        }
        if (requestID == 10 && !executed) {
            executed = true;
            parseAddress();
        }
        if (requestID == 11) {
            Toast.makeText(this, "Thanks for order", Toast.LENGTH_LONG).show();
            Config.addToCart.clear();
            Config.cartTotalAmount = 0;
            Intent intent = new Intent(this, CrunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void parseAddress() {
        boolean all = true;
        try {
            String personName = sharedPreferences.getName();
            String mobileNumber = sharedPreferences.getPhoneNumber();
            String areaName = area.getText().toString();
            String addressText1 = address1.getText().toString();
            String addressText2 = address2.getText().toString();
            String cityName = city.getText().toString();
            String landMark = landmark.getText().toString();
            String pinCode = pincode.getText().toString();

            if (areaName.isEmpty()) {
                ((TextInputLayout) findViewById(R.id.delivery_area_layout)).setError(getResources().getString(R.string.error_field_required));
                all = false;
            }
            if (addressText1.isEmpty()) {
                all = false;
                ((TextInputLayout) findViewById(R.id.delivery_address_line_1_layout)).setError(getResources().getString(R.string.error_field_required));
            }
            if (addressText2.isEmpty()) {
                all = false;
                ((TextInputLayout) findViewById(R.id.delivery_address_line_2_layout)).setError(getResources().getString(R.string.error_field_required));
            }
            if (cityName.isEmpty()) {
                all = false;
                ((TextInputLayout) findViewById(R.id.delivery_city_layout)).setError(getResources().getString(R.string.error_field_required));
            }
            if (landMark.isEmpty()) {
                all = false;
                ((TextInputLayout) findViewById(R.id.delivery_land_mark_layout)).setError(getResources().getString(R.string.error_field_required));
            }
            if (pinCode.isEmpty()) {
                all = false;
                ((TextInputLayout) findViewById(R.id.delivery_pin_code_layout)).setError(getResources().getString(R.string.error_field_required));
            }

            if (all) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("address1", addressText1);
                jsonObject.put("address2", addressText2);
                jsonObject.put("area", areaName);
                jsonObject.put("city", cityName);
                jsonObject.put("landmark", landMark);
                jsonObject.put("mobile", mobileNumber);
                jsonObject.put("name", personName);
                jsonObject.put("orderid", Config.orderId);
                jsonObject.put("pincode", pinCode);
                sendAddress(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAddress(JSONObject jsonObject) {
        requestID = 11;
        CommonMethods.showProgress(true, this);
        try {
            VolleyRequest<String> volleyRequest = new VolleyRequest<>(Request.Method.POST, "http://23.91.69.85:61090/ProductService.svc/SaveOrderAddress/", String.class, null,
                    this, this, jsonObject);
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
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "address");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        CommonMethods.showProgress(false, this);
        super.onStop();
    }
}
