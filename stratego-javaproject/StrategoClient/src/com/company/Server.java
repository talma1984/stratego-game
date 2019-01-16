package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Server {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 3333;
    public static final int SEND_MESSAGE = 100;
    public static final int GET_MESSAGES = 101;
    public static final int LOGIN = 103;
    public static final int OKAY = 200;
    public static User user;

    public static boolean sendMessage(String message){
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{

            socket = new Socket(HOST, PORT);     // On keyboard input - after login
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            byte[] messageBytes = message.getBytes();
            outputStream.write(SEND_MESSAGE);
            user.write(outputStream);
            outputStream.write(messageBytes.length);
            outputStream.write(messageBytes);
            int response = inputStream.read();
            if(response == OKAY){
                return true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean login(String userName){
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            user.write(outputStream);
            int response = inputStream.read();
            System.out.println(getPendingMessage(inputStream));
            if(response == OKAY){
                return true;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static String getPendingMessage(InputStream inputStream) throws IOException {
        int messageLength = inputStream.read();
        byte[] messageBytes = new byte[messageLength];
        int actuallyRead = inputStream.read(messageBytes);
        if(actuallyRead != messageLength)
            throw new IOException();
        return new String(messageBytes);
    }
}