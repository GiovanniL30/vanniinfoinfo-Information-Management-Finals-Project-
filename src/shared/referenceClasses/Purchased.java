package shared.referenceClasses;

import java.sql.Date;
import java.sql.Time;

public class Purchased {

   private Date date;
   private Time time;
   private String performerName;
   private int liveSetPrice;
   private String liveSetThumbnail;
   private String ticketId;
   private String ticketStatus;
   private String userName;
   private String liveSetStatus;

    public Purchased(Date date, Time time, String performerName, int liveSetPrice, String liveSetThumbnail, String ticketId, String ticketStatus, String userName, String liveSetStatus) {
        this.date = date;
        this.time = time;
        this.performerName = performerName;
        this.liveSetPrice = liveSetPrice;
        this.liveSetThumbnail = liveSetThumbnail;
        this.ticketId = ticketId;
        this.ticketStatus = ticketStatus;
        this.userName = userName;
        this.liveSetStatus = liveSetStatus;
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

    public String getPerformerName() {
        return performerName;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public int getLiveSetPrice() {
        return liveSetPrice;
    }

    public void setLiveSetPrice(int liveSetPrice) {
        this.liveSetPrice = liveSetPrice;
    }

    public String getLiveSetThumbnail() {
        return liveSetThumbnail;
    }

    public void setLiveSetThumbnail(String liveSetThumbnail) {
        this.liveSetThumbnail = liveSetThumbnail;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLiveSetStatus() {
        return liveSetStatus;
    }

    public void setLiveSetStatus(String liveSetStatus) {
        this.liveSetStatus = liveSetStatus;
    }

    @Override
    public String toString() {
        return "Purchased{" +
                "date=" + date +
                ", time=" + time +
                ", performerName='" + performerName + '\'' +
                ", liveSetPrice=" + liveSetPrice +
                ", liveSetThumbnail='" + liveSetThumbnail + '\'' +
                ", ticketId='" + ticketId + '\'' +
                ", ticketStatus='" + ticketStatus + '\'' +
                '}';
    }
}
