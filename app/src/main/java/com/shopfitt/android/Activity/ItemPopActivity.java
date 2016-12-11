package com.shopfitt.android.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.CommonMethods;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Font;
import com.shopfitt.android.Utils.FontView;
import com.shopfitt.android.Utils.Logger;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.ProductObject;

public class ItemPopActivity extends AppCompatActivity {

    private ProductObject productObject;
    private FontView nameEdtTxt, priceEdtTxt, unitEdtTxt, sugarEdtTxt, saltEdtTxt, fatEdtTxt, bhtEdtTxt, qtyEdtTxt;
    private ImageButton plusButton, minusButton;
    private Button updateButton;
    private ImageButton closeButton;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_pop);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        productObject = bundle.getParcelable("product");
        initialiseComponents();
        assignValues();
    }

    private void initialiseComponents() {
        nameEdtTxt = (FontView) findViewById(R.id.item_pop_name);
        priceEdtTxt = (FontView) findViewById(R.id.item_pop_price);
        unitEdtTxt = (FontView) findViewById(R.id.item_pop_unit);
        sugarEdtTxt = (FontView) findViewById(R.id.item_pop_sugar);
        saltEdtTxt = (FontView) findViewById(R.id.item_pop_salt);
        fatEdtTxt = (FontView) findViewById(R.id.item_pop_fat);
        bhtEdtTxt = (FontView) findViewById(R.id.item_pop_bht);
        qtyEdtTxt = (FontView) findViewById(R.id.item_pop_quantity);
        imageView = (ImageView) findViewById(R.id.item_pop_image);

        plusButton = (ImageButton) findViewById(R.id.item_pop_plus);
        minusButton = (ImageButton) findViewById(R.id.item_pop_minus);
        updateButton = (Button) findViewById(R.id.item_pop_update);
        updateButton.setTypeface(Font.getTypeface(this, Font.FONT_OPEN_SANS));

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQty();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusQty();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productObject.getQtyBought() > 0) {
                    if (CommonMethods.addProductInCart(productObject, ItemPopActivity.this)) {
                        if (productObject.getIsfood() == 1) {
                            Config.foodItems = Config.foodItems + 1;
                        }
//                    Config.addToCart.add(productObject);
                        Config.cartTotalAmount = Config.cartTotalAmount + (productObject.getQtyBought() * productObject.getMrp());
                    }
//                    Toast.makeText(ItemPopActivity.this, "Added to Cart", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(ItemPopActivity.this, "You missed adding the quantity. please check", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadImages(imageView, "http://shopfitt.in/product_images/" + productObject.getID() + ".jpg");

        closeButton = (ImageButton) findViewById(R.id.item_pop_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void assignValues() {
        if (CommonMethods.checkProductInCart(productObject) != null) {
            productObject = CommonMethods.checkProductInCart(productObject);
            updateButton.setText("Update");
        } else {
            updateButton.setText("Add");
        }
        nameEdtTxt.setText(productObject.getProduct_name());
        priceEdtTxt.setText(getResources().getString(R.string.rupee_icon) + " " + productObject.getMrp() + ".00");
        priceEdtTxt.setTypeface(Font.getTypeface(this, Font.FONT_OPEN_SANS));
        unitEdtTxt.setText(productObject.getWeightms());
        sugarEdtTxt.setText(productObject.getSugar() + "");
        saltEdtTxt.setText(productObject.getCalories() + "");
        fatEdtTxt.setText(productObject.getFat() + "");
        bhtEdtTxt.setText(productObject.getSodium() + "");
        qtyEdtTxt.setText(productObject.getQtyBought() + "");
        if(productObject.getIsfood()==1){
            (findViewById(R.id.popup_food_details)).setVisibility(View.VISIBLE);
        } else {
            (findViewById(R.id.popup_food_details)).setVisibility(View.GONE);
        }
    }

    private void addQty() {
        productObject.setQtyBought(productObject.getQtyBought() + 1);
        qtyEdtTxt.setText(productObject.getQtyBought() + "");
    }

    private void minusQty() {
        if (productObject.getQtyBought() > 1) {
            productObject.setQtyBought(productObject.getQtyBought() - 1);
            qtyEdtTxt.setText(productObject.getQtyBought() + "");
        }
    }

    public static ImageView loadImages(final ImageView imageView, String imageURL) {
        if (imageURL != null) {
            ImageLoader imageLoader = Shopfitt.getInstance().getImageLoader();
            imageLoader.get(imageURL, new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.e("Error", error.getMessage(), error.getCause());
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
