package com.couragedigital.peto.Connectivity;


import android.content.Context;
import android.content.Intent;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Adapter.ModifySpinnerItemsAdapter;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.MyListingModifyPetDetails;
import com.couragedigital.peto.MyListingModifyPetMateDetails;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ModifyPetCategorySpinnerList {
    private static final String TAG = ModifyPetCategorySpinnerList.class.getSimpleName();
    private static Context context;
    private static final String url = URLInstance.getUrl()+"?method=showPetCategories&format=json";

    public ModifyPetCategorySpinnerList(MyListingModifyPetDetails myListingModifyPetDetails) {
        this.context = myListingModifyPetDetails;
    }

    public ModifyPetCategorySpinnerList(MyListingModifyPetMateDetails myListingModifyPetMateDetails) {
        this.context = myListingModifyPetMateDetails;
    }

    public static void fetchPetCategory(final List petCategoryList, final ModifySpinnerItemsAdapter adapter) {
        JsonObjectRequest modifyPetCategoryRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetCategoriesResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        petCategoryList.add(obj.optString("pet_category"));
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
        AppController.getInstance().addToRequestQueue(modifyPetCategoryRequest);
    }
}
