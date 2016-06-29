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
import com.couragedigital.peto.model.TrainerListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchTrainerList {
    private static final String TAG = PetFetchTrainerList.class.getSimpleName();
    private static Context context;

    public PetFetchTrainerList(View v) {
        context = v.getContext();
    }

    public static List petFetchTrainerList(final List<TrainerListItem> trainerList, final RecyclerView.Adapter adapter, String url, final ProgressDialog progressDialog) {
        JsonObjectRequest petFetchTrainerRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                JSONArray jsonArray = response.getJSONArray("showPetTrainerResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        TrainerListItem trainerListItem = new TrainerListItem();
                                        trainerListItem.setTrainer_Id(obj.getString("id"));
                                        trainerListItem.setTrainerName(obj.getString("name"));
                                        trainerListItem.setTrainerAdress(obj.getString("address"));
                                        trainerListItem.setContact(obj.getString("contact"));
                                        trainerListItem.setEmail(obj.getString("email"));
                                        trainerListItem.setTrainerImage_path(obj.getString("image"));
                                        trainerListItem.setTrainerDescription(obj.getString("description"));
                                        trainerListItem.setCity(obj.getString("city"));
                                        trainerListItem.setArea(obj.getString("area"));
                                        trainerListItem.setNotes(obj.getString("timing"));
                                        trainerListItem.setLatitude(obj.getString("latitude"));
                                        trainerListItem.setLongitude(obj.getString("longitude"));
                                        // adding pet to pets array
                                        trainerList.add(trainerListItem);
                                        adapter.notifyDataSetChanged();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
                progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(petFetchTrainerRequest);
        return trainerList;
    }

}
