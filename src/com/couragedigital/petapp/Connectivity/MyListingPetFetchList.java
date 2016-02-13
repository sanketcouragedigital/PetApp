package com.couragedigital.petapp.Connectivity;


import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.MyListingPetListItems;
import com.couragedigital.petapp.model.PetListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyListingPetFetchList {

    private static final String TAG = PetFetchList.class.getSimpleName();

    public static List myListingPetFetchList(List<MyListingPetListItems> myListingPet, RecyclerView.Adapter adapter, String url) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showMyListingPetListResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    MyListingPetListItems myListingPetItems = new MyListingPetListItems();
                                    myListingPetItems.setId(obj.getInt("id"));
                                    myListingPetItems.setPetBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    myListingPetItems.setFirstImagePath(obj.getString("first_image_path"));
                                    if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                        myListingPetItems.setSecondImagePath(obj.getString("second_image_path"));
                                    }
                                    if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                        myListingPetItems.setThirdImagePath(obj.getString("third_image_path"));
                                    }
                                    if (!obj.getString("pet_adoption").equals("")) {
                                        myListingPetItems.setListingType(replaceSpecialChars(obj.getString("pet_adoption")));
                                    } else if (!obj.getString("pet_price").equals("")) {
                                        myListingPetItems.setListingType(replaceSpecialChars(obj.getString("pet_price")));
                                    }
                                    myListingPetItems.setPetCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    myListingPetItems.setPetAgeInMonth(obj.getString("pet_age_inMonth"));
                                    myListingPetItems.setPetAgeInYear(obj.getString("pet_age_inYear"));
                                    myListingPetItems.setPetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    myListingPetItems.setPetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    myListingPetItems.setPetPostDate(obj.getString("post_date"));
                                    myListingPetItems.setPetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));

                                    // adding pet to pets array
                                    myListingPet.add(myListingPetItems);
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
        return myListingPet;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]", " ");
        return str;
    }
}
