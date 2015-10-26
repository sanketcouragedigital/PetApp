package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.couragedigital.petapp.Connectivity.LoginFromServer;
import com.couragedigital.petapp.Connectivity.RegisterToServer;
import com.couragedigital.petapp.SHA_256.PasswordConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private static EditText txt_name;
    private static EditText txt_buildingname;
    private static EditText txt_area;
    private static EditText txt_city;
    private static EditText txt_mobileno;
    private static EditText txt_email;
    private static EditText txt_password;
    private static EditText txt_confirmpassword;
    private static Button btn_signin;
    private static Button btn_signup;
    private static Button btn_cancel;

    String name;
    String buildingname;
    String area;
    String city;
    String mobileno;
    String email;
    String password;
    String confirmpassword;
    ProgressDialog progressDialog = null;

    String conf_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        ButtonSignUp();
        ButtonCancel();
        ButtonSignIn();
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
                    public void onClick(View v) {
                        name = txt_name.getText().toString();
                        buildingname = txt_buildingname.getText().toString();
                        area = txt_area.getText().toString();
                        city = txt_city.getText().toString();
                        mobileno = txt_mobileno.getText().toString();
                        email = txt_email.getText().toString();
                        password = txt_password.getText().toString();
                        conf_password = txt_confirmpassword.getText().toString();

                        if (txt_name.getText().toString().isEmpty() || txt_buildingname.getText().toString().isEmpty() || txt_area.getText().toString().isEmpty() || txt_city.getText().toString().isEmpty() || txt_mobileno.getText().toString().isEmpty() || txt_email.getText().toString().isEmpty() || txt_confirmpassword.getText().toString().isEmpty() ) {
                            Toast.makeText(SignUp.this, "All Details are neccessory", Toast.LENGTH_LONG).show();
                            //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
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
                                    registerToServer.uploadToRemoteServer(name, buildingname, area, city, mobileno, email, confirmpassword);
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
    public void signupResponse(String response) {
        String responseFromServer = response;

        if(responseFromServer == "USERS_DETAILS_SAVED") {
            Toast.makeText(SignUp.this, "Registration Complete.", Toast.LENGTH_SHORT).show();
            Intent gotologinpage =new Intent(SignUp.this,SignIn.class);
            startActivity(gotologinpage);
        }
        else {
            Toast.makeText(SignUp.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
        }
        return;
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
    public void ButtonSignIn(){
        btn_signin = (Button)findViewById(R.id.btnsignin);

        btn_signin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotosigninpage = new Intent(SignUp.this,SignIn.class);
                        startActivity(gotosigninpage);

                    }
                }
        );
    }
}