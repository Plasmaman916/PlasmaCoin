package net.eclipsecraft.plasmacoin.Objects;

public class Message {
    private String type;
    private String message;

    public Message(String type, String message){
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
