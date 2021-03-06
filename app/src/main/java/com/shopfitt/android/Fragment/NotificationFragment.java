package com.shopfitt.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.adapters.NotificationAdapter;
import com.shopfitt.android.datamodels.NotificationObject;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONArray> {

    ListView listView;
    View view;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notification, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) view.findViewById(R.id.notification_list);
        getNotifications(Config.customerID);
    }

    private void getNotifications(String id) {
        CommonMethods.showProgress(true, mContext);
        JsonArrayRequest fetchNotifications = new JsonArrayRequest("http://23.91.69.85:61090/ProductService.svc/getNotificationMessages/"+id,this, this);
        Shopfitt.getInstance().addToRequestQueue(fetchNotifications, "notification");
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        CommonMethods.showProgress(false,mContext);
        Toast.makeText(mContext, "Unable to fetch Notifications", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray jsonArray) {
        try {
            CommonMethods.showProgress(false, mContext);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            List<NotificationObject> posts = Arrays.asList(gson.fromJson(jsonArray.toString(), NotificationObject[].class));
            if(posts.size()>0) {
                (view.findViewById(R.id.no_notifications)).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                setList(posts);
            } else {
                (view.findViewById(R.id.no_notifications)).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Toast.makeText(mContext, "Error in fetching Notifications", Toast.LENGTH_SHORT).show();
        }
    }

    private void setList(List<NotificationObject> notificationObjects){
        NotificationAdapter outletAdapter = new NotificationAdapter(mContext, android.R.layout.simple_list_item_1, notificationObjects);
        listView.setAdapter(outletAdapter);
    }

    @Override
    public void onStop() {
        CommonMethods.showProgress(false,mContext);
        super.onStop();
    }
}
