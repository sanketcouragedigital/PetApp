package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.*;

import java.util.HashMap;

import com.couragedigital.peto.Connectivity.SaveClinicFeedback;
import com.couragedigital.peto.SessionManager.SessionManager;

public class ClinicRateNReview extends BaseActivity {
    private RatingBar clinicRating;
    private TextView txtClinicName;
    private EditText txtFeedback;
    private Button btnSubmitFeedback;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    String clinicRatingValue;
    String clinicFeebback;
    String clinicName;
    String clinicId;
    String email;
    ProgressDialog progressDialog = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_rate_review);
    //to get user email
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);  // get email
    //to get selected clinic id
        Intent intent = getIntent();
        clinicId = intent.getExtras().getString("selectedClinicId");
        clinicName = intent.getExtras().getString("selectedClinicName");

        clinicRating = (RatingBar) findViewById(R.id.clinicRatingBar);
        txtFeedback=(EditText)findViewById(R.id.clinicFeedback);
        btnSubmitFeedback = (Button) findViewById(R.id.btnFeedbackSubmit);

        txtClinicName = (TextView) findViewById(R.id.clinicName);
        txtClinicName.setText(clinicName);

        addListenerOnRatingBar();
        addListenerOnSubmitButton();

    }
    public void addListenerOnRatingBar(){

        clinicRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = clinicRating.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    clinicRating.setRating(stars);
                    clinicRatingValue = String.valueOf(stars);

                    //Toast.makeText(ClinicRateNReview.this, String.valueOf("test"), Toast.LENGTH_SHORT).show();
                    v.setPressed(false);

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }});
    }
    public void addListenerOnSubmitButton(){
        clinicRating = (RatingBar) findViewById(R.id.clinicRatingBar);
        txtFeedback = (EditText)findViewById(R.id.clinicFeedback);
        btnSubmitFeedback = (Button)findViewById(R.id.btnFeedbackSubmit);

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ClinicRateNReview.this,
//                        String.valueOf(clinicRating.getRating()),
//                        Toast.LENGTH_SHORT).show();
                txtClinicName.setText(String.valueOf(clinicRating.getRating()));

                clinicRatingValue = String.valueOf(clinicRating.getRating());
                clinicFeebback= txtFeedback.getText().toString();
                 try{
                        SaveClinicFeedback saveClinicFeedback = new SaveClinicFeedback(ClinicRateNReview.this);
                        saveClinicFeedback.saveFeedbackOnServer(clinicRatingValue,clinicFeebback,clinicId,email);
                 }catch (Exception e){
                     e.printStackTrace();
                     progressDialog.dismiss();
                     Toast.makeText(ClinicRateNReview.this, "Exception : " + e.getMessage(),Toast.LENGTH_LONG).show();
                 }
                finish();
            }
        });
    }
}