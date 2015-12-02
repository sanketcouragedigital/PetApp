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

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.net.Uri;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.couragedigital.petapp.Connectivity.FormUploadConnectivity;
import com.couragedigital.petapp.Connectivity.PetBreedsSpinnerListConnectivity;
import com.couragedigital.petapp.Connectivity.PetCategorySpinnerListConnectivity;
import com.couragedigital.petapp.adapter.SpinnerItemsAdapter;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;


public class FormUpload extends BaseActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;

    private ProgressDialog progressDialog = null;
    private AlertDialog alertDialog;

    Spinner petCategory;
    Spinner petBreed;
    EditText ageOfPet;
    RadioGroup genderOfPet;
    RadioButton genderSelected;
    EditText descriptionOfPet;
    CheckBox adoptionOfPet;
    CheckBox giveAwayOfPet;
    EditText priceOfPet;
    Button selectImageButton;
    ImageView imageOfPet;
    Button uploadButton;

    String petCategoryName;
    String petBreedName;
    Integer petAge;
    String petGender;
    String petDescription;
    String petAdoption = "";
    String petGiveAway = "";
    Integer petPrice;
    String currentPhotoPath;

    private List<String> petCategoryList = new ArrayList<String>();
    private List<String> petBreedsList = new ArrayList<String>();
    private SpinnerItemsAdapter adapter;
    String[] petCategoryArrayList;
    String[] petBreedArrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formupload);

        petCategory = (Spinner) this.findViewById(R.id.petCategory);
        petBreed = (Spinner) this.findViewById(R.id.petBreed);
        ageOfPet = (EditText) this.findViewById(R.id.ageOfPet);
        genderOfPet = (RadioGroup) this.findViewById(R.id.genderOfPet);
        descriptionOfPet = (EditText) this.findViewById(R.id.descriptionOfPet);
        adoptionOfPet = (CheckBox) this.findViewById(R.id.forAdoptionOfPet);
        giveAwayOfPet = (CheckBox) this.findViewById(R.id.giveAwayOfPet);
        priceOfPet = (EditText) this.findViewById(R.id.priceOfPet);
        selectImageButton = (Button) this.findViewById(R.id.selectImage);
        imageOfPet = (ImageView) this.findViewById(R.id.imageOfPet);
        uploadButton = (Button) this.findViewById(R.id.uploadButton);

        petCategoryArrayList = new String[]{
                "Select Pet Category"
        };
        petCategoryList = new ArrayList<>(Arrays.asList(petCategoryArrayList));
        adapter = new SpinnerItemsAdapter(this, R.layout.spinner_item, petCategoryList);

        petCategoryList = PetCategorySpinnerListConnectivity.fetchPetCategory(petCategoryList, adapter);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petCategory.setAdapter(adapter);

        petCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    petCategoryName = (String) parent.getItemAtPosition(position);

                    petBreedsList = PetBreedsSpinnerListConnectivity.fetchPetBreeds(petBreedsList, petCategoryName, adapter);

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
        adapter = new SpinnerItemsAdapter(this, R.layout.spinner_item, petBreedsList);
        adapter.notifyDataSetChanged();
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petBreed.setAdapter(adapter);
        petBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    petBreedName = (String) parent.getItemAtPosition(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selectImageButton.setOnClickListener(this);
        ageOfPet.addTextChangedListener(textChangeListener);
        adoptionOfPet.setOnCheckedChangeListener(checkBoxListener);
        giveAwayOfPet.setOnCheckedChangeListener(checkBoxListener);
        priceOfPet.addTextChangedListener(textChangeListener);
        uploadButton.setOnClickListener(this);
    }

    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String takePetAge = ageOfPet.getText().toString();
            String takePetPrice = priceOfPet.getText().toString();
            if (!takePetAge.equals("")) {
                Integer takePetAgeInInteger = Integer.parseInt(takePetAge);
                if (takePetAgeInInteger >= 100) {
                    ageOfPet.setError("Please enter valid age");
                    ageOfPet.setText(null);
                } else {
                    petAge = takePetAgeInInteger;
                }
            } else if (takePetAge.equals("")) {
                petAge = 0;
            } else if (!takePetPrice.equals("")) {
                Integer takePetPriceInInteger = Integer.parseInt(takePetPrice);
                if (takePetPriceInInteger > 0) {
                    petPrice = takePetPriceInInteger;
                }
            } else if (takePetPrice.equals("")) {
                petPrice = 0;
            }
        }
    };

    private OnCheckedChangeListener checkBoxListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView.getId() == R.id.forAdoptionOfPet) {
                    giveAwayOfPet.setChecked(false);
                    priceOfPet.setEnabled(false);
                    petPrice = 0;
                    petGiveAway = "";
                    petAdoption = adoptionOfPet.getText().toString();
                } else if (buttonView.getId() == R.id.giveAwayOfPet) {
                    adoptionOfPet.setChecked(false);
                    priceOfPet.setEnabled(false);
                    petPrice = 0;
                    petAdoption = "";
                    petGiveAway = giveAwayOfPet.getText().toString();
                }
            } else {
                priceOfPet.setEnabled(true);
                petAdoption = "";
                petGiveAway = "";
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectImage) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item) {
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
            adapter.add("Take from Camera");
            adapter.add("Select from Gallery");
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            builder.setTitle("Select Image");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
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
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }
                        }
                    } else if (which == 1) {
                        // Create intent to Open Image applications like Gallery, Google Photos
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_REQUEST);
                    }
                }
            });
            alertDialog = builder.create();

            alertDialog.show();
        } else if (v.getId() == R.id.uploadButton) {

            Integer petAgeInInteger = null;
            Integer petPriceInInteger = null;

            if (petCategoryName == null) {
                Toast.makeText(FormUpload.this, "Please select Pet Category.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petCategory.getSelectedView();
                errorText.setError("Please select Pet Category");
            } else if (petBreedName == null) {
                Toast.makeText(FormUpload.this, "Please select Pet Breed.", Toast.LENGTH_LONG).show();
                TextView errorText = (TextView) petBreed.getSelectedView();
                errorText.setError("Please select Pet Breed");
            } else if (genderOfPet.getCheckedRadioButtonId() == -1) {
                Toast.makeText(FormUpload.this, "Please select gender.", Toast.LENGTH_LONG).show();
                genderSelected = (RadioButton) findViewById(R.id.genderFemale);
                genderSelected.setError("Please select gender");
            } else if (Objects.equals(petAdoption, "") && Objects.equals(petGiveAway, "") && priceOfPet.getText().toString().equals("")) {
                Toast.makeText(FormUpload.this, "Please select the adoption or giveaway Or fill the price of pet.", Toast.LENGTH_LONG).show();
            } else if (currentPhotoPath == null) {
                Toast.makeText(FormUpload.this, "Please select image of Pet.", Toast.LENGTH_LONG).show();
            } else {
                progressDialog = ProgressDialog.show(FormUpload.this, "", "Uploading file...", true);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
                    File image = new File(currentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap imageToShow = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

                    imageOfPet.setLayoutParams(getLayoutParams());
                    imageOfPet.setImageBitmap(imageToShow);
                } else if (requestCode == GALLERY_REQUEST) {
                    Uri uri = intent.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    currentPhotoPath = cursor.getString(columnIndex); // returns null
                    cursor.close();

                    File image = new File(currentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap imageToShow = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);

                    imageOfPet.setLayoutParams(getLayoutParams());
                    imageOfPet.setImageBitmap(imageToShow);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(FormUpload.this, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(e.getMessage(), "Error");
        }
    }

    private RelativeLayout.LayoutParams getLayoutParams() {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);
        layoutParams.addRule(RelativeLayout.BELOW, R.id.selectImage);
        layoutParams.addRule(RelativeLayout.ALIGN_END, R.id.selectImage);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        return layoutParams;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.e("Getpath", "Cool" + currentPhotoPath);
        return image;
    }

    public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                int responseFromServer = FormUploadConnectivity.uploadToRemoteServer(petCategoryName, petBreedName, petAge, petGender, petDescription, petAdoption, petGiveAway, petPrice, currentPhotoPath);
                if (responseFromServer == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(FormUpload.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(FormUpload.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }
}
