package com.couragedigital.peto.model;


import com.couragedigital.peto.Singleton.ImageURLInstance;

public class ProductListItems {

    public String firstImagePath;
    public String secondImagePath;
    public String thirdImagePath;
    public String productName;
    public String productDescription;
    public String productId;
    public String productPostDate;
    public String productPrice;



    public ProductListItems() {
    }

    public ProductListItems(String productId, String firstImagePath, String secondImagePath, String thirdImagePath, String productName,String productDescription,String productPostDate,String productPrice) {
        this.productId = productId;
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPostDate = productPostDate;
        this.productPrice = productPrice;

    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = ImageURLInstance.getUrl()+"pet_images/"+firstImagePath;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = ImageURLInstance.getUrl()+"pet_images/"+secondImagePath;
    }

    public String getThirdImagePath() {
        return thirdImagePath;
    }

    public void setThirdImagePath(String thirdImagePath) {
        this.thirdImagePath = ImageURLInstance.getUrl()+"pet_images/"+thirdImagePath;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPostDate() {
        return productPostDate;
    }

    public void setProductPostDate(String productPostDate) {
        this.productPostDate = productPostDate;
    }


    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }





}

