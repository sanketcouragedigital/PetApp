package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.WishListPetListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WishListPetFetchList {
    private static final String TAG = PetFetchList.class.getSimpleName();
    private static Context context;

    public static List wishListPetFetchList(final List<WishListPetListItem> wishListPet, final RecyclerView.Adapter adapter, String url) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetWishListResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        WishListPetListItem wishListPetListItem = new WishListPetListItem();
                                        wishListPetListItem.setId(obj.getInt("id"));
                                        wishListPetListItem.setFirstImagePath(obj.getString("first_image_path"));
                                        if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                            wishListPetListItem.setSecondImagePath(obj.getString("second_image_path"));
                                        }
                                        if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                            wishListPetListItem.setThirdImagePath(obj.getString("third_image_path"));
                                        }
                                        if (!obj.getString("pet_adoption").equals("")) {
                                            wishListPetListItem.setListingType(replaceSpecialChars(obj.getString("pet_adoption")));
                                        } else if (!obj.getString("pet_price").equals("")) {
                                            wishListPetListItem.setListingType(replaceSpecialChars(obj.getString("pet_price")));
                                        }
                                        wishListPetListItem.setPetCategory(replaceSpecialChars(obj.getString("pet_category")));
                                        wishListPetListItem.setPetBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                        wishListPetListItem.setPetAgeInMonth(obj.getString("pet_age_inMonth"));
                                        wishListPetListItem.setPetAgeInYear(obj.getString("pet_age_inYear"));
                                        wishListPetListItem.setPetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                        wishListPetListItem.setPetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                        wishListPetListItem.setPetPostDate(obj.getString("post_date"));
                                        //wishListPetListItem.setPetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                        wishListPetListItem.setAlternateNo(obj.getString("alternateNo"));
                                        wishListPetListItem.setName(obj.getString("name"));
                                        // adding pet to pets array
                                        wishListPet.add(wishListPetListItem);
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
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
        return wishListPet;
    }
    public WishListPetFetchList(Context context) {
        this.context = context;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]", " ");
        return str;
    }
}
