package net.eclipsecraft.plasmacoin.Objects;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class User {

    private Socket sock;
    private BufferedReader reader;
    private PrintWriter writer;
    private UUID id;

    public User(Socket socket) throws IOException {
        this.sock = socket;
        reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        id = UUID.randomUUID();
    }

    public UUID getId(){
        return this.id;
    }

    public Socket getSock() {
        return sock;
    }

    protected void closeSocket() throws IOException {
        sock.close();
    }

    public void sendMessage(String s){
        System.out.println("User is sending message");
        writer.println(s);
        writer.flush();
    }

    protected boolean ready() throws IOException {
        return reader.ready();
    }

    protected String getIncomingMessage() throws IOException {
        return reader.readLine();
    }
}
