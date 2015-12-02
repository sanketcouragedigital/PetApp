package com.couragedigital.petapp.model;

public class PetList {
    private String image_path, petBreedOrigin;

    public PetList() {
    }

    public PetList(String image_path, String petBreedOrigin) {
        this.image_path = image_path;
        this.petBreedOrigin = petBreedOrigin;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = "http://storage.couragedigital.com/dev/pet_images/" + image_path;
    }

    public String getPetBreedOrigin() {
        return petBreedOrigin;
    }

    public void setPetBreedOrigin(String petBreedOrigin) {
        this.petBreedOrigin = petBreedOrigin;
    }
}
