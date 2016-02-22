package com.couragedigital.petapp.Connectivity;


import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.PetClinicDetails;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.ClinicReviewsListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShowClinicFeedback {

    public static List showClinicReviews(List<ClinicReviewsListItems> clinicReviewsList, RecyclerView.Adapter adapter, String url) {
        JsonObjectRequest clinicReviewsListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showClinicReviewssResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ClinicReviewsListItems clinicReviewsListItems = new ClinicReviewsListItems();
                                    //clinicReviewsListItems.setClinicId(obj.getString("clinic_id"));
                                    clinicReviewsListItems.setClinicRatings(obj.getString("ratings"));
                                    clinicReviewsListItems.setClinicReviews(obj.getString("reviews"));
                                    clinicReviewsListItems.setEmail(obj.getString("name"));

                                    // adding R to pets array
                                    clinicReviewsList.add(clinicReviewsListItems);
                                    adapter.notifyDataSetChanged();
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //recyclerView.setAdapter(adapter);
                            /*petClinicDetails.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                }
                            });*/
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // progressDialog.hide();
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                //progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(clinicReviewsListReq);
        return clinicReviewsList;
    }
}
