package com.couragedigital.peto.Singleton;

public class EditProfileDetailsInstance {

    private static String nameInstance;
    private static String buildingNameInstance;
    private static String areaInstance;
    private static String cityInstance;
    private static String mobileNoInstance;
    private static String emailInstance;
    private static String passwordInstance;
    private static String ngoUrlInstance;
    private static String ngoNameInstance;


    public static String  getName() {
        return nameInstance;
    } public static void setName(String name) {
        nameInstance = name;
    }
    public static String  getBuildingName() {
        return buildingNameInstance;
    } public static void setBuildingName(String buildingName) {
        buildingNameInstance = buildingName;
    }
    public static String  getArea() {
        return areaInstance;
    } public static void setArea(String area) {
        areaInstance = area;
    }
    public static String  getCity() {
        return cityInstance;
    } public static void setCity (String city) {
        cityInstance = city;
    }
    public static String  getMobileNo() {
        return mobileNoInstance;
    } public static void setMobileNo(String mobileNo) {
        mobileNoInstance = mobileNo;
    }
    public static String  getEmail() {
        return emailInstance;
    } public static void setEmail(String email) {
        emailInstance = email;
    }
    public static String  getPassword() {
        return passwordInstance;
    } public static void setPassword(String password) {
        passwordInstance = password;
    }

    public static String  getNgoUrlInstance() {
        return ngoUrlInstance;
    } public static void setNgoUrlInstance(String ngoUrl) {
        ngoUrlInstance = ngoUrl;
    }

    public static String  getNgoNameInstance() {
        return ngoNameInstance;
    } public static void setNgoNameInstance(String ngoName) {
        ngoNameInstance = ngoName;
    }


}
