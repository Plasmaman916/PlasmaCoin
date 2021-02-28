package net.eclipsecraft.plasmacoin.wallet;

import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private ArrayList<Output> outputs;

    public Transaction(){
        this.id = UUID.randomUUID();
        outputs = new ArrayList<>();
    }

    public void addOutput(Output output){
        this.outputs.add(output);
    }

    public void addOutput(Double amount,String sender, String recipient){
        Output output = new Output(amount,sender,recipient);
        this.outputs.add(output);
    }

    public static Transaction newTransaction(Wallet senderWallet, String recipient, double amount){
        if(amount>senderWallet.getBalance()){
            System.out.println("You may not send ("+amount+") more than you have! (You have "+senderWallet.getBalance()+")");
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.addOutput(senderWallet.getBalance()-amount, senderWallet.getPublicKeyHex(), recipient);
        return transaction;
    }

    public static void main(String[] args) {
        Wallet wallet = new Wallet();
        Transaction transaction = Transaction.newTransaction(wallet,"a",123);
        String test = (new Gson()).toJson(transaction);
        System.out.println(test);
    }
}
