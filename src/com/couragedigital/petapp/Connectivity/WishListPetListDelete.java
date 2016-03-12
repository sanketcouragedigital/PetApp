package com.couragedigital.petapp.Connectivity;


import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.Singleton.UserPetListWishList;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WishListPetListDelete {

    private static String url;
    private static String URL = URLInstance.getUrl();
    private static ArrayList<String> wlPetListIdArray;

    // public static void deleteFromRemoteServer(String urlForFetch, View v) throws Exception {
   public static void deleteWishListPetListFromServer(String useremail, String petListId) throws Exception {

       url = URL + "?method=deleteWishListPetList&format=json&id=" + petListId + "&email=" + useremail + "";
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseOfServer = response.getString("deleteWishListPetListResponse");
                            if(responseOfServer.equals("WishList_Pet_Deleted")) {
                                //Toast.makeText(v.getContext(), "This post has been deleted!", Toast.LENGTH_LONG).show();
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
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }
}
