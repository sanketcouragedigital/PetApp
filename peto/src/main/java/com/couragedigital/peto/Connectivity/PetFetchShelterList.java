package com.couragedigital.peto.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ShelterListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchShelterList {
    private static final String TAG = PetFetchShelterList.class.getSimpleName();
    private static Context context;

    public PetFetchShelterList(View v) {
        context = v.getContext();
    }

    public static List petFetchShelterList(final List<ShelterListItem> shelterList, final RecyclerView.Adapter adapter, String url, final ProgressDialog progressDialog) {
        JsonObjectRequest petFetchShelterRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetShelterResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        ShelterListItem shelterListItem = new ShelterListItem();
                                        shelterListItem.setShelterName(obj.getString("name"));
                                        shelterListItem.setShelterAdress(obj.getString("address"));
                                        shelterListItem.setContact(obj.getString("contact"));
                                        shelterListItem.setEmail(obj.getString("email"));
                                        shelterListItem.setShelterImage_path(obj.getString("image"));
                                        shelterListItem.setArea(obj.getString("area"));
                                        shelterListItem.setCity(obj.getString("city"));
                                        // adding pet to pets array
                                        shelterList.add(shelterListItem);
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
                        progressDialog.hide();
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
                progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(petFetchShelterRequest);
        return shelterList;
    }

}
