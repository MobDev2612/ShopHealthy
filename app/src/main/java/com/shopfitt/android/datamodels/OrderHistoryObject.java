package com.shopfitt.android.datamodels;

/**
 * Created by Hari Haran on 14-Jul-16.
 */
public class OrderHistoryObject {
    String ID;
    String createdatetime;
    String ordertotal;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(String createdatetime) {
        this.createdatetime = createdatetime;
    }

    public String getOrdertotal() {
        return ordertotal;
    }

    public void setOrdertotal(String ordertotal) {
        this.ordertotal = ordertotal;
    }
}
