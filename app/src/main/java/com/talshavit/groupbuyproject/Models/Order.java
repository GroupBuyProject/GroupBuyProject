package com.talshavit.groupbuyproject.Models;

import java.time.LocalDate;
import java.util.Calendar;

public class Order {
    private Long orderID;
    private Cart cart;
    private String date;
    private String time;
    private double price;
    private Cart copiedCart;

    public Order() {
    }

    public Order(Cart cart, Cart copiedCart, double price) {
        this.copiedCart = copiedCart;
        this.cart = cart;
        this.price = price;
        setDate();
        setTime();
    }

    public Long getOrderID() {
        return orderID;
    }

    public Order setOrderID(Long orderID) {
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

    public Cart getCopiedCart() {
        return copiedCart;
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

    @Override
    public String toString() {
        String total = String.format("%.2f", price);
        return "הזמנה חדשה" + "\n" +
                "הזמנה מספר: " + orderID + "\n" +
                "התקבלה בתאריך " + date +
                " בשעה " + time + "\n" +
                "מוצרי ההזמנה: " + "\n" + copiedCart.toString() + "\n" +
                " סה\"כ לתשלום: " + total;
    }
}
