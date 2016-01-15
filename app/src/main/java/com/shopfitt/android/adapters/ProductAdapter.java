package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shopfitt.android.R;
import com.shopfitt.android.datamodels.ProductObject;

import java.util.List;

/**
 * Created by Hari Haran on 15-Jan-16.
 */
public class ProductAdapter extends ArrayAdapter<ProductObject> {
    private Context mContext;
    private int mResource;
    List<ProductObject> dataList;

    public ProductAdapter(Context context, int resource, List<ProductObject> objects) {
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
    public ProductObject getItem(int position) {
        return dataList.get(position);
    }

    public class ViewHolder {
//        public ImageView mImageView;
        public TextView mName,mDescription,mCategory,mPrice;
//        public Button cartButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) convertView.findViewById(R.id.product_list_item_name);
            viewHolder.mDescription = (TextView) convertView.findViewById(R.id.product_list_item_description);
            viewHolder.mCategory = (TextView) convertView.findViewById(R.id.product_list_item_category);
            viewHolder.mPrice = (TextView) convertView.findViewById(R.id.product_list_item_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mName.setText(dataList.get(position).getProduct_name());
        viewHolder.mDescription.setText(dataList.get(position).getProduct_description());
        viewHolder.mCategory.setText(dataList.get(position).getProduct_category()+"/"+dataList.get(position).getProduct_subcategory());
        viewHolder.mPrice.setText(dataList.get(position).getMrp());
        return convertView;
    }
}
