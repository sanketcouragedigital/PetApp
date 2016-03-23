package com.couragedigital.peto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.couragedigital.peto.Connectivity.FetchUserDetailsForEditProfile;
import com.couragedigital.peto.SHA_256.PasswordConverter;
import com.couragedigital.peto.SessionManager.SessionManager;
import java.util.HashMap;

public class CheckPasswordForEditProfile extends BaseActivity implements View.OnClickListener{
    EditText userPassword;
    Button PasswordSubmit;

    String password = "";
    String email;
    public View v;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_for_edit_profile);

        userPassword = (EditText) findViewById(R.id.passwordForEditProfile);
        PasswordSubmit = (Button) findViewById(R.id.passwordSubmit);

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        PasswordSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.passwordSubmit) {
            password = userPassword.getText().toString();
            if(password.equals("")) {
                userPassword.setError("Please Enter Your Password!");
            } else {
                try {
                    PasswordConverter passwordConverter=new PasswordConverter();
                    password = passwordConverter.ConvertPassword(password);
                    new UploadPasswordToServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class UploadPasswordToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FetchUserDetailsForEditProfile fetchUserDetailsForEditProfile = new FetchUserDetailsForEditProfile(CheckPasswordForEditProfile.this);
                fetchUserDetailsForEditProfile.fetchUserDeatils(email, password,v);
                //FetchUserDetailsForEditProfile.fetchUserDeatils(email, password,v);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
