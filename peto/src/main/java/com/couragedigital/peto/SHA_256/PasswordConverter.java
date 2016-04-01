package com.couragedigital.peto.SHA_256;


import java.security.MessageDigest;

public class PasswordConverter {
    String password;

    public String ConvertPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer stringBuffeb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            stringBuffeb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

        }
        return stringBuffeb.toString();
    }
}
