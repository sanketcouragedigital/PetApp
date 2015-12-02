package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.couragedigital.petapp.Connectivity.LoginFromServer;
import com.couragedigital.petapp.Connectivity.ResetPassword;
import com.couragedigital.petapp.SHA_256.PasswordConverter;

public class SetNewPassword extends BaseActivity {
    private static EditText userActivationCode;
    private static EditText userNewPassword;
    private static Button submit_btn;

    ProgressDialog progressDialog;
    String code;
    String password;
    String email;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        SubmitButton();
    }

    public void SubmitButton() {
        userActivationCode = (EditText) findViewById(R.id.txtActivationCode);
        userNewPassword = (EditText) findViewById(R.id.txtNewPassword);

        //Intent emailIntent = getIntent();
        //email= emailIntent.getStringExtra("EMAIL");

        submit_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (userActivationCode.getText().toString().isEmpty() || userNewPassword.getText().toString().isEmpty()) {
                            Toast.makeText(SetNewPassword.this, "Enter Activation Code & New Password", Toast.LENGTH_LONG).show();
                            //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
                        } else {
                            progressDialog = new ProgressDialog(SetNewPassword.this);
                            progressDialog.setMessage("Login Please Wait...");
                            progressDialog.show();
                            code = userActivationCode.getText().toString();
                            Intent gotologin = new Intent(SetNewPassword.this, SignIn.class);
                            startActivity(gotologin);
                            try {
                                PasswordConverter passwordConverter = new PasswordConverter();
                                password = passwordConverter.ConvertPassword(userNewPassword.getText().toString());
                                ResetPassword resetPassword = new ResetPassword(SetNewPassword.this);
                                resetPassword.SeToRemoteServer(code, password, email);
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(SetNewPassword.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }
}
