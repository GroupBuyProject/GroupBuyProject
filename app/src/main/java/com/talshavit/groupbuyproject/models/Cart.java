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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(item.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
