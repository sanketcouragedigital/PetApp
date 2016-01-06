package com.couragedigital.petapp.Connectivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Adapter.FilterBreedPetMateAdapter;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.FilterBreedList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class FilterFetchPetMateBreedList {
    private static final String TAG = PetBreedsSpinnerList.class.getSimpleName();
    private static List<String> filterSelectedCategoryPetMateLists;
    private static String method;
    private static String format;


    //http://storage.couragedigital.com/dev/api/petappapi.php
    public static void fetchPetMateBreeds(List<String> filterCategoryPetMateLists, List<FilterBreedList> filterBreedPetMateLists, FilterBreedPetMateAdapter filterBreedPetMateAdapter) {
        String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
        filterSelectedCategoryPetMateLists = filterCategoryPetMateLists;
        method = "filterCategoryWiseBreed";
        format = "json";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", method);
        params.put("format", format);
        JSONArray arrayOfSelectedFilterCategories = new JSONArray(filterSelectedCategoryPetMateLists);
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
                                    filterBreedPetMateLists.add(filterBreedList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            filterBreedPetMateAdapter.notifyDataSetChanged();
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
