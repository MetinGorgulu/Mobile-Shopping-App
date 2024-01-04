package com.example.buygo.models;

import java.util.List;

public class AdminOrdersModel {String Time;
    String Date;
    MyCartModel myCartModel;
    String Total_Price;
    String UserName;
    List<MyCartModel> list;

    public AdminOrdersModel() {
    }

    public AdminOrdersModel(String time, String date, MyCartModel myCartModel, String total_Price, String UserName, List<MyCartModel> list) {
        Time = time;
        Date = date;
        this.myCartModel = myCartModel;
        Total_Price = total_Price;
        this.UserName = UserName;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userId) {
        UserName = userId;
    }

    public List<MyCartModel> getList() {
        return list;
    }

    public void setList(List<MyCartModel> list) {
        this.list = list;
    }
}
