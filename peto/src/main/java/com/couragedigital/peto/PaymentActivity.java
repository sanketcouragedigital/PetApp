package com.couragedigital.peto;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.util.Log;

import com.couragedigital.peto.Connectivity.SendOrderConfirmationEmail;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.ContactNoInstance;
import com.razorpay.Checkout;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.StringTokenizer;

public class PaymentActivity extends AppCompatActivity
{

  String orderedNo;
  String productId;
  String custName;
  String custAddress;
  String custArea;
  String custCity;
  String custPincode;
  String nameOfProduct;
  String priceOfProduct;
  String qtyOfProduct;
  String shippingChargesOfProduct="";
  String totalPriceOfProduct;
  String email;
  String contactNo="";
  String name="";
  int totalPrice;

  TextView orderid;
  TextView productName;
  TextView productPrice;
  TextView productQty;
  TextView shppingCharges;
  TextView productTotalPrice;


  public PaymentActivity(){}

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.payment);

    Intent intent = getIntent();
    if (null != intent) {
      orderedNo = intent.getStringExtra("orderNo");
      productId = intent.getStringExtra("productId");
      nameOfProduct = intent.getStringExtra("productName");
      priceOfProduct = intent.getStringExtra("productPrice");
      qtyOfProduct = intent.getStringExtra("productQty");
      shippingChargesOfProduct = intent.getStringExtra("shippingCharges");
      totalPriceOfProduct = intent.getStringExtra("totalPrice");
      custName = intent.getStringExtra("customer_name");
      email = intent.getStringExtra("email");
      contactNo = intent.getStringExtra("contactNo");
      custAddress = intent.getStringExtra("address");
      custArea = intent.getStringExtra("area");
      custCity = intent.getStringExtra("city");
      custPincode = intent.getStringExtra("pincode");
    }

    orderid= (TextView) findViewById(R.id.orderid);
    productName = (TextView) findViewById(R.id.otpValue);
    productQty = (TextView) findViewById(R.id.qty);
    productPrice = (TextView) findViewById(R.id.sender);
    shppingCharges= (TextView) findViewById(R.id.deliveryCharges);
    productTotalPrice = (TextView) findViewById(R.id.totalPrice);


   String custOrderNo =  "Order No: "+orderedNo;
   orderid.setText(Html.fromHtml(custOrderNo));

    String name =  "Product: "+nameOfProduct;
    productName.setText(Html.fromHtml(name));

    String price =  "Price: "+priceOfProduct;
    productPrice.setText(Html.fromHtml(price));

    String qty =  "Qty: "+qtyOfProduct;
    productQty.setText(Html.fromHtml(qty));

    String extraCharges =  "Shipping Charges: "+shippingChargesOfProduct;
    shppingCharges.setText(Html.fromHtml(extraCharges));

    String total =  "Total Price: "+totalPriceOfProduct;
    productTotalPrice.setText(Html.fromHtml(total));

    totalPrice= Integer.parseInt(totalPriceOfProduct);
    // payment button created by you in xml layout
    View button = (View) findViewById(R.id.pay_btn);

    button.setOnClickListener(new OnClickListener() {
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

    final String public_key = "rzp_test_OAmWaTCnP0PB50";

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
        "description: 'Demoing Charges'," +
        //"image: 'https://rzp-mobile.s3.amazonaws.com/images/rzp.png'," +
              "image: 'http://storage.couragedigital.com/prod/ic_launcher.png'," +
        //"image: '"+PetoImage+"'," +
        "currency: 'INR'}"
      );


      options.put("amount", totalPrice*100);
      options.put("name", "Peto");
      options.put("prefill", new JSONObject("{email: '"+email+"', contact: '"+contactNo+"'}"));

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
          SendOrderConfirmationEmail sendOrderConfirmationEmail = new SendOrderConfirmationEmail(PaymentActivity.this);
          sendOrderConfirmationEmail.SendConfirmationEmail(orderedNo,productId,nameOfProduct,priceOfProduct,qtyOfProduct, shippingChargesOfProduct,totalPriceOfProduct,custName, contactNo, email, custAddress, custArea,custCity,custPincode);
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
