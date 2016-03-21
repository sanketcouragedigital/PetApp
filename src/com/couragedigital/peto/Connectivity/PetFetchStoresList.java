package com.couragedigital.peto.Connectivity;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.StoresListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchStoresList {
    private static final String TAG = PetFetchStoresList.class.getSimpleName();

    public static List petFetchStoresList(List<StoresListItem> storesList, RecyclerView.Adapter adapter, String url, ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetStoresResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    StoresListItem storesListItem = new StoresListItem();
                                    storesListItem.setStoresName(obj.getString("name"));
                                    storesListItem.setStoresAdress(obj.getString("address"));
                                    storesListItem.setContact(obj.getString("contact"));
                                    storesListItem.setEmail(obj.getString("email"));
                                    storesListItem.setStoresImage_path(obj.getString("image"));

                                    // adding pet to pets array
                                    storesList.add(storesListItem);
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
        return storesList;
    }

}
