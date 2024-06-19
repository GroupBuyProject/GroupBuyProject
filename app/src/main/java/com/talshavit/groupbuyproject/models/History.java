package com.talshavit.groupbuyproject.models;

import java.util.ArrayList;

public class History {
    private int orderID;
    private Cart cart;
    private String date;
    private double price;

    public History() {
    }

    public History(Cart cart, double price) {
        this.cart = cart;
        this.price = price;
    }

    public int getOrderID() {
        return orderID;
    }

    public History setOrderID(int orderID) {
        this.orderID = orderID;
        return this;
    }

    public Cart getCart() {
        return cart;
    }

    public History setCart(Cart cart) {
        this.cart = cart;
        return this;
    }

    public String getDate() {
        return date;
    }

    public History setDate(String date) {
        this.date = date;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public History setPrice(double price) {
        this.price = price;
        return this;
    }
}
