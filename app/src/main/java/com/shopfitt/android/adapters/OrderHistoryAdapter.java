package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.shopfitt.android.R;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.datamodels.OrderHistoryObject;

import java.util.List;

/**
 * Created by Hari Haran on 14-Jul-16.
 */
public class OrderHistoryAdapter extends ArrayAdapter<OrderHistoryObject> {
    private Context mContext;
    private int mResource;
    List<OrderHistoryObject> dataList;

    public OrderHistoryAdapter(Context context, int resource,List<OrderHistoryObject> arrayList) {
        super(context, resource);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = arrayList;
    }

    public class ViewHolder{
        FontView orderID;
        FontView orderCreatedTime;
        FontView orderTotal;
        Button viewOrder, reOrder;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public OrderHistoryObject getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);

            viewHolder = new ViewHolder();
            viewHolder.orderID = (FontView) convertView.findViewById(R.id.order_history_id);
            viewHolder.orderCreatedTime = (FontView) convertView.findViewById(R.id.order_history_date);
            viewHolder.orderTotal = (FontView) convertView.findViewById(R.id.order_history_total);
            viewHolder.viewOrder = (Button) convertView.findViewById(R.id.order_history_view);
            viewHolder.reOrder = (Button) convertView.findViewById(R.id.order_history_reorder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.orderID.setText(dataList.get(position).getID());
        viewHolder.orderCreatedTime.setText(dataList.get(position).getCreatedatetime());
        viewHolder.orderTotal.setText("Rs. "+dataList.get(position).getOrdertotal());
        return convertView;
    }
}
