package net.eclipsecraft.plasmacoin.wallet;

public class Test {
    public static void main(String[] args) {
        Wallet wallet = new Wallet();
//        Transaction transaction = Transaction.newTransaction(wallet,"recipient",100d);
//        System.out.println("Expectation: "+wallet.getBalance() + " | Reality: "+ transaction.getInput().getAmount());
//        System.out.println(transaction.toString());
//        transaction.update(wallet,"cool",50d);
//        System.out.println(transaction.toString());
//        System.out.println(Transaction.verifySignature(transaction.getInput().getAddress(),transaction.getInput().getSignature(),transaction.outputsToString()));
//        System.out.println(transaction.verify());
//        System.out.println("AAA");
//        TransactionPool pool = new TransactionPool();
//        pool.addOrUpdateTransaction(transaction);
//        pool.addOrUpdateTransaction(new Transaction());
//        System.out.println(pool.getTransactions().size());
        TransactionPool pool = new TransactionPool();
        wallet.createTransaction("Hello",500.0001d,pool);
        wallet.createTransaction("neat",500d,pool);
        System.out.println(pool.getTransactions().size());
        for(Transaction t : pool.getTransactions()){
            System.out.println(t.outputsToString());
        }
    }
}
