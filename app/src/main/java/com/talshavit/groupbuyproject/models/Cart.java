package com.talshavit.groupbuyproject.models;

import java.util.ArrayList;

public class Cart {
    public ArrayList<Item> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public Cart setItems(ArrayList<Item> items) {
        this.items = items;
        return this;
    }
}
