package net.eclipsecraft.plasmacoin.wallet;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private ArrayList<Output> outputs;
    private Input input;

    public Transaction(){
        this.id = UUID.randomUUID();
        outputs = new ArrayList<>();
    }

    public void addOutput(Output output){
        this.outputs.add(output);
    }

    public void addOutput(Double amount, String recipient){
        Output output = new Output(amount,recipient);
        this.outputs.add(output);
    }

    public void update(Wallet sender, String recipiet, Double amount){
        if(!outputs.get(0).getRecipient().equals(sender.getPublicKeyHex())){
            System.out.println("Error in outputs. Please recreate the transaction!");
            return;
        }
        if(amount>(sender.getBalance()-getTotalSentAmount())){
            System.out.println("You cannot send more that you have. ");
            return;
        }
        outputs.set(0,new Output(outputs.get(0).getAmount()-amount,sender.getPublicKeyHex()));
        addOutput(amount,recipiet);
        this.signTransaction(sender);
    }

    public UUID getId(){
        return this.id;
    }

    private double getTotalSentAmount(){
        double amount = 0;
        int loc = 0;
        for(Output o : outputs){
            if(loc==0){
                loc++;
                continue;
            }
            amount+=o.getAmount();
            loc++;
        }
        return amount;
    }

    public Input getInput(){
        return this.input;
    }

    public static Transaction newTransaction(Wallet senderWallet, String recipient, double amount){
        if(amount>senderWallet.getBalance()){
            System.out.println("You may not send ("+amount+") more than you have! (You have "+senderWallet.getBalance()+")");
            return null;
        }
        Transaction transaction = new Transaction();
        transaction.addOutput(senderWallet.getBalance()-amount, senderWallet.getPublicKeyHex());
        transaction.addOutput(amount, recipient);
        transaction.signTransaction(senderWallet);
        return transaction;
    }

    public String toString(){
        return new Gson().toJson(this);
    }

    public String outputsToString(){
        return new Gson().toJson(this.outputs);
    }

    public void signTransaction(Wallet senderWallet){
        this.input = new Input(senderWallet,this);
    }

    public boolean verify(){
        String pubKey = this.input.getAddress();
        String signature = this.input.getSignature();
        String data = this.outputsToString();
        try {
            byte[] signatureBytes = new BigInteger(signature,16).toByteArray();
            byte[] addressBytes = new BigInteger(pubKey,16).toByteArray();
            byte[] dataBytes = DigestUtils.sha256(data.getBytes());

            PublicKey publicKey =
                    KeyFactory.getInstance("ECDsA").generatePublic(new X509EncodedKeySpec(addressBytes));

            Signature sig = Signature.getInstance("ECDsA");
            sig.initVerify(publicKey);
            sig.update(dataBytes);
            return sig.verify(signatureBytes);
        }catch (SignatureException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifySignature(String pubKey, String signature, String data){
        try {
            byte[] signatureBytes = new BigInteger(signature,16).toByteArray();
            byte[] addressBytes = new BigInteger(pubKey,16).toByteArray();
            byte[] dataBytes = DigestUtils.sha256(data.getBytes());

            PublicKey publicKey =
                    KeyFactory.getInstance("ECDsA").generatePublic(new X509EncodedKeySpec(addressBytes));

            Signature sig = Signature.getInstance("ECDsA");
            sig.initVerify(publicKey);
            sig.update(dataBytes);
            return sig.verify(signatureBytes);
        }catch (SignatureException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException e){
            e.printStackTrace();
            return false;
        }
    }
}
