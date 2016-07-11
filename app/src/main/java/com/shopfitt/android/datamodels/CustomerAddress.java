package com.shopfitt.android.datamodels;

public class CustomerAddress {

    private String address1;
    private String address2;
    private String city;
    private String customer_name;
    private String landmark;
    private String mobile;
    private String pincode;

    @Override
    public String toString() {
        return customer_name + '\n' +
                address1 + ' ' + address2 + '\n' +
                city + pincode + '\n' +
                mobile;
    }

    public CustomerAddress(String address1, String address2, String city, String customer_name, String landmark, String mobile, String pincode) {
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.customer_name = customer_name;
        this.landmark = landmark;
        this.mobile = mobile;
        this.pincode = pincode;
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
