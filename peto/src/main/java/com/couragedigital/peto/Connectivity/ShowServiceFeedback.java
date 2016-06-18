package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Adapter.ServiceListAdapter;
import com.couragedigital.peto.DialogBox.EmptyListDialoge;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetClinicDetails;
import com.couragedigital.peto.TabFragmentGroomerDetails;
import com.couragedigital.peto.TabFragmentShelterDetails;
import com.couragedigital.peto.TabFragmentTrainerDetails;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ClinicReviewsListItems;
import com.couragedigital.peto.model.ReviewsListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShowServiceFeedback {
    private static Context context;

    public ShowServiceFeedback(TabFragmentTrainerDetails petServiceTrainerDetails) {
        context = petServiceTrainerDetails;
    }
    public ShowServiceFeedback(TabFragmentGroomerDetails petServicGroomereDetails) {
        context = petServicGroomereDetails;
    }
    public ShowServiceFeedback(TabFragmentShelterDetails petServiceShelterDetails) {
        context = petServiceShelterDetails;
    }

    public static void showServiceReviews(final List<ReviewsListItems> reviewsListItemsArrayList, final RecyclerView.Adapter reviewAdapter, String url, String ServiceType) {

        JsonObjectRequest showServiceReviewsListRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getJSONArray("showPetServiceReviewsResponse").equals(JSONObject.NULL)) {
                                JSONArray jsonArray = response.getJSONArray("showPetServiceReviewsResponse");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        ReviewsListItems reviewsListItems = new ReviewsListItems();
                                        if((obj.getString("emptyKey").equals("Empty"))){
                                            reviewsListItems.setEmptyKey(obj.getString("emptyKey"));
                                            reviewsListItemsArrayList.add(reviewsListItems);
                                        }else{
                                            reviewsListItems.setEmail(obj.getString("name"));
                                            reviewsListItems.setRatings(obj.getString("ratings"));
                                            reviewsListItems.setReviews(obj.getString("reviews"));
                                            reviewsListItems.setEmptyKey(obj.getString("emptyKey"));
                                            reviewsListItemsArrayList.add(reviewsListItems);
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
        AppController.getInstance().addToRequestQueue(showServiceReviewsListRequest);
    }
}
