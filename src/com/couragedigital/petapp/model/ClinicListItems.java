package com.couragedigital.petapp.model;


import com.couragedigital.petapp.Singleton.ImageURLInstance;

public class ClinicListItems {

    public String image_path;
    public String clinic_name;
    public String clinic_Id;
    public String clinic_address;
    public String doctor_name;
    public String email;
    public String contact;
    private String area;
    private String city;
    private  String notes;

    public ClinicListItems() {
    }

    public ClinicListItems(String clinicId,String clinicname, String clinicaddress, String doctorname, String email, String contact, String image_path , String area ,String city,String notes) {

        this.clinic_Id = clinicId;
        this.clinic_name = clinicname;
        this.doctor_name = doctorname;
        this.clinic_address = clinicaddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.area = area;
        this.city = city;
        this.notes = notes;
    }

    public String getClinicImage_path() {
        return image_path;
    }

    public void setClinicImage_path(String image_path) {
        this.image_path = ImageURLInstance.getUrl()+"clinic_images/" + image_path;
    }

    public String getClinicId() {
        return clinic_Id;
    }

    public void setClinicId(String clinicId) {
        this.clinic_Id = clinicId;
    }

    public String getClinicName() {
        return clinic_name;
    }

    public void setClinicName(String clinicname) {
        this.clinic_name = clinicname;
    }

    public String getClinicAddress() {
        return clinic_address;
    }

    public void setClinicAddress(String clinicaddress) {
        this.clinic_address = clinicaddress;
    }

    public String getDoctorName() {
        return doctor_name;
    }

    public void setDoctorName(String doctorname) {
        this.doctor_name = doctorname;
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

    public  String getNotes(){
        return  notes;
    }
    public  void  setNotes(String notes){
        this.notes = notes;
    }

}
