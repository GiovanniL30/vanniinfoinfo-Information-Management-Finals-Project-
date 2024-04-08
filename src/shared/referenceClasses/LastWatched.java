package shared.referenceClasses;

public class LastWatched {

    private String lastWatchedID;
    private String userName;
    private String liveSetID;

    public LastWatched() {
        this.lastWatchedID = "";
        this.userName = "";
        this.liveSetID = "";
    }

    public LastWatched(String lastWatchedID, String userName, String liveSetID) {
        this.lastWatchedID = lastWatchedID;
        this.userName = userName;
        this.liveSetID = liveSetID;
    }

    public String getLastWatchedID() {
        return lastWatchedID;
    }

    public String getUserName() {
        return userName;
    }

    public String getLiveSetID() {
        return liveSetID;
    }

    public void setLastWatchedID(String lastWatchedID) {
        this.lastWatchedID = lastWatchedID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLiveSetID(String liveSetID) {
        this.liveSetID = liveSetID;
    }
}
