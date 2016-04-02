package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Adapter.FilterCategoryAdapter;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.FilterCategoryList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class FilterFetchPetCategoryList {

    private static final String url = URLInstance.getUrl();
    private static Context context;

    public FilterFetchPetCategoryList(View v) {
        this.context = v.getContext();
    }

    public static List fetchPetCategory(final List petCategoryList, final FilterCategoryAdapter adapter) {
        String urlToFetch = url + "?method=showPetCategories&format=json";
        JsonObjectRequest filterFetchCategoryRequest = new JsonObjectRequest(Request.Method.GET, urlToFetch, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetCategoriesResponse");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {

                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        FilterCategoryList filterCategoryList = new FilterCategoryList();
                                        filterCategoryList.setCategoryText(obj.optString("pet_category"));

                                        petCategoryList.add(filterCategoryList);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);

            }
        });
        filterFetchCategoryRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(filterFetchCategoryRequest);
        return petCategoryList;
    }
}
