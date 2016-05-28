package com.couragedigital.peto.Singleton;

public class ProfileURLInstance {
    private static String url = "http://petoandme.com/API/petProfile.php";
    //private static String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
    //private static String url = "http://192.168.0.11/PetAppAPI/api/petappapi.php";
    public static String getUrl() {
        return url;
    }
}
