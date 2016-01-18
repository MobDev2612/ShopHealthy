package com.shopfitt.android.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.shopfitt.android.R;
import com.shopfitt.android.Utils.Config;
import com.shopfitt.android.Utils.Shopfitt;
import com.shopfitt.android.datamodels.ProductObject;

public class ItemPopActivity extends AppCompatActivity {

    private ProductObject productObject;
    private TextView nameEdtTxt, priceEdtTxt, unitEdtTxt, sugarEdtTxt, saltEdtTxt, fatEdtTxt, bhtEdtTxt, qtyEdtTxt;
    private Button plusButton, minusButton, updateButton;
    private ImageButton closeButton;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_item_pop);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        productObject = bundle.getParcelable("product");
        initialiseComponents();
        assignValues();
    }

    private void initialiseComponents() {
        nameEdtTxt = (TextView) findViewById(R.id.item_pop_name);
        priceEdtTxt = (TextView) findViewById(R.id.item_pop_price);
        unitEdtTxt = (TextView) findViewById(R.id.item_pop_unit);
        sugarEdtTxt = (TextView) findViewById(R.id.item_pop_sugar);
        saltEdtTxt = (TextView) findViewById(R.id.item_pop_salt);
        fatEdtTxt = (TextView) findViewById(R.id.item_pop_fat);
        bhtEdtTxt = (TextView) findViewById(R.id.item_pop_bht);
        qtyEdtTxt = (TextView) findViewById(R.id.item_pop_quantity);
        imageView = (ImageView) findViewById(R.id.item_pop_image);

        plusButton =(Button) findViewById(R.id.item_pop_plus);
        minusButton =(Button) findViewById(R.id.item_pop_minus);
        updateButton =(Button) findViewById(R.id.item_pop_update);

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
                if(!Config.addToCart.contains(productObject)) {
                    Config.addToCart.add(productObject);
                    Config.cartTotalAmount = Config.cartTotalAmount + (productObject.getQtyBought()* productObject.getMrp());
                }
                finish();
            }
        });

        loadImages(imageView, "http://shopfitt.in/product_images/"+productObject.getID()+".jpg");

        closeButton = (ImageButton) findViewById(R.id.item_pop_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void assignValues() {
        nameEdtTxt.setText(productObject.getProduct_name());
        priceEdtTxt.setText("INR "+productObject.getMrp()+".00");
        unitEdtTxt.setText(productObject.getWeightms());
        sugarEdtTxt.setText(productObject.getSugar() + "");
        saltEdtTxt.setText(productObject.getCalories() + "");
        fatEdtTxt.setText(productObject.getFat() + "");
        bhtEdtTxt.setText(productObject.getSodium()+"");
        qtyEdtTxt.setText(productObject.getQtyBought() + "");
    }

    private void addQty(){
        productObject.setQtyBought(productObject.getQtyBought() + 1);
        qtyEdtTxt.setText(productObject.getQtyBought()+"");
    }

    private void minusQty(){
        if(productObject.getQtyBought()>0) {
            productObject.setQtyBought(productObject.getQtyBought() - 1);
            qtyEdtTxt.setText(productObject.getQtyBought() + "");
        }
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
