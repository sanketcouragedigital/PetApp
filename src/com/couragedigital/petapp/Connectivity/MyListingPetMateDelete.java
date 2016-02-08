package com.couragedigital.petapp.Connectivity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Adapter.MyListingPetMateListAdapter;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class MyListingPetMateDelete {
    private static String url;

    public static void deleteFromRemoteServer(String urlForFetch, View v) throws Exception {
        url = urlForFetch;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseOfServer = response.getString("deleteMyListingPetMateListResponse");
                            if(responseOfServer.equals("MyListing_PetMate_Deleted")) {
                                Toast.makeText(v.getContext(), "This post has been deleted!", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(v.getContext(), "This post has not been deleted!", Toast.LENGTH_LONG).show();
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
