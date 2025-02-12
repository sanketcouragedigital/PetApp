package com.couragedigital.peto.SessionManager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.couragedigital.peto.SignIn;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;      // Shared pref mode
    SessionManager sessionManager;

    // Sharedpref file name
    private static final String PREF_NAME = "PetoPreference";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    public static final String KEY_NGO = "isNgo";

    public static final String KEY_FIREBASE_TOKEN = "isFirebaseToken";

    public static final String REGISTERED = "registered";

    public SessionManager(Context c) {
        this.context = c;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String email, String isNgo) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing ngo in pref
        editor.putString(KEY_NGO, isNgo);

        // commit changes
        editor.commit();
    }

    public void createUserFirebaseNotificationToken(String isFirebaseToken) {
        // Storing login value as TRUE
        editor.putBoolean(REGISTERED, true);
        editor.commit();

        // Storing firebsase token in pref
        editor.putString(KEY_FIREBASE_TOKEN, isFirebaseToken);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user is ngo or not
        user.put(KEY_NGO, pref.getString(KEY_NGO, null));

        // return user
        return user;
    }

    public HashMap<String, String> getUserFirebaseNotificationToken() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user email id
        user.put(KEY_FIREBASE_TOKEN, pref.getString(KEY_FIREBASE_TOKEN, null));

        // return user
        return user;
    }

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, SignIn.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // Staring Login Activity
        context.startActivity(i);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isRegisteredToken() {
        return pref.getBoolean(REGISTERED, false);
    }

}
