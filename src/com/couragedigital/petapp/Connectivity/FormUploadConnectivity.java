package com.couragedigital.petapp.Connectivity;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public  class FormUploadConnectivity {

    private static final String SERVER_URL = "http://storage.couragedigital.com/dev/api/petappapi.php";
    //http://storage.couragedigital.com/petappapi.php
    //http://couragedigitalexample.hostingsiteforfree.com/cameraapi.php
    //http://192.168.0.3/PetAppAPI/api/petappapi.php

    public static int uploadToRemoteServer(String pathOfImageFileOnDevice, String petBreedName) throws Exception {
        int serverResponseCode = 0;
        String upLoadServerUri = SERVER_URL;
        String petBreedOrigin = petBreedName;
        String filePath = pathOfImageFileOnDevice;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(filePath);
        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File Does not exist");
            return 0;
        }
        try { // open a URL connection to the Servlet
            String method = "savePetDetails";
            String format = "json";
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", filePath);
            conn.setRequestProperty("petBreedOrigin", petBreedOrigin);
            conn.setRequestProperty("method", method);
            conn.setRequestProperty("format", format);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"petBreedOrigin\";" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(petBreedOrigin);
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

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ filePath + "\";" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            //Toast.makeText(formupload.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            throw new MalformedURLException("Malformed URL Exception. Possible Cause:"+ex.getMessage());
        } catch (Exception e) {
           //Toast.makeText(formupload.this, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Upload file Exception", "Exception : " + e.getMessage(), e);
            throw new Exception("Exception occured: Possible Cause: "+e.getMessage());
        }
        //dialog.dismiss();
        return serverResponseCode;
    }


}
