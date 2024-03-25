package shared.referenceClasses;

public class Performer {

    private String performerID;
    private String performerName;
    private String genre;
    private String performerType;
    private String description;
    private String performerStatus;

    public Performer(String performerID, String performerName, String genre, String performerType, String description, String performerStatus) {
        this.performerID = performerID;
        this.performerName = performerName;
        this.genre = genre;
        this.performerType = performerType;
        this.description = description;
        this.performerStatus = performerStatus;
    }

    public String getPerformerID() {
        return performerID;
    }

    public void setPerformerID(String performerID) {
        this.performerID = performerID;
    }

    public String getPerformerName() {
        return performerName;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPerformerType() {
        return performerType;
    }

    public void setPerformerType(String performerType) {
        this.performerType = performerType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPerformerStatus() {
        return performerStatus;
    }

    public void setPerformerStatus(String performerStatus) {
        this.performerStatus = performerStatus;
    }

    @Override
    public String toString() {
        return "Performer{" +
                "performerID='" + performerID + '\'' +
                ", performerName='" + performerName + '\'' +
                ", genre='" + genre + '\'' +
                ", performerType='" + performerType + '\'' +
                ", description='" + description + '\'' +
                ", performerStatus='" + performerStatus + '\'' +
                '}';
    }
}
