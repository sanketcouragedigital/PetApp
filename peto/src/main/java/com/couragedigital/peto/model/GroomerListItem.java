package com.couragedigital.peto.model;

import com.couragedigital.peto.Singleton.ImageURLInstance;

public class GroomerListItem {
    public String image_path;
    public String groomer_name;
    public String groomer_address;
    public String email;
    public String contact;
    private String area;
    private String city;

    public String groomer_Id;
    private String notes;
    private  String latitude;
    private  String longitude;
    private String description;

    public GroomerListItem() {
    }

    public GroomerListItem(String groomerId,String groomername, String groomeraddress, String email, String contact, String image_path, String area, String city,String notes,String latitude,String longitude, String description) {

        this.groomer_name = groomername;
        this.groomer_address = groomeraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.area = area;
        this.city = city;

        this.groomer_Id = groomerId;
        this.notes = notes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getGroomer_Id() {
        return groomer_Id;
    }
    public void setGroomer_Id(String groomer_Id) {
        this.groomer_Id = groomer_Id;
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

    public String getGroomerImage_path() {
        return image_path;
    }

    public void setGroomerImage_path(String image_path) {
        this.image_path = ImageURLInstance.getUrl()+"groomer_images/" + image_path;
    }

    public String getGroomerName() {
        return groomer_name;
    }

    public void setGroomerName(String groomername) {
        this.groomer_name = groomername;
    }

    public String getGroomerAdress() {
        return groomer_address;
    }

    public void setGroomerAdress(String groomeraddress) {
        this.groomer_address = groomeraddress;
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
