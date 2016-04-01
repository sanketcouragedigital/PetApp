package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.couragedigital.peto.Connectivity.SaveEditProfile;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SHA_256.PasswordConverter;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.EditProfileDetailsInstance;

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


    CheckBox labelForOldPassword;
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
    private long TIME = 5000;

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

        txt_name = (EditText) findViewById(R.id.txtNameEditProfile);
        txt_buildingname = (EditText) findViewById(R.id.txtBuildingnameEditProfile);
        txt_area = (EditText) findViewById(R.id.txtAreaEditProfile);
        txt_city = (EditText) findViewById(R.id.txtCityEditProfile);
        txt_mobileno = (EditText) findViewById(R.id.txtMobileNoEditProfile);
        txt_email = (EditText) findViewById(R.id.txtEmailEditProfile);
        txt_confirmpassword = (EditText) findViewById(R.id.txtConfirmPasswordEditProfile);
        btn_signup = (Button) findViewById(R.id.btnSignUpEditProfile);
        btn_cancel = (Button) findViewById(R.id.btnCancelEditProfile);
        labelForOldPassword =(CheckBox) this.findViewById(R.id.oldPasswordCheckBox);

        txt_name.setText(name);
        txt_buildingname.setText(buildingname);
        txt_area.setText(area);
        txt_city.setText(city);
        txt_mobileno.setText(mobileno);
        txt_email.setText(email);
        //txt_confirmpassword.setText(oldconfirmpassword);

        btn_signup.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        labelForOldPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()){
                    //CheckBox is checked
                    txt_confirmpassword.setEnabled(false);
                }else{
                    //CheckBox is unchecked
                    txt_confirmpassword.setEnabled(true);
                }
            }
        });
    }

    public static boolean isValidEmail(String emailForValidation) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailForValidation).matches();

    }

    private boolean isValidMobile(String mobilenoValidation)
    {
        return android.util.Patterns.PHONE.matcher(mobilenoValidation).matches();
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
        if (v.getId() == R.id.btnSignUpEditProfile) {
            name = txt_name.getText().toString();
            buildingname = txt_buildingname.getText().toString();
            area = txt_area.getText().toString();
            city = txt_city.getText().toString();
            mobileno = txt_mobileno.getText().toString();
            email = txt_email.getText().toString();
            conf_password = txt_confirmpassword.getText().toString();

            if (txt_name.getText().toString().isEmpty() || txt_buildingname.getText().toString().isEmpty() || txt_area.getText().toString().isEmpty() || txt_city.getText().toString().isEmpty() || txt_mobileno.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() ) {
                Toast.makeText(EditProfile.this, "All Details are neccessory", Toast.LENGTH_LONG).show();
            } else {
                if( (mobileno.length() < 6 || mobileno.length() > 13) ) {
                    Toast.makeText(EditProfile.this, "Mobile No is not valid", Toast.LENGTH_LONG).show();
                }
                else if(!isValidEmail(email)) {
                    Toast.makeText(EditProfile.this, "Email is not valid", Toast.LENGTH_LONG).show();
                }
                try {
                    if(txt_confirmpassword.getText().toString().isEmpty()){
                        confirmpassword=oldconfirmpassword;
                    } else{
                        PasswordConverter passwordConverter=new PasswordConverter();
                        confirmpassword = passwordConverter.ConvertPassword(conf_password);
                    }
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

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = EditProfile.this.getPackageManager();
        ComponentName component = new ComponentName(EditProfile.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = EditProfile.this.getPackageManager();
        ComponentName component = new ComponentName(EditProfile.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}
