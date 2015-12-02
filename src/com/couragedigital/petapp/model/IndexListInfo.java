package com.couragedigital.petapp.model;

import java.util.ArrayList;
import java.util.List;

public class IndexListInfo {

    public String vtittle;
    public String vdescription;
    public int vbackground;

    public String getTittle() {
        return vtittle;
    }

    public void setTittle(String tittle) {
        this.vtittle = tittle;
    }

    public String getDescription() {
        return vdescription;
    }

    public void setDescription(String description) {
        this.vdescription = description;
    }

    public int getThumbnail() {
        return vbackground;
    }

    public void setThumbnail(int thumbnail) {
        this.vbackground = thumbnail;
    }

}
