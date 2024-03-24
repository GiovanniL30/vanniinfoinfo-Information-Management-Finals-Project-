package shared.referenceClasses;

public class Purchased {

    private String purchasedID;
    private String date;
    private String time;
    private String buyerID;
    private String ticketID;

    public Purchased(String purchasedID, String date, String time, String buyerID, String ticketID) {
        this.purchasedID = purchasedID;
        this.date = date;
        this.time = time;
        this.buyerID = buyerID;
        this.ticketID = ticketID;
    }

    public String getPurchasedID() {
        return purchasedID;
    }

    public void setPurchasedID(String purchasedID) {
        this.purchasedID = purchasedID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuyerID() {
        return buyerID;
    }

    public void setBuyerID(String buyerID) {
        this.buyerID = buyerID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }
}
