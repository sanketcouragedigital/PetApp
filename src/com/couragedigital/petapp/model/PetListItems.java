package com.couragedigital.petapp.model;

import java.util.Date;

public class PetListItems {
    public String image_path;
    public String petBreed;
    public String petPostOwner;
    public String listingType;
    public String petCategory;
    public String petAge;
    public String petGender;
    public String petDescription;
    public String petPostDate;
    public String petPostOwnerEmail;
    public String petPostOwnerMobileNo;

    public PetListItems() {
    }

    public PetListItems(String image_path, String petBreed, String petPostOwner, String listingType, String petCategory, String petAge, String petGender, String petDescription, String petPostDate, String petPostOwnerEmail, String petPostOwnerMobileNo) {
        this.image_path = image_path;
        this.petBreed = petBreed;
        this.petPostOwner = petPostOwner;
        this.listingType = listingType;
        this.petCategory = petCategory;
        this.petAge = petAge;
        this.petGender = petGender;
        this.petDescription = petDescription;
        this.petPostDate = petPostDate;
        this.petPostOwnerEmail = petPostOwnerEmail;
        this.petPostOwnerMobileNo = petPostOwnerMobileNo;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/pet_images/"+image_path;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getPetPostOwner() {
        return petPostOwner;
    }

    public void setPetPostOwner(String petPostOwner) {
        this.petPostOwner = petPostOwner;
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

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
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

    public String getPetPostOwnerMobileNo() {
        return petPostOwnerMobileNo;
    }

    public void setPetPostOwnerMobileNo(String petPostOwnerMobileNo) {
        this.petPostOwnerMobileNo = petPostOwnerMobileNo;
    }
}
