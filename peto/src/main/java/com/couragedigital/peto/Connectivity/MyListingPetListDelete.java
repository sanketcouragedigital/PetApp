package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class MyListingPetListDelete {
    private static String url;
    private static Context context;

    public static void deleteFromRemoteServer(String urlForFetch, final View v) throws Exception {
        context = v.getContext();
        url = urlForFetch;
        JsonObjectRequest myListingPetListDeleteRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseOfServer = response.getString("deleteMyListingPetListResponse");
                            if(responseOfServer.equals("MyListing_Pet_Deleted")) {
                                Toast.makeText(context, "This post has been deleted!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(context, "This post has not been deleted!", Toast.LENGTH_LONG).show();
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
        AppController.getInstance().addToRequestQueue(myListingPetListDeleteRequest);
    }
}
