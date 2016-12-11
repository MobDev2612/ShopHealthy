package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.ProductObject;

import java.util.List;

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
        public NetworkImageView mImageView;
        public FontView mName, mDescription, mCategory, mPrice, mQty;
        public Button cartButton;
        public ImageButton plusButton, minusButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(mResource, null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (FontView) convertView.findViewById(R.id.product_list_item_name);
            viewHolder.mDescription = (FontView) convertView.findViewById(R.id.product_list_item_description);
            viewHolder.mCategory = (FontView) convertView.findViewById(R.id.product_list_item_category);
            viewHolder.mPrice = (FontView) convertView.findViewById(R.id.product_list_item_price);
            viewHolder.mQty = (FontView) convertView.findViewById(R.id.product_list_qty_text);
            viewHolder.plusButton = (ImageButton) convertView.findViewById(R.id.product_list_add_qty);
            viewHolder.minusButton = (ImageButton) convertView.findViewById(R.id.product_list_minus_qty);
            viewHolder.cartButton = (Button) convertView.findViewById(R.id.product_list_add_cart);
            viewHolder.mImageView = (NetworkImageView) convertView.findViewById(R.id.product_list_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(dataList.get(position).getQtyBought()== 0){
            dataList.get(position).setQtyBought(1);
        }
        if (CommonMethods.checkProductInCart(dataList.get(position)) != null) {
            dataList.set(position, CommonMethods.checkProductInCart(dataList.get(position)));
            viewHolder.cartButton.setText("Update");
        } else {
            viewHolder.cartButton.setText("Add");
        }
        viewHolder.cartButton.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        viewHolder.mName.setText(dataList.get(position).getProduct_name());
        viewHolder.mDescription.setText(dataList.get(position).getProduct_description());
        viewHolder.mCategory.setText(dataList.get(position).getWeightms());
        viewHolder.mPrice.setText(mContext.getString(R.string.rupee_icon) + dataList.get(position).getMrp() + ".00");
        viewHolder.mPrice.setTypeface(Font.getTypeface(mContext, Font.FONT_OPEN_SANS));
        viewHolder.mQty.setText(String.valueOf(dataList.get(position).getQtyBought()));
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
                if (qty > 1)
                    productObject.setQtyBought(qty - 1);
                notifyDataSetChanged();
            }
        });

        loadImages(viewHolder.mImageView, "http://shopfitt.in/product_images/" + dataList.get(position).getID() + ".jpg");

        viewHolder.cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductObject productObject = dataList.get(position);
                if (productObject.getQtyBought() > 0) {
                    if (CommonMethods.addProductInCart(productObject, mContext)) {
                        if (productObject.getIsfood() == 1) {
                            Config.foodItems = Config.foodItems + 1;
                        }
//                    Config.addToCart.add(productObject);
                        Config.cartTotalAmount = Config.cartTotalAmount + (productObject.getQtyBought() * productObject.getMrp());
                        notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(mContext, "Please add the quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    private static void loadImages(final NetworkImageView imageView, String imageURL) {
        if (imageURL != null) {
            ImageLoader imageLoader = Shopfitt.getInstance().getImageLoader();
            imageLoader.get(imageURL, ImageLoader.getImageListener(imageView,
                    R.drawable.default_image, android.R.drawable
                            .ic_dialog_alert));
            imageView.setImageUrl(imageURL, imageLoader);
        }
    }

}
