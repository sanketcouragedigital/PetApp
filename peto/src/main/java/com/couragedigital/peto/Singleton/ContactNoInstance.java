package com.couragedigital.peto.Singleton;

public class ContactNoInstance {
    private static String mobileNoInstance;
    private static String nameInstance;
    private static String buildingNameInstance;
    private static String areaInstance;
    private static String cityInstance;

    public static String  getMobileNo() {
        return mobileNoInstance;
    }
    public static void setMobileNo(String mobileNo) {
        mobileNoInstance = mobileNo;
    }

    public static String  getName() {
        return nameInstance;
    }
    public static void setName(String name) {
        nameInstance = name;
    }

    public static String  getBuildingName() {
        return buildingNameInstance;
    }
    public static void setBuildingName(String buildingName) {
        buildingNameInstance = buildingName;
    }

    public static String  getArea() {
        return areaInstance;
    }
    public static void setArea(String area) {
        areaInstance = area;
    }

    public static String  getCity() {
        return cityInstance;
    }
    public static void setCity(String city) {
        cityInstance = city;
    }
}
