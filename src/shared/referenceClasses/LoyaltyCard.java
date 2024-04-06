package shared.referenceClasses;

import java.sql.Date;

public class LoyaltyCard {

    private String loyaltyCardID;
    private Date dateReceived;

    public LoyaltyCard(String loyaltyCardID, Date dateReceived) {
        this.loyaltyCardID = loyaltyCardID;
        this.dateReceived = dateReceived;
    }

    public String getLoyaltyCardID() {
        return loyaltyCardID;
    }

    public void setLoyaltyCardID(String loyaltyCardID) {
        this.loyaltyCardID = loyaltyCardID;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }
}
