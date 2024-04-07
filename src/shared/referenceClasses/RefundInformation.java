package shared.referenceClasses;

public class RefundInformation {

    private String buyerID;
    private String ticketID;
    private int amount;


    public RefundInformation(String buyerID, String ticketID, int amount) {
        this.buyerID = buyerID;
        this.ticketID = ticketID;
        this.amount = amount;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public int getAmount() {
        return amount;
    }
}
