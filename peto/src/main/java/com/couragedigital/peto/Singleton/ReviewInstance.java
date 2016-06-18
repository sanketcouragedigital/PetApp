package com.couragedigital.peto.Singleton;

import com.couragedigital.peto.model.ReviewsListItems;


import java.util.ArrayList;
import java.util.List;

public class ReviewInstance {
    private static String nameInstance;
    private static String ratingInstance;
    private static String reviewsInstance;
    private static String timeInstance;
    private static String serviceTypeInstance;
    private static Integer relativeLayoutHeightInstance = 0;

    private static List<ReviewsListItems> reviewsListItemsArrayList = new ArrayList<>();

    public static List<ReviewsListItems> getReviewsListItemsArrayList() {
        return reviewsListItemsArrayList;
    }

    public static void setReviewsListItemsArrayList(List<ReviewsListItems> reviewsList) {
        reviewsListItemsArrayList = reviewsList;
    }

    public static String  getUserName() {
        return nameInstance;
    } public static void setUserName(String username) {
        nameInstance = username;
    }

    public static String  getRatingNo() {
        return ratingInstance;
    } public static void setRatingNo(String ratingNo) {
        ratingInstance = ratingNo;
    }

    public static String  getReviews() {
        return reviewsInstance;
    } public static void setReviews(String reviews) {
        reviewsInstance = reviews;
    }

    public static String  getServiceTypeInstance() {
        return serviceTypeInstance;
    } public static void setServiceTypeInstance(String serviceTypeInstance) {
        serviceTypeInstance = serviceTypeInstance;
    }

    public static String  getTime() {
        return timeInstance;
    } public static void setTime(String time) {
        timeInstance = time;
    }

    public static Integer getRelativeLayoutHeightInstance() {
        return relativeLayoutHeightInstance;
    }

    public static void setRelativeLayoutHeightInstance(Integer relativeLayoutHeight) {
        relativeLayoutHeightInstance = relativeLayoutHeight;
    }
}
