package com.talshavit.groupbuyproject.models;


import java.util.ArrayList;

public class Item {
    private String id;
    private String name;
    private String price;
    private String img;
    private String weight;
    private String company;
    private Category category;
    private ArrayList<Integer> relatedItems;
    private double count;

    public Item() {
    }

    public Item(String id, String name, String price, String img, String weight, String company, Category category, ArrayList<Integer> relatedItems) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.img = img;
        this.weight = weight;
        this.company = company;
        this.category = category;
        this.relatedItems = relatedItems;
        this.count = 0;
    }

    public String getId() {
        return id;
    }

    public Item setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Item setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Item setImg(String img) {
        this.img = img;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public Item setWeight(String weight) {
        this.weight = weight;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public Item setCompany(String company) {
        this.company = company;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Item setCategory(Category category) {
        this.category = category;
        return this;
    }

    public double getCount() {
        return count;
    }

    public Item setCount(double count) {
        this.count = count;
        return this;
    }

    public ArrayList<Integer> getRelatedItems() {
        return relatedItems;
    }

    public Item setRelatedItems(ArrayList<Integer> relatedItems) {
        this.relatedItems = relatedItems;
        return this;
    }
}
