package com.couragedigital.peto.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetClinic;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ClinicListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetFetchHomeClinicList {

    private static final String TAG = PetFetchClinicList.class.getSimpleName();
    private static Context context;

    public PetFetchHomeClinicList(PetClinic petClinic) {
        context = petClinic;
    }

    public static List petFetchHomeClinicList(final List<ClinicListItems> clinicList, final RecyclerView.Adapter adapter, String url, final ProgressDialog progressDialog) {
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showClinicDetailsResponse");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        ClinicListItems clinicListItems = new ClinicListItems();
                                        clinicListItems.setClinicId(obj.getString("clinic_id"));
                                        clinicListItems.setClinicName(obj.getString("clinic_name"));
                                        clinicListItems.setClinicAddress(obj.getString("clinic_address"));
                                        clinicListItems.setArea(obj.getString("area"));
                                        clinicListItems.setDoctorName(obj.getString("doctor_name"));
                                        clinicListItems.setContact(obj.getString("contact"));
                                        clinicListItems.setEmail(obj.getString("email"));
                                        clinicListItems.setClinicImage_path(obj.getString("clinic_image"));
                                        clinicListItems.setCity(obj.getString("city"));
                                        clinicListItems.setNotes(obj.getString("notes"));

                                        // adding pet to pets array
                                        clinicList.add(clinicListItems);
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
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
                progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(petListReq);
        return clinicList;
    }

}
