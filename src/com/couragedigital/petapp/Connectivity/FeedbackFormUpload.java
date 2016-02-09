package com.couragedigital.petapp.Connectivity;

import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.Feedback;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class FeedbackFormUpload {
    private static String email;
    private static String feedbackOfUser;
    private static String method;
    private static String format;

    public static void uploadToRemoteServer(String emailOfUserFeedback, String userFeedback, Feedback feedback) throws Exception {
        method = "userFeedback";
        format = "json";
        email = emailOfUserFeedback;
        feedbackOfUser = userFeedback;
        final String URL = "http://storage.couragedigital.com/dev/api/petappapi.php";
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
                                        Toast.makeText(feedback, "Feedback not saved. Please try again!", Toast.LENGTH_LONG).show();
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
            }
        });
        AppController.getInstance().addToRequestQueue(req);
    }
}
