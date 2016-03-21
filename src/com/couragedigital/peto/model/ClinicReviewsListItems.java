package com.couragedigital.peto.model;

public class ClinicReviewsListItems {

    public String clinic_Id;
    public String clinic_ratings;
    public String clinic_reviews;
    public String email;
    public String time;

    public ClinicReviewsListItems() {
    }

    public ClinicReviewsListItems(String clinicId,String clinicRatings, String clinicReviews  ,String email, String time) {

        this.clinic_Id = clinicId;
        this.clinic_reviews = clinicReviews;
        this.clinic_ratings = clinicRatings;
        this.email = email;
        this.time = time;

    }



    public String getClinicId() {
        return clinic_Id;
    }

    public void setClinicId(String clinicId) {
        this.clinic_Id = clinicId;
    }

    public String getClinicRatings() {
        return clinic_ratings;
    }

    public void setClinicRatings(String clinicRatings) {
        this.clinic_ratings = clinicRatings;
    }

    public String getClinicReviews() {
        return clinic_reviews;
    }

    public void setClinicReviews(String clinicReviews) {
        this.clinic_reviews = clinicReviews;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }


}