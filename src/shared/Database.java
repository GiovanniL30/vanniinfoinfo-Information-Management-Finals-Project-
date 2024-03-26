package shared;

import shared.referenceClasses.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.Optional;

public class Database {

    private static Connection connection;

    private static boolean setConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/vanniinfoinfo", "root", "password");
                return true;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

        return false;
    }

    public static Optional<User> logIn(String giveUserName, String givenPassword) {

        ensureConnection();

        String query = "SELECT * FROM user WHERE userName = ? AND password = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, giveUserName);
            statement.setString(2, givenPassword);

            ResultSet resultSet  = statement.executeQuery();

            if(resultSet.next()) {
                return toUser(resultSet);
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }
        return Optional.empty();
    }

    public static LinkedList<LiveSet> getLiveSets() {

        ensureConnection();

        LinkedList<LiveSet> liveSets = new LinkedList<>();

        String query = "SELECT * from liveset";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                String liveSetID = result.getString(1);
                String status = result.getString(2);
                Date date = result.getDate(3);
                Time time = result.getTime(4);
                String thumbnail = getImage(liveSetID, result.getBlob(5));
                String streamLinkUrl = result.getString(6);
                String performerID = result.getString(7);
                int price = result.getInt(8);

                liveSets.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }

        return liveSets;
    }

    public static LinkedList<LastWatched> getLastWatched() {

        ensureConnection();

        LinkedList<LastWatched> lastWatched = new LinkedList<>();

        String query = "SELECT * FROM lastwatched";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {

                String lastWatchedID = result.getString(1);
                String userID = result.getString(2);
                String liveSetID = result.getString(3);

                lastWatched.add(new LastWatched(lastWatchedID, userID, liveSetID));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }

        return lastWatched;
    }

    public static LinkedList<LoyaltyCard> getLoyaltyCards() {
        ensureConnection();

        LinkedList<LoyaltyCard> loyaltyCards = new LinkedList<>();

        String query = "SELECT * FROM loyaltycard";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                String loyaltyCardId = resultSet.getString(1);
                String userID = resultSet.getString(2);

                loyaltyCards.add(new LoyaltyCard(loyaltyCardId, userID));
            }
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }


        return loyaltyCards;
    }

    public static LinkedList<Performer> getPerformers() {
        ensureConnection();

        LinkedList<Performer> performers = new LinkedList<>();

        String query = "SELECT * FROM performer";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {

                String performerID = resultSet.getString(1);
                String performerName = resultSet.getString(2);
                String genre = resultSet.getString(3);
                String performerType = resultSet.getString(4);
                String description = resultSet.getString(5);
                String performerStatus = resultSet.getString(6);

                performers.add(new Performer(performerID, performerName, genre, performerType, description, performerStatus));
            }
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }


        return performers;
    }


    public static LinkedList<Purchased> getPurchased() {
        ensureConnection();

        LinkedList<Purchased> purchased = new LinkedList<>();

        String query = "SELECT * FROM purchased";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {

                String purchaseID = resultSet.getString(1);
                Date date = resultSet.getDate(2);
                Time time = resultSet.getTime(3);
                String buyerId = resultSet.getString(4);
                String ticketId = resultSet.getString(5);
                purchased.add(new Purchased(purchaseID, date, time, buyerId, ticketId));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }


        return purchased;
    }

    public static LinkedList<Ticket> getTickets() {
        ensureConnection();

        LinkedList<Ticket> tickets = new LinkedList<>();

        String query = "SELECT * FROM ticket";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {

                String ticketId = resultSet.getString(1);
                String liveSetId = resultSet.getString(2);
                String status = resultSet.getString(3);
                String userId = resultSet.getString(4);

                tickets.add(new Ticket(ticketId, liveSetId, status, userId));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }


        return tickets;
    }

    public static LinkedList<User> getUsers() {
        ensureConnection();

        LinkedList<User> users = new LinkedList<>();

        String query = "SELECT * FROM user";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Optional<User> user = toUser(resultSet);
                user.ifPresent(users::add);
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }


        return users;
    }



    private static String getImage(String liveSetID, Blob blob) {
        String path = "resources/images/" + liveSetID + ".jpg";

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            fileOutputStream.write(bytes);
            return path;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ensureConnection() {
      setConnection();
    }

    private static Optional<User> toUser(ResultSet resultSet) {
        try {
            String userId = resultSet.getString(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String userName = resultSet.getString(4);
            String email = resultSet.getString(5);
            String password = resultSet.getString(6);
            int watchedCons = resultSet.getInt(7);
            String userStatus = resultSet.getString(8);
            boolean haveEarnedLoyalty = resultSet.getInt(9) == 1;
            String userType = resultSet.getString(10);
            return Optional.of(new User(userId, firstName, lastName, userName, email, password, watchedCons, userStatus, haveEarnedLoyalty, userType));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return Optional.empty();
    }

}
