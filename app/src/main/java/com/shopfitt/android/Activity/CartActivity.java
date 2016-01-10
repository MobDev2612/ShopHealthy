package com.shopfitt.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.shopfitt.android.R;
import com.shopfitt.android.adapters.CartAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView listView = (ListView) findViewById(R.id.cart_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CartActivity.this,ItemPopActivity.class);
                startActivity(intent);
            }
        });
        String[] areas = new String[]{"Maratahalli", "Kundalahalli Gate", "Bellandur", "Silk Board", "BTM Layout", "Tin Factory", "Koramangala", "WhiteField"};
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(areas));
        CartAdapter adapter = new CartAdapter(this, R.layout.cart_list_item, arrayList);
        listView.setAdapter(adapter);
    }

}
