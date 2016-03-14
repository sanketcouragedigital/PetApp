package com.couragedigital.petapp.Singleton;

public class URLInstance {
    private static String url = "http://storage.couragedigital.com/prod/api/petappapi.php";

    public static String getUrl() {
        return url;
    }
}