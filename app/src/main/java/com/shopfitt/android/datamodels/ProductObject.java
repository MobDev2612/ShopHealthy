package com.shopfitt.android.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductObject implements Parcelable{
    private int ID;
    private String product_name;
    private String product_description;
    private int product_category;
    private int product_subcategory;
    private int mrp;
    private String weightms;
    private int isfood;
    private int isCB;
    private String manfcompany;
    private double sugar;
    private double fat;
    private double calories;
    private double sodium;
    private int qtyBought;

    public ProductObject() {
    }

//    public ProductObject(ProductObject productObject){
//        ID = productObject.ID;
//        product_name = productObject.product_name;
//        product_description = productObject.product_description;
//        product_category = productObject.product_category;
//        product_subcategory = productObject.product_subcategory;
//        mrp = productObject.mrp;
//        weightms = productObject.weightms;
//        isfood = productObject.isfood;
//        isCB = productObject.isCB;
//        manfcompany = productObject.manfcompany;
//        sugar = productObject.sugar;
//        fat = productObject.fat;
//        calories = productObject.calories;
//        sodium = productObject.sodium;
//        qtyBought = productObject.qtyBought;
//    }
//
    public ProductObject(ProductObject productObject){
        this.ID = new Integer(productObject.ID);
        this.product_name = new String(productObject.product_name);
        this.product_description = new String(productObject.product_description);
        this.product_category = new Integer(productObject.product_category);
        this.product_subcategory = new Integer(productObject.product_subcategory);
        this.mrp = new Integer(productObject.mrp);
        this.weightms = new String(productObject.weightms);
        this.isfood = new Integer(productObject.isfood);
        this.isCB = new Integer(productObject.isCB);
        this.manfcompany = new String(productObject.manfcompany);
        this.sugar = new Double(productObject.sugar);
        this.fat = new Double(productObject.fat);
        this.calories = new Double(productObject.calories);
        this.sodium = new Double(productObject.sodium);
        this.qtyBought = new Integer(productObject.qtyBought);
    }

    protected ProductObject(Parcel in) {
        ID = in.readInt();
        product_name = in.readString();
        product_description = in.readString();
        product_category = in.readInt();
        product_subcategory = in.readInt();
        mrp = in.readInt();
        weightms = in.readString();
        isfood = in.readInt();
        isCB = in.readInt();
        manfcompany = in.readString();
        sugar = in.readDouble();
        fat = in.readDouble();
        calories = in.readDouble();
        sodium = in.readDouble();
        qtyBought = in.readInt();
    }

    public static final Creator<ProductObject> CREATOR = new Creator<ProductObject>() {
        @Override
        public ProductObject createFromParcel(Parcel in) {
            return new ProductObject(in);
        }

        @Override
        public ProductObject[] newArray(int size) {
            return new ProductObject[size];
        }
    };

    public int getQtyBought() {
        return qtyBought;
    }

    public void setQtyBought(int qtyBought) {
        this.qtyBought = qtyBought;
    }

    public int getID() {
        return ID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public int getProduct_category() {
        return product_category;
    }

    public int getProduct_subcategory() {
        return product_subcategory;
    }

    public int getMrp() {
        return mrp;
    }

    public String getWeightms() {
        return weightms;
    }

    public int getIsfood() {
        return isfood;
    }

    public int getIsCB() {
        return isCB;
    }

    public String getManfcompany() {
        return manfcompany;
    }

    public double getSugar() {
        return sugar;
    }

    public double getFat() {
        return fat;
    }

    public double getCalories() {
        return calories;
    }

    public double getSodium() {
        return sodium;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(product_name);
        dest.writeString(product_description);
        dest.writeInt(product_category);
        dest.writeInt(product_subcategory);
        dest.writeInt(mrp);
        dest.writeString(weightms);
        dest.writeInt(isfood);
        dest.writeInt(isCB);
        dest.writeString(manfcompany);
        dest.writeDouble(sugar);
        dest.writeDouble(fat);
        dest.writeDouble(calories);
        dest.writeDouble(sodium);
        dest.writeInt(qtyBought);
    }
}
