package com.shopfitt.android.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.shopfitt.android.Fragment.AddressHistoryFragment;
import com.shopfitt.android.Fragment.NewAddressFragment;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.Logger;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.CustomerAddress;

import org.json.JSONException;
import org.json.JSONObject;

public class DeliveryActivityNew extends AppCompatActivity implements Response.ErrorListener, AddressHistoryFragment.OnFragmentInteractionListener, NewAddressFragment.OnFragmentInteractionListener {

    //    int requestID;
    String orderId;
    //    boolean executed;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;
    CustomerAddress customerAddress;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_activity_new);
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
        RadioGroup rGroup = (RadioGroup) findViewById(R.id.radio_group);
        ((RadioButton) findViewById(R.id.existing_address)).setTypeface(Font.getTypeface(this,Font.FONT_OPEN_SANS));
        ((RadioButton) findViewById(R.id.new_address)).setTypeface(Font.getTypeface(this,Font.FONT_OPEN_SANS));
        if (rGroup != null) {
            rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.existing_address) {
                        changeFragment(new AddressHistoryFragment());
                    } else if (checkedId == R.id.new_address) {
                        changeFragment(new NewAddressFragment());
                    }
                }
            });
        }
        ((RadioButton) findViewById(R.id.new_address)).setChecked(true);
    }

    private void getOrderID(JSONObject jsonObject) {
//        requestID = 1;
        CommonMethods.showProgress(true, this);
        try {
            VolleyRequest<String> volleyRequest = new VolleyRequest<>(Request.Method.POST, "http://23.91.69.85:61090/ProductService.svc/SaveOrder/", String.class, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            CommonMethods.showProgress(false, DeliveryActivityNew.this);
                            String result = "0";
                            if (s != null) {
                                result = s;
                            }
                            if (result.equalsIgnoreCase("0")) {
                                Toast.makeText(DeliveryActivityNew.this, "Unable to get order ID", Toast.LENGTH_SHORT).show();
                            } else {
                                orderId = result;
                                Config.orderId = result;
                                Logger.i("OrderId", result);
                                sendOrder();
                            }
                        }
                    }, this, jsonObject);
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
            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "saveOrder");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendOrder() {
//        requestID = 2;
        final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        try {
            for (int i = 0; i < Config.addToCart.size(); i++) {
                String query = Uri.encode(Config.orderId + "/" + Config.addToCart.get(i).getProduct_name()
                        + "/" + Config.addToCart.get(i).getQtyBought() + "/", ALLOWED_URI_CHARS);
                String url = "http://23.91.69.85:61090/ProductService.svc/SaveOrderLine2/" + query;
                StringRequest volleyRequest = new StringRequest(Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                count = count + 1;
                                if (count == Config.addToCart.size()) {
                                    parseAddress();
                                }
                            }
                        }, this);
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
//                if (i == Config.addToCart.size() - 1) {
//                    requestID = 10;
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
//        if (requestID == 1) {
        CommonMethods.showProgress(false, this);
//        }
        Toast.makeText(this, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onResponse(Object o) {
//        if (requestID == 1) {
//            CommonMethods.showProgress(false, this);
//            String result = "0";
//            if (o instanceof String) {
//                result = (String) o;
//            } else if (o instanceof Integer) {
//                result = String.valueOf(o);
//            }
//            if (result.equalsIgnoreCase("0")) {
//                Toast.makeText(this, "Unable to get order ID", Toast.LENGTH_SHORT).show();
//            } else {
//                orderId = result;
//                Config.orderId = result;
//                Logger.i("OrderId", result);
//                sendOrder();
//            }
//        }
//        if (requestID == 10 && !executed) {
//            executed = true;
//            parseAddress();
//        }
//        if (requestID == 11) {
//            Toast.makeText(this, "Thanks for order", Toast.LENGTH_LONG).show();
//            Config.addToCart.clear();
//            Config.cartTotalAmount = 0;
//            Intent intent = new Intent(this, CrunchActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }
//    }

    private void parseAddress() {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(customerAddress, CustomerAddress.class);
            JSONObject json = new JSONObject(jsonString);
            sendAddress(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAddress(JSONObject jsonObject) {
//        requestID = 11;
        CommonMethods.showProgress(true, this);
        try {
            VolleyRequest<String> volleyRequest = new VolleyRequest<>(Request.Method.POST, "http://23.91.69.85:61090/ProductService.svc/SaveOrderAddress/", String.class, null,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Toast.makeText(DeliveryActivityNew.this, "Thanks for order", Toast.LENGTH_LONG).show();
                            Config.addToCart.clear();
                            Config.cartTotalAmount = 0;
                            Intent intent = new Intent(DeliveryActivityNew.this, CrunchActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            DeliveryActivityNew.this.finish();
                        }
                    }, this, jsonObject);
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

    @Override
    public void onFragmentInteraction(CustomerAddress customerAddress) {
        customerAddress.setMobile(sharedPreferences.getPhoneNumber());
        this.customerAddress = customerAddress;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", customerAddress.getMobile());
            jsonObject.put("address", customerAddress.getAddress1());
            jsonObject.put("total", Config.cartTotalAmount + "");
            getOrderID(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
