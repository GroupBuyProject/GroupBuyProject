package com.talshavit.groupbuyproject;


public class Market {
    private String id;
    private String name;
    private String price;
    private String img;
    private String company;
    private Category category;

    public Market() {
    }

    public Market(String name, String price, String img, String company, Category category) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.company = company;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Market setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public Market setPrice(String price) {
        this.price = price;
        return this;
    }

    public String getImg() {
        return img;
    }

    public Market setImg(String img) {
        this.img = img;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public Market setCompany(String company) {
        this.company = company;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Market setCategory(Category category) {
        this.category = category;
        return this;
    }
}
