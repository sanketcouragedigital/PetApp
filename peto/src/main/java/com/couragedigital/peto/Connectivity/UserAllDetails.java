package com.couragedigital.peto.Connectivity;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Singleton.ContactNoInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserAllDetails {

    private static final String TAG = UserAllDetails.class.getSimpleName();

    private static Context context;
    private static String detailsResponse;
    private static String urlContact = URLInstance.getUrl();

    public static String fetchContactNo(String  email) {

        urlContact = urlContact+"?method=showContactNo&format=json&email="+email+"";
        JsonObjectRequest petFetchRequest = new JsonObjectRequest(Request.Method.GET, urlContact, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showContactDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ContactNoInstance contactNoInstance = new ContactNoInstance();
                                    contactNoInstance.setName(obj.getString("name"));
                                    contactNoInstance.setBuildingName(obj.getString("buildingname"));
                                    contactNoInstance.setArea(obj.getString("area"));
                                    contactNoInstance.setCity(obj.getString("city"));
                                    contactNoInstance.setMobileNo(obj.getString("mobileno"));
                                    contactNoInstance.seIs_Ngo(obj.getString("is_ngo"));
                                    contactNoInstance.seIs_Verified(obj.getString("is_verified"));

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
        AppController.getInstance().addToRequestQueue(petFetchRequest);
        return detailsResponse;
    }
}
