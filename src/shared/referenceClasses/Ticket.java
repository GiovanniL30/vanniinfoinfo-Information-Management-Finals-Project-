package shared.referenceClasses;

public class Ticket {

    private String ticketID;
    private String liveSetID;
    private String status;
    private String userID;

    public Ticket(String ticketID, String liveSetID, String status, String userID) {
        this.ticketID = ticketID;
        this.liveSetID = liveSetID;
        this.status = status;
        this.userID = userID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getLiveSetID() {
        return liveSetID;
    }

    public void setLiveSetID(String liveSetID) {
        this.liveSetID = liveSetID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
