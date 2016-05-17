package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Campaign_List;
import com.couragedigital.peto.Campaign_Modify;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.EditProfile;
import com.couragedigital.peto.Index;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ModifyCampaignDetails{
    private static Context context = null;
    private static String ngoName;
    private static String campaignName;
    private static String description;
    private static String actualAmount;
    private static String minimumAmount;
    private static String lastDate;
    private static String email;
    private static String campaignId;
    private static String method;
    private static String format;
    private static String modifyCampaignResponse;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String updateCampaignDetails (String nameOfNgo, String nameOfCampaign, String descriptionOfCampaign, String actualAmounOfCampaign, String minimumAmounOfCampaign, String lastDateOfCampaign, String ngoEmail,String ngoCampaignId,final Campaign_Modify campaign_Modify) throws Exception {
        method = "ModifyCampaign";
        format = "json";
        ngoName = nameOfNgo;
        campaignName = nameOfCampaign;
        description = descriptionOfCampaign;
        actualAmount = actualAmounOfCampaign;
        minimumAmount = minimumAmounOfCampaign;
        lastDate = lastDateOfCampaign;
        email=ngoEmail;
        campaignId = ngoCampaignId;
        context = campaign_Modify;
        final String URL = URLInstance.getUrl();

        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("ngoName", ngoName);
            params.put("campaignName", campaignName);
            params.put("description", description);
            params.put("actualAmount", actualAmount);
            params.put("minimumAmount", minimumAmount);
            params.put("lastDate", lastDate);
            params.put("email", email);
            params.put("campaignId", campaignId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest saveCampaignModify = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        Campaign_Modify campaign_Modify = new Campaign_Modify();
                        try {
                            returnResponse(response.getString("saveModifiedCampaignDetailsResponse"));
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
        });
        saveCampaignModify.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(saveCampaignModify);
        return modifyCampaignResponse;
    }

    public ModifyCampaignDetails(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("CAMPAIGN_DETAILS_UPDATED")) {
            Toast.makeText(context, "Details Successfully Updataed.", Toast.LENGTH_SHORT).show();
            Intent gotoCamoaignList = new Intent(context, Campaign_List.class);
            context.startActivity(gotoCamoaignList);
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Intent gotoCampaignModify = new Intent(context, Campaign_Modify.class);
            context.startActivity(gotoCampaignModify);
        }
    }
}
