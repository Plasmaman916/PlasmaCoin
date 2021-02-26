package net.eclipsecraft.plasmacoin.wallet;

import org.apache.commons.lang3.tuple.Pair;

public class Wallet {
    private double balance;
    private Pair<String,String> keyPair;
    private String publicKey;

    public Wallet(){
        this.balance = 0;
        this.keyPair = null;
        this.publicKey = null;
    }
}
