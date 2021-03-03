package net.eclipsecraft.plasmacoin.app;

import com.google.gson.Gson;
import net.eclipsecraft.plasmacoin.Objects.ChainMessage;
import net.eclipsecraft.plasmacoin.Objects.User;
import net.eclipsecraft.plasmacoin.Objects.UserHandler;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;


public class PeerToPeerServer extends Thread{
    private static final Logger LOGGER = Logger.getLogger(PeerToPeerServer.class);

    private ServerSocket server;
    private ConcurrentHashMap<UserHandler, CopyOnWriteArrayList<User>> userMap = new ConcurrentHashMap<>();
    public Object userMapSync = new Object();
    private Gson gson = new Gson();

    public PeerToPeerServer(InetSocketAddress address, int backlog) throws IOException {
        server = new ServerSocket(address.getPort(),backlog,address.getAddress());
        this.start();
    }

    public void hi(){
        System.out.println("hi");
    }

    public PeerToPeerServer(InetSocketAddress address, int backlog, String peers) throws IOException {
        server = new ServerSocket(address.getPort(),backlog,address.getAddress());
        System.out.println(peers);
        if(peers!="") {
            String[] users = peers.split(",");
            System.out.println(users.length);
            for (String u : users) {
                String[] data = u.split(":");
                int port = 0;
                String addr = null;
                try {
                    port = Integer.valueOf(data[1]);
                    addr = data[0];
                } catch (NumberFormatException e) {
                    continue;
                }
                if (port <= 0 || addr == null) {
                    continue;
                }
                if (port > 65536) {
                    continue;
                }
                Socket socket = new Socket(addr, port);
                addSocket(socket);
            }
        }
        this.start();
    }

    public ConcurrentHashMap<UserHandler, CopyOnWriteArrayList<User>> getUserMap() {
        return this.userMap;
    }

    @Override
    public void run() {
        super.run();
        while(true){
            Socket socket = null;
            try {
                socket = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            addSocket(socket);
        }
    }

    public void syncChains(){
        for(UserHandler handler : userMap.keySet()){
            handler.syncChains(Main.getCurrentChain().getChain());
        }
    }

    private void addSocket(Socket socket){
        if(socket == null){
            return;
        }
        UserHandler bestHandler = null;
        int connections = 0;
        for(UserHandler h : userMap.keySet()){
            CopyOnWriteArrayList<User> userCollection = userMap.get(h);
            if(userCollection.size()<10){
                System.out.println("Collection less thtan 10");
                if(userCollection.size() > connections){
                    System.out.println("COnnections good");
                    bestHandler = h;
                    connections = userCollection.size();
                }
            }
        }
        if(bestHandler == null){
            System.out.println("NUll handler");
            UserHandler handler = new UserHandler(this);
            bestHandler = handler;
            userMap.put(bestHandler,new CopyOnWriteArrayList<>());
        }
        User u = null;
        try {
            u = new User(socket);
            userMap.get(bestHandler).add(u);
            u.sendMessage(ChainMessage.chainToJson(Main.getCurrentChain().getChain()));
        } catch (IOException e){}
        if(u == null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }
}