package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import com.couragedigital.peto.Connectivity.RegisterToServer;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SHA_256.PasswordConverter;

public class SignUp extends AppCompatActivity {
    private static EditText txt_name;
    private static EditText txt_buildingname;
    private static EditText txt_area;
    private static EditText txt_city;
    private static EditText txt_mobileno;
    private static EditText txt_email;
    private static EditText txt_password;
    private static EditText txt_confirmpassword;
    private static EditText txt_ngoUrl;
    private static EditText txt_ngoName;
    private static Button btn_signin;
    private static Button btn_signup;
    private static Button btn_cancel;
    public View cardViewRegister;
    RelativeLayout relativeLyoutForRegister;
    CheckBox isNgoCheckbox;
    TextInputLayout ngoUrlLayout;
    TextInputLayout ngoNameLayout;

    String name;
    String buildingname;
    String area;
    String city;
    String mobileno;
    String email;
    String password;
    String confirmpassword;
    String ngoUrl;
    String ngoName;
    String strIsNgo;
    ProgressDialog progressDialog = null;

    String conf_password;
    private long TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        isNgoCheckbox = (CheckBox) findViewById(R.id.chkIsNgo);
        txt_ngoUrl= (EditText) findViewById(R.id.txtNgoUrl);
        txt_ngoName= (EditText) findViewById(R.id.txtNgoName);
        ngoUrlLayout =(TextInputLayout) findViewById(R.id.textInputLayouNgoUrl);
        ngoNameLayout =(TextInputLayout) findViewById(R.id.textInputLayouNgoName);
        cardViewRegister=(View) findViewById(R.id.card_view);
        relativeLyoutForRegister=(RelativeLayout) findViewById(R.id.relativeLyoutForRegister);

        relativeLyoutForRegister.post(new Runnable() {
            @Override
            public void run() {
                //Integer heightOfFirstRelativeLayout = cardViewRegister.getHeight();
                Integer heightOfSecondRelativeLayout = cardViewRegister.getHeight();
                // cardViewRegister.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
                relativeLyoutForRegister.setMinimumHeight(heightOfSecondRelativeLayout + 200);
            }
        });

        ngoUrlLayout.setVisibility(View.GONE);
        ngoNameLayout.setVisibility(View.GONE);

        isNgoCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()){
                    ngoUrlLayout.setVisibility(View.VISIBLE);
                    ngoNameLayout.setVisibility(View.VISIBLE);
                    strIsNgo="Yes";

                }else{
                    ngoUrlLayout.setVisibility(View.GONE);
                    ngoNameLayout.setVisibility(View.GONE);
                    ngoUrl="";
                    strIsNgo="";

                }
            }
        });
        txt_ngoUrl.addTextChangedListener(ngoUrlChangeListener);
        txt_ngoName.addTextChangedListener(ngoNameChangeListener);
        ButtonSignUp();
        ButtonCancel();
    }
    private TextWatcher ngoNameChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            new GetNgoName().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };
    public class GetNgoName extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ngoName = txt_ngoName.getText().toString();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private TextWatcher ngoUrlChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            new GetNgoUrl().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        }
    };
    public class GetNgoUrl extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ngoUrl = txt_ngoUrl.getText().toString();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static boolean isValidEmail(String emailForValidation) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailForValidation).matches();

    }
    private boolean isValidMobile(String mobilenoValidation)
    {
        return android.util.Patterns.PHONE.matcher(mobilenoValidation).matches();
    }
    public void ButtonSignUp() {
        txt_name = (EditText) findViewById(R.id.txtname);
        txt_buildingname = (EditText) findViewById(R.id.txtbuildingname);
        txt_area = (EditText) findViewById(R.id.txtarea);
        txt_city = (EditText) findViewById(R.id.txtcity);
        txt_mobileno = (EditText) findViewById(R.id.txtmobileno);
        txt_email = (EditText) findViewById(R.id.txtemail);
        txt_password = (EditText) findViewById(R.id.txtpassword);
        txt_confirmpassword = (EditText) findViewById(R.id.txtconfirmpassword);
        btn_signup = (Button) findViewById(R.id.btnsignup);


        btn_signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        v.setEnabled(false);

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                v.setEnabled(true);
                            }
                        }, TIME);

                        name = txt_name.getText().toString();
                        buildingname = txt_buildingname.getText().toString();
                        area = txt_area.getText().toString();
                        city = txt_city.getText().toString();
                        mobileno = txt_mobileno.getText().toString();
                        email = txt_email.getText().toString();
                        password = txt_password.getText().toString();
                        conf_password = txt_confirmpassword.getText().toString();


                        if(isNgoCheckbox.isChecked() && txt_ngoUrl.getText().toString().isEmpty()){
                            Toast.makeText(SignUp.this, "Enter Url", Toast.LENGTH_LONG).show();
                        }
                        else if(isNgoCheckbox.isChecked()  && txt_ngoName.getText().toString().isEmpty()){
                            Toast.makeText(SignUp.this, "Enter NGO Name", Toast.LENGTH_LONG).show();
                        }
                        else if (txt_name.getText().toString().isEmpty() || txt_buildingname.getText().toString().isEmpty() || txt_area.getText().toString().isEmpty() || txt_city.getText().toString().isEmpty() || txt_mobileno.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || txt_confirmpassword.getText().toString().isEmpty() ) {
                            Toast.makeText(SignUp.this, "All Details are neccessory", Toast.LENGTH_LONG).show();
                        } else {
                            if( (mobileno.length() < 6 || mobileno.length() > 13) ) {
                                Toast.makeText(SignUp.this, "Mobile No is not valid", Toast.LENGTH_LONG).show();
                            }
                            else if(!isValidEmail(email)) {
                                Toast.makeText(SignUp.this, "Email is not valid", Toast.LENGTH_LONG).show();
                            }
                            else if(password.equals(conf_password)) {
                                progressDialog = new ProgressDialog(SignUp.this);
                                progressDialog.setMessage("Registering Data Wait...");
                                progressDialog.show();
                                try {
                                    PasswordConverter passwordConverter=new PasswordConverter();
                                    confirmpassword = passwordConverter.ConvertPassword(conf_password);
                                    RegisterToServer registerToServer = new RegisterToServer(SignUp.this);
                                    registerToServer.uploadToRemoteServer(name, buildingname, area, city, mobileno, email, confirmpassword,strIsNgo,ngoUrl,ngoName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else{
                                Toast.makeText(SignUp.this,"These Password don't match.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    public void ButtonCancel (){
        btn_cancel = (Button)findViewById(R.id.btncancel);

        btn_cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), Index.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = SignUp.this.getPackageManager();
        ComponentName component = new ComponentName(SignUp.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = SignUp.this.getPackageManager();
        ComponentName component = new ComponentName(SignUp.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}