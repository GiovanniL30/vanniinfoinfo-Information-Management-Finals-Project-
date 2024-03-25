package shared.referenceClasses;

import java.sql.Date;
import java.sql.Time;

public class Purchased {

    private String purchasedID;
    private Date date;
    private Time time;
    private String buyerID;
    private String ticketID;

    public Purchased(String purchasedID, Date date, Time time, String buyerID, String ticketID) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
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
