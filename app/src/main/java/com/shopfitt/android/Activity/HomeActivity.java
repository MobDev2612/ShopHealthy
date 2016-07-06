package com.shopfitt.android.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shopfitt.android.Fragment.EditPhoneNumberFragment;
import com.shopfitt.android.Fragment.HomeFragment;
import com.shopfitt.android.Fragment.LocationFragment;
import com.shopfitt.android.Fragment.NotificationFragment;
import com.shopfitt.android.Fragment.SettingsFragment;
import com.shopfitt.android.Network.VolleyRequest;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.SharedPreferences;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.CustomerRank;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Response.ErrorListener, Response.Listener<CustomerRank> {

    SharedPreferences sharedPreferences;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences = new SharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Config.addToCart == null) {
            Config.addToCart = new ArrayList<>();
        }
        setHeaders();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
        changeFragments(R.id.nav_home);
        getCustomerRank();
    }

    private void setHeaders() {
        FontView name = (FontView) navigationView.findViewById(R.id.header_name);
        FontView email = (FontView) navigationView.findViewById(R.id.header_email);
        if (name != null) {
            name.setText(sharedPreferences.getName());
        }
        if (email != null) {
            email.setText(sharedPreferences.getEmail());
        }
    }

    private void getCustomerRank() {
        VolleyRequest<CustomerRank> volleyRequest = new VolleyRequest<>(Request.Method.GET, "http://23.91.69.85:61090/ProductService.svc/getCustomerRank/" + Config.customerID,
                CustomerRank.class, null, this, this);
        Shopfitt.getInstance().addToRequestQueue(volleyRequest, "location");
    }

    @Override
    public void onBackPressed() {
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.cart_icon);
        RelativeLayout count = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView notificationCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);
        if (Config.addToCart.size() > 0) {
            notificationCount.setVisibility(View.VISIBLE);
            notificationCount.setText(String.valueOf(Config.addToCart.size()));
        } else {
            notificationCount.setVisibility(View.GONE);
        }
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.addToCart.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Cart is empty. Add Products to cart", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void updateCount() {
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            if (Config.addToCart.isEmpty()) {
                Toast.makeText(this, "Cart is empty. Add Products to cart", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        changeFragments(id);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void changeFragments(int id) {
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_my_account) {
            fragment = new EditPhoneNumberFragment();
        } else if (id == R.id.nav_track_order) {

        } else if (id == R.id.nav_change_store) {
            fragment = new LocationFragment();
        } else if (id == R.id.nav_notification) {
            fragment = new NotificationFragment();
        } else if (id == R.id.nav_change_password) {
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_log_out) {
            sharedPreferences.clearAll();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(this, "Not Available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCount();
        }
    };

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(this, "Unable to fetch details", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(CustomerRank customerRank) {
        Config.customerRank = customerRank;
    }
}
