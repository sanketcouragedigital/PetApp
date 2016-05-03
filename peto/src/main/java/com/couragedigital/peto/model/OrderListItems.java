package com.couragedigital.peto.model;


import com.couragedigital.peto.Singleton.ImageURLInstance;

public class OrderListItems {

    public String firstImagePath;
    public String secondImagePath;
    public String thirdImagePath;
    public String orderProductName;
    public String orderProductDescription;
    public String orderPostDate;
    public String orderProductPrice;
    public String orderProductQuantity;
    public String orderProductShipping_charges;
    public String orderProductTotal_price;
    public String orderProductId;
    public String orderId;
    public String orderProductCustomer_email;
    public String orderProductCustomer_name;
    public String orderProductCustomer_contact;
    public String orderProductBuidling_name;
    public String orderProductArea;
    public String orderProductCity;
    public String orderPinCode;

    public OrderListItems() {
    }

    public OrderListItems(String orderId, String firstImagePath, String secondImagePath, String thirdImagePath, String orderProductName,String orderProductDescription,String orderPostDate,String orderProductPrice,String orderProductQuantity,String orderProductShipping_charges,String orderProductTotal_price,String orderProductCustomer_name,String orderProductCustomer_contact,String orderProductCustomer_email,String orderProductBuidling_name,String orderProductArea,String orderProductCity,String orderProductId,String orderPinCode) {
        this.orderId = orderId;
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;
        this.orderProductName = orderProductName;
        this.orderProductDescription = orderProductDescription;
        this.orderPostDate = orderPostDate;
        this.orderProductPrice = orderProductPrice;
        this.orderProductQuantity = orderProductQuantity;
        this.orderProductShipping_charges = orderProductShipping_charges;
        this.orderProductTotal_price = orderProductTotal_price;
        this.orderProductCustomer_name = orderProductCustomer_name;
        this.orderProductCustomer_contact = orderProductCustomer_contact;
        this.orderProductCustomer_email = orderProductCustomer_email;
        this.orderProductBuidling_name = orderProductBuidling_name;
        this.orderProductArea = orderProductArea;
        this.orderProductCity = orderProductCity;
        this.orderProductId = orderProductId;
        this.orderPinCode = orderPinCode;
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

    public String getOrderListId() {
        return orderId;
    }

    public void setOrderListId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        this.orderProductName = orderProductName;
    }

    public String getOrderProductDescription() {
        return orderProductDescription;
    }

    public void setOrderProductDescription(String orderProductDescription) {
        this.orderProductDescription = orderProductDescription;
    }

    public String getOrderProductPostDate() {
        return orderPostDate;
    }

    public void setOrderProductPostDate(String orderPostDate) {
        this.orderPostDate = orderPostDate;
    }


    public String getOrderProductPrice() {
        return orderProductPrice;
    }
    public void setOrderProductPrice(String orderProductPrice) {
        this.orderProductPrice = orderProductPrice;
    }

    public String getOrderProductQuantity() {
        return orderProductQuantity;
    }
    public void setOrderProductQuantity(String orderProductQuantity) {
        this.orderProductQuantity = orderProductQuantity;
    }
    public String getOrderProductShipping_charges() {
        return orderProductShipping_charges;
    }
    public void setOorderProductShipping_charges(String orderProductShipping_charges) {
        this.orderProductShipping_charges = orderProductShipping_charges;
    }

    public String getOrderProductTotal_price() {
        return orderProductTotal_price;
    }
    public void setOrderProductTotal_price(String orderProductTotal_price) {
        this.orderProductTotal_price = orderProductTotal_price;
    }

    public String getOrderProductCustomer_name() {
        return orderProductCustomer_name;
    }
    public void setOrderProductCustomer_name(String orderProductCustomer_name) {
        this.orderProductCustomer_name = orderProductCustomer_name;
    }

    public String getOrderProductCustomer_contact() {
        return orderProductCustomer_contact;
    }
    public void setOrderProductCustomer_contact(String orderProductCustomer_contact) {
        this.orderProductCustomer_contact = orderProductCustomer_contact;
    }

    public String getOrderProductCustomer_email() {
        return orderProductCustomer_email;
    }
    public void setOrderProductCustomer_email(String orderProductCustomer_email) {
        this.orderProductCustomer_email = orderProductCustomer_email;
    }

    public String getOrderProductBuidling_name() {
        return orderProductBuidling_name;
    }
    public void setOrderProductBuidling_name(String orderProductBuidling_name) {
        this.orderProductBuidling_name = orderProductBuidling_name;
    }

    public String getOrderProductArea() {
        return orderProductArea;
    }
    public void setOrderProductArea(String orderProductArea) {
        this.orderProductArea = orderProductArea;
    }

    public String getOrderProductCity() {
        return orderProductCity;
    }
    public void setOrderProductCity(String orderProductCity) {
        this.orderProductCity = orderProductCity;
    }

    public String getOrderProductId() {
        return orderProductId;
    }
    public void setOrderProductId(String orderProductId) {
        this.orderProductId = orderProductId;
    }



    public String getOrderPinCode() {
        return orderPinCode;
    }
    public void setOrderPinCode(String orderPinCode) {
        this.orderPinCode = orderPinCode;
    }





}

