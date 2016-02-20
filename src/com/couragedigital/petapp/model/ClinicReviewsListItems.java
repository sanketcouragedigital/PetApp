package com.couragedigital.petapp.model;

public class ClinicReviewsListItems {

    public String clinic_Id;
    public String clinic_ratings;
    public String clinic_reviews;
    public String email;

    public ClinicReviewsListItems() {
    }

    public ClinicReviewsListItems(String clinicId,String clinicRatings, String clinicReviewseviews  ,String email) {

        this.clinic_Id = clinicId;
        this.clinic_reviews = clinicReviewseviews;
        this.clinic_ratings = clinicRatings;
        this.email = email;

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


}