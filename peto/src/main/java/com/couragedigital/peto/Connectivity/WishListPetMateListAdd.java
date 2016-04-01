package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetList;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONObject;

import java.util.Map;

public class WishListPetMateListAdd {

    private static Context context = null;
    private static String petMatelistId;
    private static String email;
    private static String method;
    private static String format;
    private static String addPetMateWishList;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String addPetMateListToWishList( String useremail, String petMateListId) throws Exception {
        method = "saveWishListForPetMateList";
        format = "json";
        email = useremail;
        petMatelistId = petMateListId;

        final String URL = URLInstance.getUrl();
        //final String URL = "http://192.168.0.6/PetAppAPI/api/petappapi.php";
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("userEmail", email);
            params.put("petMateListId", petMatelistId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        PetList petList = new PetList();
//                        try {
//                            returnResponse(response.getString("savePetMateWishListResponse"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        }
        );
        AppController.getInstance().addToRequestQueue(req);
        return addPetMateWishList;
    }

    public WishListPetMateListAdd(Context context) {
        this.context = context;
    }
}
