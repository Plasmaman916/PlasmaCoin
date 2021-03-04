package net.eclipsecraft.plasmacoin.app;

import com.sun.net.httpserver.HttpServer;
import net.eclipsecraft.plasmacoin.app.PeerToPeerServer;
import net.eclipsecraft.plasmacoin.blockchain.BlockChain;
import net.eclipsecraft.plasmacoin.wallet.TransactionPool;
import net.eclipsecraft.plasmacoin.wallet.Wallet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class Main {
    private static int port = 3001;
    private static int wsport = 5001;
    private static BlockChain bc;
    private static HttpServer server;
    private static PeerToPeerServer p2pserver;
    private static String peers = "";
    private static String name = UUID.randomUUID().toString();
    private static String address = "nil-address";
    private static TransactionPool pool = new TransactionPool();
    private static Wallet wallet = null;
    private static String pathToCurrentWallet = null;

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
            if (a.equals("-s-port")) {     //Checks if they are trying to start with a custom port
                if (args.length < loc + 2)   {     //Checking if the args includes an argument after the "-ws-port"
                    System.out.println("You must specify a port! (-s-port)");
                    System.exit(1);     //Exiting because there is a missing argument (-ws-port)
                }
                String p = args[loc + 1];     //Getting the port argument
                if (!isInteger(p)) {       //Checking if the port argument is valid
                    System.out.println("You have specified an invalid port! (-s-port)");
                    System.exit(1);     //Exiting because the port argument is invalid
                }
                int newPort = Integer.valueOf(p);//Parsing the port argument into an int
                if (!isValidPort(newPort)) {       //Checking if it is a valid port between 0 and 65535
                    System.out.println("The specified port is invalid! (-s-port)");
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
            if (a.equals("-address")) {
                if (args.length < loc + 2) {
                    System.out.println("You must specify an address! (-address) " + (loc + 2));
                    System.exit(1);
                }
                String newAddress = args[loc + 1];
                address = newAddress;
            }
            loc++;//Update position in args
        }
        if(address.equals("nil-address")){
            System.out.println("Warning. You are starting with an address. You will not receive any rewards from mining!");
        }
        bc = new BlockChain();
        p2pserver = new PeerToPeerServer(new InetSocketAddress(wsport),0,peers);
        System.out.println("Listening on port "+port);
        FXMain.main(new String[]{peers});
    }


    public static Wallet getWallet() {
        return wallet;
    }

    public static String getPathToCurrentWallet() {
        return pathToCurrentWallet;
    }

    public static void setPathToCurrentWallet(String pathToCurrentWallet) {
        Main.pathToCurrentWallet = pathToCurrentWallet;
    }

    public static TransactionPool getPool(){
        return pool;
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

    public static void loadWallet(Wallet newWallet){
        wallet = newWallet;
    }
}
