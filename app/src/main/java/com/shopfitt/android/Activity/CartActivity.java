package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.adapters.CartAdapter;

public class CartActivity extends AppCompatActivity {
//        implements  Response.ErrorListener, Response.Listener{
    
    private Button placeOrder;
    private FontView outletName,cartTotalAmount;
//    private EditText address;
//    int requestID;
//    String orderId;
//    boolean executed;
    SharedPreferences sharedPreferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = new SharedPreferences(this);
        ListView listView = (ListView) findViewById(R.id.cart_list);
        placeOrder = (Button) findViewById(R.id.cart_place_order);
        outletName = (FontView) findViewById(R.id.cart_place_outlet_name);
        cartTotalAmount = (FontView) findViewById(R.id.cart_total_amount);
//        address = (EditText) findViewById(R.id.delivery_address);

        placeOrder.setTypeface(Font.getTypeface(this,Font.FONT_OPEN_SANS));
//        address.setTypeface(Font.getTypeface(this,Font.FONT_OPEN_SANS));
        setTitle(getResources().getString(R.string.title_activity_delivery));

        if(Config.addToCart!=null) {
//            List<ProductObject> cartItems = new ArrayList<>();
//            cartItems.addAll(Config.addToCart);
            CartAdapter adapter = new CartAdapter(this, R.layout.cart_list_item, Config.addToCart);
            listView.setAdapter(adapter);
        }
        cartTotalAmount.setText("Total: "+getResources().getString(R.string.rupee_icon)+" "+Config.cartTotalAmount);
        cartTotalAmount.setTypeface(Font.getTypeface(this,Font.FONT_OPEN_SANS));
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelivery();
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        FontView title1 = (FontView) toolbar.findViewById(R.id.app_bar_title);
        title1.setText(title);
    }

    private void showDelivery() {
        if(!Config.addToCart.isEmpty()) {
//            try {
//                String mobileNumber = sharedPreferences.getPhoneNumber();
//                String addressText = address.getText().toString();
//                if (addressText.isEmpty()) {
//                    ((TextInputLayout) findViewById(R.id.delivery_address_layout)).setError(getResources().getString(R.string.error_field_required));
//                }
//                if (!addressText.isEmpty()) {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("mobile", mobileNumber);
//                    jsonObject.put("address", addressText);
//                    jsonObject.put("total", Config.cartTotalAmount + "");
//                    getOrderID(jsonObject);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            Intent intent = new Intent(this, DeliveryActivityNew.class);
            startActivity(intent);
        } else {
            Toast.makeText(this,"Add items to cart",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String outlet = sharedPreferences.getOutlet();
        outletName.setText(outlet);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

//    private void getOrderID(JSONObject jsonObject) {
//        requestID =1;
//        CommonMethods.showProgress(true, this);
//        try {
//            VolleyRequest<String> volleyRequest = new VolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveOrder/",String.class,null,
//                    this,this,jsonObject);
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
//            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "orderid");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    private void sendOrder(){
////        requestID =2;
////        CommonMethods.showProgress(true, this);
////        try {
////            JSONArray jsonArray = new JSONArray();
////            for (int i=0;i< Config.addToCart.size();i++){
////                JSONObject jsonObject = new JSONObject();
////                jsonObject.put("OrderID",Config.orderId);
////                jsonObject.put("Product",Config.addToCart.get(i).getProduct_name());
////                jsonObject.put("Quantity",Config.addToCart.get(i).getQtyBought());
////                jsonArray.put(jsonObject);
////            }
////            CustomVolleyRequest<String> volleyRequest = new CustomVolleyRequest<String>(Request.Method.POST,"http://23.91.69.85:61090/ProductService.svc/SaveOrderLine/",String.class,jsonArray,
////                    this,this);
////            volleyRequest.setRetryPolicy(new RetryPolicy() {
////                @Override
////                public int getCurrentTimeout() {
////                    return 30000;
////                }
////
////                @Override
////                public int getCurrentRetryCount() {
////                    return 0;
////                }
////
////                @Override
////                public void retry(VolleyError volleyError) throws VolleyError {
////
////                }
////            });
////            Shopfitt.getInstance().addToRequestQueue(volleyRequest, "order");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    private void sendOrder(){
//        requestID =2;
//        try {
//            for (int i=0;i< Config.addToCart.size();i++) {
//                String url = "http://23.91.69.85:61090/ProductService.svc/SaveOrderLine2/"+Config.orderId+"/"+Config.addToCart.get(i).getProduct_name()
//                        +"/"+Config.addToCart.get(i).getQtyBought()+"/";
//                url = url.replace(" ", "%20");
//                StringRequest volleyRequest = new StringRequest(Request.Method.GET,
//                        url,
//                        this, this);
//                volleyRequest.setRetryPolicy(new RetryPolicy() {
//                    @Override
//                    public int getCurrentTimeout() {
//                        return 30000;
//                    }
//
//                    @Override
//                    public int getCurrentRetryCount() {
//                        return 0;
//                    }
//
//                    @Override
//                    public void retry(VolleyError volleyError) throws VolleyError {
//
//                    }
//                });
//                Shopfitt.getInstance().addToRequestQueue(volleyRequest, "order");
//                if(i == Config.addToCart.size() -1){
//                    requestID = 10;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onErrorResponse(VolleyError volleyError) {
//        if(requestID == 1) {
//            CommonMethods.showProgress(false, this);
//        }
//        Toast.makeText(this, "Unable to connect. Please try later", Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onResponse(Object o) {
//        if(requestID == 1){
//            CommonMethods.showProgress(false,this);
//            String result ="0" ;
//            if(o instanceof String){
//                result = (String) o;
//            } else if (o instanceof Integer){
//                result = String.valueOf(o);
//            }
//            if(result.equalsIgnoreCase("0")){
//                Toast.makeText(this,"Unable to get order ID",Toast.LENGTH_SHORT).show();
//            } else {
//                orderId = result;
//                Config.orderId = result;
//                Log.e("OrderId", result);
//                sendOrder();
//            }
//        }
//        if(requestID == 10 && !executed){
//            executed = true;
//            Toast.makeText(this,"Thanks for order",Toast.LENGTH_LONG).show();
//            Config.addToCart.clear();
//            Intent intent = new Intent(this,CrunchActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//        }
//    }
//

    @Override
    public void onStop() {
        CommonMethods.showProgress(false,this);
        super.onStop();
    }


}
