package net.eclipsecraft.plasmacoin.Objects;

import com.google.gson.Gson;
import net.eclipsecraft.plasmacoin.blockchain.Block;

import java.util.ArrayList;

public class ChainMessage {
    private ArrayList<Block> chain;

    public ChainMessage(ArrayList<Block> chain){
        this.chain = chain;
    }

    public ArrayList<Block> getChain() {
        return chain;
    }

    public static ArrayList<Block> getChainFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, ChainMessage.class).getChain();
    }

    public static String chainToJson(ArrayList<Block> chain){
        Gson gson = new Gson();
        return gson.toJson(new ChainMessage(chain));
    }

}
