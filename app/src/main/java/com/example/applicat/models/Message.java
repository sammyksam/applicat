package com.example.applicat.models;

public class Message {
    String message_id, recipient, sender, message;
    long timestamp;

    public Message() {
    }

    public Message(String message_id, String recipient, String sender, String message, long timestamp) {
        this.message_id = message_id;
        this.recipient = recipient;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
