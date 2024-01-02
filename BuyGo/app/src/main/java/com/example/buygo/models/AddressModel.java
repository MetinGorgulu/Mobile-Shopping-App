package com.example.buygo.models;

public class AddressModel {

    private String addressName;
    private String cityName;
    private String address;
    private String postalCode;
    private String userNumber;
    private Boolean isSelected;

    public AddressModel() {
    }

    public AddressModel(String addressName, String cityName, String address, String postalCode, String userNumber) {
        this.addressName = addressName;
        this.cityName = cityName;
        this.address = address;
        this.postalCode = postalCode;
        this.userNumber = userNumber;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}