package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.datamodels.LocationObject;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<LocationObject> {
    private Context mContext;
    private int mResource;
    List<LocationObject> dataList,dataListCopy;

    public LocationAdapter(Context context, int resource, List<LocationObject> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = objects;
        dataListCopy = new ArrayList<>();
        dataListCopy.addAll(dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.mTextView.setTypeface(Font.getTypeface(mContext, Font.FONT_AWESOME));
            viewHolder.mTextView.setBackgroundColor(Color.WHITE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(dataList.get(position).getArea());
        return convertView;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public LocationObject getItem(int position) {
        return dataList.get(position);
    }

    public class ViewHolder {
        public TextView mTextView;
    }

    public void filter(String search){
        dataList.clear();
        for(int i=0;i< dataListCopy.size();i++){
            if(dataListCopy.get(i).getArea().toLowerCase().contains(search.toLowerCase())){
                dataList.add(dataListCopy.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
