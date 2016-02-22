package com.couragedigital.petapp.model;

import com.couragedigital.petapp.Singleton.ImageURLInstance;

public class ShelterListItem {
    public String image_path;
    public String shelter_name;
    public String shelter_address;
    public String email;
    public String contact;
    private String area;
    private String city;

    public ShelterListItem() {
    }

    public ShelterListItem(String sheltername, String shelteraddress, String email, String contact, String image_path,String area, String city) {

        this.shelter_name = sheltername;
        this.shelter_address = shelteraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.area = area;
        this.city = city;

    }

    public String getShelterImage_path() {
        return image_path;
    }

    public void setShelterImage_path(String image_path) {
        this.image_path = ImageURLInstance.getUrl()+"shelter_images/" + image_path;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
