package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.datamodels.CategoryObject;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<CategoryObject> {
    private Context mContext;
    private int mResource;
    List<CategoryObject> dataList;

    public CategoryAdapter(Context context, int resource, List<CategoryObject> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = objects;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public CategoryObject getItem(int position) {
        return dataList.get(position);
    }

    public class ViewHolder {
        public TextView mTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.mTextView.setTypeface(Font.getTypeface(mContext,Font.FONT_OPEN_SANS));
            viewHolder.mTextView.setBackgroundColor(Color.WHITE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                viewHolder.mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
            } else {
                viewHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(dataList.get(position).getProduct_category());
        return convertView;
    }
}
