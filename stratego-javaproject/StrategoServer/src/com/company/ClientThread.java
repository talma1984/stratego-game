package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;


public class ClientThread extends Thread {
    public static final int SEND_MESSAGE = 100;
    public static final int GET_MESSAGES = 101;
    public static final int OKAY = 200;
    public static final int LOGIN = 103;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private List<Message> messages;
    private Map<String, User> users;
    private MainGame game;

    public ClientThread(Socket socket, List<Message> messages, Map<String, User> users) {
        this.socket = socket;
        this.messages = messages;
        this.users = users;
        this.game = new MainGame();
    }

    @Override
    public void run() {
        try{
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int action = inputStream.read();
            switch (action){
                case SEND_MESSAGE:
                    sendMessage();
                    break;
                case GET_MESSAGES:
                    getMessages();
                    break;
                case LOGIN:
                    login();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void signup() throws IOException{
        User user = new User(inputStream);
        boolean success = false;
        outputStream.write(OKAY);

    }

    private void login() throws IOException{
        User user = new User(inputStream);
        users.put(user.getUserName(), user);
//        game.AddPlayer();

        outputStream.write(OKAY);

        sendStringToClients("Hello from server1");
        sendStringToClients("Hello from server2");
        sendStringToClients("Hello from server3");

    }

    private void sendStringToClients(String messageText) throws IOException{
        byte[] userNameBytes = messageText.getBytes();
        outputStream.write(userNameBytes.length);
        outputStream.write(userNameBytes);
    }

    private void sendMessage() throws IOException{
        // GETS HERE WHEN CLIENT SEND MESSAGE
        User user = new User(inputStream);
        int messageLength = inputStream.read();
        if(messageLength == -1)
            return;
        byte[] messageBytes = new byte[messageLength];
        int actuallyRead = inputStream.read(messageBytes);
        if(actuallyRead != messageLength)
            return;
        String UserName = user.getUserName();
        String UserAction =  new String(messageBytes);
        // game.tryToMakeAMove(UserName,UserAction);

        Message message = new Message(user.getUserName(), new String(messageBytes));
        messages.add(message);
        outputStream.write(OKAY);
        sendStringToClients( "Hello "+ UserName+
                "\n, Your message is: "+ UserAction
                + "\n, So many lines!!!! "
        );
    }

    // Server being asked to
    private void getMessages() throws IOException{
        User user = new User(inputStream);
        byte[] fromBytes = new byte[4];
        int actuallyRead = inputStream.read(fromBytes);
        if(actuallyRead != 4)
            return;
        int from = ByteBuffer.wrap(fromBytes).getInt();
        for (int i = from; i < messages.size(); i++) {
            Message message = messages.get(i);
            message.write(outputStream);
        }
    }
}