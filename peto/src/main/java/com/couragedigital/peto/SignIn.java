package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.couragedigital.peto.Connectivity.LoginFromServer;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.SHA_256.PasswordConverter;
//import android.support.design.widget.Snackbar;

public class SignIn extends AppCompatActivity {


    private static EditText username;
    private static EditText password;
    private static Button signin_btn;
    private static Button cancel_btn;
    private static Button signup_btn;
    private static TextView forget_password;
    private static TextView skip_all;
    DrawerLayout drawerLayout;

    ProgressDialog progressDialog;
    String email;
    String userpassword;
    private long TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        SignInButton();
        SignUpButton();
        ForgetPassword();
    }

    public void SignInButton() {
        username = (EditText) findViewById(R.id.txtemail);
        password = (EditText) findViewById(R.id.txtpassword);
        signin_btn = (Button) findViewById(R.id.btnsignin);

        signin_btn.setOnClickListener(
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
                        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                            Toast.makeText(SignIn.this, "Enter Username & Password", Toast.LENGTH_LONG).show();
                            //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
                        } else {
                            progressDialog = new ProgressDialog(SignIn.this);
                            progressDialog.setMessage("Login Please Wait...");
                            progressDialog.show();
                            email = username.getText().toString();

                            userpassword = password.getText().toString();
                            try {
                                PasswordConverter passwordConverter = new PasswordConverter();
                                userpassword = passwordConverter.ConvertPassword(password.getText().toString());
                                LoginFromServer loginFromServer = new LoginFromServer(SignIn.this);
                                loginFromServer.CheckToRemoteServer(email, userpassword);
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }


    public void SignUpButton() {
        signup_btn = (Button) findViewById(R.id.btnsignup);

        signup_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotosignuppage = new Intent(SignIn.this, SignUp.class);
                        startActivity(gotosignuppage);
                    }
                }
        );
    }

    public void ForgetPassword() {
        forget_password = (TextView) findViewById(R.id.lblforgetpassword);

        forget_password.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotoforgetpassword = new Intent(SignIn.this, EmailForCode.class);
                        startActivity(gotoforgetpassword);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = SignIn.this.getPackageManager();
        ComponentName component = new ComponentName(SignIn.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = SignIn.this.getPackageManager();
        ComponentName component = new ComponentName(SignIn.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}