package com.couragedigital.petapp.Connectivity;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetMateListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class PetMateFetchList {
    private static final String TAG = PetMateFetchList.class.getSimpleName();

    public static List petMateFetchList(List<PetMateListItems> petMateLists, RecyclerView.Adapter adapter, String url, ProgressDialog progressDialog) {
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
                                    petMateListItems.setFirstImagePath(obj.getString("first_image_path"));
                                    if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                        petMateListItems.setSecondImagePath(obj.getString("second_image_path"));
                                    }
                                    if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                        petMateListItems.setThirdImagePath(obj.getString("third_image_path"));
                                    }
                                    petMateListItems.setPetMateCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    petMateListItems.setPetMateAge(obj.getString("pet_age"));
                                    petMateListItems.setPetMateGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petMateListItems.setPetMateDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petMateListItems.setPetMatePostDate(obj.getString("post_date"));
                                    petMateListItems.setPetMatePostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petMateListItems.setPetMatePostOwnerMobileNo(obj.getString("mobileno"));

                                    // adding pet to pets array
                                    petMateLists.add(petMateListItems);
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
        return petMateLists;
    }

    private static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
