package com.couragedigital.petapp.Connectivity;

import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.WishListPetListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WishListPetFetchList {
    private static final String TAG = PetFetchList.class.getSimpleName();

    public static List wishListPetFetchList(List<WishListPetListItem> wishListPet, RecyclerView.Adapter adapter, String url) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetWishListResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    WishListPetListItem wishListPetListItem = new WishListPetListItem();
                                    wishListPetListItem.setId(obj.getInt("id"));
                                    wishListPetListItem.setPetBreed(replaceSpecialChars(obj.getString("pet_breed")));
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
                                    wishListPetListItem.setPetAgeInMonth(obj.getString("pet_age_inMonth"));
                                    wishListPetListItem.setPetAgeInYear(obj.getString("pet_age_inYear"));
                                    wishListPetListItem.setPetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    wishListPetListItem.setPetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    wishListPetListItem.setPetPostDate(obj.getString("post_date"));
                                    wishListPetListItem.setPetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));

                                    // adding pet to pets array
                                    wishListPet.add(wishListPetListItem);
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
        return wishListPet;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]", " ");
        return str;
    }
}
