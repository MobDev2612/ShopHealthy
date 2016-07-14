package com.shopfitt.android.datamodels;

public class CustomerAddress {

    private String address1;
    private String address2;
    private String city;
    private String landmark;
    private String mobile;
    private String pincode;

    private String customer_name;

    private String name;
    private String orderid;
    private String area;


    @Override
    public String toString() {
        return customer_name + ",\n" +
                address1 + ',' + address2 + ",\n" +
                city + ',' + pincode + ',' + mobile;
    }

    public CustomerAddress(String address1, String address2, String city, String landmark, String mobile, String pincode, String name, String orderid, String area) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.landmark = landmark;
        this.mobile = mobile;
        this.pincode = pincode;
        this.name = name;
        this.orderid = orderid;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
