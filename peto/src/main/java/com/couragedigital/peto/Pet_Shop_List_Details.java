package com.couragedigital.peto;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import com.couragedigital.peto.Adapter.SpinnerItemsAdapter;
import com.couragedigital.peto.Connectivity.Generate_Order;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.ContactNoInstance;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Pet_Shop_List_Details extends AppCompatActivity implements View.OnClickListener {
    String productId = "";
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String nameofProduct = "";
    String price = "";
    String description = "";
    String productQtySpinner;
    String[] stringArrayListForProductQty;
    String totalPrice="";
    String shippingCharges= "110";
    String email;
    String contactNo="";
    String name="";
    String username="";
    String buildingName="";
    String area="";
    String city="";
    String pincode="";

    private ProgressDialog progressDialog = null;
    private List<String> productQtyListNos = new ArrayList<String>();
    Spinner productQty;
    CheckBox addresscheckBox;
    TextView productDetailsImageText;
    View productDetailsImagesDividerLine;
    ImageView productDetailsFirstImageThumbnail;
    ImageView productDetailsSecondImageThumbnail;
    ImageView productDetailsThirdImageThumbnail;
    ImageView productImage;
    TextView productName;
    TextView productPrice;
    TextView productDescription;
    EditText txt_buildingName;
    EditText txt_area;
    EditText txt_city;
    EditText txt_pincode;

    View productDetailsDividerLine;
    View v;
    View productListDetailsDividerLine;
    View productListDetailsBuyNowButtonDividerLine;
    Button productBuyNowButton;
    //Button petDetailsEmailButton;

    Bitmap productDetailsbitmap;
    Toolbar productDetailstoolbar;
    CollapsingToolbarLayout productDetailsCollapsingToolbar;
    CoordinatorLayout productDetailsCoordinatorLayout;
    AppBarLayout productDetailsAppBarLayout;
    NestedScrollView productDetailsNestedScrollView;
    private int mutedColor;

    LinearLayout productListDetailsLinearLayout;
    //RelativeLayout productListDetailsLinearLayout;
    RelativeLayout productListDetailsFirstRelativeLayout;
    RelativeLayout productListDetailsSecondRelativeLayout;
    RelativeLayout shopRelativeLayoutForAddress;
    RelativeLayout shopLayoutForAddress;


    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_shop_list_details);

        Intent intent = getIntent();
        if (null != intent) {
            productId = intent.getStringExtra("LIST_ID");
            firstImagePath = intent.getStringExtra("PRODUCT_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("PRODUCT_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("PRODUCT_THIRD_IMAGE");
            nameofProduct = intent.getStringExtra("PRODUCT_NAME");
            price = intent.getStringExtra("PRODUCT_PRICE");
            description = intent.getStringExtra("PRODUCT_DESCRIPTION");
        }
        SessionManager sessionManager = new SessionManager(Pet_Shop_List_Details.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        ContactNoInstance contactNoInstance= new ContactNoInstance();
        contactNo = contactNoInstance.getMobileNo();
        username = contactNoInstance.getName();
        buildingName = contactNoInstance.getBuildingName();
        area = contactNoInstance.getArea();
        city = contactNoInstance.getCity();

        productDetailstoolbar = (Toolbar) findViewById(R.id.productDetailsToolbar);
        setSupportActionBar(productDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        productDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.productListingTypeInPetDetailsCollapsingToolbar);
        productDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.produtDetailsCoordinatorLayout);
        productDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.productDetailsAppBar);
        productDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.productDetailsNestedScrollView);

        productImage = (ImageView) findViewById(R.id.productHeaderImage);
        productDetailsImageText = (TextView) findViewById(R.id.productDetailsImageText);
        productDetailsImagesDividerLine = findViewById(R.id.productDetailsImagesDividerLine);
        productDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.productDetailsFirstImageThumbnail);
        productDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.productDetailsSecondImageThumbnail);
        productDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.productDetailsThirdImageThumbnail);

        productName = (TextView) findViewById(R.id.productNameText);
        productQty = (Spinner) findViewById(R.id.productQty);
        productPrice = (TextView) findViewById(R.id.productPrice);
        productDescription = (TextView) findViewById(R.id.productDescription);
        productDetailsDividerLine = findViewById(R.id.shopDetailsDividerLine);
        productListDetailsDividerLine = findViewById(R.id.shopDetailsDividerLine);
        productBuyNowButton = (Button) findViewById(R.id.productBuyNowButton);
        addresscheckBox = (CheckBox) findViewById(R.id.addresscheckBox);
        txt_buildingName = (EditText) findViewById(R.id.txtBuildingname);
        txt_area = (EditText) findViewById(R.id.txtArea);
        txt_city = (EditText) findViewById(R.id.txtCity);
        txt_pincode= (EditText) findViewById(R.id.txtPincode);

        productListDetailsLinearLayout = (LinearLayout) findViewById(R.id.productListDetailsLinearLayout);
        //productListDetailsLinearLayout = (RelativeLayout) findViewById(R.id.detailsRelativeLayout);
        productListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.productListDetailsFirstRelativeLayout);
        productListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.productListDetailsSecondRelativeLayout);
        shopLayoutForAddress = (RelativeLayout) findViewById(R.id.shopLayoutForAddress);

        productListDetailsBuyNowButtonDividerLine=findViewById(R.id.shopDetailsDividerLine);

        productBuyNowButton.setOnClickListener(this);
        //petDetailsEmailButton.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        //petDetailsCollapsingToolbar.setTitle(setListingTypeTitle(listingType));

        productDetailsCollapsingToolbar.setTitle(nameofProduct);

        productDetailsImageText.setText("Images");
        productListDetailsBuyNowButtonDividerLine.setBackgroundResource(R.color.list_internal_divider);
        productDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }
        String name =  nameofProduct;
        productName.setText(Html.fromHtml(name));
        String priceOfProduct =  price;
        productPrice.setText(Html.fromHtml(priceOfProduct));
        String descriptionOfProduct =  description;
        productDescription.setText(Html.fromHtml(descriptionOfProduct));

        productDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);
        productListDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) productDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        productDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        txt_buildingName.setText(buildingName);
        txt_area.setText(area);
        txt_city.setText(city);
        shopLayoutForAddress.setVisibility(View.GONE);

        addresscheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()){
                    shopLayoutForAddress.setVisibility(View.GONE);
                    productListDetailsLinearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            Integer heightOfFirstRelativeLayout = productListDetailsFirstRelativeLayout.getHeight();
                            Integer heightOfSecondRelativeLayout = productListDetailsSecondRelativeLayout.getHeight();
                            productListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout - heightOfSecondRelativeLayout - 200);
                        }
                    });
                }else{
                    shopLayoutForAddress.setVisibility(View.VISIBLE);
                    productListDetailsLinearLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            Integer heightOfFirstRelativeLayout = productListDetailsFirstRelativeLayout.getHeight();
                            Integer heightOfSecondRelativeLayout = productListDetailsSecondRelativeLayout.getHeight();
                            productListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
                        }
                    });
                }
            }
        });
        GenarateSpinerForQty();
    }
    public void GenarateSpinerForQty(){
        stringArrayListForProductQty = new String[]{
                "Select Quantity"
        };
        productQtyListNos = new ArrayList<>(Arrays.asList(stringArrayListForProductQty));
        for(int i=1;i<=5;i++){
            String j=String.valueOf(i);
            productQtyListNos.add(j);
        }
        SpinnerItemsAdapter qtyAdapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, productQtyListNos);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productQty.setAdapter(qtyAdapter);
        productQty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    productQtySpinner = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                Glide.with(productImage.getContext()).load(url).centerCrop().crossFade().into(productImage);
                Glide.with(productDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(productDetailsFirstImageThumbnail);
                productDetailsFirstImageThumbnail.setOnClickListener(Pet_Shop_List_Details.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(productDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(productDetailsSecondImageThumbnail);
                productDetailsSecondImageThumbnail.setOnClickListener(Pet_Shop_List_Details.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(productDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(productDetailsThirdImageThumbnail);
                productDetailsThirdImageThumbnail.setOnClickListener(Pet_Shop_List_Details.this);
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
        productDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return productDetailsbitmap;
    }


    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) productDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(productDetailsCoordinatorLayout, productDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        Pet_Shop_List_Details.this.finish();
    }

    @Override
    public void onClick(View v) {
        buildingName = txt_buildingName.getText().toString();
        area = txt_area.getText().toString();
        city = txt_city.getText().toString();
        pincode = txt_pincode.getText().toString();
        if(v.getId() == R.id.productBuyNowButton) {
            if (productQtySpinner == null || productQtySpinner.isEmpty()) {
                Toast.makeText(Pet_Shop_List_Details.this, "Please select Quantity.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) productQty.getSelectedView();
                errorText.setError("Please select Quantity");
            } else if(buildingName == null || buildingName.isEmpty()) {
                Toast.makeText(Pet_Shop_List_Details.this, "Please Enter Address.", Toast.LENGTH_LONG).show();
            }else if(area == null || area.isEmpty()) {
                Toast.makeText(Pet_Shop_List_Details.this, "Please Enter Area.", Toast.LENGTH_LONG).show();
            }
            else if(city == null || city.isEmpty()) {
                Toast.makeText(Pet_Shop_List_Details.this, "Please Enter City.", Toast.LENGTH_LONG).show();
            }else if(pincode.equals("") || pincode.isEmpty()) {
                Toast.makeText(Pet_Shop_List_Details.this, "Please Enter Pincode.", Toast.LENGTH_LONG).show();
            }
            else{
                progressDialog = ProgressDialog.show(Pet_Shop_List_Details.this, "", "Loading...", true);
                int ProducttotalPrice = Integer.valueOf(productQtySpinner)  * Integer.valueOf(price)+ Integer.valueOf(shippingCharges);
                totalPrice=String.valueOf(ProducttotalPrice);
                new OrderUploadToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }
        else if(v.getId() == R.id.petDetailsFirstImageThumbnail) {
            Glide.with(productImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(productImage);
        }
        else if(v.getId() == R.id.petDetailsSecondImageThumbnail) {
            Glide.with(productImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(productImage);
        }
        else if(v.getId() == R.id.petDetailsThirdImageThumbnail) {
            Glide.with(productImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(productImage);
        }
    }
    public class OrderUploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Generate_Order generate_Order = new Generate_Order(Pet_Shop_List_Details.this);
                generate_Order.GenarateOrder(productId, nameofProduct,price,productQtySpinner, shippingCharges,totalPrice,username, contactNo, email, buildingName, area, city, pincode);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Pet_Shop_List_Details.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Pet_Shop_List_Details.this.getPackageManager();
        ComponentName component = new ComponentName(Pet_Shop_List_Details.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Pet_Shop_List_Details.this.getPackageManager();
        ComponentName component = new ComponentName(Pet_Shop_List_Details.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
