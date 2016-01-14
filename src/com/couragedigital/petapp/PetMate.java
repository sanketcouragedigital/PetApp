package com.couragedigital.petapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

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

import android.support.design.widget.FloatingActionButton;
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

    private ProgressDialog progressDialog = null;

    public AlertDialog alertDialog;
    public ArrayAdapter<String> dialogAdapter;

    Spinner petCategory;
    Spinner petBreed;
    EditText ageOfPet;
    RadioGroup genderOfPet;
    RadioButton genderSelected;
    EditText descriptionOfPet;

    Button selectImageButton;
    ImageView firstImageOfPetMate;
    ImageView secondImageOfPetMate;
    ImageView thirdImageOfPetMate;
    FloatingActionButton uploadButton;
    String email;
    String petCategoryName;
    String petBreedName;
    Integer petAge;
    String petGender;
    String petDescription;
    String currentPhotoPath;

    SessionManager sessionManager;

    private List<String> petCategoryList = new ArrayList<String>();
    private List<String> petBreedsList = new ArrayList<String>();
    private SpinnerItemsAdapter adapter;
    String[] petCategoryArrayList;
    String[] petBreedArrayList;

    Bitmap imageToShow;
    final int PIC_CAMERA_CROP = 3;
    final int PIC_GALLERY_CROP = 4;
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
        ageOfPet = (EditText) this.findViewById(R.id.ageOfPetMate);
        genderOfPet = (RadioGroup) this.findViewById(R.id.genderOfPetMate);
        descriptionOfPet = (EditText) this.findViewById(R.id.descriptionOfPetMate);
        selectImageButton = (Button) this.findViewById(R.id.selectImagePetMate);
        firstImageOfPetMate = (ImageView) this.findViewById(R.id.firstImageOfPet);
        secondImageOfPetMate = (ImageView) this.findViewById(R.id.secondImageOfPet);
        thirdImageOfPetMate = (ImageView) this.findViewById(R.id.thirdImageOfPet);
        uploadButton = (FloatingActionButton) this.findViewById(R.id.petMateFormSubmitFab);

        petCategoryArrayList = new String[]{
                "Select Pet Category"
        };
        petCategoryList = new ArrayList<>(Arrays.asList(petCategoryArrayList));
        adapter = new SpinnerItemsAdapter(this, R.layout.spinneritem, petCategoryList);

        petCategoryList = PetCategorySpinnerList.fetchPetCategory(petCategoryList, adapter);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petCategory.setAdapter(adapter);

        petCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    petCategoryName = (String) parent.getItemAtPosition(position);

                    petBreedsList = PetBreedsSpinnerList.fetchPetBreeds(petBreedsList, petCategoryName, adapter);

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


        selectImageButton.setOnClickListener(this);
        ageOfPet.addTextChangedListener(ageChangeListener);
        uploadButton.setOnClickListener(this);
    }

    private TextWatcher ageChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String takePetAge = ageOfPet.getText().toString();
            if(!takePetAge.equals("")) {
                Integer takePetAgeInInteger = Integer.parseInt(takePetAge);
                if(takePetAgeInInteger >= 100) {
                    ageOfPet.setError("Please enter valid age");
                    ageOfPet.setText(null);
                }
                else {
                    petAge = takePetAgeInInteger;
                }
            }
            else if(takePetAge.equals("")) {
                petAge = 0;
            }
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
                        }
                        else if(which == 1) {
                            alertDialog.dismiss();
                            // Create intent to Open Image applications like Gallery, Google Photos
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY_REQUEST);
                        }
                    }
                });
                alertDialog = builder.create();

                alertDialog.show();
            }
        }
        else if(v.getId() == R.id.petMateFormSubmitFab) {

            Integer petAgeInInteger = null;

            if (petCategoryName == null) {
                Toast.makeText(PetMate.this, "Please select Pet Category.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petCategory.getSelectedView();
                errorText.setError("Please select Pet Category");
            }
            else if(petBreedName == null) {
                Toast.makeText(PetMate.this, "Please select Pet Breed.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petBreed.getSelectedView();
                errorText.setError("Please select Pet Breed");
            }
            else if(genderOfPet.getCheckedRadioButtonId() == -1) {
                Toast.makeText(PetMate.this, "Please select gender.", Toast.LENGTH_LONG).show();
                genderSelected = (RadioButton) findViewById(R.id.genderFemale);
                genderSelected.setError("Please select gender");
            }
            else if(firstImagePath == null) {
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

                new UploadToServer().execute();
            }
        }
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

    private RelativeLayout.LayoutParams getLayoutParams() {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.selectImagePetMate);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        return layoutParams;
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

    public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {
            try {
                int responseFromServer = PetMateFormUpload.uploadToRemoteServer(email, petCategoryName, petBreedName, petAge, petGender, petDescription, firstImagePath, secondImagePath, thirdImagePath);
                if(responseFromServer == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(PetMate.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
