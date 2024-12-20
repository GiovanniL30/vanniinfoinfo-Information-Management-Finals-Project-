package shared.referenceClasses;

import java.util.Optional;

public class User {

    private String userID;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private int watchedConsecutiveShows;
    private String userStatus;
    private Optional<LoyaltyCard> loyaltyCard;
    private String userType;

    public User(String userID, String firstName, String lastName, String userName, String email, String password, int watchedConsecutiveShows, String userStatus, Optional<LoyaltyCard> loyaltyCard, String userType) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.watchedConsecutiveShows = watchedConsecutiveShows;
        this.userStatus = userStatus;
        this.loyaltyCard = loyaltyCard;
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWatchedConsecutiveShows() {
        return watchedConsecutiveShows;
    }

    public void setWatchedConsecutiveShows(int watchedConsecutiveShows) {
        this.watchedConsecutiveShows = watchedConsecutiveShows;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }


    public Optional<LoyaltyCard> getLoyaltyCard() {
        return loyaltyCard;
    }
}
