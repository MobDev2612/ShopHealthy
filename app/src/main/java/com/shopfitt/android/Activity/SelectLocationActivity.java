package com.shopfitt.android.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.shopfitt.android.Fragment.LocationFragment;
import com.shopfitt.android.R;

public class SelectLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SharedPreferences sharedPreferences = new SharedPreferences(this);
//        String location = sharedPreferences.getLocation();
//        if(location.length()>0){
//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
        setContentView(R.layout.activity_select_location);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = new LocationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
