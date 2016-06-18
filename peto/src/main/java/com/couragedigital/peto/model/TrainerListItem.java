package com.couragedigital.peto.model;

import com.couragedigital.peto.Singleton.ImageURLInstance;

public class TrainerListItem {
    public String image_path;
    public String trainer_name;
    public String trainer_Id;
    public String trainer_address;
    public String email;
    public String contact;
    private String area;
    private String city;
    private String notes;
    private  String latitude;
    private  String longitude;
    private String trainerDescription;


    public TrainerListItem() {
    }

    public TrainerListItem(String trainerId,String trainername, String traineraddress, String email, String contact, String image_path , String area ,String city,String notes,String latitude,String longitude, String trainerDescription) {

        this.trainer_Id = trainerId;
        this.trainer_name = trainername;
        this.trainer_address = traineraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.area = area;
        this.city = city;
        this.notes = notes;
        this.latitude = latitude;
        this.longitude = longitude;
        this.trainerDescription = trainerDescription;
    }

    public String getTrainerImage_path() {
        return image_path;
    }

    public void setTrainerImage_path(String image_path) {
        this.image_path = ImageURLInstance.getUrl()+"trainer_images/" + image_path;
    }

    public String getTrainer_Id() {
        return trainer_Id;
    }

    public void setTrainer_Id(String trainer_Id) {
        this.trainer_Id = trainer_Id;
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


}
