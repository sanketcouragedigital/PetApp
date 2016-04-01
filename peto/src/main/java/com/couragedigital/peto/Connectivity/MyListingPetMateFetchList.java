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
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.MyListingPetMateListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyListingPetMateFetchList {
    private static final String TAG = MyListingPetMateFetchList.class.getSimpleName();
    private static Context context;

    public MyListingPetMateFetchList(View v) {
        context = v.getContext();
    }

    public static List myListingPetMateFetchList(final List<MyListingPetMateListItem> petMateLists, final RecyclerView.Adapter adapter, String url) {
        JsonObjectRequest petMateListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showMyListingPetMateListResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        MyListingPetMateListItem myListingPetMateListItems = new MyListingPetMateListItem();
                                        myListingPetMateListItems.setId(obj.getInt("id"));
                                        myListingPetMateListItems.setPetMateBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                        myListingPetMateListItems.setFirstImagePath(obj.getString("first_image_path"));
                                        if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                            myListingPetMateListItems.setSecondImagePath(obj.getString("second_image_path"));
                                        }
                                        if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                            myListingPetMateListItems.setThirdImagePath(obj.getString("third_image_path"));
                                        }
                                        myListingPetMateListItems.setPetMateCategory(replaceSpecialChars(obj.getString("pet_category")));
                                        myListingPetMateListItems.setPetMateAgeInMonth(obj.getString("pet_age_inMonth"));
                                        myListingPetMateListItems.setPetMateAgeInYear(obj.getString("pet_age_inYear"));
                                        myListingPetMateListItems.setPetMateGender(replaceSpecialChars(obj.getString("pet_gender")));
                                        myListingPetMateListItems.setPetMateDescription(replaceSpecialChars(obj.getString("pet_description")));
                                        myListingPetMateListItems.setPetMatePostDate(obj.getString("post_date"));
                                        myListingPetMateListItems.setPetMatePostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                        // adding pet to pets array
                                        petMateLists.add(myListingPetMateListItems);
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
        AppController.getInstance().addToRequestQueue(petMateListReq);
        return petMateLists;
    }

    private static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
