package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.NGO_Donation;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CampaignDelete {

    private static Context context = null;

    private static String campaignId;
    private static String userEmail;
    private static String campaignName;
    private static String ngoName;
    private static String ngoEmail;
    private static String lastDate;
    private static String postDate;

    private static String mobileNo;
    private static String OrderResponse;

    private static String method;
    private static String format;
    private static String oerderGenerateResponse;
    public static NGO_Donation ngo_Donation;

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;


    public String campaignDeleteFromServer(String campaignIdtxt, String campaignNametxt,String ngoNametxt,String ngoEmailtxt,String lastDatetxt,String postDatetxt,String userEmailtxt,String mobileNotxt) throws Exception {

        method = "deleteCampaign";
        format = "json";
        campaignId = campaignIdtxt;
        campaignName = campaignNametxt;
        ngoName = ngoNametxt;
        ngoEmail = ngoEmailtxt;
        lastDate = lastDatetxt;
        postDate =  postDatetxt;
        userEmail = userEmailtxt;
        mobileNo = mobileNotxt;


        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("campaignId", campaignId);
            params.put("campaignName", campaignName);
            params.put("ngoName", ngoName);
            params.put("ngoEmail", ngoEmail);
            params.put("lastDate", lastDate);
            params.put("postDate", postDate);
            params.put("userEmail", userEmail);
            params.put("mobileNo", mobileNo);

        } catch (Exception e) {

        }
        JsonObjectRequest donationRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response: %n %s", response.toString());
                        try {
                            returnResponse(response.getString("deleteCampaignDetailsResponse"));
                            //response.getString("deleteCampaignDetailsResponse");
                                                    } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                        context.startActivity(gotoTimeOutError);
                    }
                }
        );
        donationRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(donationRequest);
        return oerderGenerateResponse;
    }

    public CampaignDelete(Context context) {
        this.context = context;
    }

    public void returnResponse(String response) {
        if (response.equals("EMAIL_SUCCESSFUULY_SENT_FOR_DELETE_CAMPAIGN")) {
            Toast.makeText(context, "Will delete after verification.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
