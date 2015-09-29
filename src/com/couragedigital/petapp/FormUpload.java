package com.couragedigital.petapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.net.Uri;
import com.couragedigital.petapp.Connectivity.FormUploadConnectivity;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FormUpload extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private ImageView imageView;
    ProgressDialog progressDialog = null;
    String mCurrentPhotoPath;
    EditText petBreed;
    String petBreedName;
    Button photoButton;
    Button uploadButton;
    AlertDialog alertDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formupload);

        imageView = (ImageView)this.findViewById(R.id.imageOfPet);
        photoButton = (Button) this.findViewById(R.id.takePhoto);
        uploadButton = (Button) this.findViewById(R.id.uploadButton);
        petBreed = (EditText)findViewById(R.id.petBreed);

        photoButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        petBreed.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.takePhoto) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView text = (TextView) view.findViewById(android.R.id.text1);
                    text.setTextColor(Color.BLACK);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.alertDialogListNames));
                    text.setTypeface(null, Typeface.ITALIC);
                    return view;
                }
            };
            adapter.add("Take from Camera");
            adapter.add("Select from Gallery");
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            builder.setTitle("Select Image");
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // Ensure that there's a camera activity to handle the intent
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            // Create the File where the photo should go
                            File photoFile = null;
                            try {
                                photoFile = createImageFile();
                            } catch (IOException ex) {
                                // Error occurred while creating the File
                                ex.printStackTrace();
                            }
                            // Continue only if the File was successfully created
                            if (photoFile != null) {
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(photoFile));
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }
                        }
                    }
                    else if(which == 1) {
                        // Create intent to Open Image applications like Gallery, Google Photos
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, GALLERY_REQUEST);

                    }
                }
            });
            alertDialog = builder.create();

            alertDialog.show();
        }
        else if(v.getId() == R.id.uploadButton) {
            progressDialog = ProgressDialog.show(FormUpload.this, "", "Uploading file...", true);
            petBreedName = petBreed.getText().toString();
            new UploadToServer().execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
                    File image = new File(mCurrentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap imageToShow = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageBitmap(imageToShow);
                }
                else if(requestCode == GALLERY_REQUEST) {
                    Uri uri = intent.getData();
                    String[] projection = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    mCurrentPhotoPath = cursor.getString(columnIndex); // returns null
                    cursor.close();

                    File image = new File(mCurrentPhotoPath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap imageToShow = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setImageBitmap(imageToShow);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(FormUpload.this, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.i(e.getMessage(), "Error");
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
        return image;
    }
  public class UploadToServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
          try {
                int responseFromServer = FormUploadConnectivity.uploadToRemoteServer(mCurrentPhotoPath, petBreedName);
                if(responseFromServer == 200){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(FormUpload.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(FormUpload.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            return null;
        }
    }
}
