package com.talshavit.groupbuyproject.models;

public class CategoriesModel {

    private final String categoryName;

    public CategoriesModel(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
