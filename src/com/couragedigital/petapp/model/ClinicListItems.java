package com.couragedigital.petapp.model;


public class ClinicListItems {

    public String image_path;
    public String clinic_name;
    public String clinic_address;
    public String doctor_name;
    public String email;
    public String contact;

    public ClinicListItems() {
    }

    public ClinicListItems(String clinicname, String clinicaddress, String doctorname, String email, String contact, String image_path) {

        this.clinic_name = clinicname;
        this.clinic_address = clinicaddress;
        this.doctor_name = doctorname;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
    }

    public String getClinicImage_path() {
        return image_path;
    }

    public void setClinicImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/clinic_images/" + image_path;
    }

    public String getClinicName() {
        return clinic_name;
    }

    public void setClinicName(String clinicname) {
        this.clinic_name = clinicname;
    }

    public String getClinicAdress() {
        return clinic_address;
    }

    public void setClinicAdress(String clinicaddress) {
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

}
