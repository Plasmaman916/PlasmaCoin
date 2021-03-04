package net.eclipsecraft.plasmacoin.Objects;

import net.eclipsecraft.plasmacoin.app.PeerToPeerServer;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Heartbeat extends Thread{
    private ArrayList<String> peers;
    private PeerToPeerServer p2pServer;

    public Heartbeat(PeerToPeerServer p2pServer, ArrayList<String> peers){
        this.peers = peers;
        this.p2pServer = p2pServer;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        while(true){
            if(peers!=null) {
                for (String u : peers) {
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
                    try {
                        boolean alreadyExists = false;
                        for(UserHandler hand : p2pServer.getUserMap().keySet()){
                            for(User user : p2pServer.getUserMap().get(hand)){
                                if(addr.equalsIgnoreCase("localhost")){
                                    addr = "127.0.0.1";
                                }
                                if(user.getSock().getInetAddress().getHostAddress().equals(addr)){
                                    if(user.getSock().getPort() == port){
                                        alreadyExists = true;
                                        System.out.println("Exists");
                                        user.sendMessage("keepAlive");
                                        if(!user.isRunningAlive()) {
                                            user.setRunningAlive(true);
                                            Thread thread = new Thread(() -> {
                                                long time = System.currentTimeMillis();
                                                boolean dead = false;
                                                while (!user.isAlive() && !dead) {
                                                    if (System.currentTimeMillis() - time > 20000) {
                                                        dead = true;
                                                    }
//                                                    System.out.println(System.currentTimeMillis() - time);
                                                    try {
                                                        Thread.sleep(100);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                user.setAlive(false);
                                                if (dead) {
                                                    System.out.println("IS DEAD");
                                                    p2pServer.removeUser(user);
                                                }else {
                                                    System.out.println("IS ALIVE a");
                                                }
                                                user.setRunningAlive(false);
                                                currentThread().stop();
                                            });
                                            thread.start();
                                        }
                                    }
                                }
                            }
                        }
                        if(!alreadyExists) {
                            Socket socket = new Socket(addr, port);
                            p2pServer.addSocket(socket);
                            System.out.println("ADDING NEW");
                        }
                    }catch (IOException e){

                    }
                }
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}