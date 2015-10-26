package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.couragedigital.petapp.Connectivity.LoginFromServer;
import com.couragedigital.petapp.SHA_256.PasswordConverter;
//import android.support.design.widget.Snackbar;

public class SignIn extends AppCompatActivity {


    private static EditText username;
    private static EditText password;
    private static Button signin_btn;
    private static Button cancel_btn;
    private static Button signup_btn;
    private static TextView forget_password;
    private static TextView skip_all;

    ProgressDialog progressDialog;
    String email;
    String userpassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        SignInButton();
        CancelButton();
        SignUpButton();
        ForgetPassword();
        SkipAll();
    }
    public void SignInButton(){
        username =(EditText)findViewById(R.id.txtemail);
        password =(EditText)findViewById(R.id.txtpassword);
        signin_btn = (Button)findViewById(R.id.btnsignin);

        signin_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                            Toast.makeText(SignIn.this,"Enter Username & Password",Toast.LENGTH_LONG).show();
                            //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
                        }else {
                            progressDialog = new ProgressDialog(SignIn.this);
                            progressDialog.setMessage("Login Please Wait...");
                            progressDialog.show();
                            email = username.getText().toString();

                            userpassword = password.getText().toString();
                            try{
                                PasswordConverter passwordConverter = new PasswordConverter();
                                userpassword = passwordConverter.ConvertPassword(password.getText().toString());
                                LoginFromServer loginFromServer = new LoginFromServer(SignIn.this);
                                loginFromServer.CheckToRemoteServer(email, userpassword);
                            } catch (Exception e){
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(SignIn.this,"Exception : "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
        );
    }

    public void CancelButton(){
        cancel_btn = (Button)findViewById(R.id.btncancel);

        cancel_btn.setOnClickListener(
                new View.OnClickListener(){
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

    public void SignUpButton(){
        signup_btn = (Button)findViewById(R.id.btnsignup);

        signup_btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent gotosignuppage = new Intent(SignIn.this,SignUp.class);
                        startActivity(gotosignuppage);
                    }
                }
        );
    }
    public void ForgetPassword(){
        forget_password =(TextView)findViewById(R.id.lblforgetpassword);

        forget_password.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotoforgetpassword = new Intent(SignIn.this,EmailForCode.class);
                        startActivity(gotoforgetpassword);
                    }
                }
        );
    }
    public void SkipAll(){
        skip_all = (TextView)findViewById(R.id.lblskipall);

        skip_all.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent gotoindexpage = new Intent(SignIn.this,Index.class);
                        startActivity(gotoindexpage);
                    }
                }
        );
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Index.class);
        startActivity(intent);
    }
}