package com.couragedigital.peto.model;


import com.couragedigital.peto.Singleton.ImageURLInstance;

public class WishListPetListItem {
    public Integer id;
    public String firstImagePath;
    public String secondImagePath;
    public String thirdImagePath;
    public String petBreed;
    public String listingType;
    public String petCategory;
    //    public String petAge;
    public String petAgeInMonth;
    public String petAgeInYear;
    public String petGender;
    public String petDescription;
    public String petPostDate;
    public String petPostOwnerEmail;
    public String alternateNo;
    public String name;

    public WishListPetListItem() {
    }

    public WishListPetListItem(Integer id, String firstImagePath, String secondImagePath, String thirdImagePath, String petBreed, String petPostOwner, String listingType, String petCategory, String petAgeInMonth, String petAgeInYear, String petGender, String petDescription, String petPostDate, String petPostOwnerEmail,String alternateNo, String name) {
        this.id = id;
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;
        this.petBreed = petBreed;
        this.listingType = listingType;
        this.petCategory = petCategory;
        this.petAgeInMonth = petAgeInMonth;
        this.petAgeInYear = petAgeInYear;
        this.petGender = petGender;
        this.petDescription = petDescription;
        this.petPostDate = petPostDate;
        this.petPostOwnerEmail = petPostOwnerEmail;
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
        this.firstImagePath = ImageURLInstance.getUrl()+"pet_images/" + firstImagePath;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = ImageURLInstance.getUrl()+"pet_images/" + secondImagePath;
    }

    public String getThirdImagePath() {
        return thirdImagePath;
    }

    public void setThirdImagePath(String thirdImagePath) {
        this.thirdImagePath = ImageURLInstance.getUrl()+"pet_images/" + thirdImagePath;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getListingType() {
        return listingType;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }

    public String getPetCategory() {
        return petCategory;
    }

    public void setPetCategory(String petCategory) {
        this.petCategory = petCategory;
    }
    public String getPetAgeInMonth() {
        return petAgeInMonth;
    }

    public void setPetAgeInMonth(String petAgeInMonth) {
        this.petAgeInMonth = petAgeInMonth;
    }

    public String getPetAgeInYear() {
        return petAgeInYear;
    }

    public void setPetAgeInYear(String petAgeInYear) {
        this.petAgeInYear = petAgeInYear;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getPetPostDate() {
        return petPostDate;
    }

    public void setPetPostDate(String petPostDate) {
        this.petPostDate = petPostDate;
    }

    public String getPetPostOwnerEmail() {
        return petPostOwnerEmail;
    }

    public void setPetPostOwnerEmail(String petPostOwnerEmail) {
        this.petPostOwnerEmail = petPostOwnerEmail;
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
