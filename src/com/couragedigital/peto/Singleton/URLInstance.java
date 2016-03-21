package com.couragedigital.peto.Singleton;

public class URLInstance {
    private static String url = "http://storage.couragedigital.com/prod/api/petappapi.php";
    //private static String url = "http://192.168.1.2/PetAppAPI/api/petappapi.php";

    public static String getUrl() {
        return url;
    }
}