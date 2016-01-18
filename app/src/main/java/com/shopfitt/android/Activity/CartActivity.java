package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.adapters.CartAdapter;

public class CartActivity extends AppCompatActivity {
    
    private Button placeOrder;
    private TextView outletName,cartTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.cart_list);
        placeOrder = (Button) findViewById(R.id.cart_place_order);
        outletName = (TextView) findViewById(R.id.cart_place_outlet_name);
        cartTotalAmount = (TextView) findViewById(R.id.cart_total_amount);
        if(Config.addToCart!=null) {
            CartAdapter adapter = new CartAdapter(this, R.layout.cart_list_item, Config.addToCart);
            listView.setAdapter(adapter);
        }
        cartTotalAmount.setText("Total: Rs."+Config.cartTotalAmount+"");
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelivery();
            }
        });
    }

    private void showDelivery() {
        Intent intent = new Intent(this,DeliveryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = new SharedPreferences(this);
        String outlet = sharedPreferences.getOutlet();
        outletName.setText(outlet);
    }
}
