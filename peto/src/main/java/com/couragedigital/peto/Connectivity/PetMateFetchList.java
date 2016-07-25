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
import com.couragedigital.peto.PetMateList;
import com.couragedigital.peto.Singleton.UserPetMateListWishList;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.PetMateListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetMateFetchList {
    private static final String TAG = PetMateFetchList.class.getSimpleName();
	private static ArrayList<String> wishListPetMateListId = new ArrayList<String>();
    private static  int checkRequestState;
    private static Context context;

    public PetMateFetchList(PetMateList petMateList) {
        context = petMateList;
    }

    public static List petMateFetchList(final List<PetMateListItems> petMateLists, final RecyclerView.Adapter adapter, String url, int requestState, final ProgressDialog progressDialog) {
        checkRequestState = requestState;
        JsonObjectRequest petMateFetchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
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
                                            wishListPetMateListId.add(item);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    UserPetMateListWishList userPetMateListWishList = new UserPetMateListWishList();
                                    userPetMateListWishList.setPetMateWishList(wishListPetMateListId);
                                    checkRequestState = 1;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("showPetMateDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetMateListItems petMateListItems = new PetMateListItems();
                                    petMateListItems.setListId(replaceSpecialChars(obj.getString("id")));
                                    petMateListItems.setPetMateBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    petMateListItems.setPetMatePostOwner(replaceSpecialChars(obj.getString("name")));
                                    petMateListItems.setFirstImagePath(obj.getString("first_image_path"));
                                    if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                        petMateListItems.setSecondImagePath(obj.getString("second_image_path"));
                                    }
                                    if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                        petMateListItems.setThirdImagePath(obj.getString("third_image_path"));
                                    }
                                    petMateListItems.setPetMateCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    petMateListItems.setPetMateAgeInMonth(obj.getString("pet_age_inMonth"));
                                    petMateListItems.setPetMateAgeInYear(obj.getString("pet_age_inYear"));
                                    petMateListItems.setPetMateGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petMateListItems.setPetMateDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petMateListItems.setPetMatePostDate(obj.getString("post_date"));
                                    petMateListItems.setPetMatePostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petMateListItems.setPetMatePostOwnerMobileNo(obj.getString("alternateNo"));

                                    // adding pet to pets array
                                    petMateLists.add(petMateListItems);
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if(jsonArray.length() == 10){
                                PetMateListItems petMateListItems = new PetMateListItems();
                                petMateLists.add(petMateListItems);
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
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
                progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(petMateFetchRequest);
        return petMateLists;
    }

    private static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
