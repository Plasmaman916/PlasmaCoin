package net.eclipsecraft.plasmacoin.test;

import com.google.gson.Gson;
import net.eclipsecraft.plasmacoin.blockchain.Block;
import net.eclipsecraft.plasmacoin.blockchain.BlockChain;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Test {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        //Checking Block#toString(). CHECK 001
//        Block block = new Block(1000213,"lastHashHere","hashHere",new Date());
//        System.out.println("(CHECK 001) "+block.toString());

        //Checking if a valid chain is valid. TEST 001
        BlockChain bc = new BlockChain();
        bc.addBlock("Cool");
        System.out.println("Should be True (TEST 001): "+BlockChain.isValidChain(bc.getChain()));

        //Checking for bad genesis block. TEST 002
        BlockChain badBc = new BlockChain();
        badBc.addBlock("Cool");
        badBc.addBlock("Neat");
        ArrayList<Block> badChain = badBc.getChain();
//        badChain.set(0,new Block(123456,"--none--","--first--","Bad Data"));
        System.out.println("Should be False (TEST 002): "+BlockChain.isValidChain(badChain));

        //Checking for corrupt data. TEST 003
        BlockChain bc3 = new BlockChain();
        bc3.addBlock("cool");
        bc3.getChain().set(1,bc3.getChain().get(1).changeData("hi"));
        bc3.addBlock("Cool");
        System.out.println("Should be false (TEST 003): "+BlockChain.isValidChain(bc3.getChain()));

        //Checking replace chain function with valid chain. TEST 004
        BlockChain rbc = new BlockChain();
        rbc.addBlock("Block 2");
        rbc.addBlock("Block 3");
        rbc.addBlock("Block 4");
        rbc.addBlock("Block 5");
        boolean replaceWorked = bc.replaceChain(rbc.getChain());
        System.out.println("Should be True (TEST 004): "+replaceWorked);

        //Checking replace chain function with INVALID chain. TEST 005
        BlockChain rbcBad = new BlockChain();
        rbcBad.addBlock("Block 1");
        rbcBad.addBlock("Block 2");
        rbcBad.addBlock("Block 3");
        rbcBad.addBlock("Block 4");
        rbcBad.addBlock("Block 5");
        ArrayList<Block> rbcChain = rbcBad.getChain();
        rbcChain.set(3,rbcChain.get(3).changeData("Bad Data"));
        boolean replaceWorkedBad = bc.replaceChain(rbcBad.getChain());
        System.out.println("Should be False (TEST 005): "+replaceWorkedBad);

        //Checking replace chain function with SMALLER chain. TEST 006
        BlockChain smallbc = new BlockChain();
        smallbc.addBlock("asdasfrtfrewfsc");
        boolean replaceWorkedSmall = bc.replaceChain(smallbc.getChain());
        System.out.println("Should be False (TEST 006): "+replaceWorkedSmall);

//        Socket socket = new Socket( "localhost", 3001 );

        // Create input and output streams to read from and write to the server
//        PrintStream out = new PrintStream( socket.getOutputStream(),true );
//        BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );

        // Follow the HTTP protocol of GET <path> HTTP/1.0 followed by an empty line
//        out.println( "blocks" );
        URL url = new URL("http://192.168.0.15:3001/blocks");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("accept", "application/json");
        con.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        System.out.println(reader.readLine());


//        String json = in.readLine();

//        System.out.println(json);
//        BlockChain chain = null;
//        chain = gson.fromJson(json,BlockChain.class);
//        for(Block b : chain.getChain()){
//            System.out.println(b.getData());
//        }

    }
}
