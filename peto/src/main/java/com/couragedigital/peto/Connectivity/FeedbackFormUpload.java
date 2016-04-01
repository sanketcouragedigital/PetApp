package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.Data_NotSaved_DialogBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Feedback;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedbackFormUpload {
    private static String email;
    private static String feedbackOfUser;
    private static String method;
    private static String format;
    private static Context context;

    public static void uploadToRemoteServer(String emailOfUserFeedback, String userFeedback, final Feedback feedback) throws Exception {
        method = "userFeedback";
        format = "json";
        email = emailOfUserFeedback;
        feedbackOfUser = userFeedback;
        context = feedback;
        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("email", email);
            params.put("feedback", feedbackOfUser);
        } catch (Exception e) {

        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseOfServer = response.getString("saveFeedbackResponse");
                            if(responseOfServer.equals("USER_FEEDBACK_SAVED")) {
                                feedback.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(feedback, "Thanks For your valuable Feedback!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                feedback.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(feedback, "Feedback not saved. Please try again!", Toast.LENGTH_LONG).show();
                                        Intent gotoDataNotSavedError = new Intent(context, Data_NotSaved_DialogBox.class);
                                        context.startActivity(gotoDataNotSavedError);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }
}
