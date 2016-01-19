package com.shopfitt.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
        public TextView mName, mPrice;
//        public ImageButton cartRemoveButton;
        public EditText editText;
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
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.list_item_image);
//            viewHolder.cartRemoveButton = (ImageButton) convertView.findViewById(R.id.list_item_remove);
            viewHolder.editText = (EditText) convertView.findViewById(R.id.list_item_qty);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mName.setText(dataList.get(position).getProduct_name());
        viewHolder.mPrice.setText(dataList.get(position).getMrp() + "");
        viewHolder.editText.setText(dataList.get(position).getQtyBought() + "");
        viewHolder.editText.setId(position);
//        viewHolder.editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if(!s.toString().isEmpty())
//                    dataList.get(position).setQtyBought(Integer.parseInt(s.toString()));
//            }
//        });
        viewHolder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int pos = v.getId();
                    final EditText Qty = (EditText) v;
                    if(!Qty.getText().toString().isEmpty()) {
                        dataList.get(pos).setQtyBought(Integer.parseInt(Qty.getText().toString()));
                        Config.addToCart.get(pos).setQtyBought(Integer.parseInt(Qty.getText().toString()));
                    }
                }
            }
        });
//        viewHolder.cartRemoveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dataList.remove(position);
//                notifyDataSetChanged();
//            }
//        });

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
