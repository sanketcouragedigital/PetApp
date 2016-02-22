package com.couragedigital.petapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.couragedigital.petapp.Adapter.ModifySpinnerItemsAdapter;
import com.couragedigital.petapp.Connectivity.ModifyPetBreedsSpinnerList;
import com.couragedigital.petapp.Connectivity.ModifyPetCategorySpinnerList;
import com.couragedigital.petapp.Connectivity.MyListingModifyPetMateDetailUpload;
import com.couragedigital.petapp.SessionManager.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyListingModifyPetMateDetails extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    String petCategoryName;
    String petBreedName;
    String petMateAgeMonthSpinner = "0";
    String petMateAgeYearSpinner = "0";
    String petMateGender = "";
    String petMateDescription;
    String emailOfUser;
    Integer id;

    TextView petMateSelectedCategory;
    TextView petMateSelectedBreed;
    TextView petMatechangeBreed;
    TextView petMatechangeCategory;
    TextView petMatecurrentAge;
    Spinner petMateMyListingEditPetCategory;
    Spinner petMateMyListingEditPetBreed;
    Spinner petMateMyListingEditPetAgeInMonths;
    Spinner petMateMyListingEditPetAgeInYears;
    RadioGroup petMateMyListingEditGenderOfPet;
    RadioButton petMateMyListingEditGenderMale;
    RadioButton petMateMyListingEditGenderFemale;
    EditText petMateMyListingEditDescriptionOfPet;
    FloatingActionButton petMateMyListingUploadFabButton;

    private ProgressDialog progressDialog = null;
    private List<String> petCategoryList = new ArrayList<String>();
    private List<String> petBreedsList = new ArrayList<String>();
    private ModifySpinnerItemsAdapter adapter;
    String[] petCategoryArrayList;
    String[] petBreedArrayList;

    private  List<String> petAgeListInMonth = new ArrayList<String>();
    private List<String> petAgeListInYear = new ArrayList<String>();
    String[] stringArrayListForMonth;
    String[] stringArrayListForYear;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylistingeditypetmatedetails);


        SessionManager sessionManager = new SessionManager(MyListingModifyPetMateDetails.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        emailOfUser = user.get(SessionManager.KEY_EMAIL);

        getData();
        petMateSelectedCategory = (TextView) this.findViewById(R.id.myListingPetMateSelectedCategory);
        petMateSelectedBreed = (TextView) this.findViewById(R.id.myListingPetMateSelectedBreed);
        petMatechangeCategory = (TextView) this.findViewById(R.id.myListingPetMateChangeCategory);
        petMatechangeBreed = (TextView) this.findViewById(R.id.myListingPetMateChangeBreed);
        petMateMyListingEditPetCategory = (Spinner) this.findViewById(R.id.myListingEditPetMateCategory);
        petMateMyListingEditPetBreed = (Spinner) this.findViewById(R.id.myListingEditPetMateBreed);
        petMatecurrentAge = (TextView) this.findViewById(R.id.myListingPetMateCurrentAge);
        petMateMyListingEditPetAgeInMonths = (Spinner) this.findViewById(R.id.myListingEditPetMateAgeInMonths);
        petMateMyListingEditPetAgeInYears = (Spinner) this.findViewById(R.id.myListingEditPetMateAgeInYears);
        petMateMyListingEditGenderOfPet = (RadioGroup) this.findViewById(R.id.myListingEditPetMateGenderRadioGroup);
        petMateMyListingEditGenderMale = (RadioButton) this.findViewById(R.id.myListingEditPetMateGenderMale);
        petMateMyListingEditGenderFemale = (RadioButton) this.findViewById(R.id.myListingEditPetMateGenderFemale);
        petMateMyListingEditDescriptionOfPet = (EditText) this.findViewById(R.id.myListingEditPetMateDescription);
        petMateMyListingUploadFabButton = (FloatingActionButton) this.findViewById(R.id.myListingPetMateSubmitFloatingButton);
        fillData();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.myListingPetMateSubmitFloatingButton) {
            progressDialog = ProgressDialog.show(MyListingModifyPetMateDetails.this, "", "Uploading file...", true);
            new UploadModifiedPetMateDataToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    private void getData() {

        Intent intent = getIntent();
        if (null != intent) {
            id = intent.getIntExtra("ID",0);
            petCategoryName = intent.getStringExtra("PET_MATE_CATEGORY");
            petBreedName = intent.getStringExtra("PET_MATE_BREED");
            petMateAgeMonthSpinner = intent.getStringExtra("PET_MATE_IN_MONTH");
            petMateAgeYearSpinner = intent.getStringExtra("PET_MATE_IN_YEAR");
            petMateGender = intent.getStringExtra("PET_MATE_GENDER");
            petMateDescription = intent.getStringExtra("PET_MATE_DESCRIPTION");
        }
    }

    private void fillData() {

        petMateSelectedCategory.setText("Current Category : " + petCategoryName);
        petMateSelectedBreed.setText("Current Breed : " + petBreedName);
        petMatechangeCategory.setText("Change To : ");
        petMatechangeBreed.setText("Change To : ");
        petMatecurrentAge.setText("Current Age : " + petMateAgeMonthSpinner + " Month" + " , " + petMateAgeYearSpinner + " Years ");

        petMateMyListingEditDescriptionOfPet.setText(petMateDescription);
        GenarateSpinerForAge();

        petCategoryArrayList = new String[]{
                "Select Category"
        };
        petCategoryList = new ArrayList<>(Arrays.asList(petCategoryArrayList));
        adapter = new ModifySpinnerItemsAdapter(this, R.layout.modifyspinneritem, petCategoryList);

        new FetchCategoryListFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petMateMyListingEditPetCategory.setAdapter(adapter);

        petMateMyListingEditPetCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    petCategoryName = (String) parent.getItemAtPosition(position);

                    new FetchBreedListCategoryWiseFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

                    petMateMyListingEditPetBreed.setSelection(petBreedsList.indexOf(0));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        petBreedArrayList = new String[]{
                "Select Breed"
        };
        petBreedsList = new ArrayList<>(Arrays.asList(petBreedArrayList));
        new FetchBreedListCategoryWiseFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        adapter = new ModifySpinnerItemsAdapter(this, R.layout.modifyspinneritem, petBreedsList);
        adapter.notifyDataSetChanged();
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petMateMyListingEditPetBreed.setAdapter(adapter);
        petMateMyListingEditPetBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        if (petMateGender.equals("Male")) {
            // myListingEditGenderMale.setChecked(true);
            petMateMyListingEditGenderOfPet.check(R.id.myListingEditPetMateGenderMale);
            petMateMyListingEditGenderOfPet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.myListingEditPetMateGenderMale) {
                        petMateMyListingEditGenderFemale.setChecked(false);
                        petMateGender = "Male";

                    } else {
                        petMateMyListingEditGenderFemale.setChecked(true);
                        petMateGender = "Female";

                    }
                }
            });

        } else {
            //  myListingEditGenderFemale.setChecked(true);
            petMateMyListingEditGenderOfPet.check(R.id.myListingEditPetMateGenderFemale);
            petMateMyListingEditGenderOfPet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.myListingEditPetMateGenderFemale) {
                        petMateMyListingEditGenderMale.setChecked(false);
                        petMateGender = "Female";
                    } else {
                        petMateMyListingEditGenderMale.setChecked(true);
                        petMateGender = "Male";

                    }
                }
            });
        }


        petMateMyListingEditDescriptionOfPet.addTextChangedListener(descriptionChangeListener);
        petMateMyListingUploadFabButton.setOnClickListener(this);

    }

    public void GenarateSpinerForAge() {
        stringArrayListForMonth = new String[]{
                "Months"
        };
        petAgeListInMonth = new ArrayList<>(Arrays.asList(stringArrayListForMonth));
        for (int i = 0; i <= 11; i++) {
            String j = String.valueOf(i);
            petAgeListInMonth.add(j);
        }
        ModifySpinnerItemsAdapter monthAdapter = new ModifySpinnerItemsAdapter(this, R.layout.modifyspinneritem, petAgeListInMonth);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petMateMyListingEditPetAgeInMonths.setAdapter(monthAdapter);
        petMateMyListingEditPetAgeInMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position > 0) {
                    petMateAgeMonthSpinner = parent.getItemAtPosition(position).toString();
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
        for (int i = 0; i <= 11; i++) {
            String j = String.valueOf(i);
            petAgeListInYear.add(j);
        }
        ModifySpinnerItemsAdapter yearAdapter = new ModifySpinnerItemsAdapter(this, R.layout.spinneritem, petAgeListInYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petMateMyListingEditPetAgeInYears.setAdapter(yearAdapter);
        petMateMyListingEditPetAgeInYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position > 0) {
                    petMateAgeYearSpinner = parent.getItemAtPosition(position).toString();
                }
                //petAgeYearSpinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private TextWatcher descriptionChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            new GetPetDescription().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };

    public class GetPetDescription extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        petMateDescription = petMateMyListingEditDescriptionOfPet.getText().toString();
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
                petCategoryList = ModifyPetCategorySpinnerList.fetchPetCategory(petCategoryList, adapter);
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
                petBreedsList = ModifyPetBreedsSpinnerList.fetchPetBreeds(petBreedsList, petCategoryName, adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    public class UploadModifiedPetMateDataToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                MyListingModifyPetMateDetailUpload.uploadToRemoteServer(petCategoryName, petBreedName, petMateAgeMonthSpinner, petMateAgeYearSpinner, petMateGender, petMateDescription, emailOfUser, id, MyListingModifyPetMateDetails.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(MyListingModifyPetMateDetails.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }
}