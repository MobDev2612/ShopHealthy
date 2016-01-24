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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
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
        public ImageView mImageView;
        public TextView mName,mDescription,mCategory,mPrice,mQty;
        public Button cartButton,plusButton,minusButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) convertView.findViewById(R.id.product_list_item_name);
            viewHolder.mDescription = (TextView) convertView.findViewById(R.id.product_list_item_description);
            viewHolder.mCategory = (TextView) convertView.findViewById(R.id.product_list_item_category);
            viewHolder.mPrice = (TextView) convertView.findViewById(R.id.product_list_item_price);

            viewHolder.mQty = (TextView) convertView.findViewById(R.id.product_list_qty_text);
            viewHolder.plusButton = (Button) convertView.findViewById(R.id.product_list_add_qty);
            viewHolder.minusButton = (Button) convertView.findViewById(R.id.product_list_minus_qty);

            viewHolder.cartButton= (Button) convertView.findViewById(R.id.product_list_add_cart);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.product_list_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position).getQtyBought()== 0){
            dataList.get(position).setQtyBought(1);
        }

        if(CommonMethods.checkProductInCart(dataList.get(position))!=null){
            dataList.set(position,CommonMethods.checkProductInCart(dataList.get(position)));
            viewHolder.cartButton.setText("Update");
        } else {
            viewHolder.cartButton.setText("Add");
        }

        viewHolder.mName.setText(dataList.get(position).getProduct_name());
        viewHolder.mDescription.setText(dataList.get(position).getProduct_description());
        viewHolder.mCategory.setText(dataList.get(position).getWeightms());
        viewHolder.mPrice.setText("INR "+dataList.get(position).getMrp()+".00");
        viewHolder.mQty.setText(dataList.get(position).getQtyBought() + "");
        viewHolder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductObject productObject = dataList.get(position);
                int qty = productObject.getQtyBought();
                productObject.setQtyBought(qty + 1);
                notifyDataSetChanged();
            }
        });

        viewHolder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductObject productObject = dataList.get(position);
                int qty = productObject.getQtyBought();
                if(qty > 1)
                productObject.setQtyBought(qty-1);
                notifyDataSetChanged();
            }
        });

        loadImages(viewHolder.mImageView, "http://shopfitt.in/product_images/" + dataList.get(position).getID() + ".jpg");

        viewHolder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductObject productObject = dataList.get(position);
                if(CommonMethods.addProductInCart(productObject)) {
                    if(productObject.getIsfood() == 1) {
                        Config.foodItems = Config.foodItems+1;
                    }
//                    Config.addToCart.add(productObject);
                    Config.cartTotalAmount = Config.cartTotalAmount + (productObject.getQtyBought()* productObject.getMrp());
                }
            }
        });
        return convertView;
    }

    public static ImageView loadImages(final ImageView imageView, String imageURL) {
        if (imageURL!= null) {
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
