package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.ProductObject;

import java.util.List;

/**
 * Created by Hari Haran on 09-Jan-16.
 */
public class CartAdapter extends ArrayAdapter<ProductObject> {
    private Context mContext;
    private int mResource;
    List<ProductObject> dataList;

    public CartAdapter(Context context, int resource, List<ProductObject> arrayList) {
        super(context, resource);
        this.mContext = context;
        this.mResource = resource;
        this.dataList = arrayList;
    }

    public class ViewHolder {
        public ImageView mImageView;
        public TextView mName, mPrice, mSugar, mCalories,mFat,mSodium;
        public Button cartRemoveButton,plusButton,minusButton;
        public TextView qtyText;
        public LinearLayout linearLayout;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) convertView.findViewById(R.id.list_item_text1);
            viewHolder.mPrice = (TextView) convertView.findViewById(R.id.list_item_text2);
            viewHolder.mSugar = (TextView) convertView.findViewById(R.id.list_item_text3);
            viewHolder.mCalories = (TextView) convertView.findViewById(R.id.list_item_text4);
            viewHolder.mSodium = (TextView) convertView.findViewById(R.id.list_item_text5);
            viewHolder.mFat = (TextView) convertView.findViewById(R.id.list_item_text6);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.list_item_image);

            viewHolder.cartRemoveButton = (Button) convertView.findViewById(R.id.list_item_remove);
            viewHolder.plusButton = (Button) convertView.findViewById(R.id.product_list_add_qty);
            viewHolder.minusButton = (Button) convertView.findViewById(R.id.product_list_minus_qty);

            viewHolder.qtyText = (TextView) convertView.findViewById(R.id.list_item_qty);
            viewHolder.linearLayout =(LinearLayout) convertView.findViewById(R.id.list_item_desc_visible);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mName.setText(dataList.get(position).getProduct_name());
        viewHolder.mPrice.setText("INR "+dataList.get(position).getMrp() + "");
        viewHolder.qtyText.setText(dataList.get(position).getQtyBought() + "");
        viewHolder.qtyText.setId(position);
        if(dataList.get(position).getIsfood()==1) {
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            viewHolder.mSugar.setText("" + dataList.get(position).getSugar());
            viewHolder.mCalories.setText(dataList.get(position).getCalories()+"");
            viewHolder.mFat.setText("" + dataList.get(position).getFat());
            viewHolder.mSodium.setText("" + dataList.get(position).getSodium());
        } else {
            viewHolder.linearLayout.setVisibility(View.GONE);
        }
        viewHolder.cartRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataList.get(position).getIsfood() == 1) {
                    Config.foodItems = Config.foodItems - 1;
                }
                Config.cartTotalAmount = Config.cartTotalAmount - (dataList.get(position).getQtyBought()* dataList.get(position).getMrp());
//                Config.addToCart.remove(position);
                dataList.remove(position);
                notifyDataSetChanged();
            }
        });

        viewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductObject productObject = dataList.get(position);
                int qty = productObject.getQtyBought();
                productObject.setQtyBought(qty + 1);
//
//                ProductObject productObject1 = Config.addToCart.get(position);
//                int qty1 = productObject1.getQtyBought();
//                productObject.setQtyBought(qty1 + 1);

                notifyDataSetChanged();
            }
        });

        viewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductObject productObject = dataList.get(position);
                int qty = productObject.getQtyBought();
                if(qty > 1) {
                    productObject.setQtyBought(qty - 1);
//                    ProductObject productObject1 = Config.addToCart.get(position);
//                    int qty1 = productObject1.getQtyBought();
//                    productObject1.setQtyBought(qty1-1);
                }
                notifyDataSetChanged();
            }
        });

        loadImages(viewHolder.mImageView, "http://shopfitt.in/product_images/" + dataList.get(position).getID() + ".jpg");
        return convertView;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ProductObject getItem(int position) {
        return dataList.get(position);
    }

    public static ImageView loadImages(final ImageView imageView, String imageURL) {
        if (imageURL != null) {
            ImageLoader imageLoader = Shopfitt.getInstance().getImageLoader();
            imageLoader.get(imageURL, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.getMessage(), error.getCause());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        Bitmap imageBitMap = response.getBitmap();
                        imageView.setImageBitmap(imageBitMap);
                    }
                }
            });
        }
        return imageView;
    }
}
