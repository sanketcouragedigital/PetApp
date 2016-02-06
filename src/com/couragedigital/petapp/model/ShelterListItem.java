package com.couragedigital.petapp.model;

public class ShelterListItem {
    public String image_path;
    public String shelter_name;
    public String shelter_address;
    public String email;
    public String contact;

    public ShelterListItem() {
    }

    public ShelterListItem(String sheltername, String shelteraddress, String email, String contact, String image_path) {

        this.shelter_name = sheltername;
        this.shelter_address = shelteraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
    }

    public String getShelterImage_path() {
        return image_path;
    }

    public void setShelterImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/shelter_images/" + image_path;
    }

    public String getShelterName() {
        return shelter_name;
    }

    public void setShelterName(String sheltername) {
        this.shelter_name = sheltername;
    }

    public String getShelterAdress() {
        return shelter_address;
    }

    public void setShelterAdress(String shelteraddress) {
        this.shelter_address = shelteraddress;
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
}
