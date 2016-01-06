package com.couragedigital.petapp.model;

import java.util.Date;
public class PetMateListItems {
    public String image_path;
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

    public PetMateListItems(String image_path, String petMateBreed, String petMatePostOwner, String petMateCategory, String petMateAge, String petMateGender, String petMateDescription, String petMatePostDate, String petMatePostOwnerEmail, String petMatePostOwnerMobileNo) {
        this.image_path = image_path;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/pet_mate_images/"+image_path;
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
