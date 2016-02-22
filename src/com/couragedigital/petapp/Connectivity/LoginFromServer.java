package com.couragedigital.petapp.Connectivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.service.voice.VoiceInteractionSession;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.BaseActivity;
import com.couragedigital.petapp.Index;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.SignIn;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Stack;

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
