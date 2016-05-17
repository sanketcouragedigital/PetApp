package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Campaign_List;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PaymentActivity;
import com.couragedigital.peto.NGO_Donation;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Donate_For_Ngo {

    private static Context context = null;
    private static String campaignId;
    private static String donarEmail;
    private static String donationAmount;
    private static String ngoOwnerEmail;

    private static String OrderResponse;

    private static String method;
    private static String format;
    private static String oerderGenerateResponse;
    public static NGO_Donation ngo_Donation;

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;


    public String CollectDonationInfo(String campaignIdForDonation, String email, String amount,String ngoEmail) throws Exception {

        method = "saveDonation";
        format = "json";
        campaignId = campaignIdForDonation;
        donarEmail = email;
        donationAmount = amount;
        ngoOwnerEmail = ngoEmail;

        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("campaignId", campaignId);
            params.put("donarEmail", donarEmail);
            params.put("donationAmount", donationAmount);
            params.put("ngoOwnerEmail", ngoOwnerEmail);

        } catch (Exception e) {

        }
        JsonObjectRequest donationRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response: %n %s", response.toString());
                        try {
                            returnResponse(response.getString("saveDonationDetailsResponse"));
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

    public Donate_For_Ngo(Context context) {
        this.context = context;
    }

    public void returnResponse(String response) {
        if (response.equals("DONATION_DETAILS_SAVED_SUCCESSFULLY")) {
            Toast.makeText(context, "Your transaction succsessfully done.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
