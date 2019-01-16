package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import static com.company.Server.HOST;
import static com.company.Server.PORT;
import static com.company.Server.GET_MESSAGES;

public class GetMessagesThread extends Thread {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean go;
    private List<Message> messages;
    private NewMessageListener listener;
    private User user;

    public GetMessagesThread(List<Message> messages, NewMessageListener listener, User user) {
        this.messages = messages;
        go = true;
        this.listener = listener;
        this.user = user;
    }

    @Override
    public void run() {
        try{
            while (go) {
                socket = new Socket(HOST, PORT);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                outputStream.write(GET_MESSAGES);   //action:
                user.write(outputStream);   //authenticate:
                byte[] fromBytes = new byte[4];         //sending to the server from which message we want to pull message from
                ByteBuffer.wrap(fromBytes).putInt(messages.size());
                outputStream.write(fromBytes);
                Message message;
                while (true){
                    try {
                        message = new Message(inputStream);
                        messages.add(message);
                        if (listener != null) {
                            listener.onNewMessage(message);
                        }
                    }catch (Exception ex){
                        break;
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
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

    public void stopGettingMessages(){
        go = false;
        interrupt();
    }
    public interface NewMessageListener{
        void onNewMessage(Message message);
    }
}