package shared.referenceClasses;

public class LoyaltyCard {

    private String loyaltyCardID;
    private String userID;

    public LoyaltyCard(String loyaltyCardID, String userID) {
        this.loyaltyCardID = loyaltyCardID;
        this.userID = userID;
    }

    public String getLoyaltyCardID() {
        return loyaltyCardID;
    }

    public void setLoyaltyCardID(String loyaltyCardID) {
        this.loyaltyCardID = loyaltyCardID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
