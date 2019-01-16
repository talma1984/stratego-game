package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Main {

    public static final int PORT = 3333;

    public static void main(String[] args) {
       MainGame game = new MainGame();
//        ServerSocket serverSocket = null;
//        try{
//           serverSocket = new ServerSocket(PORT);
//            List<Message> messages = new ArrayList<>();
//            Map<String, User> users = new HashMap<>();
//            while (true){
//                Socket socket = serverSocket.accept();
//                new ClientThread(socket, messages, users).start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            if (serverSocket != null) {
//                try {
//                    serverSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}