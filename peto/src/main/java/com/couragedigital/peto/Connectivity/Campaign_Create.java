package com.couragedigital.peto.Connectivity;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.couragedigital.peto.Campaign_Form;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Campaign_Create {
    private static final String SERVER_URL = URLInstance.getUrl();
    private static Context context;
    private static Map<String, String> headerPart;
    private static Map<String, File> filePartData;
    private static Map<String, String> stringPart;
    public static Campaign_Form campaignFormActivity;

    public static void uploadCampaignDetails(String campaignNameText, String campaignDescriptionText, String campaignActualAmountText, String campaignMinimumAmountText, String campaignLastDateText, String firstImagePath, String secondImagePath, String thirdImagePath, String emailOfNGO, Campaign_Form campaign_form) throws Exception {
        campaignFormActivity = campaign_form;
        context = campaign_form.getApplicationContext();
        int serverResponseCode = 0;

        String campaignName = campaignNameText;
        String description= campaignDescriptionText;
        String actualAmount= campaignActualAmountText;
        String minimumAmount = campaignMinimumAmountText;
        String lastDate = campaignLastDateText;
        String firstCampaignImage = firstImagePath;
        String secondCampaignImage = secondImagePath;
        String thirdCampaignImage = thirdImagePath;
        String email = emailOfNGO;
        String method = "CreateCampaign";
        String format = "json";

        //Auth header
        headerPart = new HashMap<>();
        headerPart.put("Content-type", "multipart/form-data;");

        //File part
        filePartData = new HashMap<>();
        filePartData.put("firstCampaignImage", new File(firstCampaignImage));
        if(!secondCampaignImage.isEmpty() && secondCampaignImage != null) {
            filePartData.put("secondCampaignImage", new File(secondCampaignImage));
        }
        if(!thirdCampaignImage.isEmpty() && thirdCampaignImage != null) {
            filePartData.put("thirdCampaignImage", new File(thirdCampaignImage));
        }

        //String part
        stringPart = new HashMap<>();
        stringPart.put("campaignName", campaignName);
        stringPart.put("description", description);
        stringPart.put("actualAmount", actualAmount);
        stringPart.put("minimumAmount", minimumAmount);
        stringPart.put("lastDate", lastDate);
        stringPart.put("email", email);
        stringPart.put("method", method);
        stringPart.put("format", format);
        new UploadToServerCustomRequest().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static class UploadToServerCustomRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            CustomMultipartRequest campaignFormUploadCustomRequest = new CustomMultipartRequest(Request.Method.POST, context, SERVER_URL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Toast.makeText(context, "Campaign Succefully Created", Toast.LENGTH_LONG).show();
                    campaignFormActivity.finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "Error to Creating Campaign", Toast.LENGTH_LONG).show();
                    Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                    context.startActivity(gotoTimeOutError);
                }
            }, filePartData, stringPart, headerPart);
            campaignFormUploadCustomRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(campaignFormUploadCustomRequest);
            return null;
        }
    }
}
