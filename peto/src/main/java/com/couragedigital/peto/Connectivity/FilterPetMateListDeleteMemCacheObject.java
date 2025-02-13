package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetMateList;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class FilterPetMateListDeleteMemCacheObject {

    public static String url = URLInstance.getUrl();
    private static Context context;

    public FilterPetMateListDeleteMemCacheObject(PetMateList petMateList) {
        this.context = petMateList;
    }

    public static void deletePetMateListFilterObject(String email) {

        url = url + "?method=deleteFilterPetMateListObject&format=json&email="+email;
        JsonObjectRequest filterPetMateListDeleteMemCacheObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getBoolean("deleteFilterPetListObjectResponse")== true) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(filterPetMateListDeleteMemCacheObjectRequest);
    }
}
