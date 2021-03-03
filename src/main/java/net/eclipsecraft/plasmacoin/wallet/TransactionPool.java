package net.eclipsecraft.plasmacoin.wallet;

import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionPool {
    private CopyOnWriteArrayList<Transaction> transactions;

    public TransactionPool(){
        this.transactions = new CopyOnWriteArrayList<>();
    }

    public void addOrUpdateTransaction(Transaction transaction){
        for(Transaction t : transactions){
            if(t.getId().toString().equals(transaction.getId().toString())){
                transactions.remove(t);
            }
        }
        transactions.add(transaction);
    }

    public CopyOnWriteArrayList<Transaction> getTransactions(){
        return (CopyOnWriteArrayList<Transaction>) transactions.clone();
    }

    public Transaction existingTransaction(String pubKeyHex){
        for(Transaction t : transactions){
            if(t.getInput().getAddress().equals(pubKeyHex)){
                return t;
            }
        }
        return null;
    }

    public void clear(){
        transactions.clear();
    }

}
