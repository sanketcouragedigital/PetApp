package com.couragedigital.petapp.Connectivity;


import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;
import com.couragedigital.petapp.model.PetMetList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class PetMetFetchList {
    private static final String TAG = PetMetFetchList.class.getSimpleName();

    public static List petMetFetchList(List<PetMetList> petMetLists, RecyclerView.Adapter adapter, String url, ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetMetDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetMetList petMetList = new PetMetList();
                                    petMetList.setPetMetBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    petMetList.setPetMetPostOwner(replaceSpecialChars(obj.getString("name")));
                                    petMetList.setImage_path(obj.getString("image_path"));
                                    petMetList.setPetMetCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    petMetList.setPetMetAge(obj.getString("pet_age"));
                                    petMetList.setPetMetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petMetList.setPetMetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petMetList.setPetMetPostDate(obj.getString("post_date"));
                                    petMetList.setPetMetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petMetList.setPetMetPostOwnerMobileNo(obj.getString("mobileno"));

                                    // adding pet to pets array
                                    petMetLists.add(petMetList);
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
        return petMetLists;
    }

    private static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
