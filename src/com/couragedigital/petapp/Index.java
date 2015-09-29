package com.couragedigital.petapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Index extends AppCompatActivity implements View.OnClickListener {

    Button petDetailsFormUpload;
    Button petDetailsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        petDetailsFormUpload = (Button) findViewById(R.id.uploadFormDetails);
        petDetailsList = (Button) findViewById(R.id.listOfPets);

        petDetailsFormUpload.setOnClickListener(this);
        petDetailsList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.uploadFormDetails) {
            Intent uploadFormIntent = new Intent(Index.this, FormUpload.class);
            startActivity(uploadFormIntent);
        }
        else if(v.getId() == R.id.listOfPets) {
            Intent listOfPetsIntent = new Intent(Index.this, ListOfPets.class);
            startActivity(listOfPetsIntent);
        }
    }
}