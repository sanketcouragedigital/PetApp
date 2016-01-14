package com.couragedigital.petapp.Connectivity;

import android.util.Log;
import android.widget.Toast;
import com.couragedigital.petapp.PetMate;
import com.couragedigital.petapp.SessionManager.SessionManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class PetMateFormUpload {
    private static final String SERVER_URL = "http://storage.couragedigital.com/dev/api/petappapi.php";
    //http://storage.couragedigital.com/petappapi.php
    //http://couragedigitalexample.hostingsiteforfree.com/cameraapi.php
    //http://192.168.0.3/PetAppAPI/api/petappapi.php
    //http://storage.couragedigital.com/dev/api/petappapi.php

    public static int uploadToRemoteServer(String emailforlatlong, String petCategoryName, String petBreedName, Integer petAge, String petGender, String petDescription, String firstImagePath, String secondImagePath, String thirdImagePath) throws Exception {
        int serverResponseCode = 0;
        String upLoadServerUri = SERVER_URL;
        String categoryOfPet = petCategoryName;
        String breedOfPet = petBreedName;
        Integer ageOfPet = petAge;
        String genderOfPet = petGender;
        String descriptionOfPet = petDescription;
        String firstPetImage = firstImagePath;
        String secondPetImage = secondImagePath;
        String thirdPetImage = thirdImagePath;
        String email = emailforlatlong;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File firstSourceFile = null;
        File secondSourceFile = null;
        File thirdSourceFile = null;
        FileInputStream firstFileInputStream = null;
        FileInputStream secondFileInputStream = null;
        FileInputStream thirdFileInputStream = null;

        if(!firstPetImage.isEmpty() && firstPetImage != null) {
            firstSourceFile = new File(firstPetImage);
            if (!firstSourceFile.isFile()) {
                Log.e("uploadFile", "First source File not exist in the phone");
                return 0;
            }
        }
        if(!secondPetImage.isEmpty() && secondPetImage != null) {
            secondSourceFile = new File(secondPetImage);
            if (!secondSourceFile.isFile()) {
                Log.e("uploadFile", "Second source File not exist in the phone");
                return 0;
            }
        }
        if(!thirdPetImage.isEmpty() && thirdPetImage != null) {
            thirdSourceFile = new File(thirdPetImage);
            if (!thirdSourceFile.isFile()) {
                Log.e("uploadFile", "Third source File not exist in the phone");
                return 0;
            }
        }
        try { // open a URL connection to the Servlet
            String method = "savePetMateDetails";
            String format = "json";
            firstFileInputStream = new FileInputStream(firstSourceFile);
            if(!secondPetImage.isEmpty()) {
                secondFileInputStream = new FileInputStream(secondSourceFile);
            }
            if(!thirdPetImage.isEmpty()) {
                thirdFileInputStream = new FileInputStream(thirdSourceFile);
            }
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("categoryOfPet", categoryOfPet);
            conn.setRequestProperty("breedOfPet", breedOfPet);
            conn.setRequestProperty("ageOfPet", String.valueOf(ageOfPet));
            conn.setRequestProperty("genderOfPet", genderOfPet);
            conn.setRequestProperty("descriptionOfPet", descriptionOfPet);
            conn.setRequestProperty("thirdPetImage", thirdPetImage);
            conn.setRequestProperty("secondPetImage", secondPetImage);
            conn.setRequestProperty("firstPetImage", firstPetImage);
            conn.setRequestProperty("email", email);
            conn.setRequestProperty("method", method);
            conn.setRequestProperty("format", format);
            dos = new DataOutputStream(conn.getOutputStream());


            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"categoryOfPet\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(categoryOfPet);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"breedOfPet\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(breedOfPet);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"ageOfPet\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(String.valueOf(ageOfPet));
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"genderOfPet\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(genderOfPet);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"descriptionOfPet\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(descriptionOfPet);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"email\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(email);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"method\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(method);
            dos.writeBytes(lineEnd);

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"format\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(format);
            dos.writeBytes(lineEnd);

            if(!thirdPetImage.isEmpty()) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"thirdPetImage\";filename=\""+ thirdPetImage + "\";" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(format);
                dos.writeBytes(lineEnd);
            }

            if(!secondPetImage.isEmpty()) {
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"secondPetImage\";filename=\""+ secondPetImage + "\";" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(format);
                dos.writeBytes(lineEnd);
            }

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"firstPetImage\";filename=\""+ firstPetImage + "\";" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = firstFileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = firstFileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = firstFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = firstFileInputStream.read(buffer, 0, bufferSize);
            }

            if(!secondPetImage.isEmpty()) {
                bytesAvailable = secondFileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = secondFileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = secondFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = secondFileInputStream.read(buffer, 0, bufferSize);
                }
            }

            if(!thirdPetImage.isEmpty()) {
                bytesAvailable = thirdFileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = thirdFileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = thirdFileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = thirdFileInputStream.read(buffer, 0, bufferSize);
                }
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            //close the streams //
            firstFileInputStream.close();
            if(!secondPetImage.isEmpty()) {
                secondFileInputStream.close();
            }
            if(!thirdPetImage.isEmpty()) {
                thirdFileInputStream.close();
            }
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            //Toast.makeText(petform.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            throw new MalformedURLException("Malformed URL Exception. Possible Cause:"+ex.getMessage());
        } catch (Exception e) {
            //Toast.makeText(petform.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            throw new Exception("Exception occured: Possible Cause: "+e.getMessage());
        }
        //dialog.dismiss();
        return serverResponseCode;
    }

}
