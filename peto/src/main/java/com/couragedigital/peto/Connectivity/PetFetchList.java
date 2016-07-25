package com.couragedigital.peto.Connectivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetList;
import com.couragedigital.peto.Singleton.UserPetListWishList;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.PetListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetFetchList {

    private static final String TAG = PetFetchList.class.getSimpleName();
    private static ArrayList<String> wishListPetListId = new ArrayList<String>();
    private static  int checkRequestState;
    private static Context context;

    public PetFetchList(PetList petList) {
        context = petList;
    }

    public static List petFetchList(final List<PetListItems> petLists, final RecyclerView.Adapter adapter, String url, int requestState, final ProgressDialog progressDialog) {
        checkRequestState = requestState;
        JsonObjectRequest petFetchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(!response.getJSONArray("showWishListResponse").equals(JSONObject.NULL)) {
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
                                    userPetListWishList.setPetWishList(wishListPetListId);
                                    checkRequestState = 1;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("showPetDetailsResponse");
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
                            if(jsonArray.length() == 10){
                                PetListItems petListItems = new PetListItems();
                                petLists.add(petListItems);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
				Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(petFetchRequest);
        return petLists;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
