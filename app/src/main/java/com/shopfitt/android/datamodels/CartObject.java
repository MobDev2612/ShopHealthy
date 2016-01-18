package com.shopfitt.android.datamodels;

/**
 * Created by Hari Haran on 16-Jan-16.
 */
public class CartObject {
private String OrderID;
    private String Product;
    private String Quantity ;

    public CartObject(String orderID, String product, String quantity) {
        OrderID = orderID;
        Product = product;
        Quantity = quantity;
    }

    public String getOrderID() {

        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}

