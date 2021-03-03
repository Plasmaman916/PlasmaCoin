package net.eclipsecraft.plasmacoin.app;

import com.sun.net.httpserver.HttpServer;
import net.eclipsecraft.plasmacoin.blockchain.BlockChain;
import net.eclipsecraft.plasmacoin.wallet.TransactionPool;
import net.eclipsecraft.plasmacoin.wallet.Wallet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    private static int port = 3001;
    private static int sport = 5001;
    private static BlockChain bc;
    private static HttpServer server;
    private static PeerToPeerServer p2pserver;
    private static String peers = "";
    private static String name = UUID.randomUUID().toString();
    private static String address = "3056301006072a8648ce3d020106052b8104000a03420004838604f2435a10bd769e0bc1c6978735d887fe787e6c904276357e79e7fd8b18306be634607d7837169226465f9feefd71f1f4ba219a3d0e7cce5274c2e72664";
    private static TransactionPool pool = new TransactionPool();

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
                sport = newPort;     //Setting the port to the new port
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
        p2pserver = new PeerToPeerServer(new InetSocketAddress(sport),0,peers);
        server = HttpServer.create(new InetSocketAddress("0.0.0.0",port),0);
        server.createContext("/blocks", new HttpHandlerSendChain());
        server.createContext("/mine", new HttpHandlerReceiveChain(p2pserver));
        server.createContext("/transactions", new GetTransactions());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println("Listening on port "+port);
        Object o;
        Wallet wallet = new Wallet();
        wallet.createTransaction("Coolio",123d,pool);
        synchronized (o = new Object()){
            while(true){
                bc.addBlock(name);
                p2pserver.syncChains();
            }
        }
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
}
