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
import com.couragedigital.petapp.model.PetMetList;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetMetRefreshFetchList {
    private static final String TAG = PetFetchList.class.getSimpleName();

    public static List petMetRefreshFetchList(List<PetMetList> petMetLists, RecyclerView.Adapter adapter, String url, SwipeRefreshLayout petListSwipeRefreshLayout) {
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
                                    petMetList.setPetBreed(obj.getString("pet_breed"));
                                    petMetList.setImage_path(obj.getString("image_path"));

                                    petMetList.setPetCategory(obj.getString("pet_category"));
                                    petMetList.setPetAge(obj.getString("pet_age"));
                                    petMetList.setPetGender(obj.getString("pet_gender"));
                                    petMetList.setPetDescription(obj.getString("pet_description"));
                                    petMetList.setPetPostDate(obj.getString("post_date"));

                                    // adding pet to pets array
                                    petMetLists.add(0, petMetList);
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
        return petMetLists;
    }
}
