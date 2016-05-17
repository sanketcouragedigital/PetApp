package com.couragedigital.peto.Singleton;

public class ContactNoInstance {
    private static String mobileNoInstance;
    private static String nameInstance;
    private static String buildingNameInstance;
    private static String areaInstance;
    private static String cityInstance;

    private static String is_NgoInstance;
    private static String Is_VerifiedInstance;
    private static String ngo_urlInstance;

//    public static String  getNgo_Url() {
//        return ngo_urlInstance;
//    }
//    public static void setNgo_Url(String ngo_url) {
//        ngo_urlInstance = ngo_url;
//    }

    public static String  getIs_Verified() {
        return Is_VerifiedInstance;
    }
    public static void seIs_Verified(String Is_Verified) {
        Is_VerifiedInstance = Is_Verified;
    }


    public static String  getIs_Ngo() {
        return is_NgoInstance;
    }
    public static void seIs_Ngo(String is_Ngo) {
        is_NgoInstance = is_Ngo;
    }

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
