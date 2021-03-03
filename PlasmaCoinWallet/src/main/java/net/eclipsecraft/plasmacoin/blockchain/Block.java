package net.eclipsecraft.plasmacoin.blockchain;


import net.eclipsecraft.plasmacoin.Config;
import net.eclipsecraft.plasmacoin.app.Main;
import net.eclipsecraft.plasmacoin.wallet.Transaction;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Block {
    private long timestamp;
    private String lastHash;
    private String hash;
    private Object data;
    private int difficulty;

    private int nonce;

    public Block(long timestamp, String lastHash, String hash, Object newData, int nonce, int difficulty) {
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = newData;
        this.nonce = nonce;
        this.difficulty = difficulty;
    }

    public long getTimestamp(){
        return this.timestamp;
    }

    public String getLastHash(){
        return this.lastHash;
    }

    public String getHash(){
        return this.hash;
    }

    public Object getData(){
        return this.data;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    public int getNonce(){
        return this.nonce;
    }

    public String toString(){
        String hashValue = String.format("%64s",new BigInteger(this.hash,2).toString(16)).replace(' ','0');
        String str = "Block:\n"
                +"Timestamp : "+this.timestamp+"\n"
                +"Last Hash : "+this.lastHash+"\n"
                +"Hash      : "+this.hash+"\n"
                +"Nonce     : "+this.nonce+"\n"
                +"Difficulty: "+this.difficulty+"\n"
                +"Data      : "+this.data;
        return str;
    }

    public static Block genesis(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        return new Block(12345,"--none--","--first--",transactions,0, Config.DIFFICULTY);
    }

    private static int storedAmount = 0;
    public static Block mineBlock(Block lastBlock, Object data){
        System.out.println("--Found new block--");
        long timestamp = System.currentTimeMillis();
        String lastHash = lastBlock.hash;
        int nonce = 0;
        int difficulty = lastBlock.getDifficulty();
        int repeat = 256-difficulty;
        String hash = Block.doHash(timestamp,lastHash,data,nonce,difficulty);
        while(!hash.matches("0{"+difficulty+"}.{"+repeat+"}")){
            nonce++;
            if(nonce == Integer.MAX_VALUE){
                nonce = 0;
            }
            timestamp = System.currentTimeMillis();
            lastBlock = Main.getCurrentChain().getChain().get(Main.getCurrentChain().getChain().size()-1);
            lastHash = lastBlock.hash;
            difficulty = Block.adjustDifficulty(lastBlock,timestamp);
            repeat = 256-difficulty;
//            System.out.println("Diff: "+difficulty);
//            System.out.println(hash);
            hash = Block.doHash(timestamp,lastHash,data,nonce,difficulty);
        }
        Block minedBlock = new Block(timestamp,lastHash,hash,data,nonce,difficulty);
        System.out.println("----------New Block Mined----------");
//        System.out.println(minedBlock.toString());
        System.out.println("-----------------------------------");
        int storedAmount = 0;
        for(Block b : Main.getCurrentChain().getChain()){
            if(b.data.equals(Main.getName())){
                storedAmount++;
            }
        }
        storedAmount++;
        System.out.println("You have "+storedAmount+" / "+ Main.getCurrentChain().getChain().size());
        return minedBlock;
    }

    public static String blockHash(Block block) {
        long timestamp = block.getTimestamp();
        String lastHash = block.getLastHash();
        Object data = block.getData();
        int nonce = block.getNonce();
        int difficulty = block.getDifficulty();;
        return Block.doHash(timestamp, lastHash, data, nonce, difficulty);
    }

    public static String doHash(long timestamp,String lastHash,Object data, int nonce,int difficulty){
        ArrayList<Byte> bytes = new ArrayList<>();
        for(byte b : longToBytes(timestamp)){
            bytes.add(b);
        }
        for(byte b : lastHash.getBytes(StandardCharsets.UTF_8)){
            bytes.add(b);
        }
        for(byte b : objectToByte(data)){
            bytes.add(b);
        }
        for(byte b : BigInteger.valueOf(nonce).toString(16).getBytes()){
            bytes.add(b);
        }
        for(byte b : BigInteger.valueOf(difficulty).toString(16).getBytes()){
            bytes.add(b);
        }
        byte[] result = new byte[bytes.size()];
        for(int i = 0; i < bytes.size(); i++) {
            result[i] = bytes.get(i).byteValue();
        }
        String sha256hex = DigestUtils.sha256Hex(result);
        String sha256binary = String.format("%256s",new BigInteger(sha256hex, 16).toString(2)).replace(' ','0');
        return sha256binary;
    }

    private static int adjustDifficulty(Block lastBlock, long currentTime){
        long lastTimestamp = lastBlock.getTimestamp();
        int difficulty = (lastTimestamp + Config.MINE_RATE) > currentTime ? (lastBlock.getDifficulty()+1) : lastBlock.getDifficulty()-1;
        return difficulty;
    }

    public static byte[] objectToByte(Object obj){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            byte[] data = bos.toByteArray();
            return data;
        }catch (IOException e){
            return null;
        }
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public Block changeData(Object data){
        this.data = data;
        return this;
    }
}
