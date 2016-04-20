package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.couragedigital.peto.Adapter.ModifySpinnerItemsAdapter;
import com.couragedigital.peto.Connectivity.ModifyPetBreedsSpinnerList;
import com.couragedigital.peto.Connectivity.ModifyPetCategorySpinnerList;
import com.couragedigital.peto.Connectivity.MyListingModifyPetDetailUpload;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SessionManager.SessionManager;

import java.util.*;


public class MyListingModifyPetDetails extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {


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
    String petListingType;
    String emailOfUser;
    Integer id;

    TextView selectedCategory;
    TextView selectedBreed;
    TextView changeBreed;
    TextView changeCategory;
    TextView currentAge;
    Spinner myListingEditPetCategory;
    Spinner myListingEditPetBreed;
    Spinner myListingEditPetAgeInMonths;
    Spinner myListingEditPetAgeInYears;
    RadioGroup myListingEditGenderOfPet;
    RadioButton myListingEditGenderMale;
    RadioButton myListingEditGenderFemale;
    EditText myListingEditDescriptionOfPet;
    EditText otherBreed;
    RadioGroup myListingEditGiveAwayType;
    RadioButton myListingEditPetsell;
    RadioButton myListingEditAdoptionOfPet;
    EditText myListingEditPriceOfPet;
    FloatingActionButton myListingUploadFabButton;

    private List<String> petCategoryList = new ArrayList<String>();
    private List<String> petBreedsList = new ArrayList<String>();
    private ModifySpinnerItemsAdapter adapter;
    public String[] petCategoryArrayList;
    public String[] petBreedArrayList;

    private List<String> petAgeListInMonth = new ArrayList<String>();
    private List<String> petAgeListInYear = new ArrayList<String>();
    String[] stringArrayListForMonth;
    String[] stringArrayListForYear;

    private ProgressDialog progressDialog = null;
    private long TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylistingeditpetdetails);

        SessionManager sessionManager = new SessionManager(MyListingModifyPetDetails.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        emailOfUser = user.get(SessionManager.KEY_EMAIL);


        getData();
        //selectedCategory = (TextView) this.findViewById(R.id.myListingPetListSelectedCategory);
        //selectedBreed = (TextView) this.findViewById(R.id.myListingPetListSelectedBreed);
        changeCategory = (TextView) this.findViewById(R.id.myListingPetListChangeCategory);
        changeBreed = (TextView) this.findViewById(R.id.myListingPetListChangeBreed);
        myListingEditPetCategory = (Spinner) this.findViewById(R.id.myListingEditPetCategory);
        myListingEditPetBreed = (Spinner) this.findViewById(R.id.myListingEditPetBreed);
        //ageOfPet = (EditText) this.findViewById(R.id.ageOfPet);
        //currentAge = (TextView) this.findViewById(R.id.myListingPetListCurrentAge);
        myListingEditPetAgeInMonths = (Spinner) this.findViewById(R.id.myListingEditPetAgeInMonths);
        myListingEditPetAgeInYears = (Spinner) this.findViewById(R.id.myListingEditPetAgeInYears);
        myListingEditGenderOfPet = (RadioGroup) this.findViewById(R.id.myListingEditPetGenderRadioGroup);
        myListingEditGenderMale = (RadioButton) this.findViewById(R.id.myListingEditPetGenderMale);
        myListingEditGenderFemale = (RadioButton) this.findViewById(R.id.myListingEditPetGenderFemale);
        myListingEditPriceOfPet = (EditText) this.findViewById(R.id.myListingEditPriceOfPet);
        myListingEditGiveAwayType = (RadioGroup) this.findViewById(R.id.myListingEditPetGiveAwayOfPet);
        myListingEditAdoptionOfPet = (RadioButton) findViewById(R.id.myListingEditPetForAdoption);
        myListingEditPetsell = (RadioButton) findViewById(R.id.myListingEditPetSell);
        otherBreed = (EditText) findViewById(R.id.otherBreedtxtMyListingPet);
        myListingEditDescriptionOfPet = (EditText) this.findViewById(R.id.myListingEditPetDescriptionOfPet);
        myListingUploadFabButton = (FloatingActionButton) this.findViewById(R.id.myListingEditPetFormSubmitFab);
        fillData();
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
        if (v.getId() == R.id.myListingEditPetFormSubmitFab) {
            progressDialog = ProgressDialog.show(MyListingModifyPetDetails.this, "", "Uploading file...", true);
            new UploadModifiedDataToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    }

    private void getData() {

        Intent intent = getIntent();
        if (null != intent) {
            id = intent.getIntExtra("ID",0);
            petCategoryName = intent.getStringExtra("PET_CATEGORY");
            petBreedName = intent.getStringExtra("PET_BREED");
            petAgeMonthSpinner = intent.getStringExtra("PET_AGE_IN_MONTH");
            petAgeYearSpinner = intent.getStringExtra("PET_AGE_IN_YEAR");
            petGender = intent.getStringExtra("PET_GENDER");
            petDescription = intent.getStringExtra("PET_DESCRIPTION");
            petListingType = intent.getStringExtra("PET_LISTING_TYPE");
        }
    }

    private void fillData() {

//        selectedCategory.setText("Current Category : " + petCategoryName);
//        selectedBreed.setText("Current Breed : " + petBreedName);
        changeCategory.setText("Category : ");
        changeBreed.setText("Breed : ");
//        currentAge.setText("Current Age : " + petAgeMonthSpinner + " Month" + " , " + petAgeYearSpinner + " Years ");

        myListingEditDescriptionOfPet.setText(petDescription);
        GenarateSpinerForAge();

        petCategoryArrayList = new String[]{
                petCategoryName
        };
        petCategoryList = new ArrayList<>(Arrays.asList(petCategoryArrayList));
        adapter = new ModifySpinnerItemsAdapter(this, R.layout.spinneritem, petCategoryList);

        new FetchCategoryListFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myListingEditPetCategory.setAdapter(adapter);

        myListingEditPetCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    petCategoryName = (String) parent.getItemAtPosition(position);

                    new FetchBreedListCategoryWiseFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);

                    myListingEditPetBreed.setSelection(petBreedsList.indexOf(0));
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
        adapter = new ModifySpinnerItemsAdapter(this, R.layout.spinneritem, petBreedsList);
        adapter.notifyDataSetChanged();
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myListingEditPetBreed.setAdapter(adapter);
        myListingEditPetBreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    petBreedName = (String) parent.getItemAtPosition(position);
                    if(petBreedName.equals("Other")){
                        otherBreed.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (petGender.equals("Male")) {
            // myListingEditGenderMale.setChecked(true);
            myListingEditGenderOfPet.check(R.id.myListingEditPetGenderMale);
            myListingEditGenderOfPet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.myListingEditPetGenderMale) {
                        myListingEditGenderFemale.setChecked(false);
                        petGender = "Male";

                    } else {
                        myListingEditGenderFemale.setChecked(true);
                        petGender = "Female";

                    }
                }
            });

        } else {
            //  myListingEditGenderFemale.setChecked(true);
            myListingEditGenderOfPet.check(R.id.myListingEditPetGenderFemale);
            myListingEditGenderOfPet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.myListingEditPetGenderFemale) {
                        myListingEditGenderMale.setChecked(false);
                        petGender = "Female";
                    } else {
                        myListingEditGenderMale.setChecked(true);
                        petGender = "Male";

                    }
                }
            });
        }

        if (petListingType.equals("For Adoption")) {
            petPrice = 0;
            myListingEditGiveAwayType.check(R.id.myListingEditPetForAdoption);
            myListingEditGiveAwayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int i) {
                    if (i == R.id.myListingEditPetForAdoption) {
                        petPrice = 0;
                        myListingEditPriceOfPet.setEnabled(false);
                        petAdoption = "For Adoption";

                    } else {
                        petAdoption = "";
                        myListingEditPriceOfPet.setEnabled(true);
                    }
                    if (myListingEditPetsell.getError() != null) {
                        myListingEditPetsell.setError(null);
                    }
                }
            });
        } else {
            myListingEditGiveAwayType.check(R.id.myListingEditPetSell);
            myListingEditPriceOfPet.setEnabled(true);
            myListingEditPriceOfPet.setText(petListingType);

            myListingEditGiveAwayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int i) {
                    if (i == R.id.myListingEditPetForAdoption) {
                        myListingEditPriceOfPet.setText("");
                        petPrice = 0;
                        myListingEditPriceOfPet.setEnabled(false);
                        petAdoption = "For Adoption";

                    } else {
                        petAdoption = "";
                        myListingEditPriceOfPet.setEnabled(true);
                        myListingEditPriceOfPet.setText(petListingType);
                    }
                }
            });
        }
        otherBreed.addTextChangedListener(otherBreedNameChangeListener);
        myListingEditPriceOfPet.addTextChangedListener(priceChangeListener);
        myListingEditDescriptionOfPet.addTextChangedListener(descriptionChangeListener);
        myListingUploadFabButton.setOnClickListener(this);
    }
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
                        takePetPrice = myListingEditPriceOfPet.getText().toString();
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
                        petDescription = myListingEditDescriptionOfPet.getText().toString();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void GenarateSpinerForAge() {
        stringArrayListForMonth = new String[]{
                petAgeMonthSpinner+" "+"Months"
        };
        petAgeListInMonth = new ArrayList<>(Arrays.asList(stringArrayListForMonth));
        for (int i = 0; i <= 11; i++) {
            String j = String.valueOf(i);
            petAgeListInMonth.add(j);
        }
        ModifySpinnerItemsAdapter monthAdapter = new ModifySpinnerItemsAdapter(this, R.layout.spinneritem, petAgeListInMonth);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myListingEditPetAgeInMonths.setAdapter(monthAdapter);
        myListingEditPetAgeInMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position > 0) {
                    petAgeMonthSpinner = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        stringArrayListForYear = new String[]{
                petAgeYearSpinner+" "+"Years"
        };
        petAgeListInYear = new ArrayList<>(Arrays.asList(stringArrayListForYear));
        for (int i = 0; i <= 11; i++) {
            String j = String.valueOf(i);
            petAgeListInYear.add(j);
        }
        ModifySpinnerItemsAdapter yearAdapter = new ModifySpinnerItemsAdapter(this, R.layout.spinneritem, petAgeListInYear);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myListingEditPetAgeInYears.setAdapter(yearAdapter);
        myListingEditPetAgeInYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                if (position > 0) {
                    petAgeYearSpinner = parent.getItemAtPosition(position).toString();
                }
                //petAgeYearSpinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public class FetchCategoryListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                ModifyPetCategorySpinnerList modifyPetCategorySpinnerList = new ModifyPetCategorySpinnerList(MyListingModifyPetDetails.this);
                modifyPetCategorySpinnerList.fetchPetCategory(petCategoryList,petCategoryName, adapter);
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
                ModifyPetBreedsSpinnerList modifyPetBreedsSpinnerList = new ModifyPetBreedsSpinnerList(MyListingModifyPetDetails.this);
                modifyPetBreedsSpinnerList.fetchPetBreeds(petBreedsList, petCategoryName, petBreedName,adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class UploadModifiedDataToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                MyListingModifyPetDetailUpload.uploadToRemoteServer(petCategoryName, petBreedName, petAgeMonthSpinner, petAgeYearSpinner, petGender, petDescription, petAdoption, petPrice, emailOfUser, id, MyListingModifyPetDetails.this);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(MyListingModifyPetDetails.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = MyListingModifyPetDetails.this.getPackageManager();
        ComponentName component = new ComponentName(MyListingModifyPetDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = MyListingModifyPetDetails.this.getPackageManager();
        ComponentName component = new ComponentName(MyListingModifyPetDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}