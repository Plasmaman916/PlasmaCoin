package net.eclipsecraft.plasmacoin.wallet;

public class Output {
    private Double amount;
    private String recipient;

    public Output(Double amount, String recipient){
        this.amount = amount;
        this.recipient = recipient;
    }

    public Double getAmount(){
        return this.amount;
    }

    public String getRecipient() {
        return recipient;
    }
}
