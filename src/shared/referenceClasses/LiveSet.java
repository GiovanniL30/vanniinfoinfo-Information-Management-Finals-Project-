package shared.referenceClasses;

import java.sql.Time;
import java.sql.Date;


public class LiveSet {

    private String liveSetID;
    private String status;
    private int price;
    private Date date;
    private Time time;
    private String thumbnail;
    private String streamLinkURL;
    private String performerID;

    public LiveSet() {
        this.liveSetID = "";
        this.status = "";
        this.price = 0;
        this.date = new Date(1);
        this.time = new Time(1);
        this.thumbnail = "";
        this.streamLinkURL = "";
        this.performerID = "";
    }

    public LiveSet(String liveSetID, String status, int price, Date date, Time time, String thumbnail, String streamLinkURL, String performerID) {
        this.liveSetID = liveSetID;
        this.status = status;
        this.price = price;
        this.date = date;
        this.time = time;
        this.thumbnail = thumbnail;
        this.streamLinkURL = streamLinkURL;
        this.performerID = performerID;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStreamLinkURL() {
        return streamLinkURL;
    }

    public void setStreamLinkURL(String streamLinkURL) {
        this.streamLinkURL = streamLinkURL;
    }

    public String getPerformerID() {
        return performerID;
    }

    public void setPerformerID(String performerID) {
        this.performerID = performerID;
    }

    @Override
    public String toString() {
        return "LiveSet{" +
                "liveSetID='" + liveSetID + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", time=" + time +
                ", thumbnail='" + thumbnail + '\'' +
                ", streamLinkURL='" + streamLinkURL + '\'' +
                ", performerID='" + performerID + '\'' +
                '}';
    }
}
