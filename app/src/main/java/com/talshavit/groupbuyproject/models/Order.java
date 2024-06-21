package com.talshavit.groupbuyproject.models;

import java.time.LocalDate;
import java.util.Calendar;

public class Order {
    private int orderID;
    private Cart cart;
    private String date;
    private String time;
    private double price;

    public Order() {
    }

    public Order(Cart cart, double price) {
        this.cart = cart;
        this.price = price;
        setDate();
        setTime();
    }

    public int getOrderID() {
        return orderID;
    }

    public Order setOrderID(int orderID) {
        this.orderID = orderID;
        return this;
    }

    public Cart getCart() {
        return cart;
    }

    public Order setCart(Cart cart) {
        this.cart = cart;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Order setDate() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        String formattedDate = day + "/" + month + "/" + year;
        this.date = formattedDate;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Order setPrice(double price) {
        this.price = price;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Order setTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String currentTime = hour + ":" + minute + ":" + second;
        this.time = currentTime;
        return this;
    }
}
