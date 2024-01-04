package com.example.buygo.models;

import java.util.List;

public class OrdersModel {

    String Time;
    String Date;
    MyCartModel myCartModel;
    String Total_Price;
    String UserId;
    List<MyCartModel> list;

    public OrdersModel() {
    }


    public OrdersModel(String Time, String Date, MyCartModel MyCartModel, String Total_Price, String UserId, List<MyCartModel> list) {
        this.Time = Time;
        this.Date = Date;
        this.myCartModel = MyCartModel;
        this.Total_Price = Total_Price;
        this.UserId = UserId;
        this.list = list;
    }

    public List<MyCartModel> getList() {
        return list;
    }

    public void setList(List<MyCartModel> list) {
        this.list = list;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public MyCartModel getMyCartModel() {
        return myCartModel;
    }

    public void setMyCartModel(MyCartModel myCartModel) {
        this.myCartModel = myCartModel;
    }

    public String getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(String total_Price) {
        Total_Price = total_Price;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
