package com.talshavit.groupbuyproject.models;

import android.graphics.drawable.Drawable;

import com.talshavit.groupbuyproject.R;

public class CategoriesModel {

    private final String categoryName;
    private final String img;

    public CategoriesModel(String categoryName, String img) {
        this.categoryName = categoryName;
        this.img = img;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public String getImg() {
        return img;
    }
}
