package net.eclipsecraft.plasmacoin.wallet;

import org.apache.commons.codec.digest.DigestUtils;

public class Input {
    private long timestamp;
    private Double amount;
    private String address;
    private String signature;

    public Input(Wallet wallet, Transaction transaction){
        this.timestamp = System.currentTimeMillis();
        this.address = wallet.getPublicKeyHex();
        this.amount = wallet.getBalance();
        byte[] data = DigestUtils.sha256(transaction.outputsToString().getBytes());
        this.signature = wallet.sign(data);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public String getAddress() {
        return address;
    }

    public String getSignature() {
        return signature;
    }
}
