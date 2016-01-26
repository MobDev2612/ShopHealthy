package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.datamodels.NotificationObject;

import java.util.List;

/**
 * Created by Hari Haran on 26-Jan-16.
 */
public class NotificationAdapter extends ArrayAdapter<NotificationObject> {
    private Context mContext;
    private int mResource;
    List<NotificationObject> dataList;

    public NotificationAdapter(Context context, int resource, List<NotificationObject> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(dataList.get(position).getMessage());
        viewHolder.mTextView.setTypeface(Font.getTypeface(mContext, Font.FONTAWESOME));
        return convertView;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public NotificationObject getItem(int position) {
        return dataList.get(position);
    }

    public class ViewHolder {
        public TextView mTextView;
    }
}
