package shared.referenceClasses;

public class LastWatched {

    private String lastWatchedID;
    private String userID;
    private String liveSetID;

    public LastWatched() {
        this.lastWatchedID = "";
        this.userID = "";
        this.liveSetID = "";
    }

    public LastWatched(String lastWatchedID, String userID, String liveSetID) {
        this.lastWatchedID = lastWatchedID;
        this.userID = userID;
        this.liveSetID = liveSetID;
    }

    public String getLastWatchedID() {
        return lastWatchedID;
    }

    public String getUserID() {
        return userID;
    }

    public String getLiveSetID() {
        return liveSetID;
    }

    public void setLastWatchedID(String lastWatchedID) {
        this.lastWatchedID = lastWatchedID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setLiveSetID(String liveSetID) {
        this.liveSetID = liveSetID;
    }
}
