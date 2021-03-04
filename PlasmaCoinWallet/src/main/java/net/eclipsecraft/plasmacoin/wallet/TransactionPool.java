package net.eclipsecraft.plasmacoin.wallet;

import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionPool {
    private CopyOnWriteArrayList<Transaction> transactions;

    public TransactionPool(){
        this.transactions = new CopyOnWriteArrayList<>();
    }

    public boolean addOrUpdateTransaction(Transaction transaction){
        boolean alreadyExisted = false;
        for(Transaction t : transactions){
            if(t.getId().toString().equals(transaction.getId().toString())){
                if(transaction == t){
                    alreadyExisted = true;
                }
                transactions.remove(t);
            }
        }
        transactions.add(transaction);
        return alreadyExisted;
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
