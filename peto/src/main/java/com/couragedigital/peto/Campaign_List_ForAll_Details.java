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
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.ContactNoInstance;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Campaign_List_ForAll_Details extends AppCompatActivity implements View.OnClickListener {
    String campaignId = "";
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String nameOfCampaign = "";
    String nameOfNgo = "";
    String urlOfNgo = "";
    String descriptionOfCampaign = "";
    String actualAmounOfCampaign;
    String minimumAmounOfCampaign="";
    String remainingAmounOfCampaign= "";
    String emailofCampaignOwner;
    String lastDateOfCampaign="";
    String postDateOfCampaign="";
    String donarEmail;
    String danationAmount="";
    String contactNo="";

    private ProgressDialog progressDialog = null;

    TextView campaignDetailsImageText;
    ImageView campaignDetailsFirstImageThumbnail;
    ImageView campaignDetailsSecondImageThumbnail;
    ImageView campaignDetailsThirdImageThumbnail;
    ImageView campaignImage;
    TextView campaignName;
    TextView ngoName;
    TextView ngoUrl;
    TextView campaignDescription;
    TextView campaignActualAmount;
    TextView campaignMinimumAmount;
    TextView campaignLastDate;
    EditText txt_doantionAmount;

    View v;
    View campaignDetailsDividerLine;
    View campaignListDetailsDividerLine;
    View campaignListDetailsDonateNowButtonDividerLine;
    View campaignDetailsImagesDividerLine;
    Button donateNowButton;

    Bitmap campaignDetailsbitmap;
    Toolbar campaignDetailstoolbar;
    CollapsingToolbarLayout campaignDetailsCollapsingToolbar;
    CoordinatorLayout campaignDetailsCoordinatorLayout;
    AppBarLayout campaigntDetailsAppBarLayout;
    NestedScrollView campaignDetailsNestedScrollView;
    private int mutedColor;

    LinearLayout campaignListDetailsLinearLayout;
    RelativeLayout campaignListDetailsFirstRelativeLayout;
    RelativeLayout campaignListDetailsSecondRelativeLayout;
    LinearLayout campaignMinimumAmountLayout;



    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_list_forall_details);

        Intent intent = getIntent();
        if (null != intent) {
            campaignId = intent.getStringExtra("CAMPAIGN_ID");
            firstImagePath = intent.getStringExtra("CAMPAIGN_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("CAMPAIGN_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("CAMPAIGN_THIRD_IMAGE");
            nameOfCampaign = intent.getStringExtra("CAMPAIGN_NAME");
            nameOfNgo = intent.getStringExtra("CAMPAIGN_NGO_NAME");
            descriptionOfCampaign = intent.getStringExtra("CAMPAIGN_DESCRIPTION");
            actualAmounOfCampaign = intent.getStringExtra("CAMPAIGN_ACTUAL_AMOUNT");
            minimumAmounOfCampaign = intent.getStringExtra("CAMPAIGN_MINIMUM_AMOUNT");
            emailofCampaignOwner = intent.getStringExtra("CAMPAIGN_OWNER_EMAIL");
            lastDateOfCampaign = intent.getStringExtra("CAMPAIGN_LAST_DATE");
            postDateOfCampaign = intent.getStringExtra("CAMPAIGN_POST_DATE");
            urlOfNgo=intent.getStringExtra("NGO_URL");
        }
        ContactNoInstance contactNoInstance= new ContactNoInstance();
        contactNo = contactNoInstance.getMobileNo();


        SessionManager sessionManager = new SessionManager(Campaign_List_ForAll_Details.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        donarEmail = user.get(SessionManager.KEY_EMAIL);


        campaignDetailstoolbar = (Toolbar) findViewById(R.id.campaignDetailsToolbar);
        setSupportActionBar(campaignDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        campaignDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        campaignDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.campaignDetailsCollapsingToolbar);
        campaignDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.campaignDetailsCoordinatorLayout);
        campaigntDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.campaignDetailsAppBar);
        campaignDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.campaignDetailsNestedScrollView);
        campaignImage = (ImageView) findViewById(R.id.campaignHeaderImage);
        campaignDetailsImageText = (TextView) findViewById(R.id.campaignDetailsImageText);
        campaignDetailsImagesDividerLine = findViewById(R.id.campaignDetailsImagesDividerLine);
        campaignDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.campaignDetailsFirstImageThumbnail);
        campaignDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.campaignDetailsSecondImageThumbnail);
        campaignDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.campaignDetailsThirdImageThumbnail);
        campaignName = (TextView) findViewById(R.id.campaignNameText);
        ngoName = (TextView) findViewById(R.id.ngoName);
        ngoUrl = (TextView) findViewById(R.id.ngoUrl);
        campaignDescription = (TextView) findViewById(R.id.description);
        campaignActualAmount = (TextView) findViewById(R.id.actualAmount);
        campaignMinimumAmount = (TextView) findViewById(R.id.minimumAmount);
        campaignLastDate = (TextView) findViewById(R.id.lastDate);
        donateNowButton = (Button) findViewById(R.id.donateNowButtonForAll);
        txt_doantionAmount = (EditText) findViewById(R.id.donationAmount);

        campaignDetailsDividerLine = findViewById(R.id.campaignDetailsDividerLine);
        campaignListDetailsDividerLine = findViewById(R.id.campaignDetailsDividerLine);
        campaignListDetailsLinearLayout = (LinearLayout) findViewById(R.id.campaignListDetailsLinearLayout);
        campaignListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.campaignListDetailsFirstRelativeLayout);
        campaignListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.campaignListDetailsSecondRelativeLayout);
        campaignMinimumAmountLayout = (LinearLayout) findViewById(R.id.minimumAmountLayout);
        campaignListDetailsDonateNowButtonDividerLine=findViewById(R.id.campaignDetailsDividerLine);
        donateNowButton.setOnClickListener(this);
        campaignDetailsCollapsingToolbar.setTitle(nameOfCampaign);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);

        campaignDetailstoolbar.setTitle(nameOfCampaign);
        campaignDetailsImageText.setText("Images");
        campaignListDetailsDonateNowButtonDividerLine.setBackgroundResource(R.color.list_internal_divider);
        campaignDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }

        String name =  nameOfCampaign;
        campaignName.setText(Html.fromHtml(name));
        String ngo =  nameOfNgo;
        ngoName.setText(Html.fromHtml(ngo));
        String url = urlOfNgo;
        ngoUrl.setText(Html.fromHtml(url));
        String Description =  descriptionOfCampaign;
        campaignDescription.setText(Html.fromHtml(Description));
        String actualAmount =  actualAmounOfCampaign;
        campaignActualAmount.setText(Html.fromHtml(actualAmount));
        String lasDate =  lastDateOfCampaign;
        campaignLastDate.setText(Html.fromHtml(lasDate));

        if(minimumAmounOfCampaign.equals("0") ||minimumAmounOfCampaign.equals(" ") ){
            //minimumAmountLayout
            campaignMinimumAmountLayout.setVisibility(View.GONE);
        }else {
            String minimumAmount =  minimumAmounOfCampaign;
            campaignMinimumAmount.setText(Html.fromHtml(minimumAmount));
        }

        campaignDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);
        campaignListDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) campaigntDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        campaigntDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });
        campaignListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = campaignListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = campaignListDetailsSecondRelativeLayout.getHeight();
                campaignListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
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
                Glide.with(campaignImage.getContext()).load(url).centerCrop().crossFade().into(campaignImage);
                Glide.with(campaignDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(campaignDetailsFirstImageThumbnail);
                campaignDetailsFirstImageThumbnail.setOnClickListener(Campaign_List_ForAll_Details.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(campaignDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(campaignDetailsSecondImageThumbnail);
                campaignDetailsSecondImageThumbnail.setOnClickListener(Campaign_List_ForAll_Details.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(campaignDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(campaignDetailsThirdImageThumbnail);
                campaignDetailsThirdImageThumbnail.setOnClickListener(Campaign_List_ForAll_Details.this);
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
        campaignDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return campaignDetailsbitmap;
    }


    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) campaigntDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(campaignDetailsCoordinatorLayout, campaigntDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        Campaign_List_ForAll_Details.this.finish();
    }

    @Override
    public void onClick(View v) {
        danationAmount = txt_doantionAmount.getText().toString();

        if(v.getId() == R.id.donateNowButtonForAll) {

            if(danationAmount == null || danationAmount.isEmpty()) {
                Toast.makeText(Campaign_List_ForAll_Details.this, "Please Enter Amount.", Toast.LENGTH_LONG).show();
            }
            else{
                int donatedAmount = Integer.parseInt(danationAmount);
                int minAmount = Integer.parseInt(minimumAmounOfCampaign);
                if(donatedAmount < minAmount  || donatedAmount==0){
                    Toast.makeText(Campaign_List_ForAll_Details.this, "Please Enter Minimum "+minAmount+" rs.", Toast.LENGTH_LONG).show();
                }else{

                    Intent gotoDonation = new Intent(Campaign_List_ForAll_Details.this,NGO_Donation.class);
                    gotoDonation.putExtra("campaignId",campaignId);
                    gotoDonation.putExtra("campaignName",nameOfCampaign);
                    gotoDonation.putExtra("ngoName",nameOfNgo);
                    gotoDonation.putExtra("amount",danationAmount);
                    gotoDonation.putExtra("donarEmail",donarEmail);
                    gotoDonation.putExtra("contactNo",contactNo);
                    gotoDonation.putExtra("emailofNgoOwner",emailofCampaignOwner);


                    startActivity(gotoDonation);
                }
            }
        }
        else if(v.getId() == R.id.campaignDetailsFirstImageThumbnail) {
            Glide.with(campaignImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(campaignImage);
        }
        else if(v.getId() == R.id.campaignDetailsSecondImageThumbnail) {
            Glide.with(campaignImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(campaignImage);
        }
        else if(v.getId() == R.id.campaignDetailsThirdImageThumbnail) {
            Glide.with(campaignImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(campaignImage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Campaign_List_ForAll_Details.this.getPackageManager();
        ComponentName component = new ComponentName(Campaign_List_ForAll_Details.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Campaign_List_ForAll_Details.this.getPackageManager();
        ComponentName component = new ComponentName(Campaign_List_ForAll_Details.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}

