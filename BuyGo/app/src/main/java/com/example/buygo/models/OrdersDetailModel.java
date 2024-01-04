package com.example.buygo.models;

import java.util.List;

public class OrdersDetailModel {


    String productName;
    String totalQuantity;
    String img_url;
    String productPrice;
    int totalPrice;
    public OrdersDetailModel() {
    }

    public OrdersDetailModel(String productName, String totalQuantity, String img_url, String productPrice, int totalPrice) {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
        this.img_url = img_url;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
