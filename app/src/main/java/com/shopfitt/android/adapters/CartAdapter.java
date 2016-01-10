package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hari Haran on 09-Jan-16.
 */
public class CartAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int mResource;
    List<String> dataList;

    public CartAdapter(Context context, int resource, ArrayList<String> arrayList) {
        super(context, resource);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
//            viewHolder = new ViewHolder();
//            viewHolder.itemName = (TextView) convertView.findViewById(R.id.food_list_title);
//            viewHolder.itemValue = (TextView) convertView.findViewById(R.id.food_list_value);
//            viewHolder.itemImage = (ImageView) convertView.findViewById(R.id.food_item_image);
//            viewHolder.itemType = (ImageView) convertView.findViewById(R.id.food_item_type);
//            viewHolder.itemRating = (RatingBar) convertView.findViewById(R.id.food_rating);
//            convertView.setTag(viewHolder);
        } else {
//            viewHolder = (RecyclerView.ViewHolder) convertView.getTag();
        }
        return convertView;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }
}
