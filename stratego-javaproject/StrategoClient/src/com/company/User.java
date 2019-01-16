package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class User {
    private String userName;

    public User(InputStream inputStream) throws IOException {
        int userNameLength = inputStream.read();
        byte[] userNameBytes = new byte[userNameLength];
        int actuallyRead;
        actuallyRead = inputStream.read(userNameBytes);
        if(actuallyRead != userNameLength)
            throw new IOException();
        this.userName = new String(userNameBytes);
        int passwordLength = inputStream.read();
        byte[] passwordBytes = new byte[passwordLength];
        actuallyRead = inputStream.read(passwordBytes);
        if(actuallyRead != passwordLength)
            throw new IOException();
    }

    public User(String userName) {
        this.userName = userName;
    }

    public User() {
    }

    public void write(OutputStream outputStream) throws IOException{
        byte[] userNameBytes = userName.getBytes();
        outputStream.write(userNameBytes.length);
        outputStream.write(userNameBytes);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}