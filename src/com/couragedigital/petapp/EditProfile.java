package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.couragedigital.petapp.Connectivity.FetchUserDetailsForEditProfile;
import com.couragedigital.petapp.Connectivity.MyListingPetFetchList;
import com.couragedigital.petapp.Connectivity.RegisterToServer;
import com.couragedigital.petapp.Connectivity.SaveEditProfile;
import com.couragedigital.petapp.SHA_256.PasswordConverter;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.Singleton.EditProfileDetailsInstance;

import java.util.HashMap;

public class EditProfile extends BaseActivity  implements View.OnClickListener{
    private static EditText txt_name;
    private static EditText txt_buildingname;
    private static EditText txt_area;
    private static EditText txt_city;
    private static EditText txt_mobileno;
    private static EditText txt_email;
    private static EditText txt_confirmpassword;
    private static Button btn_signup;
    private static Button btn_cancel;



    String name;
    String buildingname;
    String area;
    String city;
    String mobileno;
    String email;
    String oldEmail;
    String confirmpassword;
    String oldconfirmpassword;
    String convertedconfirmpassword;
    ProgressDialog progressDialog = null;
    String conf_password;

    View v;
    String urlForFetch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);


        SessionManager sessionManager = new SessionManager(EditProfile.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        oldEmail = user.get(SessionManager.KEY_EMAIL);

        EditProfileDetailsInstance editProfileDetailsInstance= new EditProfileDetailsInstance();
        name=editProfileDetailsInstance.getName();
        buildingname=editProfileDetailsInstance.getBuildingName();
        area=editProfileDetailsInstance.getArea();
        city=editProfileDetailsInstance.getCity();
        mobileno=editProfileDetailsInstance.getMobileNo();
        email=editProfileDetailsInstance.getEmail();
        oldconfirmpassword=editProfileDetailsInstance.getPassword();

//        try{
//            PasswordConverter passwordConverter=new PasswordConverter();
//            convertedconfirmpassword = passwordConverter.ConvertPassword(oldconfirmpassword);
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        txt_name = (EditText) findViewById(R.id.txtNameEditProfile);
        txt_buildingname = (EditText) findViewById(R.id.txtBuildingnameEditProfile);
        txt_area = (EditText) findViewById(R.id.txtAreaEditProfile);
        txt_city = (EditText) findViewById(R.id.txtCityEditProfile);
        txt_mobileno = (EditText) findViewById(R.id.txtMobileNoEditProfile);
        txt_email = (EditText) findViewById(R.id.txtEmailEditProfile);
        txt_confirmpassword = (EditText) findViewById(R.id.txtConfirmPasswordEditProfile);
        btn_signup = (Button) findViewById(R.id.btnSignUpEditProfile);
        btn_cancel = (Button) findViewById(R.id.btnCancelEditProfile);

        txt_name.setText(name);
        txt_buildingname.setText(buildingname);
        txt_area.setText(area);
        txt_city.setText(city);
        txt_mobileno.setText(mobileno);
        txt_email.setText(email);
        txt_confirmpassword.setText(oldconfirmpassword);

        btn_signup.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }
    public static boolean isValidEmail(String emailForValidation) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailForValidation).matches();

    }
    private boolean isValidMobile(String mobilenoValidation)
    {
        return android.util.Patterns.PHONE.matcher(mobilenoValidation).matches();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignUpEditProfile) {
            name = txt_name.getText().toString();
            buildingname = txt_buildingname.getText().toString();
            area = txt_area.getText().toString();
            city = txt_city.getText().toString();
            mobileno = txt_mobileno.getText().toString();
            email = txt_email.getText().toString();
            conf_password = txt_confirmpassword.getText().toString();

            if (txt_name.getText().toString().isEmpty() || txt_buildingname.getText().toString().isEmpty() || txt_area.getText().toString().isEmpty() || txt_city.getText().toString().isEmpty() || txt_mobileno.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || txt_confirmpassword.getText().toString().isEmpty() ) {
                Toast.makeText(EditProfile.this, "All Details are neccessory", Toast.LENGTH_LONG).show();
            } else {
                if( (mobileno.length() < 6 || mobileno.length() > 13) ) {
                    Toast.makeText(EditProfile.this, "Mobile No is not valid", Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmail(email)) {
                    Toast.makeText(EditProfile.this, "Email is not valid", Toast.LENGTH_LONG).show();
                }
                try {
                    PasswordConverter passwordConverter=new PasswordConverter();
                    confirmpassword = passwordConverter.ConvertPassword(conf_password);
                    SaveEditProfile saveEditProfile = new SaveEditProfile(EditProfile.this);
                    saveEditProfile.uploadEditedDetails(name, buildingname, area, city, mobileno, email,oldEmail, confirmpassword);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(EditProfile.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        } else if (v.getId() == R.id.btnCancelEditProfile) {
            Intent intent = new Intent(getApplicationContext(), Index.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);

        }
    }

}
