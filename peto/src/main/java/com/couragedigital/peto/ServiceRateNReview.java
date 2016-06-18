package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.couragedigital.peto.Connectivity.SaveServiceFeedback;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.util.HashMap;

public class ServiceRateNReview extends BaseActivity {
    private RatingBar serviceRating;
    private TextView txtServiceName;
    private EditText txtFeedback;
    private Button btnSubmitFeedback;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    String serviceRatingValue;
    String serviceFeebback;
    String serviceName;
    String serviceId;
    String serviceType;
    String email;
    ProgressDialog progressDialog = null;
    private long TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_rate_review);
        //to get user email
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);  // get email
        //to get selected service id
        Intent intent = getIntent();
        serviceId = intent.getExtras().getString("selectedId");
        serviceName = intent.getExtras().getString("selectedName");
        serviceType = intent.getExtras().getString("ServiceType");

        serviceRating = (RatingBar) findViewById(R.id.serviceRatingBar);
        txtFeedback=(EditText)findViewById(R.id.serviceFeedback);
        btnSubmitFeedback = (Button) findViewById(R.id.btnFeedbackSubmit);

        txtServiceName = (TextView) findViewById(R.id.serviceName);
        txtServiceName.setText(serviceName);

        addListenerOnRatingBar();
        addListenerOnSubmitButton();

    }
    public void addListenerOnRatingBar(){

        serviceRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = serviceRating.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    serviceRating.setRating(stars);
                    serviceRatingValue = String.valueOf(stars);

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
        serviceRating = (RatingBar) findViewById(R.id.serviceRatingBar);
        txtFeedback = (EditText)findViewById(R.id.serviceFeedback);
        btnSubmitFeedback = (Button)findViewById(R.id.btnFeedbackSubmit);

        btnSubmitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                }, TIME);
                txtServiceName.setText(String.valueOf(serviceRating.getRating()));
                serviceRatingValue = String.valueOf(serviceRating.getRating());
                serviceFeebback = txtFeedback.getText().toString();
                try {
                    SaveServiceFeedback saveServiceFeedback = new SaveServiceFeedback(ServiceRateNReview.this);
                    saveServiceFeedback.saveFeedbackOnServer(serviceRatingValue, serviceFeebback, serviceId, email,serviceType);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(ServiceRateNReview.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = ServiceRateNReview.this.getPackageManager();
        ComponentName component = new ComponentName(ServiceRateNReview.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = ServiceRateNReview.this.getPackageManager();
        ComponentName component = new ComponentName(ServiceRateNReview.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
