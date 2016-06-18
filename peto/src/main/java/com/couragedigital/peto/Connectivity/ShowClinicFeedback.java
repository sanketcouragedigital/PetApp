package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetClinicDetails;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ClinicReviewsListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShowClinicFeedback {
    private static Context context;

    public ShowClinicFeedback(PetClinicDetails petClinicDetails) {
        context = petClinicDetails;
    }

    public static void showClinicReviews(final List<ClinicReviewsListItems> clinicReviewsListItemsArrayList, final RecyclerView.Adapter reviewAdapter, String url) {

        JsonObjectRequest showClinicReviewsListRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getJSONArray("showClinicReviewsResponse").equals(JSONObject.NULL)) {
                                JSONArray jsonArray = response.getJSONArray("showClinicReviewsResponse");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        ClinicReviewsListItems clinicReviewsListItems = new ClinicReviewsListItems();
                                        if((obj.getString("emptyKey").equals("Empty"))){
                                            clinicReviewsListItems.setEmptyKey(obj.getString("emptyKey"));
                                            clinicReviewsListItemsArrayList.add(clinicReviewsListItems);
                                        }else {
                                            clinicReviewsListItems.setEmail(obj.getString("name"));
                                            clinicReviewsListItems.setClinicRatings(obj.getString("ratings"));
                                            clinicReviewsListItems.setClinicReviews(obj.getString("reviews"));
                                            clinicReviewsListItems.setEmptyKey(obj.getString("emptyKey"));
                                            clinicReviewsListItemsArrayList.add(clinicReviewsListItems);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                reviewAdapter.notifyDataSetChanged();
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
                //progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(showClinicReviewsListRequest);
    }
}
