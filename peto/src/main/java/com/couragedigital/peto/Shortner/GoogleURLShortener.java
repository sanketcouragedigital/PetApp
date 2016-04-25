package com.couragedigital.peto.Shortner;

import android.os.StrictMode;

public class GoogleURLShortener {

    public static String shortner(String longUrl) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String shortUrl = com.pddstudio.urlshortener.URLShortener.shortUrl(longUrl);
        return  shortUrl;
    }
}
