package com.couragedigital.peto.Connectivity;


import android.content.Intent;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.EditProfile;
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
    private static String detailsResponse;
    private static final String TAG = PetFetchList.class.getSimpleName();

    public static String fetchUserDeatils(String oldEmail, View view) {

        //String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
        String url = URLInstance.getUrl();
        method = "fetchUserDetails";
        format = "json";
        userOldEmail=oldEmail;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", method);
        params.put("format", format);
        params.put("oldEmail", userOldEmail);

        JsonObjectRequest petFilterCategoryReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showUserDetails");
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

                                    Intent gotoEditProfile = new Intent(view.getContext(), EditProfile.class);
                                    view.getContext().startActivity(gotoEditProfile);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petFilterCategoryReq);
        return detailsResponse;
    }
}
