package net.eclipsecraft.plasmacoin.test;

import java.util.ArrayList;
import java.util.UUID;

public class POWTest {
    private static Object locSync = new Object();
    public static Object getLocSync(){
        return locSync;
    }

    public static int loc = 0;

    private static ArrayList<CalcThread> threads = new ArrayList<>();

    public static boolean cryptFound = false;
    private static String bin = "";

    public static void foundCrypt(String binary,int location){
        bin=binary;
        cryptFound=true;
    }

    public static Object uSync = new Object();
    public static UUID randomUUID = UUID.randomUUID();

    private static Object sync = new Object();
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Stating with "+randomUUID);
        for(int i = 0; i < 8; i++){
            CalcThread thread = new CalcThread();
            threads.add(thread);
        }
        synchronized (sync) {
            while (!cryptFound) {
                sync.wait(10);
            }
        }
        System.out.println("FOUNNNNDDD!!!!");
        System.out.println(bin);
        System.out.println(loc);
    }

}
