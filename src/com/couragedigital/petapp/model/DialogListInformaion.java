package com.couragedigital.petapp.model;

public class DialogListInformaion {
    public String vname;
    public int vicon;

    public DialogListInformaion() {

    }

    public DialogListInformaion(String vname, int vicon) {
        this.vname = vname;
        this.vicon = vicon;
    }

    public String getTittle() {
        return vname;
    }

    public void setTittle(String tittle) {
        this.vname = tittle;
    }

    public int getIcons() {
        return vicon;
    }

    public void setIcons(int icons) {
        this.vicon = icons;
    }

}
