package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Adapter.SpinnerItemsAdapter;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetForm;
import com.couragedigital.peto.PetMate;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetCategorySpinnerList {

    private static final String TAG = PetCategorySpinnerList.class.getSimpleName();
    private static final String url = URLInstance.getUrl();
    private static Context context;

    public PetCategorySpinnerList(PetMate petMate) {
        context = petMate;
    }

    public PetCategorySpinnerList(PetForm petForm) {
        context = petForm;
    }

    public static void fetchPetCategory(final List petCategoryList, final SpinnerItemsAdapter adapter) {
        String urlToFetch = url + "?method=showPetCategories&format=json";
        JsonObjectRequest petCategoryReq = new JsonObjectRequest(Request.Method.GET, urlToFetch, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetCategoriesResponse");                           
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        petCategoryList.add(obj.optString("pet_category"));
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(petCategoryReq);
    }

}
