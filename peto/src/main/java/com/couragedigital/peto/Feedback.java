package com.couragedigital.peto;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.couragedigital.peto.Connectivity.FeedbackFormUpload;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.util.HashMap;

public class Feedback extends BaseActivity implements View.OnClickListener {

    EditText feedbackEmail;
    EditText feedbackOfUser;
    Button feedbackSubmit;

    String emailOfUserFeedback = "";
    String userFeedback = "";
    private long TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        feedbackEmail = (EditText) findViewById(R.id.feedbackEmail);
        feedbackOfUser = (EditText) findViewById(R.id.feedbackOfUser);
        feedbackSubmit = (Button) findViewById(R.id.feedbackSubmit);

        HashMap<String, String> user = sessionManager.getUserDetails();
        emailOfUserFeedback = user.get(SessionManager.KEY_EMAIL);
        feedbackEmail.setText(emailOfUserFeedback);

        feedbackSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        v.setEnabled(false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, TIME);

        if(v.getId() == R.id.feedbackSubmit) {
            userFeedback = feedbackOfUser.getText().toString();
            if(emailOfUserFeedback.trim().equals("")) {
                feedbackEmail.setError("Please insert Email!");
            }
            else if(userFeedback.trim().equals("")) {
                feedbackOfUser.setError("Please give Feedback!");
            }
            else {
                emailOfUserFeedback = feedbackEmail.getText().toString();
                new UploadFeedbackToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }
    }

    public class UploadFeedbackToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FeedbackFormUpload.uploadToRemoteServer(emailOfUserFeedback, userFeedback, Feedback.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Feedback.this.getPackageManager();
        ComponentName component = new ComponentName(Feedback.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Feedback.this.getPackageManager();
        ComponentName component = new ComponentName(Feedback.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}