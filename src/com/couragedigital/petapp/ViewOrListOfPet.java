package com.couragedigital.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewOrListOfPet extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworlistofpet);

        Button petDetailsFormUpload = (Button) findViewById(R.id.uploadFormDetails);
        Button petDetailsList = (Button) findViewById(R.id.listOfPets);

        petDetailsFormUpload.setOnClickListener(this);
        petDetailsList.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.uploadFormDetails) {
            Intent uploadFormIntent = new Intent(ViewOrListOfPet.this,FormUpload.class);
            startActivity(uploadFormIntent);
        }
        else if(view.getId() == R.id.listOfPets) {
            Intent listOfPetsIntent = new Intent(ViewOrListOfPet.this, ListOfPets.class);
            startActivity(listOfPetsIntent);
        }
    }
}