package com.couragedigital.petapp.model;

public class FilterCategoryList {
    public String categoryText;
    public boolean categoryCheckState;

    public String getCategoryText() {
        return categoryText;
    }

    public void setCategoryText(String categoryText) {
        this.categoryText = categoryText;
    }

    public boolean getCategoryCheckState() {
        return categoryCheckState;
    }

    public void setCategoryCheckState(boolean categoryCheckState) {
        this.categoryCheckState = categoryCheckState;
    }
}
