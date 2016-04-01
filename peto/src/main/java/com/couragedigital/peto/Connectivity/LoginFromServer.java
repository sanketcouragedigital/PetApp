package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Index;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.SignIn;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginFromServer {
    private static Context context = null;
    private static String username;
    private static String password;
    private static String method;
    private static String format;
    private static String signInResponse;
    private static LayoutInflater layoutInflater;
    private String email;



    public String CheckToRemoteServer(String email, String confirmpassword) throws Exception {

        String method = "userLogin";
        String format = "json";
        String username = email;
        String password = confirmpassword;

        this.email = email;

        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("email", username);
            params.put("confirmpassword", password);

        } catch (Exception e) {

        }
        JsonObjectRequest signinReq = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response: %n %s", response.toString());
                        try {
                            returnResponse(response.getString("loginDetailsResponse"));
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
        return signInResponse;
    }

    public LoginFromServer(Context context) {
        this.context = context;
    }

    public void returnResponse(String response) {

        SessionManager sessionManager;
        sessionManager = new SessionManager(context);
        if (response.equals("LOGIN_SUCCESS")) {
            sessionManager.createUserLoginSession("Android Example", email);
            Intent gotoindexpage = new Intent(context, Index.class);
            gotoindexpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            gotoindexpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(gotoindexpage);
            Toast.makeText(context, "Logged In.", Toast.LENGTH_SHORT).show();

        } else if (response.equals("LOGIN_FAILED")) {
            Intent gotologinpage = new Intent(context, SignIn.class);
            context.startActivity(gotologinpage);
            Toast.makeText(context, "Enter Valid Credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
