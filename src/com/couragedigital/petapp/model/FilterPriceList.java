package com.couragedigital.petapp.model;

public class FilterPriceList {
    public String priceText;
    public String adoptionText;
    public boolean adoptionCheckState;

    public String getPriceText() {
        return priceText;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public String getAdoptionText() {
        return adoptionText;
    }

    public void setAdoptionText(String adoptionText) {
        this.adoptionText = adoptionText;
    }

    public boolean getAdoptionCheckState() {
        return adoptionCheckState;
    }

    public void setAdoptionCheckState(boolean adoptionCheckState) {
        this.adoptionCheckState = adoptionCheckState;
    }
}
