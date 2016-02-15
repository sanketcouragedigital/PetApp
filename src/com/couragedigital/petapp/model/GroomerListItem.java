package com.couragedigital.petapp.model;

public class GroomerListItem {
    public String image_path;
    public String groomer_name;
    public String groomer_address;
    public String email;
    public String contact;
    private String area;
    private String city;

    public GroomerListItem() {
    }

    public GroomerListItem(String groomername, String groomeraddress, String email, String contact, String image_path, String area, String city) {

        this.groomer_name = groomername;
        this.groomer_address = groomeraddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
        this.area = area;
        this.city = city;
    }

    public String getGroomerImage_path() {
        return image_path;
    }

    public void setGroomerImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/groomer_images/" + image_path;
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
