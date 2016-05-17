package com.couragedigital.peto.model;


import com.couragedigital.peto.Singleton.ImageURLInstance;

public class CampaignListItem {

    public String id;
    public String firstImagePath;
    public String secondImagePath;
    public String thirdImagePath;
    public String ngoName;
    public String remainingAmount;
    public String collectedAmount;
    public String email;
    public String campaignName;
    public String description;
    public String actualAmount;
    public String lastDate;
    public String minimumAmount;
    public String postDate;
    public String ngoUrl;
    public String mobileno;

    public CampaignListItem() {
    }

        public CampaignListItem(String id, String firstImagePath, String secondImagePath, String thirdImagePath,String remainingAmount,String collectedAmount,String nameOfNgo,String emailOfUser,String nameOfCampaign,String descriptionOfCampaign,String actualAmountOfCampaign,String minimumAmountOfCampaign,String lastDateOfCampaign,String postDateOfCampaign,String urlOfNgo,String mobileNoOfUser) {
        this.id = id;
        this.firstImagePath = firstImagePath;
        this.secondImagePath = secondImagePath;
        this.thirdImagePath = thirdImagePath;

        this.remainingAmount = remainingAmount;
        this.collectedAmount = collectedAmount;
        this.ngoName = nameOfNgo;
        this.email = emailOfUser;
        this.campaignName = nameOfCampaign;
        this.description = descriptionOfCampaign;
        this.actualAmount = actualAmountOfCampaign;
        this.minimumAmount = minimumAmountOfCampaign;
        this.lastDate = lastDateOfCampaign;
        this.postDate = postDateOfCampaign;
        this.ngoUrl = urlOfNgo;
        this.mobileno = mobileNoOfUser;
    }

    public String getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(String collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }


    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = ImageURLInstance.getUrl()+"pet_images/"+firstImagePath;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = ImageURLInstance.getUrl()+"pet_images/"+secondImagePath;
    }

    public String getThirdImagePath() {
        return thirdImagePath;
    }

    public void setThirdImagePath(String thirdImagePath) {
        this.thirdImagePath = ImageURLInstance.getUrl()+"pet_images/"+thirdImagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getActualAmount() {
        return actualAmount;
    }
    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }
    public String getMinimumAmount() {
        return minimumAmount;
    }
    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getLastDate() {
        return lastDate;
    }
    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getPostDate() {
        return postDate;
    }
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
    public String getNgo_url() {
        return ngoUrl;
    }
    public void setNgo_url(String ngoUrl) {
        this.ngoUrl = ngoUrl;
    }

    public String getMobileno() {
        return mobileno;
    }
    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }





}
