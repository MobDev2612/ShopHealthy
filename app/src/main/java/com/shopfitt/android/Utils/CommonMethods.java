package com.shopfitt.android.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.shopfitt.android.datamodels.ProductObject;

public class CommonMethods {
    public static void showProgress(final boolean show, Context context) {
        CustomProgressDialog customProgressDialog = CustomProgressDialog.getInstance();
        if (show) {
            customProgressDialog.showProgress("Please wait", context);
        } else {
            customProgressDialog.dismissProgress();
        }
    }

    public static void setActionBarProperties(Context context, Activity activity, int drawableResourceId) {
        try {
            ((AppCompatActivity) activity).getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (NullPointerException e) {
            Logger.e("test", e.getLocalizedMessage(), e);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((AppCompatActivity) activity).getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(drawableResourceId, null));
        } else {
            ((AppCompatActivity) activity).getSupportActionBar().setHomeAsUpIndicator(context.getResources().getDrawable(drawableResourceId));
        }
        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static boolean addProductInCart(ProductObject productObject, Context mContext) {
        boolean found = false;
        for (int i = 0; i < Config.addToCart.size(); i++) {
            if (productObject.getID() == Config.addToCart.get(i).getID()) {
                found = true;
//                Config.addToCart.get(i).setQtyBought(Config.addToCart.get(i).getQtyBought() + productObject.getQtyBought());
            }
        }
        if (!found) {
            ProductObject newProduct = new ProductObject(productObject);
//            newProduct = productObject;
            Config.addToCart.add(newProduct);
            Toast.makeText(mContext, "Added to Cart", Toast.LENGTH_SHORT).show();
            found = true;
            Intent intent = new Intent("custom-event-name");
            intent.putExtra("message", "This is my message!");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
        return found;
    }

    public static ProductObject checkProductInCart(ProductObject productObject) {
        ProductObject cartProduct = null;
        for (int i = 0; i < Config.addToCart.size(); i++) {
            if (productObject.getID() == Config.addToCart.get(i).getID()) {
                cartProduct = Config.addToCart.get(i);
            }
        }
        return cartProduct;
    }
}
