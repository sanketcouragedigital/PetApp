package com.couragedigital.peto;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MyOrder_List_Details extends AppCompatActivity implements View.OnClickListener {
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String nameoforderProduct = "";
    String price = "";
    String description = "";
    String orderProductQtySpinner;
    String email;
    String contactNo="";
    String name="";
    String username="";
    String buildingName="";
    String area="";
    String city="";
    String orderedDate="";
    String orderedProductQuantity="";
    String orderedProductShipping_charges="";
    String orderedProductTotal_price="";
    String orderedProductId="";
    String orderedId="";
    String orderedCustomer_email="";
    String orderedPincode="";

    TextView orderProductQty;
    TextView orderProductDetailsImageText;
    View orderProductDetailsImagesDividerLine;
    ImageView orderProductDetailsFirstImageThumbnail;
    ImageView orderProductDetailsSecondImageThumbnail;
    ImageView orderProductDetailsThirdImageThumbnail;
    ImageView orderProductImage;
    TextView orderProductName;
    TextView orderProductPrice;
    TextView orderProductDescription;
    TextView txt_buildingName;
    TextView txt_area;
    TextView txt_city;
    TextView txt_orderedDate;
    TextView txt_orderedProductQuantity;
    TextView txt_orderedProductShipping_charges;
    TextView txt_orderedProductTotal_price;
    TextView txt_orderedProductId;
    TextView txt_orderedId;
    TextView txt_orderedCustomer_email;
    TextView txt_pincode;
    View orderProductDetailsDividerLine;
    View v;
    View orderProductListDetailsDividerLine;
    View orderProductListDetailsBuyNowButtonDividerLine;
    Button orderProductBuyNowButton;

    Bitmap orderProductDetailsbitmap;
    Toolbar orderProductDetailstoolbar;
    CollapsingToolbarLayout orderProductDetailsCollapsingToolbar;
    CoordinatorLayout orderProductDetailsCoordinatorLayout;
    AppBarLayout orderProductDetailsAppBarLayout;
    NestedScrollView orderProductDetailsNestedScrollView;
    private int mutedColor;

    LinearLayout orderProductListDetailsLinearLayout;
    RelativeLayout orderProductListDetailsFirstRelativeLayout;
    RelativeLayout orderProductListDetailsSecondRelativeLayout;
    RelativeLayout shopLayoutForAddress;


    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_list_details);

        Intent intent = getIntent();
        if (null != intent) {
            firstImagePath = intent.getStringExtra("ORDER_PRODUCT_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("ORDER_PRODUCT_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("ORDER_PRODUCTt_THIRD_IMAGE");
            nameoforderProduct = intent.getStringExtra("ORDER_PRODUCT_NAME");
            price = intent.getStringExtra("ORDER_PRODUCT_PRICE");
            description = intent.getStringExtra("ORDER_PRODUCT_DESCRIPTION");
            contactNo =  intent.getStringExtra("ORDER_CONTACT_NO");
            username =  intent.getStringExtra("ORDER_NAME");
            buildingName =  intent.getStringExtra("ORDER_BUILDING_NAME");
            area =  intent.getStringExtra("ORDER_AREA");
            city =  intent.getStringExtra("ORDER_CITY");
            orderedDate=intent.getStringExtra("ORDER_POST_DATE");
            orderedProductQuantity=intent.getStringExtra("ORDER_QUANTITY");
            orderedProductShipping_charges=intent.getStringExtra("ORDER_SHIPPING_CHARGES");
            orderedProductTotal_price=intent.getStringExtra("ORDER_TOTAL_PRICE");
            orderedProductId=intent.getStringExtra("PRODUCT_LIST_ID");
            orderedId=intent.getStringExtra("ORDER_ID");
            orderedCustomer_email=intent.getStringExtra("ORDER_EMAIL");
            orderedPincode=intent.getStringExtra("PIN_CODE");
        }
        SessionManager sessionManager = new SessionManager(MyOrder_List_Details.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        orderProductDetailstoolbar = (Toolbar) findViewById(R.id.orderProductDetailsToolbar);
        setSupportActionBar(orderProductDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderProductDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orderProductDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.orderProductListingTypeInPetDetailsCollapsingToolbar);
        orderProductDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.orderProductDetailsCoordinatorLayout);
        orderProductDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.orderProductDetailsAppBar);
        orderProductDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.orderProductDetailsNestedScrollView);

        orderProductImage = (ImageView) findViewById(R.id.orderProductHeaderImage);
        orderProductDetailsImageText = (TextView) findViewById(R.id.orderProductDetailsImageText);
        orderProductDetailsImagesDividerLine = findViewById(R.id.orderProductDetailsImagesDividerLine);
        orderProductDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.orderProductDetailsFirstImageThumbnail);
        orderProductDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.orderProductDetailsSecondImageThumbnail);
        orderProductDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.orderProductDetailsThirdImageThumbnail);

        orderProductName = (TextView) findViewById(R.id.orderProductNameText);
        orderProductQty = (TextView) findViewById(R.id.orderedProductQuantity);
        orderProductPrice = (TextView) findViewById(R.id.orderProductPrice);
        orderProductDescription = (TextView) findViewById(R.id.orderProductDescription);
        orderProductDetailsDividerLine = findViewById(R.id.orderDetailsDividerLine);
        orderProductListDetailsDividerLine = findViewById(R.id.orderDetailsDividerLine);
        orderProductBuyNowButton = (Button) findViewById(R.id.orderProductCancelNowButton);
        txt_buildingName = (TextView) findViewById(R.id.orderedBuildingName);
        txt_area = (TextView) findViewById(R.id.orderedArea);
        txt_city = (TextView) findViewById(R.id.orderedCity);
        txt_orderedDate= (TextView) findViewById(R.id.orderedDate);
        txt_orderedProductQuantity= (TextView) findViewById(R.id.orderedProductQuantity);
        txt_orderedProductShipping_charges= (TextView) findViewById(R.id.orderedProductShipping_charges);
        txt_orderedProductTotal_price= (TextView) findViewById(R.id.orderedProductTotal_price);
        txt_orderedProductId= (TextView) findViewById(R.id.orderedProductId);
        txt_orderedId = (TextView) findViewById(R.id.orderedId);
        txt_pincode= (TextView) findViewById(R.id.orderedPincode);
         //txt_orderedCustomer_email= (TextView) findViewById(R.id.orderedCustomer_email);

        orderProductListDetailsLinearLayout = (LinearLayout) findViewById(R.id.orderProductListDetailsLinearLayout);
        orderProductListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.orderProductListDetailsFirstRelativeLayout);
        orderProductListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.orderProductListDetailsSecondRelativeLayout);
        orderProductListDetailsBuyNowButtonDividerLine=findViewById(R.id.orderDetailsDividerLine);

        orderProductBuyNowButton.setOnClickListener(this);
        //petDetailsEmailButton.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        //petDetailsCollapsingToolbar.setTitle(setListingTypeTitle(listingType));

        orderProductDetailsCollapsingToolbar.setTitle(nameoforderProduct);
        orderProductDetailsImageText.setText("Images");
        orderProductListDetailsBuyNowButtonDividerLine.setBackgroundResource(R.color.list_internal_divider);
        orderProductDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }
        String name =  nameoforderProduct;
        orderProductName.setText(Html.fromHtml(name));
        String priceOforderProduct =  price;
        orderProductPrice.setText(Html.fromHtml(priceOforderProduct));
        String descriptionOforderProduct =  description;
        orderProductDescription.setText(Html.fromHtml(descriptionOforderProduct));
        String custBuildingName =  buildingName;
        txt_buildingName.setText(Html.fromHtml(custBuildingName));
        String custArea =  area;
        txt_area.setText(Html.fromHtml(custArea));
        String custCity =  city;
        txt_city.setText(Html.fromHtml(custCity));
        String custOrderDate =  orderedDate;
        txt_orderedDate.setText(Html.fromHtml(custOrderDate));
        String custProductQty =  orderedProductQuantity;
        txt_orderedProductQuantity.setText(Html.fromHtml(custProductQty));
        String custOrderShppingCharges =  orderedProductShipping_charges;
        txt_orderedProductShipping_charges.setText(Html.fromHtml(custOrderShppingCharges));
        String custProductTotalPrice =  orderedProductTotal_price;
        txt_orderedProductTotal_price.setText(Html.fromHtml(custProductTotalPrice));
        String custProductId =  orderedProductId;
        txt_orderedProductId.setText(Html.fromHtml(custProductId));
        String custOrderId =  orderedId;
        txt_orderedId.setText(Html.fromHtml(custOrderId));
        String custOrderPincode =  orderedPincode;
        txt_pincode.setText(Html.fromHtml(custOrderPincode));

        orderProductDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);
        orderProductListDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) orderProductDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        orderProductDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        orderProductListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = orderProductListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = orderProductListDetailsSecondRelativeLayout.getHeight();

                orderProductListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
            }
        });
    }


    public class FetchImageFromServer extends AsyncTask<String, String, String> {
        String urlForFetch;
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return urlForFetch;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            if(urlForFetch.equals(firstImagePath)) {
                Glide.with(orderProductImage.getContext()).load(url).centerCrop().crossFade().into(orderProductImage);
                Glide.with(orderProductDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(orderProductDetailsFirstImageThumbnail);
                orderProductDetailsFirstImageThumbnail.setOnClickListener(MyOrder_List_Details.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(orderProductDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(orderProductDetailsSecondImageThumbnail);
                orderProductDetailsSecondImageThumbnail.setOnClickListener(MyOrder_List_Details.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(orderProductDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(orderProductDetailsThirdImageThumbnail);
                orderProductDetailsThirdImageThumbnail.setOnClickListener(MyOrder_List_Details.this);
            }
        }
    }

    private Bitmap getBitmapImageFromURL(String imagePath) {
        InputStream in = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }
            in = new BufferedInputStream(new URL(imagePath).openStream(), 4 * 1024);
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(dataStream, 4 * 1024);
        try {
            copy(in, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = dataStream.toByteArray();
        orderProductDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return orderProductDetailsbitmap;
    }


    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) orderProductDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(orderProductDetailsCoordinatorLayout, orderProductDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    @Override
    public void onBackPressed() {
        MyOrder_List_Details.this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.orderProductCancelNowButton) {

            Toast.makeText(MyOrder_List_Details.this, "You Cannot Cancel The Order .", Toast.LENGTH_LONG).show();

                //CancelOrder cancelOrder
//                Intent payment = new Intent(MyOrder_List_Details.this,PaymentActivity.class);
//                payment.putExtra("orderProductName",name);
//                payment.putExtra("orderProductPrice",price);
//                payment.putExtra("orderProductQty",orderProductQtySpinner);
//                payment.putExtra("email",email);
//                payment.putExtra("contactNo",contactNo);
//                //payment.putExtra("orderedId",orderedId);
//                startActivity(payment);

        }
        else if(v.getId() == R.id.petDetailsFirstImageThumbnail) {
            Glide.with(orderProductImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(orderProductImage);
        }
        else if(v.getId() == R.id.petDetailsSecondImageThumbnail) {
            Glide.with(orderProductImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(orderProductImage);
        }
        else if(v.getId() == R.id.petDetailsThirdImageThumbnail) {
            Glide.with(orderProductImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(orderProductImage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = MyOrder_List_Details.this.getPackageManager();
        ComponentName component = new ComponentName(MyOrder_List_Details.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = MyOrder_List_Details.this.getPackageManager();
        ComponentName component = new ComponentName(MyOrder_List_Details.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
