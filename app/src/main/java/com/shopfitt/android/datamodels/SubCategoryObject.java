package com.shopfitt.android.datamodels;

/**
 * Created by Hari Haran on 13-Jan-16.
 */
public class SubCategoryObject {
    private int ID;
    private String subcategory;
    private int cat_id;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
}
