package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userName;
        System.out.println("Welcome, enter your'e name:");
        String name = scanner.nextLine();
        Server.login(name);
        List<Message> messages = new ArrayList<>();
        GetMessagesThread getMessagesThread = new GetMessagesThread(messages, new GetMessagesThread.NewMessageListener() {
            @Override
            public void onNewMessage(Message message) {  // all messages from server only from 'getMessages'
            }
        }, Server.user);
        getMessagesThread.start();
        String message;

        while (!(message = scanner.nextLine()).equals("exit")){
            Server.sendMessage(message);
        }
        System.out.println("bye bye");
        getMessagesThread.stopGettingMessages();
    }
}