package com.talshavit.groupbuyproject.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class User {
    private String name;
    private ArrayList<Order> histories;
    private ArrayList<Payment> payments;

    public User() {
        this.histories = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, ArrayList<Order> histories) {
        this.name = name;
        this.histories = histories;
    }

    public User(String name, ArrayList<Order> histories, ArrayList<Payment> payments) {
        this.name = name;
        this.histories = histories;
        this.payments = payments;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<Order> getHistories() {
        return histories;
    }

    public User setHistories(ArrayList<Order> histories) {
        this.histories = histories;
        return this;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public User setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
        return this;
    }

    public void addHistory(Order history) {
        histories.add(history);

        Collections.sort(histories, new Comparator<Order>() {
            @Override
            public int compare(Order order1, Order order2) {
                int dateComparison = order2.getDate().compareTo(order1.getDate());
                if (dateComparison == 0) {
                    return order2.getTime().compareTo(order1.getTime());
                }
                return dateComparison;
            }
        });
    }

    public void addPayment(Payment payment){
        payments.add(payment);
    }
}

