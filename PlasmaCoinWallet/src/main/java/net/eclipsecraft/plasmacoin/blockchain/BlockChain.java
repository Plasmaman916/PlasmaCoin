package net.eclipsecraft.plasmacoin.blockchain;

import com.google.gson.Gson;
import net.eclipsecraft.plasmacoin.Config;

import java.io.Serializable;
import java.util.ArrayList;

public class BlockChain implements Serializable {
    private ArrayList<Block> chain = new ArrayList<>();
    private static Gson gson = new Gson();

    public BlockChain(){
        chain.add(Block.genesis());
    }

    public Block addBlock(Object data){
        Block block = Block.mineBlock(this.chain.get(this.chain.size()-1),data);
        this.chain.add(block);
        return block;
    }

    public ArrayList<Block> getChain(){
        return this.chain;
    }

    public static boolean isValidChain(ArrayList<Block> chain){
        if(!gson.toJson(chain.get(0)).equals(gson.toJson(Block.genesis()))) {
            System.out.println("Genesis block in invalid");
            return false;
        }
        for(int i = 1; i<chain.size(); i++){
            Block blockToCheck = chain.get(i);
            Block lastBlock = chain.get(i-1);
            long timeTookToMine = blockToCheck.getTimestamp()-lastBlock.getTimestamp();
            int changeInDiff = blockToCheck.getDifficulty()-lastBlock.getDifficulty();
            if(changeInDiff != 0 && changeInDiff != 1 && changeInDiff != -1){
                System.out.println("Invalid Change in diff");
                return false;
            }
            if(changeInDiff == 1){
                if(timeTookToMine >= Config.MINE_RATE){
                    System.out.println("Change in diff up is invalid");
                    return false;
                }
            }
            if(changeInDiff == -1){
                if(timeTookToMine <= Config.MINE_RATE){
                    System.out.println("Change in diff down is invalid");
                    return false;
                }
            }
            if(changeInDiff == 0){
                if(timeTookToMine != Config.MINE_RATE){
                    System.out.println("No change in diff is invalid");
                    return false;
                }
            }
            if(!blockToCheck.getLastHash().equals(lastBlock.getHash())){
                System.out.println("Inorrect lasthash");
                return false;
            }
            String hash1 = blockToCheck.getHash();
            String hash2 = Block.blockHash(blockToCheck);
            if(!hash1.equals(hash2)){
                System.out.println("Invalid (Not Equal): '"+hash1+"' | '"+hash2+"'");
                return false;
            }
            if(blockToCheck.getData().toString().getBytes().length> Config.MAX_BLOCK_SIZE){
                System.out.println("Data in block is too large");
                return false;
            }
        }
        return true;
    }

    public boolean replaceChain(ArrayList<Block> newChain){
        if(newChain.size() <= this.chain.size()){
            System.out.println("New chain is not longer than the current chain!");
            return false;
        }else if(!isValidChain(newChain)){
            System.out.println("New chain is not a valid chain!");
            return false;
        }
        this.chain = newChain;
        System.out.println("Replacing with new chain!");
        return true;
    }
}