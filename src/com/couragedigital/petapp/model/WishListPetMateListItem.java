package com.couragedigital.petapp.model;

import com.couragedigital.petapp.Singleton.ImageURLInstance;

public class WishListPetMateListItem {
    public int id;
    public String firstImagePath;
    public String secondImagePath;
    public String thirdImagePath;
    public String petMateBreed;
    public String petMateCategory;
    public String petMateAgeInMonth;
    public String petMateAgeInYear;
    public String petMateGender;
    public String petMateDescription;
    public String petMatePostDate;
    public String petMatePostOwnerEmail;
    public String alternateNo;
    public String name;


    public WishListPetMateListItem() {

    }

    public WishListPetMateListItem(int id, String firstImagePath, String secondImagePath, String thirdImagePath, String petMateBreed, String petMatePostOwner,
                                    String petMateCategory, String petMateAgeInMonth, String petMateAgeInYear, String petMateGender, String petMateDescription, String petMatePostDate, String petMatePostOwnerEmail,String alternateNo,String petMatePostOwnerMobileNo,String name) {
        this.id = id;
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;
        this.petMateBreed = petMateBreed;
        this.petMateCategory = petMateCategory;
        this.petMateAgeInMonth = petMateAgeInMonth;
        this.petMateAgeInYear = petMateAgeInYear;
        this.petMateGender = petMateGender;
        this.petMateDescription = petMateDescription;
        this.petMatePostDate = petMatePostDate;
        this.petMatePostOwnerEmail = petMatePostOwnerEmail;
        this.alternateNo = alternateNo;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = ImageURLInstance.getUrl()+"pet_mate_images/" + firstImagePath;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = ImageURLInstance.getUrl()+"pet_mate_images/" + secondImagePath;
    }

    public String getThirdImagePath() {
        return thirdImagePath;
    }

    public void setThirdImagePath(String thirdImagePath) {
        this.thirdImagePath = ImageURLInstance.getUrl()+"pet_mate_images/" + thirdImagePath;
    }

    public String getPetMateBreed() {
        return petMateBreed;
    }

    public void setPetMateBreed(String petMateBreed) {
        this.petMateBreed = petMateBreed;
    }

    public String getPetMateCategory() {
        return petMateCategory;
    }

    public void setPetMateCategory(String petMateCategory) {
        this.petMateCategory = petMateCategory;
    }


    public String getPetMateAgeInMonth() {
        return petMateAgeInMonth;
    }

    public void setPetMateAgeInMonth(String petMateAgeInMonth) {
        this.petMateAgeInMonth = petMateAgeInMonth;
    }

    public String getPetMateAgeInYear() {
        return petMateAgeInYear;
    }

    public void setPetMateAgeInYear(String petMateAgeInYear) {
        this.petMateAgeInYear = petMateAgeInYear;
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
    public String getAlternateNo() {
        return alternateNo;
    }

    public void setAlternateNo(String alternateNo) {
        this.alternateNo = alternateNo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
