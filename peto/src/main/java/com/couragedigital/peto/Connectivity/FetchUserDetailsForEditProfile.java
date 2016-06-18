package com.couragedigital.peto.Connectivity;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.*;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.EditProfileDetailsInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FetchUserDetailsForEditProfile {

    private static String method;
    private static String format;
    private static String userOldEmail;
    private static String userOldPassword;
    private static String detailsResponse;
    private static Context context;
    private static final String TAG = PetFetchList.class.getSimpleName();

    public String fetchUserDeatils(String oldEmail,String confPassword, View view) {

        //String url = "http://192.168.0.5/PetAppAPI/api/petappapi.php";
        String url = URLInstance.getUrl();
        method = "fetchUserDetails";
        format = "json";
        userOldEmail=oldEmail;
        userOldPassword=confPassword;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", method);
        params.put("format", format);
        params.put("oldEmail", userOldEmail);
        params.put("confirmpassword", userOldPassword);

        JsonObjectRequest fetchUserDetailsRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            returnResponse(response.getString("showUserDetails"));

                            JSONArray jsonArray = response.getJSONArray("showUserDetails");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {

                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        EditProfileDetailsInstance editProfileDetailsInstance = new EditProfileDetailsInstance();

                                        editProfileDetailsInstance.setName(obj.getString("name"));
                                        editProfileDetailsInstance.setBuildingName(obj.getString("buildingname"));
                                        editProfileDetailsInstance.setArea(obj.getString("area"));
                                        editProfileDetailsInstance.setCity(obj.getString("city"));
                                        editProfileDetailsInstance.setMobileNo(obj.getString("mobileno"));
                                        editProfileDetailsInstance.setEmail(obj.getString("email"));
                                        editProfileDetailsInstance.setPassword(obj.getString("password"));
                                        editProfileDetailsInstance.setNgoUrlInstance(obj.getString("ngo_url"));
                                        editProfileDetailsInstance.setNgoNameInstance(obj.getString("ngo_name"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Intent gotoEditProfile = new Intent(context, EditProfile.class);
                                context.startActivity(gotoEditProfile);
                            }else{
                                Intent gotoNullError = new Intent(context, NullRespone_DialogeBox.class);
                                context.startActivity(gotoNullError);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        fetchUserDetailsRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(fetchUserDetailsRequest);
        return detailsResponse;
    }
    public FetchUserDetailsForEditProfile(Context context) {
        this.context = context;
    }

    public void returnResponse(String response) {
        if (response.equals("INVALID_PASSWORD")) {
            Intent gotoCheckPassword = new Intent(context, CheckPasswordForEditProfile.class);
            context.startActivity(gotoCheckPassword);
            Toast.makeText(context, "Enter Valid Password...", Toast.LENGTH_SHORT).show();
        }
    }

}
