package com.couragedigital.peto;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.couragedigital.peto.Connectivity.Donate_For_Ngo;
import com.couragedigital.peto.Connectivity.SendOrderConfirmationEmail;
import com.razorpay.Checkout;
import org.json.JSONObject;

public class NGO_Donation  extends AppCompatActivity
{
    String campaignId;
    String campaignName;
    String ngoName;
    String amount;
    String donarEmail;
    String contactNo="";
    String ngoEmail="";


    int totalPrice;

    TextView donationId;
    TextView nameOfCampaign;
    TextView nameOfNgo;

    TextView donationAmount;
    public NGO_Donation(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ngo_donation);

        Intent intent = getIntent();
        if (null != intent) {
            campaignId = intent.getStringExtra("campaignId");
            campaignName = intent.getStringExtra("campaignName");
            ngoName = intent.getStringExtra("ngoName");
            amount = intent.getStringExtra("amount");
            donarEmail = intent.getStringExtra("donarEmail");
            contactNo= intent.getStringExtra("contactNo");
            ngoEmail= intent.getStringExtra("emailofNgoOwner");

        }

        donationId= (TextView) findViewById(R.id.donationId);
        nameOfCampaign = (TextView) findViewById(R.id.otpValue);
        nameOfNgo = (TextView) findViewById(R.id.sender);
        donationAmount = (TextView) findViewById(R.id.totalPrice);

        String ngoname =  "<b>NGO: </b>"+ngoName;
        nameOfNgo.setText(Html.fromHtml(ngoname));

        String campaignname =  "<b>Campaign: </b>"+campaignName;
        nameOfCampaign.setText(Html.fromHtml(campaignname));

        String donatonamount =  "<b>Donation Amount: </b>"+amount;
        donationAmount.setText(Html.fromHtml(donatonamount));

        totalPrice= Integer.parseInt(amount);
        // payment button created by you in xml layout
        View button = (View) findViewById(R.id.pay_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                startPayment();
            };
        });
    }

    public void startPayment(){
        /**
         * Replace with your public key
         */

       // final String public_key = "rzp_test_OAmWaTCnP0PB50";

         final String public_key = "rzp_live_q18pNB7FUtMPxp";
        /**
         * You need to pass current activity in order to let razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setPublicKey(public_key);

//    int PetoImage;
//    PetoImage = android.R.drawable.ic_launcher.png;

        try{
            JSONObject options = new JSONObject("{" +
                   // "description: 'Demoing Charges'," +
                    //"image: 'https://rzp-mobile.s3.amazonaws.com/images/rzp.png'," +
                    "image: 'http://storage.couragedigital.com/prod/ic_launcher.png'," +
                    //"image: '"+PetoImage+"'," +
                    "currency: 'INR'}"
            );


            options.put("amount", totalPrice*100);
            options.put("name", "Peto");
            options.put("prefill", new JSONObject("{email: '"+donarEmail+"', contact: '"+contactNo+"'}"));

            co.open(activity, options);

        } catch(Exception e){
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     *   onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentSuccess(String razorpayPaymentID){
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            //SendOrderConfirmationEmail
            try {
                Donate_For_Ngo donate_For_NGO = new Donate_For_Ngo(NGO_Donation.this);
                donate_For_NGO.CollectDonationInfo(campaignId,donarEmail,amount,ngoEmail);
                Intent gotoListPage = new Intent(this,Campaign_List_ForAll.class);
                startActivity(gotoListPage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (Exception e){
            Log.e("com.merchant", e.getMessage(), e);
        }
    }

    /**
     * The name of the function has to be
     *   onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    public void onPaymentError(int code, String response){
        try {
            Toast.makeText(this, "Payment failed: " + Integer.toString(code) + " " + response, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Log.e("com.merchant", e.getMessage(), e);
        }
    }
}