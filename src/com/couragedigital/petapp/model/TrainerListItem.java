package com.couragedigital.petapp.model;

import com.couragedigital.petapp.Singleton.ImageURLInstance;

public class TrainerListItem {
    public String image_path;
    public String trainer_name;
    public String trainer_address;
    public String email;
    public String contact;
    public String trainerDescription;

    public TrainerListItem() {
    }

    public TrainerListItem(String trainername, String traineraddress, String email, String contact, String image_path, String trainerDescription) {

        this.trainer_name = trainername;
        this.trainer_address = traineraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.trainerDescription = trainerDescription;
    }

    public String getTrainerImage_path() {
        return image_path;
    }

    public void setTrainerImage_path(String image_path) {
        this.image_path = ImageURLInstance.getUrl()+"trainer_images/" + image_path;
    }

    public String getTrainerName() {
        return trainer_name;
    }

    public void setTrainerName(String trainername) {
        this.trainer_name = trainername;
    }

    public String getTrainerAdress() {
        return trainer_address;
    }

    public void setTrainerAdress(String traineraddress) {
        this.trainer_address = traineraddress;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTrainerDescription(String trainerDescription) {
        this.trainerDescription = trainerDescription;
    }

    public String getTrainerDescription() {
        return trainerDescription;
    }


}
