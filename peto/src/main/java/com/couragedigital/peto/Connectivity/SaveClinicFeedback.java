package com.couragedigital.peto.Connectivity;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.ClinicRateNReview;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SaveClinicFeedback {
    private static Context context = null;
    private static String clinicRatings;
    private static String clinicFeedback;
    private static String selectedClinicId;
    private static String userEmail;
    private static String method;
    private static String format;

    private static String clinicFeedbackResponse;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String saveFeedbackOnServer(String ratingValue,String feedback,String clinicId,String email){
        method = "submitClinicFeedback";
        format = "json";
        clinicRatings = ratingValue;
        clinicFeedback = feedback;
        selectedClinicId = clinicId;
        userEmail = email;
        final String URL = URLInstance.getUrl();

        JSONObject params=new JSONObject();
        try{
            params.put("method",method);
            params.put("format",format);
            params.put("ratings",clinicRatings);
            params.put("feedback",clinicFeedback);
            params.put("email",email);
            params.put("clinicId",clinicId);
        }catch(Exception e){

        }

        JsonObjectRequest saveClinicFeedbackRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        ClinicRateNReview clinicRateNReview = new ClinicRateNReview();
                        try {
                            returnResponse(response.getString("saveClinicFeedbackResponse"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        }
        );
        saveClinicFeedbackRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(saveClinicFeedbackRequest);

        return clinicFeedbackResponse;
    }
    public SaveClinicFeedback(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("CLINIC_FEEDBACK_SAVED")) {
            Toast.makeText(context, "Successfully Saved.", Toast.LENGTH_SHORT).show();
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Intent gotoRateNReview = new Intent(context, ClinicRateNReview.class);
            context.startActivity(gotoRateNReview);
        }
    }

}
