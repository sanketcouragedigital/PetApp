package com.couragedigital.peto;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.net.Uri;
import com.couragedigital.peto.Connectivity.PetListFormUpload;
import com.couragedigital.peto.Connectivity.PetBreedsSpinnerList;
import com.couragedigital.peto.Connectivity.PetCategorySpinnerList;
import com.couragedigital.peto.Adapter.SpinnerItemsAdapter;
import com.couragedigital.peto.CropImage.CropImage;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;
import android.support.v7.widget.Toolbar;


public class PetForm extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

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

    Spinner petCategory;
    Spinner petBreed;
    Spinner petAgeInMonths;
    Spinner petAgeInYears;
    //EditText ageOfPet;
    RadioGroup genderOfPet;
    RadioButton genderSelected;
    EditText descriptionOfPet;
    RadioGroup giveAwayType;
    RadioButton petsell;
    RadioButton adoptionOfPet;
    EditText priceOfPet;
    Button selectImageButton;
    ImageView firstImageOfPet;
    ImageView secondImageOfPet;
    ImageView thirdImageOfPet;
    FloatingActionButton uploadFabButton;
    CheckBox labelForRegisterdNo;
    EditText alternateNo;
    EditText otherBreed;

    String txtAlternateNo="";
    String petCategoryName;
    String petBreedName;
    String petAgeMonthSpinner = "0";
    String petAgeYearSpinner = "0";
    //Integer petAge;
    String petGender = "";
    String petDescription;
    String petAdoption = "";
    Integer petPrice;
    String currentPhotoPath;
    String email;


    private List<String> petCategoryList = new ArrayList<String>();
    private List<String> petBreedsList = new ArrayList<String>();
    private SpinnerItemsAdapter adapter;
    String[] petCategoryArrayList;
    String[] petBreedArrayList;

    private  List<String> petAgeListInMonth = new ArrayList<String>();
    private List<String> petAgeListInYear = new ArrayList<String>();
    String[] stringArrayListForMonth;
    String[] stringArrayListForYear;

    Bitmap imageToShow;
    String timeStamp;
    File image;
    File storageDir;
    File cropFile;
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";

    private long TIME = 5000;
    private Toolbar petFormToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petform);

      petFormToolbar = (Toolbar) findViewById(R.id.petFormToolbar);
        setSupportActionBar(petFormToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        petFormToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        petCategory = (Spinner) this.findViewById(R.id.petCategory);
        petBreed = (Spinner) this.findViewById(R.id.petBreed);
        //ageOfPet = (EditText) this.findViewById(R.id.ageOfPet);
        genderOfPet = (RadioGroup) this.findViewById(R.id.genderOfPet);
        descriptionOfPet = (EditText) this.findViewById(R.id.descriptionOfPet);
        giveAwayType = (RadioGroup) this.findViewById(R.id.giveAwayOfPet);
        priceOfPet = (EditText) this.findViewById(R.id.priceOfPet);
        selectImageButton = (Button) this.findViewById(R.id.selectImage);
        firstImageOfPet = (ImageView) this.findViewById(R.id.firstImageOfPet);
        secondImageOfPet = (ImageView) this.findViewById(R.id.secondImageOfPet);
        thirdImageOfPet = (ImageView) this.findViewById(R.id.thirdImageOfPet);
        uploadFabButton = (FloatingActionButton) this.findViewById(R.id.petFormSubmitFab);
        petAgeInMonths = (Spinner) this.findViewById(R.id.ageInMonths);
        petAgeInYears = (Spinner) this.findViewById(R.id.ageInYears);
        petsell = (RadioButton) findViewById(R.id.petSell);
        alternateNo = (EditText) findViewById(R.id.alternateNotxt);
        otherBreed = (EditText) findViewById(R.id.otherBreedtxtPet);
        labelForRegisterdNo =(CheckBox) this.findViewById(R.id.contactNocheckBox);

        GenarateSpinerForAge();

        labelForRegisterdNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()){
                    //CheckBox is checked
                    txtAlternateNo="";
                    alternateNo.setEnabled(false);
                }else{
                    //CheckBox is unchecked
                    alternateNo.setEnabled(true);
                }
            }
        });

        SessionManager sessionManager = new SessionManager(PetForm.this.getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        petCategoryArrayList = new String[]{
                "Select Pet Category"
        };
        petCategoryList = new ArrayList<>(Arrays.asList(petCategoryArrayList));
        adapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petCategoryList);

        new FetchCategoryListFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petCategory.setAdapter(adapter);

        petCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    petCategoryName = (String) parent.getItemAtPosition(position);
                    new FetchBreedListCategoryWiseFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    petBreed.setSelection(petBreedsList.indexOf(0));
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        petBreedArrayList = new String[]{
                "Select Pet Breed"
        };
        petBreedsList = new ArrayList<>(Arrays.asList(petBreedArrayList));
        adapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petBreedsList);
        adapter.notifyDataSetChanged();
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petBreed.setAdapter(adapter);
        petBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    petBreedName = (String) parent.getItemAtPosition(position);
                    if(petBreedName.equals("Other") ){
                        otherBreed.setEnabled(true);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        giveAwayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i == R.id.forAdoptionOfPet) {
                    petPrice = 0;
                    adoptionOfPet = (RadioButton) findViewById(R.id.forAdoptionOfPet);
                    priceOfPet.setEnabled(false);
                    petAdoption = "For Adoption";
                } else {
                    petAdoption = "";
                    priceOfPet.setEnabled(true);
                }
                if(petsell.getError() != null) {
                    petsell.setError(null);
                }
            }
        });

        otherBreed.addTextChangedListener(otherBreedNameChangeListener);
        alternateNo.addTextChangedListener(alternatePhoneNoChangeListener);
        priceOfPet.addTextChangedListener(priceChangeListener);
        selectImageButton.setOnClickListener(this);
        uploadFabButton.setOnClickListener(this);
    }

    public void GenarateSpinerForAge(){
        stringArrayListForMonth = new String[]{
                "Months"
        };
        petAgeListInMonth = new ArrayList<>(Arrays.asList(stringArrayListForMonth));
        for(int i=0;i<=11;i++){
            String j=String.valueOf(i);
            petAgeListInMonth.add(j);
        }
        SpinnerItemsAdapter monthAdapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petAgeListInMonth);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petAgeInMonths.setAdapter(monthAdapter);
        petAgeInMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    petAgeMonthSpinner = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        stringArrayListForYear = new String[]{
                "Years"
        };
        petAgeListInYear = new ArrayList<>(Arrays.asList(stringArrayListForYear));
        for(int i=0;i<=11;i++){
            String j=String.valueOf(i);
            petAgeListInYear.add(j);
        }
        SpinnerItemsAdapter yearAdapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petAgeListInYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petAgeInYears.setAdapter(yearAdapter);
        petAgeInYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    petAgeYearSpinner = parent.getItemAtPosition(position).toString();
                }
                //petAgeYearSpinner = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private TextWatcher alternatePhoneNoChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            new GetPetAlternateNo().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };

    private TextWatcher otherBreedNameChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            new GetBreedName().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };

    private TextWatcher priceChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            new GetPetPrice().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
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
                Toast.makeText(PetForm.this, "Can not select more than 3 images", Toast.LENGTH_LONG).show();
                selectImageButton.setClickable(false);
            }
            else {
                if(ActivityCompat.checkSelfPermission(PetForm.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWriteStoragePermission();
                }
                else {
                    if(ActivityCompat.checkSelfPermission(PetForm.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestReadStoragePermission();
                    }
                    else {
                        createPetFormSelectImageDialogChooser();
                    }
                }
            }
        }
        else if(v.getId() == R.id.petFormSubmitFab) {
            if (petCategoryName == null || petCategoryName.isEmpty()) {
                Toast.makeText(PetForm.this, "Please select Pet Category.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petCategory.getSelectedView();
                errorText.setError("Please select Pet Category");
            }
            else if(petBreedName == null || petBreedName.isEmpty()) {
                Toast.makeText(PetForm.this, "Please select Pet Breed.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petBreed.getSelectedView();
                errorText.setError("Please select Pet Breed");
            }
            else if(giveAwayType.getCheckedRadioButtonId() == -1) {
                Toast.makeText(PetForm.this, "Please select Options.", Toast.LENGTH_LONG).show();
                petsell.setError(("Please Select Option"));
            }
            else if(firstImagePath == null || firstImagePath.isEmpty()) {
                Toast.makeText(PetForm.this, "Please select image of Pet.", Toast.LENGTH_LONG).show();
            }
            else {
                progressDialog = ProgressDialog.show(PetForm.this, "", "Uploading file...", true);
                int selectedGender = genderOfPet.getCheckedRadioButtonId();
                View genderSelected = genderOfPet.findViewById(selectedGender);
                int selectedGenderId = genderOfPet.indexOfChild(genderSelected);
                RadioButton gender = (RadioButton) genderOfPet.getChildAt(selectedGenderId);
                petGender = (String) gender.getText();

                petDescription = descriptionOfPet.getText().toString();
               // petBreedName = otherBreed.getText().toString();

                new UploadToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }
    }

    private void createPetFormSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(PetForm.this, android.R.layout.select_dialog_item) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PetForm.this, R.style.AlertDialogCustom));
        builder.setTitle("Select Image");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    alertDialog.dismiss();
                    if(ActivityCompat.checkSelfPermission(PetForm.this, android.Manifest.permission.CAMERA)
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
                Toast.makeText(PetForm.this, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(e.getMessage(), "Error");
            Toast.makeText(PetForm.this, "Error", Toast.LENGTH_LONG).show();
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
            Toast.makeText(PetForm.this, "Crop Error", Toast.LENGTH_LONG).show();
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

    public class GetPetAlternateNo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtAlternateNo = alternateNo.getText().toString();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetBreedName extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        petBreedName = otherBreed.getText().toString();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class GetPetPrice extends AsyncTask<Void, Void, Void> {
        String takePetPrice;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        takePetPrice = priceOfPet.getText().toString();
                        if (!takePetPrice.equals("")) {
                            Integer takePetPriceInInteger = Integer.parseInt(takePetPrice);
                            if (takePetPriceInInteger > 0) {
                                petPrice = takePetPriceInInteger;
                            }
                        } else if (takePetPrice.equals("")) {
                            petPrice = 0;
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class FetchCategoryListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                PetCategorySpinnerList petCategorySpinnerList = new PetCategorySpinnerList(PetForm.this);
                petCategorySpinnerList.fetchPetCategory(petCategoryList, adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class FetchBreedListCategoryWiseFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                PetBreedsSpinnerList petBreedsSpinnerList = new PetBreedsSpinnerList(PetForm.this);
                petBreedsSpinnerList.fetchPetBreeds(petBreedsList, petCategoryName, adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetForm.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetForm.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(PetForm.this,
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
                Toast.makeText(PetForm.this, "CAMERA permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(PetForm.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPetFormSelectImageDialogChooser();
            } else {
                Toast.makeText(PetForm.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                PetListFormUpload.uploadToRemoteServer(petCategoryName, petBreedName, petAgeMonthSpinner,petAgeYearSpinner,petGender, petDescription, petAdoption, petPrice, firstImagePath, secondImagePath, thirdImagePath, email, txtAlternateNo, PetForm.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(PetForm.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetForm.this.getPackageManager();
        ComponentName component = new ComponentName(PetForm.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetForm.this.getPackageManager();
        ComponentName component = new ComponentName(PetForm.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
