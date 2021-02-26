package net.eclipsecraft.plasmacoin.app;

import com.sun.net.httpserver.HttpServer;
import net.eclipsecraft.plasmacoin.blockchain.BlockChain;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    private static int port = 3001;
    private static int wsport = 5001;
    private static BlockChain bc;
    private static HttpServer server;
    private static PeerToPeerServer p2pserver;
    private static String peers = "";
    private static String name = UUID.randomUUID().toString();

    public static void main(String[] args) throws IOException {
        int loc = 0;        //Keeps position in args
        for (String a : args) {       //Checking all args
            if (a.equals("-p")) {     //Checks if they are trying to start with a custom port
                if (args.length < loc + 2)   {     //Checking if the args includes an argument after the "-p"
                    System.out.println("You must specify a port! (-p)");
                    System.exit(1);     //Exiting because there is a missing argument (port)
                }
                String p = args[loc + 1];     //Getting the port argument
                if (!isInteger(p)) {       //Checking if the port argument is valid
                    System.out.println("You have specified an invalid port! (-p)");
                    System.exit(1);     //Exiting because the port argument is invalid
                }
                int newPort = Integer.valueOf(p);//Parsing the port argument into an int
                if (!isValidPort(newPort)) {       //Checking if it is a valid port between 0 and 65535
                    System.out.println("The specified port is invalid! (-p)");
                    System.exit(1);     //Exiting due to specified port being invalid
                }
                port = newPort;     //Setting the port to the new port
            }
            if (a.equals("-ws-port")) {     //Checks if they are trying to start with a custom port
                if (args.length < loc + 2)   {     //Checking if the args includes an argument after the "-ws-port"
                    System.out.println("You must specify a port! (-ws-port)");
                    System.exit(1);     //Exiting because there is a missing argument (-ws-port)
                }
                String p = args[loc + 1];     //Getting the port argument
                if (!isInteger(p)) {       //Checking if the port argument is valid
                    System.out.println("You have specified an invalid port! (-ws-port)");
                    System.exit(1);     //Exiting because the port argument is invalid
                }
                int newPort = Integer.valueOf(p);//Parsing the port argument into an int
                if (!isValidPort(newPort)) {       //Checking if it is a valid port between 0 and 65535
                    System.out.println("The specified port is invalid! (-ws-port)");
                    System.exit(1);     //Exiting due to specified port being invalid
                }
                wsport = newPort;     //Setting the port to the new port
            }
            if (a.equals("-peers")) {     //Checks if they are trying to start with  peers
                if (args.length < loc + 2)   {     //Checking if the args includes an argument after the "-peers"
                    System.out.println("You must specify peers! (-peers) "+(loc+2));
                    System.exit(1);     //Exiting because there is a missing argument (-peers)
                }
                String p = args[loc + 1];     //Getting the peers argument
                peers = p;     //Setting the peers to this
            }
            if (a.equals("-name")) {
                if (args.length < loc + 2) {
                    System.out.println("You must specify a name! (-name) " + (loc + 2));
                    System.exit(1);
                }
                String newName = args[loc + 1];
                name = newName;
            }
            loc++;//Update position in args
        }
        bc = new BlockChain();
        p2pserver = new PeerToPeerServer(new InetSocketAddress(wsport),0,peers);
        server = HttpServer.create(new InetSocketAddress("0.0.0.0",port),0);
        server.createContext("/blocks", new HttpHandlerSendChain());
        server.createContext("/mine", new HttpHandlerReceiveChain(p2pserver));
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Listening on port "+port);
        Object o;
        synchronized (o = new Object()){
            while(true){
                bc.addBlock(name);
                p2pserver.syncChains();
            }
        }
    }

    public static PeerToPeerServer getP2pserver() {
        return p2pserver;
    }

    public static BlockChain getCurrentChain(){
        return bc;
    }

    public static String getName() {
        return name;
    }

    public static boolean isValidPort(int i) {
        if (i > 0 && i <= 65535) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
