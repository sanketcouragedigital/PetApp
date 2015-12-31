package com.couragedigital.petapp.model;

import java.util.Date;
public class PetMetList {
    public String image_path;
    public String petMetBreed;
    public String petMetPostOwner;
    public String petMetCategory;
    public String petMetAge;
    public String petMetGender;
    public String petMetDescription;
    public String petMetPostDate;
    public String petMetPostOwnerEmail;
    public String petMetPostOwnerMobileNo;

    public PetMetList() {
    }

    public PetMetList(String image_path, String petMetBreed, String petMetPostOwner, String petMetCategory, String petMetAge, String petMetGender, String petMetDescription, String petMetPostDate, String petMetPostOwnerEmail, String petMetPostOwnerMobileNo) {
        this.image_path = image_path;
        this.petMetBreed = petMetBreed;
        this.petMetPostOwner = petMetPostOwner;
        this.petMetCategory = petMetCategory;
        this.petMetAge = petMetAge;
        this.petMetGender = petMetGender;
        this.petMetDescription = petMetDescription;
        this.petMetPostDate = petMetPostDate;
        this.petMetPostOwnerEmail = petMetPostOwnerEmail;
        this.petMetPostOwnerMobileNo = petMetPostOwnerMobileNo;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/pet_met_images/"+image_path;
    }

    public String getPetMetBreed() {
        return petMetBreed;
    }

    public void setPetMetBreed(String petMetBreed) {
        this.petMetBreed = petMetBreed;
    }

    public String getPetMetPostOwner() {
        return petMetPostOwner;
    }

    public void setPetMetPostOwner(String petMetPostOwner) {
        this.petMetPostOwner = petMetPostOwner;
    }

    public String getPetMetCategory() {
        return petMetCategory;
    }

    public void setPetMetCategory(String petMetCategory) {
        this.petMetCategory = petMetCategory;
    }

    public String getPetMetAge() {
        return petMetAge;
    }

    public void setPetMetAge(String petMetAge) {
        this.petMetAge = petMetAge;
    }

    public String getPetMetGender() {
        return petMetGender;
    }

    public void setPetMetGender(String petMetGender) {
        this.petMetGender = petMetGender;
    }

    public String getPetMetDescription() {
        return petMetDescription;
    }

    public void setPetMetDescription(String petMetDescription) {
        this.petMetDescription = petMetDescription;
    }

    public String getPetMetPostDate() {
        return petMetPostDate;
    }

    public void setPetMetPostDate(String petMetPostDate) {
        this.petMetPostDate = petMetPostDate;
    }

    public String getPetMetPostOwnerEmail() {
        return petMetPostOwnerEmail;
    }

    public void setPetMetPostOwnerEmail(String petMetPostOwnerEmail) {
        this.petMetPostOwnerEmail = petMetPostOwnerEmail;
    }

    public String getPetMetPostOwnerMobileNo() {
        return petMetPostOwnerMobileNo;
    }

    public void setPetMetPostOwnerMobileNo(String petMetPostOwnerMobileNo) {
        this.petMetPostOwnerMobileNo = petMetPostOwnerMobileNo;
    }
}
