package net.eclipsecraft.plasmacoin.wallet;

public class Output {
    private Double amount;
    private String sender;
    private String recipient;

    public Output(Double amount, String sender, String recipient){
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public Double getAmount(){
        return this.amount;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }
}
