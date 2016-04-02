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
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class WishListPetMateListDelete {
    private static String url;
    private static String URL = URLInstance.getUrl();
    private static Context context;

    public WishListPetMateListDelete(View v) {
        context = v.getContext();
    }

    public static void deleteWishListPetMateFromServer(String useremail, String petMateListId) throws Exception {

        url = URL + "?method=deleteWishListPetMateList&format=json&id=" + petMateListId + "&email=" + useremail + "";
        JsonObjectRequest wishListPetMateDeleteRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseOfServer = response.getString("deleteWishListPetMateListResponse");
                            if(responseOfServer.equals("WishList_PetMate_Deleted")) {
                              //  Toast.makeText(v.getContext(), "This post has been deleted!", Toast.LENGTH_LONG).show();
                            }
                            else {
                               // Toast.makeText(v.getContext(), "This post has not been deleted!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(wishListPetMateDeleteRequest);
    }

}
