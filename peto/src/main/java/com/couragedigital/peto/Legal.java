package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;

public class Legal extends AppCompatActivity implements View.OnClickListener {

    TextView legalPrivacyPolicyLabel;
    TextView legalTNCLabel;
    TextView legalRefundLabel;

    TextView legalPrivacyPolicyDetails;
    TextView legalTNCDetails;
    TextView legalRefundDetails;

    RelativeLayout legalPrivacyPolicyLayout;
    RelativeLayout legalTNCLayout;
    RelativeLayout legalRefundLayout;

    int lauoyt1State =0;
    int lauoyt2State =0;
    int lauoyt3State =0;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal);

        legalPrivacyPolicyLabel= (TextView) findViewById(R.id.legalPrivacyPolicyLabel);
        legalTNCLabel= (TextView) findViewById(R.id.legalTNCLabel);
        legalRefundLabel= (TextView) findViewById(R.id.legalRefundLabel);

        legalPrivacyPolicyDetails= (TextView) findViewById(R.id.legalPrivacyPolicyDetails);
        legalTNCDetails= (TextView) findViewById(R.id.legalTNCDetails);
        legalRefundDetails= (TextView) findViewById(R.id.legalRefundDetails);

        legalPrivacyPolicyLayout = (RelativeLayout) findViewById(R.id.legalPrivacyPolicyLayout);
        legalTNCLayout = (RelativeLayout) findViewById(R.id.legalTNCLayout);
        legalRefundLayout = (RelativeLayout) findViewById(R.id.legalRefundLayout);

        legalPrivacyPolicyLabel.setOnClickListener(this);
        legalTNCLabel.setOnClickListener(this);
        legalRefundLabel.setOnClickListener(this);

        legalPrivacyPolicyLayout.setVisibility(View.GONE);
        legalTNCLayout.setVisibility(View.GONE);
        legalRefundLayout.setVisibility(View.GONE);

        String htmlForPrivacyAsString = getString(R.string.PrivacyPolicy);
        Spanned htmlForPrivacyAsSpanned = Html.fromHtml(htmlForPrivacyAsString);
        legalPrivacyPolicyDetails.setText(htmlForPrivacyAsSpanned);

        String htmlForTNCAsString1 = getString(R.string.TNC1);
//        String htmlForTNCAsString2 = getString(R.string.TNC2);
//        String htmlForTNCAsString3 = getString(R.string.TNC3);
//        String htmlForTNCAsString4 = htmlForTNCAsString1 + htmlForTNCAsString2 + htmlForTNCAsString3 ;
        Spanned htmlForTNCAsSpanned = Html.fromHtml(htmlForTNCAsString1);
        legalTNCDetails.setText(htmlForTNCAsSpanned);

        String htmlForRefundAsString3 = getString(R.string.Refund);
        Spanned htmlForRefundAsSpanned3 = Html.fromHtml(htmlForRefundAsString3);
        legalRefundDetails.setText(htmlForRefundAsSpanned3);

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.legalPrivacyPolicyLabel) {
            //legalPrivacyPolicyLayout.setVisibility(View.VISIBLE);
            if(lauoyt1State==0){
                legalPrivacyPolicyLayout.setVisibility(View.GONE);
                legalPrivacyPolicyLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.for_show, 0);
                lauoyt1State=1;
            }else{
                legalPrivacyPolicyLayout.setVisibility(View.VISIBLE);
                legalPrivacyPolicyLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.for_hide, 0);
                lauoyt1State=0;
            }
        }
        else if(v.getId() == R.id.legalTNCLabel) {
           // legalTNCLayout.setVisibility(View.VISIBLE);
            if(lauoyt2State==0){
                legalTNCLayout.setVisibility(View.GONE);
                legalTNCLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.for_show, 0);
                lauoyt2State=1;
            }else{
                legalTNCLayout.setVisibility(View.VISIBLE);
                legalTNCLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.for_hide, 0);
                lauoyt2State=0;
            }

        }
        else if(v.getId() == R.id.legalRefundLabel) {
            //legalRefundLayout.setVisibility(View.VISIBLE);
            if(lauoyt3State==0){
                legalRefundLayout.setVisibility(View.GONE);
                legalRefundLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.for_show, 0);
                lauoyt3State=1;
            }else{
                legalRefundLayout.setVisibility(View.VISIBLE);
                legalRefundLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.for_hide, 0);
                lauoyt3State=0;
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Legal.this.getPackageManager();
        ComponentName component = new ComponentName(Legal.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Legal.this.getPackageManager();
        ComponentName component = new ComponentName(Legal.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }


}


