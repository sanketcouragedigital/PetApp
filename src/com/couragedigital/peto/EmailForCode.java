package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.couragedigital.peto.Connectivity.EmailVerifying;

public class EmailForCode extends AppCompatActivity {

    private static EditText txt_Email;
    private static Button btnSubmit;

    ProgressDialog progressDialog;
    String email;
    private Toolbar emailForCodeToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emailforcode);

        emailForCodeToolbar = (Toolbar) findViewById(R.id.emailForCodeToolbar);
        setSupportActionBar(emailForCodeToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        emailForCodeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SubmitButton();
    }

    public void SubmitButton() {
        txt_Email = (EditText) findViewById(R.id.txtemail);
        btnSubmit = (Button) findViewById(R.id.btnsubmit);

        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (txt_Email.getText().toString().isEmpty()) {
                            Toast.makeText(EmailForCode.this, "Enter Email", Toast.LENGTH_LONG).show();
                            //Snackbar.make(SignIn.this,"Enter Username & Password",Snackbar.LENGTH_LONG).show();
                        } else {
                            progressDialog = new ProgressDialog(EmailForCode.this);
                            progressDialog.setMessage("Checking Your Email..");
                            progressDialog.show();
                            email = txt_Email.getText().toString();
                            try {
                                EmailVerifying emailVerifying = new EmailVerifying(EmailForCode.this);
                                emailVerifying.SendEmailForPassword(email);
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(EmailForCode.this, "Exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }


                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        EmailForCode.this.finish();
    }

}
