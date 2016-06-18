package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.ClinicRateNReview;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetClinicDetails;
import com.couragedigital.peto.ServiceRateNReview;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.TabFragmentTrainerDetails;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ClinicReviewsListItems;
import com.couragedigital.peto.model.ReviewsListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class SaveServiceFeedback {
    private static Context context = null;
    private static String serviceRatings;
    private static String serviceFeedback;
    private static String selectedId;
    private static String serviceType;
    private static String userEmail;
    private static String method;
    private static String format;

    private static String serviceFeedbackResponse;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String saveFeedbackOnServer(String ratingValue,String feedback,String serviceListId,String email,String servicetype){
        method = "submitPetServiceFeedback";
        format = "json";
        serviceRatings = ratingValue;
        serviceFeedback = feedback;
        selectedId = serviceListId;
        userEmail = email;
        serviceType = servicetype;
        final String URL = URLInstance.getUrl();

        JSONObject params=new JSONObject();
        try{
            params.put("method",method);
            params.put("format",format);
            params.put("ratings",serviceRatings);
            params.put("feedback",serviceFeedback);
            params.put("email",email);
            params.put("serviceType",serviceType);
            params.put("serviceListId",selectedId);
        }catch(Exception e){

        }

        JsonObjectRequest saveServiceFeedbackRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        ServiceRateNReview clinicRateNReview = new ServiceRateNReview();
                        try {
                            returnResponse(response.getString("savePetServiceFeedbackResponse"));
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
        saveServiceFeedbackRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(saveServiceFeedbackRequest);

        return serviceFeedbackResponse;
    }
    public SaveServiceFeedback(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("PET_SERVICE_FEEDBACK_SAVED")) {
            Toast.makeText(context, "Successfully Saved.", Toast.LENGTH_SHORT).show();
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Intent gotoRateNReview = new Intent(context, ServiceRateNReview.class);
            context.startActivity(gotoRateNReview);
        }
    }

}
