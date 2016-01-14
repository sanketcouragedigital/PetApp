package com.couragedigital.petapp.model;

import java.util.Date;
public class PetMateListItems {
    public String firstImagePath;
    public String secondImagePath;
    public String thirdImagePath;
    public String petMateBreed;
    public String petMatePostOwner;
    public String petMateCategory;
    public String petMateAge;
    public String petMateGender;
    public String petMateDescription;
    public String petMatePostDate;
    public String petMatePostOwnerEmail;
    public String petMatePostOwnerMobileNo;

    public PetMateListItems() {
    }

    public PetMateListItems(String firstImagePath, String secondImagePath, String thirdImagePath, String petMateBreed, String petMatePostOwner, String petMateCategory, String petMateAge, String petMateGender, String petMateDescription, String petMatePostDate, String petMatePostOwnerEmail, String petMatePostOwnerMobileNo) {
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;
        this.petMateBreed = petMateBreed;
        this.petMatePostOwner = petMatePostOwner;
        this.petMateCategory = petMateCategory;
        this.petMateAge = petMateAge;
        this.petMateGender = petMateGender;
        this.petMateDescription = petMateDescription;
        this.petMatePostDate = petMatePostDate;
        this.petMatePostOwnerEmail = petMatePostOwnerEmail;
        this.petMatePostOwnerMobileNo = petMatePostOwnerMobileNo;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = "http://storage.couragedigital.com/dev/pet_mate_images/"+firstImagePath;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = "http://storage.couragedigital.com/dev/pet_mate_images/"+secondImagePath;
    }

    public String getThirdImagePath() {
        return thirdImagePath;
    }

    public void setThirdImagePath(String thirdImagePath) {
        this.thirdImagePath = "http://storage.couragedigital.com/dev/pet_mate_images/"+thirdImagePath;
    }

    public String getPetMateBreed() {
        return petMateBreed;
    }

    public void setPetMateBreed(String petMateBreed) {
        this.petMateBreed = petMateBreed;
    }

    public String getPetMatePostOwner() {
        return petMatePostOwner;
    }

    public void setPetMatePostOwner(String petMatePostOwner) {
        this.petMatePostOwner = petMatePostOwner;
    }

    public String getPetMateCategory() {
        return petMateCategory;
    }

    public void setPetMateCategory(String petMateCategory) {
        this.petMateCategory = petMateCategory;
    }

    public String getPetMateAge() {
        return petMateAge;
    }

    public void setPetMateAge(String petMateAge) {
        this.petMateAge = petMateAge;
    }

    public String getPetMateGender() {
        return petMateGender;
    }

    public void setPetMateGender(String petMateGender) {
        this.petMateGender = petMateGender;
    }

    public String getPetMateDescription() {
        return petMateDescription;
    }

    public void setPetMateDescription(String petMateDescription) {
        this.petMateDescription = petMateDescription;
    }

    public String getPetMatePostDate() {
        return petMatePostDate;
    }

    public void setPetMatePostDate(String petMatePostDate) {
        this.petMatePostDate = petMatePostDate;
    }

    public String getPetMatePostOwnerEmail() {
        return petMatePostOwnerEmail;
    }

    public void setPetMatePostOwnerEmail(String petMatePostOwnerEmail) {
        this.petMatePostOwnerEmail = petMatePostOwnerEmail;
    }

    public String getPetMatePostOwnerMobileNo() {
        return petMatePostOwnerMobileNo;
    }

    public void setPetMatePostOwnerMobileNo(String petMatePostOwnerMobileNo) {
        this.petMatePostOwnerMobileNo = petMatePostOwnerMobileNo;
    }
}
