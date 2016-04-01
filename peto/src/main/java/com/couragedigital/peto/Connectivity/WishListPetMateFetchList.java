package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.WishListPetMateListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WishListPetMateFetchList {
    private static final String TAG = WishListPetMateFetchList.class.getSimpleName();
    private static Context context;

    public WishListPetMateFetchList(View v) {
        context = v.getContext();
    }

    public static List wishListPetMateFetchList(final List<WishListPetMateListItem> petMateLists, final RecyclerView.Adapter adapter, String url) {
        JsonObjectRequest petMateListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetMateWishListResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {

                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        WishListPetMateListItem wishListPetMateListItem = new WishListPetMateListItem();
                                        wishListPetMateListItem.setId(obj.getInt("id"));
                                        wishListPetMateListItem.setFirstImagePath(obj.getString("first_image_path"));
                                        if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                            wishListPetMateListItem.setSecondImagePath(obj.getString("second_image_path"));
                                        }
                                        if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                            wishListPetMateListItem.setThirdImagePath(obj.getString("third_image_path"));
                                        }
                                        wishListPetMateListItem.setPetMateCategory(replaceSpecialChars(obj.getString("pet_category")));
                                        wishListPetMateListItem.setPetMateBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                        wishListPetMateListItem.setPetMateAgeInMonth(obj.getString("pet_age_inMonth"));
                                        wishListPetMateListItem.setPetMateAgeInYear(obj.getString("pet_age_inYear"));
                                        wishListPetMateListItem.setPetMateGender(replaceSpecialChars(obj.getString("pet_gender")));
                                        wishListPetMateListItem.setPetMateDescription(replaceSpecialChars(obj.getString("pet_description")));
                                        wishListPetMateListItem.setPetMatePostDate(obj.getString("post_date"));
                                       // wishListPetMateListItem.setPetMatePostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                        wishListPetMateListItem.setAlternateNo(obj.getString("alternateNo"));
                                        wishListPetMateListItem.setName(obj.getString("name"));

                                        // adding pet to pets array
                                        petMateLists.add(wishListPetMateListItem);
                                        adapter.notifyDataSetChanged();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                Intent gotoNullError = new Intent(context, NullRespone_DialogeBox.class);
                                context.startActivity(gotoNullError);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Intent gotoTimeOutError = new Intent(context, TimeOut_DialogBox.class);
                //context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(petMateListReq);
        return petMateLists;
    }

    private static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
