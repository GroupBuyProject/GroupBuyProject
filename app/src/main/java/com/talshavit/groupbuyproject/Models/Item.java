package com.talshavit.groupbuyproject.Models;


import com.talshavit.groupbuyproject.General.Constants;

import java.util.ArrayList;

public class Item {
    private String id;
    private String name;
    private String price;
    private String sale;
    private String img;
    private String weight;
    private String company;
    private Category category;
    private ArrayList<Integer> relatedItems;
    private ArrayList<Integer> similarItems = new ArrayList<>();
    private double count;
    private String comment = "";
    private int remainingCharacters = Constants.MAX_CHARACTER_COMMENT;

    public Item() {
    }

    public Item(String id, String name, String price, String sale, String img, String weight, String company, Category category, ArrayList<Integer> relatedItems, ArrayList<Integer> similarItems) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sale = sale;
        this.img = img;
        this.weight = weight;
        this.company = company;
        this.category = category;
        this.relatedItems = relatedItems;
        this.similarItems = similarItems;
        this.count = 0;
    }

    public Item(Item item) {
        this.id = item.id;
        this.name = item.name;
        this.price = item.price;
        this.sale = item.sale;
        this.img = item.img;
        this.weight = item.weight;
        this.company = item.company;
        this.category = item.category;
        this.relatedItems = item.relatedItems;
        this.count = item.count;
        this.remainingCharacters = item.remainingCharacters;
        this.similarItems = item.similarItems;
        this.comment = item.comment;
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

    public ArrayList<Integer> getSimilarItems() {
        return similarItems;
    }

    public Item setSimilarItems(ArrayList<Integer> similarItems) {
        this.similarItems = similarItems;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Item setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getSale() {
        return sale;
    }

    public Item setSale(String sale) {
        this.sale = sale;
        return this;
    }

    public int getRemainingCharacters() {
        return remainingCharacters;
    }

    public Item setRemainingCharacters(int remainingCharacters) {
        this.remainingCharacters = remainingCharacters;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("שם מוצר: ").append(name).append("\n")
                .append("מזהה: ").append(id).append("\n")
                .append("משקל: ").append(weight).append("\n")
                .append("חברה: ").append(company).append("\n")
                .append("קטגוריה: ").append(category).append("\n")
                .append("כמות: ").append(count).append("\n");

        if (Double.parseDouble(sale) > 0.0) {
            sb.append("מחיר: ").append(sale).append("\n");
            if (count > 1)
                sb.append("מחיר כולל: ").append(String.valueOf(Double.parseDouble(sale) * count)).append("\n");
        } else {
            sb.append("מחיר: ").append(price).append("\n");
            if (count > 1)
                sb.append("מחיר כולל: ").append(String.valueOf(Double.parseDouble(price) * count)).append("\n");
        }

        if (!comment.isEmpty()) {
            sb.append("הערת הלקוח במידה ויש: ").append(comment).append("\n");
        }

        return sb.toString();
    }
}
