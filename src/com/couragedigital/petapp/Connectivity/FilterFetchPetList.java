package com.couragedigital.petapp.Connectivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Adapter.FilterBreedAdapter;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.FilterBreedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class FilterFetchPetList {
    private static final String TAG = PetBreedsSpinnerList.class.getSimpleName();
    private static List<String> filterSelectedCategoryLists;
    private static String method;
    private static String format;

    //http://storage.couragedigital.com/dev/api/petappapi.php
    public static void fetchPetBreeds(List<String> filterCategoryLists, List<FilterBreedList> filterBreedLists, FilterBreedAdapter filterBreedAdapter) {
        String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
        filterSelectedCategoryLists = filterCategoryLists;
        method = "filterCategoryWiseBreed";
        format = "json";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", method);
        params.put("format", format);
        JSONArray arrayOfSelectedFilterCategories = new JSONArray(filterSelectedCategoryLists);
        params.put("filterSelectedCategories", arrayOfSelectedFilterCategories.toString());

        JsonObjectRequest petFilterCategoryReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("filterBreedsCategoryWise");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    FilterBreedList filterBreedList = new FilterBreedList();
                                    filterBreedList.setBreedText(obj.getString("pet_breed"));

                                    // adding pet to pets array
                                    filterBreedLists.add(filterBreedList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            filterBreedAdapter.notifyDataSetChanged();
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
        AppController.getInstance().addToRequestQueue(petFilterCategoryReq);
    }
}
