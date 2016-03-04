package com.couragedigital.petapp.Connectivity;


import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Singleton.UserPetListWishList;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetFetchList {

    private static final String TAG = PetFetchList.class.getSimpleName();
    private static ArrayList<String> wishListPetListId = new ArrayList<String>();
    private static  int checkRequestState;

    public static List petFetchList(List<PetListItems> petLists, RecyclerView.Adapter adapter, String url,int requestState,ProgressDialog progressDialog) {
        checkRequestState=requestState;
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (checkRequestState == 0) {
                                JSONArray wishListjsonArray = response.getJSONArray("showWishListResponse");

                                for (int i = 0; i < wishListjsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = wishListjsonArray.getJSONObject(i);
                                        String item = (obj.getString("listId"));
                                        wishListPetListId.add(item);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                UserPetListWishList userPetListWishList = new UserPetListWishList();
                                userPetListWishList.setPetListId(wishListPetListId);
                                checkRequestState=1;
                            }
                            JSONArray jsonArray = response.getJSONArray("showPetDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetListItems petListItems = new PetListItems();
                                    petListItems.setListId(obj.getString("id"));
                                    petListItems.setPetBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    petListItems.setPetPostOwner(replaceSpecialChars(obj.getString("name")));
                                    petListItems.setFirstImagePath(obj.getString("first_image_path"));
                                    if (!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                        petListItems.setSecondImagePath(obj.getString("second_image_path"));
                                    }
                                    if (!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                        petListItems.setThirdImagePath(obj.getString("third_image_path"));
                                    }
                                    if (!obj.getString("pet_adoption").equals("")) {
                                        petListItems.setListingType(replaceSpecialChars(obj.getString("pet_adoption")));
                                    } else if (!obj.getString("pet_price").equals("")) {
                                        petListItems.setListingType(replaceSpecialChars(obj.getString("pet_price")));
                                    }
                                    petListItems.setPetCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    //petListItems.setPetAge(obj.getString("pet_age"));
                                    petListItems.setPetAgeInMonth(obj.getString("pet_age_inMonth"));
                                    petListItems.setPetAgeInYear(obj.getString("pet_age_inYear"));
                                    petListItems.setPetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petListItems.setPetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petListItems.setPetPostDate(obj.getString("post_date"));
                                    petListItems.setPetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petListItems.setPetPostOwnerMobileNo(obj.getString("alternateNo"));

                                    // adding pet to pets array
                                    petLists.add(petListItems);
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

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
