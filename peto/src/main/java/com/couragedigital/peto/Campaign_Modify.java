package com.couragedigital.peto;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.couragedigital.peto.Connectivity.ModifyCampaignDetails;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

public class Campaign_Modify extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private ProgressDialog progressDialog = null;
    EditText modifyngoName;
    EditText modifycampaignName;
    EditText modifycampaignDescription;
    EditText modifycampaignActualAmount;
    CheckBox modifycampaignCHKMinimumAmount;
    EditText modifycampaignMinimumAmount;
    TextView modifycampaignLastDate;
    Button modifyCampaignButton;

    String campaignId = "";
    String nameOfNgo = "";
    String descriptionOfCampaign = "";
    String actualAmounOfCampaign;
    String minimumAmounOfCampaign="";
    String emailofCampaignOwner;
    String lastDateOfCampaign="";
    String postDateOfCampaign="";
    String campaignMinimumAmountText="";
    String nameOfCampaign="";

    private int mYear, mMonth, mDay;


    String email;

    private long TIME = 5000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_modify);

        modifyngoName = (EditText) this.findViewById(R.id.modifytxtNGOName);
        modifycampaignName = (EditText) this.findViewById(R.id.modifytxtCampaignName);
        modifycampaignDescription = (EditText) this.findViewById(R.id.modifytxtDescription);
        modifycampaignActualAmount = (EditText) this.findViewById(R.id.modifytxtCampaignActualAmount);
        modifycampaignMinimumAmount = (EditText) findViewById(R.id.modifytxtMinmumAmount);
        modifycampaignLastDate = (TextView) findViewById(R.id.modifytxtCampaignLastDate);
        modifyCampaignButton = (Button) this.findViewById(R.id.modifyCampaignbtn);
        modifycampaignCHKMinimumAmount = (CheckBox) this.findViewById(R.id.modifychkMinmumAmount);

        modifyCampaignButton.setOnClickListener(this);
        modifycampaignLastDate.setOnClickListener(this);
        modifycampaignMinimumAmount.addTextChangedListener(campaignMinimumAmountChangeListener);
        modifycampaignMinimumAmount.setVisibility(View.GONE);
        modifycampaignCHKMinimumAmount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //CheckBox is checked
                    modifycampaignMinimumAmount.setVisibility(View.VISIBLE);
                } else {
                    //CheckBox is unchecked
                    campaignMinimumAmountText = "0";
                    modifycampaignMinimumAmount.setVisibility(View.GONE);
                }
            }
        });

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        getData();
        fillData();
    }
    private void getData() {

        Intent intent = getIntent();
        if (null != intent) {
            campaignId = intent.getStringExtra("CAMPAIGN_ID");
            nameOfCampaign = intent.getStringExtra("CAMPAIGN_NAME");
            nameOfNgo = intent.getStringExtra("CAMPAIGN_NGO_NAME");
            descriptionOfCampaign = intent.getStringExtra("CAMPAIGN_DESCRIPTION");
            actualAmounOfCampaign = intent.getStringExtra("CAMPAIGN_ACTUAL_AMOUNT");
            minimumAmounOfCampaign = intent.getStringExtra("CAMPAIGN_MINIMUM_AMOUNT");
            emailofCampaignOwner = intent.getStringExtra("CAMPAIGN_OWNER_EMAIL");
            lastDateOfCampaign = intent.getStringExtra("CAMPAIGN_LAST_DATE");
            postDateOfCampaign = intent.getStringExtra("CAMPAIGN_POST_DATE");
            postDateOfCampaign = intent.getStringExtra("CAMPAIGN_POST_DATE");
        }
    }
    private void fillData() {
        modifyngoName.setText(nameOfNgo);
        modifycampaignName.setText(nameOfCampaign);
        modifycampaignDescription.setText(descriptionOfCampaign);
        modifycampaignActualAmount.setText(actualAmounOfCampaign);
        modifycampaignMinimumAmount.setText(minimumAmounOfCampaign);
        modifycampaignLastDate.setText(lastDateOfCampaign);
    }
    private TextWatcher campaignMinimumAmountChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            new GetCampaignMinimumAmount().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };
    public class GetCampaignMinimumAmount extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        campaignMinimumAmountText = modifycampaignMinimumAmount.getText().toString();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
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
        if (v.getId() == R.id.modifyCampaignbtn) {

            nameOfNgo  = modifyngoName.getText().toString();
            nameOfCampaign  = modifycampaignName.getText().toString();
            descriptionOfCampaign  = modifycampaignDescription.getText().toString();
            actualAmounOfCampaign  = modifycampaignActualAmount.getText().toString();
            minimumAmounOfCampaign  = modifycampaignMinimumAmount.getText().toString();
            lastDateOfCampaign  = modifycampaignLastDate.getText().toString();

            progressDialog = ProgressDialog.show(Campaign_Modify.this, "", "Uploading file...", true);
            new UploadModifiedCampaignDataToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
        if (v == modifycampaignLastDate) {
            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in textbox
                            modifycampaignLastDate.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }

    }
    public class UploadModifiedCampaignDataToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                ModifyCampaignDetails.updateCampaignDetails(nameOfNgo, nameOfCampaign, descriptionOfCampaign, actualAmounOfCampaign, minimumAmounOfCampaign, lastDateOfCampaign, email, campaignId, Campaign_Modify.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Campaign_Modify.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Campaign_Modify.this.getPackageManager();
        ComponentName component = new ComponentName(Campaign_Modify.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Campaign_Modify.this.getPackageManager();
        ComponentName component = new ComponentName(Campaign_Modify.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }

}
