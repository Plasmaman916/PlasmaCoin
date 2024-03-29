package net.eclipsecraft.plasmacoin.Objects;

import com.google.gson.Gson;
import net.eclipsecraft.plasmacoin.app.Main;
import net.eclipsecraft.plasmacoin.app.PeerToPeerServer;
import net.eclipsecraft.plasmacoin.blockchain.Block;
import net.eclipsecraft.plasmacoin.wallet.Transaction;

import java.io.IOException;
import java.util.ArrayList;

public class UserHandler extends Thread{

    private PeerToPeerServer p2pserver;

    public UserHandler(PeerToPeerServer p2pserver){
        this.p2pserver = p2pserver;
        this.start();
    }

    @Override
    public void run() {
        super.run();
        while(true){
            try{
                for(User u : p2pserver.getUserMap().get(this)){
                    boolean ready = false;
                    try{
                        ready = u.ready();
                    }catch (IOException e){}
                    if(!ready){
                        continue;
                    }
                    String message = null;
                    try{
                        message = u.getIncomingMessage();
                    }catch (IOException e){}
                    if(message == null) continue;
                    handleMessage(u,message);
                }
            }catch (NullPointerException e){
                continue;
            }
        }
    }

    private Gson gson = new Gson();
    private void handleMessage(User u, String message){
        if(u.getSock().isClosed()){
            p2pserver.getUserMap().get(this).remove(u);
            return;
        }
        if(message.equalsIgnoreCase("keepAlive")){
            u.sendMessage("alive");
            u.setAlive(true);
            return;
        }
        if(message.equalsIgnoreCase("alive")){
            u.setAlive(true);
            return;
        }
//        System.out.println(message);
        Message message1 = gson.fromJson(message, Message.class);
        if(message1.getType().equalsIgnoreCase("chain")) {
            ArrayList<Block> chain = new ArrayList<>();
            chain = ChainMessage.getChainFromJson(message1.getMessage());
            if (chain == null) {
                return;
            }
            boolean newChain = Main.getCurrentChain().replaceChain(chain);
            if (newChain) {
                System.out.println("New Chain good, syncing with peers");
                int storedAmount = 0;
                for (Block b : Main.getCurrentChain().getChain()) {
                    if (b.getData().equals(Main.getName())) {
                        storedAmount++;
                    }
                }
                storedAmount++;
                System.out.println("You have " + storedAmount + " / " + Main.getCurrentChain().getChain().size());
                syncChains(chain);
            }
        }else if(message1.getType().equalsIgnoreCase("transaction")){
            Transaction transaction = gson.fromJson(message,Transaction.class);
            boolean existed = Main.getPool().addOrUpdateTransaction(transaction);
            if(!existed){
                broadcastTransaction(transaction);
                System.out.println("Broadcasting!!!!!!!!!");
            }
        }
    }

    public void syncChains(ArrayList<Block> chain){
        System.out.println("Handler is syncing chain");
        for(User u : p2pserver.getUserMap().get(this)){
            u.sendMessage(newMessage("chain",ChainMessage.chainToJson(chain)));
        }
    }

    public void broadcastTransaction(Transaction transaction){
        for(User u : p2pserver.getUserMap().get(this)){
            u.sendMessage(newMessage("transaction",gson.toJson(transaction)));
        }
    }

    public String newMessage(String type, String message){
        Message message1 = new Message(type,message);
        return gson.toJson(message1);
    }
}
