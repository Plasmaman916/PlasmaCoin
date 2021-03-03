package net.eclipsecraft.plasmacoin.wallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Wallet {
    private double balance;
    private KeyPair keyPair;

    public Wallet(){
        KeyPairGenerator keyGen = null;
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            keyGen = KeyPairGenerator.getInstance("ECDsA", "BC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
            keyGen.initialize(ecSpec, new SecureRandom());
        }catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e){
            e.printStackTrace();
        }
        this.balance = 1000d;
        this.keyPair = keyGen.generateKeyPair();
    }

    public Wallet(String path){
        this.balance = 1000d;
        this.loadKeyPair(path);
    }

    public String sign(byte[] data){
        try {
            Signature sig = Signature.getInstance("ECDsA");
            sig.initSign(keyPair.getPrivate());
            sig.update(data);
            byte[] signatureBytes = sig.sign();
            return new BigInteger(signatureBytes).toString(16);
        }catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e){
            return null;
        }
    }

    public PublicKey getPublicKey(){
        return this.keyPair.getPublic();
    }

    public String getPublicKeyHex(){
        byte[] key = this.keyPair.getPublic().getEncoded();
        BigInteger bigInteger = new BigInteger(key);
        return bigInteger.toString(16);
    }

    public double getBalance(){
        return this.balance;
    }

    public Transaction createTransaction(String recipient, double amount, TransactionPool pool){
        if(amount>balance){
            System.out.println("You cannot send more than you have!");
            return null;
        }
        Transaction transaction = pool.existingTransaction(getPublicKeyHex());
        if(transaction == null){
            transaction = Transaction.newTransaction(this,recipient,amount);
        }else{
            transaction.update(this,recipient,amount);
        }
        pool.addOrUpdateTransaction(transaction);
        return transaction;
    }

    public boolean saveKeyPair(String path) {
        path = System.getProperty("user.dir")+"\\"+path;
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdir();
        }
        try {
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // Store Public Key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded(),"AES_256_CBC");
            FileOutputStream fos = new FileOutputStream(path + "/public.key");
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();

            // Store Private Key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded(),"AES_256_CBC");
            fos = new FileOutputStream(path + "/private.key");
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadKeyPair(String path) {
        path = System.getProperty("user.dir")+"\\"+path;
        File dir = new File(path);
        if(!dir.exists()){
            return false;
        }
        try {
            // Read Public Key.
            File filePublicKey = new File(path + "/public.key");
            FileInputStream fis = new FileInputStream(path + "/public.key");
            byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
            fis.read(encodedPublicKey);
            fis.close();

            // Read Private Key.
            File filePrivateKey = new File(path + "/private.key");
            fis = new FileInputStream(path + "/private.key");
            byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();

            // Generate KeyPair.
            KeyFactory keyFactory = KeyFactory.getInstance("ECDsA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey,"AES_256_CBC");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey,"AES_256_CBC");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            keyPair = new KeyPair(publicKey, privateKey);
            return true;
        }catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e){
            e.printStackTrace();
            return false;
        }
    }
}
