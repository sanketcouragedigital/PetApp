package com.couragedigital.peto;


import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.couragedigital.peto.Connectivity.Campaign_Create;
import com.couragedigital.peto.CropImage.CropImage;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Campaign_Form extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int PIC_CAMERA_CROP = 3;
    private static final int PIC_GALLERY_CROP = 4;
    private static final int CAMERA_PERMISSION_REQUEST = 5;
    private static final int READ_STORAGE_PERMISSION_REQUEST = 6;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 7;

    private ProgressDialog progressDialog = null;

    public AlertDialog alertDialog;
    public ArrayAdapter<String> dialogAdapter;


    EditText campaignName;
    EditText campaignDescription;
    EditText campaignActualAmount;
    CheckBox campaignCHKMinimumAmount;
    EditText campaignMinimumAmount;
    //DatePicker campaignLastDate;
    TextView campaignLastDate;

    Button selectImageButton;
    ImageView firstImageOfPet;
    ImageView secondImageOfPet;
    ImageView thirdImageOfPet;
    FloatingActionButton uploadFabButton;

    String campaignNameText ="";
    String campaignDescriptionText ="";
    String campaignActualAmountText ="";
    String campaignMinimumAmountText = "0";
    String campaignLastDateText ="";
    String email;

    String currentPhotoPath;
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";

    Bitmap imageToShow;
    String timeStamp;
    File image;
    File storageDir;
    File cropFile;

    private int mYear, mMonth, mDay;

    private long TIME = 5000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_form);

        //ngoName = (EditText) this.findViewById(R.id.txtNGOName);
        campaignName = (EditText) this.findViewById(R.id.txtCampaignName);
        campaignDescription = (EditText) this.findViewById(R.id.txtDescription);
        campaignActualAmount = (EditText) this.findViewById(R.id.txtCampaignActualAmount);
        campaignMinimumAmount = (EditText) findViewById(R.id.txtMinmumAmount);
        campaignLastDate = (TextView) findViewById(R.id.txtCampaignLastDate);

        selectImageButton = (Button) this.findViewById(R.id.selectImage);
        firstImageOfPet = (ImageView) this.findViewById(R.id.firstImageOfPet);
        secondImageOfPet = (ImageView) this.findViewById(R.id.secondImageOfPet);
        thirdImageOfPet = (ImageView) this.findViewById(R.id.thirdImageOfPet);
        uploadFabButton = (FloatingActionButton) this.findViewById(R.id.campaignFormSubmitFab);

        campaignCHKMinimumAmount = (CheckBox) this.findViewById(R.id.chkMinmumAmount);

        selectImageButton.setOnClickListener(this);
        uploadFabButton.setOnClickListener(this);
        campaignLastDate.setOnClickListener(this);
        campaignMinimumAmount.addTextChangedListener(campaignMinimumAmountChangeListener);

        campaignMinimumAmount.setVisibility(View.GONE);
        campaignCHKMinimumAmount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    //CheckBox is checked
                    campaignMinimumAmount.setVisibility(View.VISIBLE);
                } else {
                    //CheckBox is unchecked
                    campaignMinimumAmountText = "0";
                    campaignMinimumAmount.setVisibility(View.GONE);
                }
            }
        });

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
    } //on create close

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
                        campaignMinimumAmountText = campaignMinimumAmount.getText().toString();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    //for images
    private void createCampaignFormSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(Campaign_Form.this, android.R.layout.select_dialog_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.alertDialogListNames));
                text.setTypeface(null, Typeface.ITALIC);
                return view;
            }
        };
        dialogAdapter.add("Take from Camera");
        dialogAdapter.add("Select from Gallery");
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Campaign_Form.this, R.style.AlertDialogCustom));
        builder.setTitle("Select Image");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    alertDialog.dismiss();
                    if(ActivityCompat.checkSelfPermission(Campaign_Form.this, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission();
                    }
                    else {
                        new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    }
                } else if (which == 1) {
                    alertDialog.dismiss();
                    new SelectGalleryImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    @TargetApi(23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
                    File image = new File(currentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    this.imageToShow = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

                    doCropping(image, PIC_CAMERA_CROP);
                }
                else if(requestCode == GALLERY_REQUEST) {
                    Uri uri = intent.getData();
                    String[] projection = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    currentPhotoPath = cursor.getString(columnIndex);
                    cursor.close();

                    File image = new File(currentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    this.imageToShow = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

                    doCropping(image, PIC_GALLERY_CROP);
                }
                else if(requestCode == PIC_CAMERA_CROP) {
                    Bundle extras = intent.getExtras();
                    this.imageToShow = extras.getParcelable("data");
                    String filename=currentPhotoPath.substring(currentPhotoPath.lastIndexOf("/")+1);
                    this.imageToShow = saveCameraCropBitmap(filename, imageToShow);
                    setBitmapToImage(this.imageToShow);
                }
                else if(requestCode == PIC_GALLERY_CROP) {
                    Bundle extras = intent.getExtras();
                    this.imageToShow = extras.getParcelable("data");
                    this.imageToShow = saveGalleryCropBitmap(imageToShow);
                    setBitmapToImage(this.imageToShow);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(Campaign_Form.this, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(e.getMessage(), "Error");
            Toast.makeText(Campaign_Form.this, "Error", Toast.LENGTH_LONG).show();
        }
    }

    private void setBitmapToImage(Bitmap imageToShow) {
        if(firstImageOfPet.getDrawable() == null) {
            //firstImageOfPet.setLayoutParams(getLayoutParams());
            firstImageOfPet.setImageBitmap(imageToShow);
            firstImagePath = cropFile.getAbsolutePath();
            selectImageButton.setText("Select More Images");
        }
        else if(secondImageOfPet.getDrawable() == null) {
            //secondImageOfPet.setLayoutParams(getLayoutParams());
            secondImageOfPet.setImageBitmap(imageToShow);
            secondImagePath = cropFile.getAbsolutePath();
        }
        else if(thirdImageOfPet.getDrawable() == null) {
            //thirdImageOfPet.setLayoutParams(getLayoutParams());
            thirdImageOfPet.setImageBitmap(imageToShow);
            thirdImagePath = cropFile.getAbsolutePath();
        }
    }

    private void doCropping(File image, int request_code) {
        /*Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(Uri.fromFile(image), "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);*/

        Intent cropIntent = new Intent(this, CropImage.class);

        cropIntent.putExtra("image-path", currentPhotoPath);
        cropIntent.putExtra("crop", true);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);

        try {
            startActivityForResult(cropIntent, request_code);
        } catch (Exception e) {
            Toast.makeText(Campaign_Form.this, "Crop Error", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap saveCameraCropBitmap(String filename, Bitmap imageToShow) {
        FileOutputStream outStream = null;

        cropFile = new File(currentPhotoPath);
        if (cropFile.exists()) {
            cropFile.delete();
            cropFile = new File(storageDir, timeStamp + ".png");
        }
        try {
            outStream = new FileOutputStream(cropFile);
            imageToShow.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageToShow;
    }

    private Bitmap saveGalleryCropBitmap(Bitmap imageToShow) {
        FileOutputStream outStream = null;

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        cropFile = new File(storageDir, timeStamp + ".png");
        try {
            outStream = new FileOutputStream(cropFile);
            imageToShow.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageToShow;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        /*image = File.createTempFile(
                timeStamp,
                ".png",
                storageDir
        );*/
        image = new File(storageDir, timeStamp + ".png");
        try {
            currentPhotoPath = image.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(Campaign_Form.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(Campaign_Form.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(Campaign_Form.this,
                    new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    public class SelectCameraImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
            return null;
        }
    }

    public class SelectGalleryImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                Toast.makeText(Campaign_Form.this, "CAMERA permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(Campaign_Form.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createCampaignFormSelectImageDialogChooser();
            } else {
                Toast.makeText(Campaign_Form.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = Campaign_Form.this.getPackageManager();
        ComponentName component = new ComponentName(Campaign_Form.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = Campaign_Form.this.getPackageManager();
        ComponentName component = new ComponentName(Campaign_Form.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
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

            if(v.getId() == R.id.selectImage) {
                if(thirdImageOfPet.getDrawable() != null) {
                    Toast.makeText(Campaign_Form.this, "Can not select more than 3 images", Toast.LENGTH_LONG).show();
                    selectImageButton.setClickable(false);
                }
                else {
                    if(ActivityCompat.checkSelfPermission(Campaign_Form.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestWriteStoragePermission();
                    }
                    else {
                        if(ActivityCompat.checkSelfPermission(Campaign_Form.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestReadStoragePermission();
                        }
                        else {
                            createCampaignFormSelectImageDialogChooser();
                        }
                    }
                }
            }
            if (v == campaignLastDate) {

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
                                campaignLastDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
                //String campaignMinimumAmountText = "";
            else if(v.getId() == R.id.campaignFormSubmitFab) {
                //ngoNameText = ngoName.getText().toString();
                campaignNameText = campaignName.getText().toString();
                campaignDescriptionText = campaignDescription.getText().toString();
                campaignActualAmountText = campaignActualAmount.getText().toString();
                //campaignMinimumAmountText = campaignMinimumAmount.getText().toString();
                campaignLastDateText = campaignLastDate.getText().toString();
                //campaignLastDateText= campaignLastDate.getYear() +"-"+(campaignLastDate.getMonth()+1) +"-"+campaignLastDate.getDayOfMonth();

                if(campaignNameText == null || campaignNameText.isEmpty()) {
                    Toast.makeText(Campaign_Form.this, "Please Enter Name For Campaign.", Toast.LENGTH_LONG).show();
                }
                else if(campaignDescriptionText == null || campaignDescriptionText.isEmpty()) {
                    Toast.makeText(Campaign_Form.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                }
                else if(campaignActualAmountText == null || campaignActualAmountText.isEmpty()) {
                    Toast.makeText(Campaign_Form.this, "Please Enter Amount.", Toast.LENGTH_LONG).show();
                }
                else if(campaignLastDateText.equals("")) {
                    Toast.makeText(Campaign_Form.this, "Please Enter Date.", Toast.LENGTH_LONG).show();
                }

                else if(firstImagePath == null || firstImagePath.isEmpty()) {
                    Toast.makeText(Campaign_Form.this, "Please select image of Campaign.", Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog = ProgressDialog.show(Campaign_Form.this, "", "Creating  Campaign...", true);

                    new UploadToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }
            }
        }

    public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Campaign_Create.uploadCampaignDetails(campaignNameText, campaignDescriptionText,campaignActualAmountText,campaignMinimumAmountText, campaignLastDateText, firstImagePath, secondImagePath, thirdImagePath, email,Campaign_Form.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(Campaign_Form.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

}

