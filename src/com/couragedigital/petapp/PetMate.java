package com.couragedigital.petapp;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.net.Uri;
import com.couragedigital.petapp.Connectivity.PetBreedsSpinnerList;
import com.couragedigital.petapp.Connectivity.PetCategorySpinnerList;
import com.couragedigital.petapp.Adapter.SpinnerItemsAdapter;
import com.couragedigital.petapp.Connectivity.PetMateFormUpload;
import com.couragedigital.petapp.CropImage.CropImage;
import com.couragedigital.petapp.SessionManager.SessionManager;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class PetMate extends BaseActivity implements View.OnClickListener {

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
    Spinner ageOfPetInMonth;
    Spinner ageOfPetInYear;

    RadioGroup genderOfPet;
    RadioButton genderSelected;
    EditText descriptionOfPet;

    Button selectImageButton;
    ImageView firstImageOfPetMate;
    ImageView secondImageOfPetMate;
    ImageView thirdImageOfPetMate;
    FloatingActionButton uploadButton;
    CheckBox labelForRegisterdNo;
    EditText alternateNo;

    String txtAlternateNo="";
    String email;
    String petCategoryName;
    String petBreedName;
    String petMateAgeMonthSpinner = "0";
    String petMateAgeYearSpinner = "0";
    String petGender = "";
    String petDescription;
    String currentPhotoPath;

    SessionManager sessionManager;

    private List<String> petCategoryList = new ArrayList<String>();
    private List<String> petBreedsList = new ArrayList<String>();
    private SpinnerItemsAdapter adapter;
    String[] petCategoryArrayList;
    String[] petBreedArrayList;

    private  List<String> petMateAgeListInMonth = new ArrayList<String>();
    private List<String> petMateAgeListInYear = new ArrayList<String>();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmate);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);  // get email from session

        petCategory = (Spinner) this.findViewById(R.id.petMateCategorySpinner);
        petBreed = (Spinner) this.findViewById(R.id.petMateBreedSpinner);
        //ageOfPet = (EditText) this.findViewById(R.id.ageOfPetMate);
        ageOfPetInMonth = (Spinner) this.findViewById(R.id.ageOfPetMateInMonths);
        ageOfPetInYear = (Spinner) this.findViewById(R.id.ageOfPetMateInYears);
        genderOfPet = (RadioGroup) this.findViewById(R.id.genderOfPetMate);
        descriptionOfPet = (EditText) this.findViewById(R.id.descriptionOfPetMate);
        selectImageButton = (Button) this.findViewById(R.id.selectImagePetMate);
        firstImageOfPetMate = (ImageView) this.findViewById(R.id.firstImageOfPetMate);
        secondImageOfPetMate = (ImageView) this.findViewById(R.id.secondImageOfPetMate);
        thirdImageOfPetMate = (ImageView) this.findViewById(R.id.thirdImageOfPetMate);
        uploadButton = (FloatingActionButton) this.findViewById(R.id.petMateFormSubmitFab);
        alternateNo = (EditText) findViewById(R.id.alternateNotxt);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        alternateNo.addTextChangedListener(alternatePhoneNoChangeListener);
        selectImageButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
    }

    public void GenarateSpinerForAge(){
        //for month
        stringArrayListForMonth = new String[]{
                "Months"
        };
        petMateAgeListInMonth = new ArrayList<>(Arrays.asList(stringArrayListForMonth));
        for(int i=0;i<=11;i++){
            String j=String.valueOf(i);
            petMateAgeListInMonth.add(j);
        }
        SpinnerItemsAdapter monthAdapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petMateAgeListInMonth);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageOfPetInMonth.setAdapter(monthAdapter);
        ageOfPetInMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    petMateAgeMonthSpinner = (String) parent.getItemAtPosition(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //for month
        stringArrayListForYear = new String[]{
                "Years"
        };
        petMateAgeListInYear = new ArrayList<>(Arrays.asList(stringArrayListForYear));
        for(int i=0;i<=99;i++){
            String j=String.valueOf(i);
            petMateAgeListInYear.add(j);
        }
        SpinnerItemsAdapter yearAdapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petMateAgeListInYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageOfPetInYear.setAdapter(yearAdapter);
        ageOfPetInYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if(position > 0) {
                    petMateAgeYearSpinner = parent.getItemAtPosition(position).toString();
                }
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.selectImagePetMate) {
            if(thirdImageOfPetMate.getDrawable() != null) {
                Toast.makeText(PetMate.this, "Can not select more than 3 images", Toast.LENGTH_LONG).show();
                selectImageButton.setClickable(false);
            }
            else {
                if(ActivityCompat.checkSelfPermission(PetMate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestWriteStoragePermission();
                }
                else {
                    if(ActivityCompat.checkSelfPermission(PetMate.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestReadStoragePermission();
                    }
                    else {
                        createPetMateFormSelectImageDialogChooser();
                    }
                }
            }
        }
        else if(v.getId() == R.id.petMateFormSubmitFab) {
            if (petCategoryName == null || petCategoryName.isEmpty()) {
                Toast.makeText(PetMate.this, "Please select Pet Category.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petCategory.getSelectedView();
                errorText.setError("Please select Pet Category");
            }
            else if(petBreedName == null || petBreedName.isEmpty()) {
                Toast.makeText(PetMate.this, "Please select Pet Breed.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petBreed.getSelectedView();
                errorText.setError("Please select Pet Breed");
            }
            else if(firstImagePath == null || firstImagePath.isEmpty()) {
                Toast.makeText(PetMate.this, "Please select image of Pet.", Toast.LENGTH_LONG).show();
            }
            else {
                progressDialog = ProgressDialog.show(PetMate.this, "", "Uploading file...", true);
                int selectedGender = genderOfPet.getCheckedRadioButtonId();
                View genderSelected = genderOfPet.findViewById(selectedGender);
                int selectedGenderId = genderOfPet.indexOfChild(genderSelected);
                RadioButton gender = (RadioButton) genderOfPet.getChildAt(selectedGenderId);
                petGender = (String) gender.getText();

                petDescription = descriptionOfPet.getText().toString();

                new UploadToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            }
        }
    }

    private void createPetMateFormSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(PetMate.this, android.R.layout.select_dialog_item){
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
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PetMate.this, R.style.AlertDialogCustom));
        builder.setTitle("Select Image");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    alertDialog.dismiss();
                    if(ActivityCompat.checkSelfPermission(PetMate.this, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission();
                    }
                    else {
                        new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    }
                }
                else if(which == 1) {
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
                Toast.makeText(PetMate.this, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(e.getMessage(), "Error");
            Toast.makeText(PetMate.this, "Camera Crop Error", Toast.LENGTH_LONG).show();
        }
    }

    private void setBitmapToImage(Bitmap imageToShow) {
        if(firstImageOfPetMate.getDrawable() == null) {
            //firstImageOfPet.setLayoutParams(getLayoutParams());
            firstImageOfPetMate.setImageBitmap(imageToShow);
            firstImagePath = cropFile.getAbsolutePath();
            selectImageButton.setText("Select More Images");
        }
        else if(secondImageOfPetMate.getDrawable() == null) {
            //secondImageOfPet.setLayoutParams(getLayoutParams());
            secondImageOfPetMate.setImageBitmap(imageToShow);
            secondImagePath = cropFile.getAbsolutePath();
        }
        else if(thirdImageOfPetMate.getDrawable() == null) {
            //thirdImageOfPet.setLayoutParams(getLayoutParams());
            thirdImageOfPetMate.setImageBitmap(imageToShow);
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
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);

        try {
            startActivityForResult(cropIntent, request_code);
        } catch (Exception e) {

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


        image = File.createTempFile(
                timeStamp,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

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

    public class FetchCategoryListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                petCategoryList = PetCategorySpinnerList.fetchPetCategory(petCategoryList, adapter);
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
                petBreedsList = PetBreedsSpinnerList.fetchPetBreeds(petBreedsList, petCategoryName, adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetMate.this,
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
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetMate.this,
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
            ActivityCompat.requestPermissions(PetMate.this,
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
                Toast.makeText(PetMate.this, "CAMERA permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(PetMate.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }

        else if(requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPetMateFormSelectImageDialogChooser();
            } else {
                Toast.makeText(PetMate.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            try {
                PetMateFormUpload.uploadToRemoteServer(email, petCategoryName, petBreedName, petMateAgeMonthSpinner,petMateAgeYearSpinner, petGender, petDescription, firstImagePath, secondImagePath, thirdImagePath, txtAlternateNo, PetMate.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(PetMate.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

}
