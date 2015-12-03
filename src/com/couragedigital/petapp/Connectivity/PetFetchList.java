package com.couragedigital.petapp.Connectivity;


import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchList {

    private static final String TAG = PetFetchList.class.getSimpleName();

    public static List petFetchList(List<PetList> petLists, RecyclerView.Adapter adapter, String url, ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetList petList = new PetList();
                                    petList.setPetBreed(obj.getString("pet_breed"));
                                    petList.setImage_path(obj.getString("image_path"));
                                    if(!obj.getString("pet_adoption").equals("")) {
                                        petList.setListingType(obj.getString("pet_adoption"));
                                    }
                                    else if(!obj.getString("pet_giveaway").equals("")) {
                                        petList.setListingType(obj.getString("pet_giveaway"));
                                    }
                                    else if(!obj.getString("pet_price").equals("")) {
                                        petList.setListingType("Rs. :- " + obj.getString("pet_price"));
                                    }
                                    petList.setPetCategory(obj.getString("pet_category"));
                                    petList.setPetAge(obj.getString("pet_age"));
                                    petList.setPetGender(obj.getString("pet_gender"));
                                    petList.setPetDescription(obj.getString("pet_description"));
                                    petList.setPetPostDate(obj.getString("post_date"));

                                    // adding pet to pets array
                                    petLists.add(petList);
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
        AppController.getInstance().addToRequestQueue(petListReq);
        return petLists;
    }

}
