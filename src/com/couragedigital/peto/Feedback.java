package com.couragedigital.peto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.couragedigital.peto.Connectivity.FeedbackFormUpload;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.util.HashMap;

public class Feedback extends BaseActivity implements View.OnClickListener {

    EditText feedbackEmail;
    EditText feedbackOfUser;
    Button feedbackSubmit;

    String emailOfUserFeedback = "";
    String userFeedback = "";

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
    public void onClick(View v) {
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
}