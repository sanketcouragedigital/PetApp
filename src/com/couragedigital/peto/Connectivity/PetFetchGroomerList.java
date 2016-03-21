package com.couragedigital.peto.Connectivity;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.GroomerListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchGroomerList {
    private static final String TAG = PetFetchGroomerList.class.getSimpleName();

    public static List petFetchGroomerList(List<GroomerListItem> groomerList, RecyclerView.Adapter adapter, String url, ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetGroomerResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    GroomerListItem groomerListItem = new GroomerListItem();
                                    groomerListItem.setGroomerName(obj.getString("name"));
                                    groomerListItem.setGroomerAdress(obj.getString("address"));
                                    groomerListItem.setContact(obj.getString("contact"));
                                    groomerListItem.setEmail(obj.getString("email"));
                                    groomerListItem.setGroomerImage_path(obj.getString("image"));
                                    groomerListItem.setCity(obj.getString("city"));
                                    groomerListItem.setArea(obj.getString("area"));

                                    // adding pet to pets array
                                    groomerList.add(groomerListItem);
                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
            }
        });
        /*petListReq.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        AppController.getInstance().addToRequestQueue(petListReq);
        return groomerList;
    }

}
