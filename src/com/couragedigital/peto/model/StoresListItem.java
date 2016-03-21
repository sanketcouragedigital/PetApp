package com.couragedigital.peto.model;

import com.couragedigital.peto.Singleton.ImageURLInstance;

public class StoresListItem {
    public String image_path;
    public String stores_name;
    public String stores_address;
    public String email;
    public String contact;

    public StoresListItem() {
    }

    public StoresListItem(String storesname, String storesaddress, String email, String contact, String image_path) {

        this.stores_name = storesname;
        this.stores_address = storesaddress;
        this.email = email;
        this.contact = contact;
        this.image_path = image_path;
    }

    public String getStoresImage_path() {
        return image_path;
    }

    public void setStoresImage_path(String image_path) {
        this.image_path = ImageURLInstance.getUrl()+"store_images/" + image_path;
    }

    public String getStoresName() {
        return stores_name;
    }

    public void setStoresName(String storesname) {
        this.stores_name = storesname;
    }

    public String getStoresAdress() {
        return stores_address;
    }

    public void setStoresAdress(String storesaddress) {
        this.stores_address = storesaddress;
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
