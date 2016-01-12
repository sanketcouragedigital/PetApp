package com.couragedigital.petapp.Connectivity;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.model.PetMateListItems;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetMateRefreshFetchList {
    private static final String TAG = PetFetchList.class.getSimpleName();

    public static List petMateRefreshFetchList(List<PetMateListItems> petMateLists, RecyclerView.Adapter adapter, String url, SwipeRefreshLayout petListSwipeRefreshLayout) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetMateDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetMateListItems petMateListItems = new PetMateListItems();
                                    petMateListItems.setPetMateBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    petMateListItems.setPetMatePostOwner(replaceSpecialChars(obj.getString("name")));
                                    petMateListItems.setImage_path(obj.getString("image_path"));
                                    petMateListItems.setPetMateCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    petMateListItems.setPetMateAge(obj.getString("pet_age"));
                                    petMateListItems.setPetMateGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petMateListItems.setPetMateDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petMateListItems.setPetMatePostDate(obj.getString("post_date"));
                                    petMateListItems.setPetMatePostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petMateListItems.setPetMatePostOwnerMobileNo(obj.getString("mobileno"));

                                    // adding pet to pets array
                                    petMateLists.add(0, petMateListItems);
                                    adapter.notifyDataSetChanged();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data


                        if(petListSwipeRefreshLayout.isRefreshing()) {
                            petListSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(petListSwipeRefreshLayout.isRefreshing()) {
                    petListSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
        return petMateLists;
    }

    private static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
