package com.couragedigital.petapp.Singleton;


import com.couragedigital.petapp.model.ClinicReviewsListItems;

import java.util.ArrayList;
import java.util.List;

public class ClinicReviewInstance {
    private static String nameInstance;
    private static String ratingInstance;
    private static String reviewsInstance;
    private static String timeInstance;

    private static List<ClinicReviewsListItems> clinicReviewsListItemsArrayList = new ArrayList<>();

    public static List<ClinicReviewsListItems> getClinicReviewsListItemsArrayList() {
        return clinicReviewsListItemsArrayList;
    }
    public static void setClinicReviewsListItemsArrayList(List<ClinicReviewsListItems> clinicReviewsList) {
        clinicReviewsListItemsArrayList = clinicReviewsList;
    }
    /////////////////
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

    public static String  getTime() {
        return timeInstance;
    } public static void setTime(String time) {
        timeInstance = time;
    }
}
