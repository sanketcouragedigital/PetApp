package com.couragedigital.peto.model;

import com.couragedigital.peto.Singleton.ImageURLInstance;

public class ShelterListItem {
    public String image_path;
    public String shelter_name;
    public String shelter_address;
    public String email;
    public String contact;
    private String area;
    private String city;

    public String shelter_Id;
    private String notes;
    private  String latitude;
    private  String longitude;
    private String description;

    public ShelterListItem() {
    }

    public ShelterListItem(String shelterId,String sheltername, String shelteraddress, String email, String contact, String image_path,String area, String city,String notes,String latitude,String longitude, String description) {

        this.shelter_name = sheltername;
        this.shelter_address = shelteraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.area = area;
        this.city = city;

        this.shelter_Id = shelterId;
        this.notes = notes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;

    }

    public String getShelter_Id() {
        return shelter_Id;
    }
    public void setShelter_Id(String shelter_Id) {
        this.shelter_Id = shelter_Id;
    }
    public  String getNotes(){
        return  notes;
    }
    public  void  setNotes(String notes){
        this.notes = notes;
    }

    public  void  setLatitude(String latitude){
        this.latitude = latitude;
    }

    public  String getlatitude(){
        return  latitude;
    }

    public  void  setLongitude(String longitude){
        this.longitude = longitude;
    }

    public  String getLongitude(){
        return  longitude;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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
