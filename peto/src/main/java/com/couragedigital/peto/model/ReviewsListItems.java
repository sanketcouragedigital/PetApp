package com.couragedigital.peto.model;


public class ReviewsListItems {
    public String Id;
    public String ratings;
    public String reviews;
    public String email;
    public String time;
    public String serviceType;
    public String emptyList;
    public String emptyKey;

    public ReviewsListItems() {
    }

    public ReviewsListItems(String Id, String ratings, String reviews  , String email, String time, String serviceType, String emptyList, String emptyKey) {

        this.Id = Id;
        this.reviews = reviews;
        this.ratings = ratings;
        this.email = email;
        this.time = time;
        this.serviceType = serviceType;
        this.emptyList = emptyList;
        this.emptyKey = emptyKey;

    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmptyKey() {
        return emptyKey;
    }
    public void setEmptyKey(String emptyKey) {
        this.emptyKey = emptyKey;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getEmptyList() {
        return emptyList;
    }

    public void setEmptyList(String emptyList) {
        this.emptyList = emptyList;
    }
}
