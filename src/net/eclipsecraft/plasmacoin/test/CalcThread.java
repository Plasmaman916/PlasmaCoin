package net.eclipsecraft.plasmacoin.test;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;

public class CalcThread extends Thread{
    public CalcThread(){
        this.start();
    }

    public static byte[] objToByte(Object o){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.flush();
            byte[] yourBytes = bos.toByteArray();
            return yourBytes;
        } catch (IOException e) { e.printStackTrace(); return null;} finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    @Override
    public void run() {
        super.run();
        if(1==1) {
            String UUID = "";
            synchronized (POWTest.uSync) {
                UUID = POWTest.randomUUID.toString();
            }
            String crypt = "11111111";
            int diff = 20;
            String diffcalc = "";
            for (int i = 0; i < diff; i++) {
                diffcalc = diffcalc + "0";
            }
            crypt = doCalc(0, UUID);
            synchronized (POWTest.getLocSync()) {
                POWTest.loc = POWTest.loc + 1;
            }
            int loc = 0;
            while (!crypt.substring(0, diff).equals(diffcalc) && !POWTest.cryptFound) {
                synchronized (POWTest.getLocSync()) {
                    loc = POWTest.loc;
                }
                crypt = doCalc(loc, UUID);
                synchronized (POWTest.getLocSync()) {
                    POWTest.loc = POWTest.loc + 1;
                }
                if (loc % 2000000 == 0 && loc != 0) {
                    System.out.println("Location: " + loc);
                    System.out.println(crypt);
                }
            }
            if (crypt.substring(0, diff).equals(diffcalc)) {
                POWTest.foundCrypt(crypt, loc);
            }
        }
    }

    private static String doCalc(Object data, String uuid){
        int byteLoc = 0;
        ArrayList<Byte> arrayBytes = new ArrayList<>();
        for(byte b : objToByte(data)){
            arrayBytes.add(b);
        }
        for(byte b : uuid.getBytes()){
            arrayBytes.add(b);
        }
        byte[] bytes = new byte[arrayBytes.size()];
        for(byte b : arrayBytes){
            bytes[byteLoc] = b;
            byteLoc++;
        }
        String hex = DigestUtils.sha512Hex(bytes);
        arrayBytes.clear();
        bytes=null;
        String big = String.format("%512s", new BigInteger(hex,16).toString(2));
        return big.replaceAll(" ","0");
    }
}
