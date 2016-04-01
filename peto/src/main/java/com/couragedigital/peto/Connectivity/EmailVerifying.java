package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.EmailForCode;
import com.couragedigital.peto.SetNewPassword;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailVerifying {

    private static Context context = null;
    private static String emailtochangepass;
    private static String method;
    private static String format;
    private static String changepasswordresponse;

    public static String SendEmailForPassword(String email) throws Exception {
        String method = "checkemail";
        String format = "json";
        emailtochangepass = email;

        final String url = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("email", emailtochangepass);

        } catch (Exception e) {

        }
        JsonObjectRequest signinReq = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response: %n %s", response.toString());
                        try {
                            returnResponse(response.getString("checkemailResponse"));
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
        AppController.getInstance().addToRequestQueue(signinReq);
        return changepasswordresponse;
    }

    public EmailVerifying(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("RANDOM_NO_SUCCESSFULLY_UPDATED")) {
            Intent gotonewpassword = new Intent(context, SetNewPassword.class);
            //gotonewpassword.putExtra("EMAIL", emailtochangepass);
            context.startActivity(gotonewpassword);
            Toast.makeText(context, "Please Check Your Email.", Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, "Activation Code sent on your Email..", Toast.LENGTH_SHORT).show();
        } else if (response.equals("INVALID_EMAIL")) {
            Intent gotologinpage = new Intent(context, EmailForCode.class);
            context.startActivity(gotologinpage);
            Toast.makeText(context, "Enter Registered Email.", Toast.LENGTH_SHORT).show();
        }
    }
}
