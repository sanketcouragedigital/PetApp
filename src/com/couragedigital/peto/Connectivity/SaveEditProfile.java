package com.couragedigital.peto.Connectivity;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.EditProfile;
import com.couragedigital.peto.Index;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SaveEditProfile {
    private static Context context = null;
    private static String username;
    private static String userbuildingname;
    private static String userarea;
    private static String usercity;
    private static String usermobileno;
    private static String useremail;
    private static String userOldEmail;
    private static String userconfirmpassword;
    private static String method;
    private static String format;
    private static String editResponse;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String uploadEditedDetails(String name, String buildingname, String area, String city, String mobileno, String email, String oldEmail,String confirmpassword) throws Exception {
        method = "editProfile";
        format = "json";
        username = name;
        userbuildingname = buildingname;
        userarea = area;
        usercity = city;
        usermobileno = mobileno;
        useremail = email;
        userOldEmail=oldEmail;
        userconfirmpassword = confirmpassword;
        final String URL = URLInstance.getUrl();
        //final String URL = "http://192.168.0.6/PetAppAPI/api/petappapi.php";
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("name", username);
            params.put("buildingname", userbuildingname);
            params.put("area", userarea);
            params.put("city", usercity);
            params.put("mobileno", usermobileno);
            params.put("email", useremail);
            params.put("oldEmail", userOldEmail);
            params.put("confirmpassword", userconfirmpassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        EditProfile editProfile = new EditProfile();
                        try {
                            returnResponse(response.getString("saveUsersEditDetailsResponse"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
        AppController.getInstance().addToRequestQueue(req);
        return editResponse;
    }

    public SaveEditProfile(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("USERS_DETAILS_EDITED")) {
            Toast.makeText(context, "Details Successfully Change.", Toast.LENGTH_SHORT).show();
            Intent gotologinpage = new Intent(context, Index.class);
            context.startActivity(gotologinpage);
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Intent gotosignupgae = new Intent(context, EditProfile.class);
            context.startActivity(gotosignupgae);
        }
    }
}
