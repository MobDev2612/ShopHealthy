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
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.datamodels.TopRank;

import java.util.List;

public class TopRankAdapter extends ArrayAdapter<TopRank> {

    private Context mContext;
    private int mResource;
    List<TopRank> dataList;

    public TopRankAdapter(Context context, int resource, List<TopRank> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();

            viewHolder.mTextView = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.mTextView.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
//            viewHolder.mTextView.setBackgroundColor(Color.WHITE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                viewHolder.mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
            } else {
                viewHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            }
            viewHolder.mSecondTextView = (TextView) convertView.findViewById(android.R.id.text2);
            viewHolder.mSecondTextView.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
//            viewHolder.mSecondTextView.setBackgroundColor(Color.WHITE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                viewHolder.mSecondTextView.setTextColor(ContextCompat.getColor(mContext, R.color.primary_text));
            } else {
                viewHolder.mSecondTextView.setTextColor(mContext.getResources().getColor(R.color.primary_text));
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position).getCust_id().equals(Config.customerID)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.primary_light));
            } else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.primary_light));
            }
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }
        viewHolder.mTextView.setText(dataList.get(position).getCustomer_name());
        String text = "Rank: "+(position+1)+", Points: "+dataList.get(position).getPoints();
        viewHolder.mSecondTextView.setText(text);
        return convertView;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public TopRank getItem(int position) {
        return dataList.get(position);
    }

    public class ViewHolder {
        public TextView mTextView;
        public TextView mSecondTextView;
    }
}
