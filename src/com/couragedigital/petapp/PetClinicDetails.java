package com.couragedigital.petapp;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.couragedigital.petapp.Adapter.ClinicReviewsListAdapter;
import com.couragedigital.petapp.Connectivity.ShowClinicFeedback;
import com.couragedigital.petapp.Singleton.ClinicReviewInstance;
import com.couragedigital.petapp.model.ClinicReviewsListItems;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PetClinicDetails extends BaseActivity implements View.OnClickListener {

    String image_path = "";
    String clinicaddress = "";
    String doctorname = "";
    String cliniccity = "";
    String clinicarea = "";
    String clinicnotes;


    ImageView clinicImage;
    TextView clinicDoctorName;
    TextView address;
    TextView clinicnotestxt;
    ImageButton callbutton;
    ImageButton rateAndReviewButton;
    //ImageButton mapButton;
    Button emailbutton;
    String phoneno;

    Bitmap clinicDetailsbitmap;
    Toolbar clinicDetailsToolbar;
    CollapsingToolbarLayout clinicDetailsCollapsingToolbar;
    CoordinatorLayout clinicDetailsCoordinatorLayout;
    AppBarLayout clinicDetailsAppBaLayoutr;
    NestedScrollView clinicDetailsNestedScrollView;

    String clinicRatingValue;
    String clinicFeebback;
    String clinicName;
    String clinicId;
    String email;

    //List<ClinicReviewsListItems> responseOfReviewList;
    ProgressDialog progressDialog = null;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;
    private int current_page = 1;

    private String url;
    public List<ClinicReviewsListItems> clinicReviewsListItemsArrayList = new ArrayList<ClinicReviewsListItems>();

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.clinic_details_new);
        setContentView(R.layout.clinic_details_new);

        ClinicReviewInstance clinicReviewInstance =new ClinicReviewInstance();
        clinicReviewsListItemsArrayList=clinicReviewInstance.getClinicReviewsListItemsArrayList();

        recyclerView = (RecyclerView) findViewById(R.id.clinicRateNReview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ClinicReviewsListAdapter(clinicReviewsListItemsArrayList);
        // url= "http://192.168.0.7/PetAppAPI/api/petappapi.php?method=showClinicReviews&format=json&currentPage=" + current_page + "&clinicId=" + clinicId + "";
        //new FetchClinicReviewListFromServer().execute(url);
        recyclerView.setAdapter(adapter);
//        LinearLayout.LayoutParams params = new
//                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        // calculate height of RecyclerView based on child count
//        int i = adapter.getItemCount();
//        params.height = 400 * i;
//        // set height of RecyclerView
//        recyclerView.setLayoutParams(params);

        Intent intent = getIntent();
        if (null != intent) {
            clinicId = intent.getStringExtra("CLINIC_ID");
            clinicName = intent.getStringExtra("CLINIC_NAME");
            image_path = intent.getStringExtra("CLINIC_IMAGE");
            clinicaddress = intent.getStringExtra("CLINIC_ADDRESS");
            doctorname = intent.getStringExtra("DOCTOR_NAME");
            clinicnotes = intent.getStringExtra("CLINIC_NOTES");
            //email = intent.getStringExtra("DOCTOR_EMAIL");
            phoneno = intent.getStringExtra("DOCTOR_CONTACT");
        }
        //to create Reviews List

       // clinicDetailsToolbar = (Toolbar) findViewById(R.id.petClinicDetailsToolbar);
//        setSupportActionBar(clinicDetailsToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        clinicDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

//       clinicDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.clinicDetailsCollapsingToolbar);
//        clinicDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.clinicDetailsCoordinatorLayout);
//        clinicDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petClinicDetailsAppBar);
//       clinicDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petClinicDetailsNestedScrollView);

        clinicImage = (ImageView) findViewById(R.id.petClicnicHeaderImage);
       // clinicDoctorName = (TextView) findViewById(R.id.clinicDoctorName);
        address = (TextView) findViewById(R.id.petClinicAddress);
        clinicnotestxt = (TextView) findViewById(R.id.petClinicNotes);
        callbutton = (ImageButton) findViewById(R.id.clinicDetailCallButton);
        rateAndReviewButton= (ImageButton) findViewById(R.id.clinicDetailsRateNReviewButton);
        //mapButton= (ImageButton) findViewById(R.id.clinicDetailsMapButton);
       // emailbutton = (Button) findViewById(R.id.clinicDetailsEmailButton);

        //new FetchImageFromServer().execute();
        InputStream in = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }
            in = new BufferedInputStream(new URL(image_path).openStream(), 4 * 1024);
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
        clinicDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        clinicImage.setImageBitmap(clinicDetailsbitmap);


        //clinicDoctorName.setText(doctorname);
        address.setText(clinicaddress);
        clinicnotestxt.setText(clinicnotes);

//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) clinicDetailsAppBaLayoutr.getLayoutParams();
//        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

//        clinicDetailsAppBaLayoutr.post(new Runnable() {
//            @Override
//            public void run() {
//                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
//                setAppBarOffset(heightPx);
//            }
//        });

        callbutton.setOnClickListener(this);
        //mapButton.setOnClickListener(this);
        rateAndReviewButton.setOnClickListener(this);
        //emailbutton.setOnClickListener(this);


   }

   /*public class FetchImageFromServer extends AsyncTask<String, String, Bitmap> {

       @Override
        protected Bitmap doInBackground(String... url) {
            try {
                InputStream in = null;
                try {
                    if (android.os.Build.VERSION.SDK_INT > 9) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();

                        StrictMode.setThreadPolicy(policy);
                    }
                    in = new BufferedInputStream(new URL(image_path).openStream(), 4 * 1024);
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
                clinicDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return clinicDetailsbitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            clinicImage.setImageBitmap(bitmap);
        }
    }*/

//   public class FetchClinicReviewListFromServer extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... url) {
//            try {
//               ShowClinicFeedback.showClinicReviews(clinicReviewsListItemsArrayList,adapter, url[0]);
//            } catch (Exception e) {
//                e.printStackTrace();
//                progressDialog.dismiss();
//            }
//            return null;
//        }
//   }

    private void copy(InputStream in, BufferedOutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

//    private void setAppBarOffset(int i) {
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) clinicDetailsAppBaLayoutr.getLayoutParams();
//        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//        behavior.onNestedPreScroll(clinicDetailsCoordinatorLayout, clinicDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
//    }

    @Override
    public void onBackPressed() {
        PetClinicDetails.this.finish();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.clinicDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            startActivity(callIntent);
        }else if (view.getId() == R.id.clinicDetailsRateNReviewButton) {
           Intent gotorateAndReviewPage = new Intent(PetClinicDetails.this,ClinicRateNReview.class);
            gotorateAndReviewPage.putExtra("selectedClinicId",clinicId);
            gotorateAndReviewPage.putExtra("selectedClinicName",clinicName);
            startActivity(gotorateAndReviewPage);
        }
//          else if (view.getId() == R.id.clinicDetailsEmailButton) {
//            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
//            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
//        }
    }
}


