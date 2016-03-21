package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.SignIn;
import com.couragedigital.peto.SignUp;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RegisterToServer {

    private static Context context = null;
    private static String username;
    private static String userbuildingname;
    private static String userarea;
    private static String usercity;
    private static String usermobileno;
    private static String useremail;
    private static String userconfirmpassword;
    private static String method;
    private static String format;
    private static String signupResponse;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String uploadToRemoteServer(String name, String buildingname, String area, String city, String mobileno, String email, String confirmpassword) throws Exception {
        method = "userRegistration";
        format = "json";
        username = name;
        userbuildingname = buildingname;
        userarea = area;
        usercity = city;
        usermobileno = mobileno;
        useremail = email;
        userconfirmpassword = confirmpassword;
        final String URL = URLInstance.getUrl();
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
            params.put("confirmpassword", userconfirmpassword);
        } catch (Exception e) {

        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        SignUp signUp = new SignUp();
                        try {
                            returnResponse(response.getString("saveUsersDetailsResponse"));
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
        return signupResponse;
    }

    public RegisterToServer(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("USERS_DETAILS_SAVED")) {
            Toast.makeText(context, "Successfully Registered.", Toast.LENGTH_SHORT).show();
            Intent gotologinpage = new Intent(context, SignIn.class);
            context.startActivity(gotologinpage);
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Intent gotosignupgae = new Intent(context, SignUp.class);
            context.startActivity(gotosignupgae);
        }
    }


}
