package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Message {
    private String sender;
    private String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public Message(InputStream inputStream) throws IOException {
        int senderLength = inputStream.read();
        if(senderLength == -1)
            throw new IOException("sdgf");
        byte[] senderBytes = new byte[senderLength];
        int actuallyRead = inputStream.read(senderBytes);
        if(actuallyRead != senderLength)
            throw new IOException("sdfgdfg");
        this.sender = new String(senderBytes);
        int contentLength = inputStream.read();
        if(contentLength == -1)
            throw new IOException("sdgf");
        byte[] contentBytes = new byte[contentLength];
        actuallyRead = inputStream.read(contentBytes);
        if(actuallyRead != contentLength)
            throw new IOException("sdfgdfg");
        this.content = new String(contentBytes);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void write(OutputStream outputStream) throws IOException{
        byte[] senderBytes = sender.getBytes();
        outputStream.write(senderBytes.length);
        outputStream.write(senderBytes);
        byte[] contentBytes = content.getBytes();
        outputStream.write(contentBytes.length);
        outputStream.write(contentBytes);
    }



    public void setContent(String content) {
        this.content = content;
    }
}