package shared.model;

import shared.referenceClasses.*;
import shared.utilityClasses.UtilityMethods;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Optional;

public class Database {

    private static Connection connection;

    private static boolean setConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/vanniinfofo", "root", "");
                return true;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.exit(0);
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

    public static User getTicketUser(String ticketId, String userId) {

        ensureConnection();

        String query = "SELECT * FROM user" +
                " INNER JOIN ticket ON user.userID = ticket.userID" +
                " WHERE ticket.ticketID = ? AND user.userID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticketId);
            statement.setString(2, userId);
            ResultSet resultSet = statement.executeQuery();
            Optional<User> user = toUser(resultSet);
            if(user.isPresent()) {
                return user.get();
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }
        return null;
    }

    public static boolean addPerformer(Performer performer) {

        ensureConnection();
        String query = "INSERT INTO performer(performerID, performerName, genre, performerType, description, performerStatus)" + " VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, performer.getPerformerID());
            preparedStatement.setString(2, performer.getPerformerName());
            preparedStatement.setString(3, performer.getGenre());
            preparedStatement.setString(4, performer.getPerformerType());
            preparedStatement.setString(5, performer.getDescription());
            preparedStatement.setString(6, performer.getPerformerStatus());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean updatePerformer(Performer performer) {

        ensureConnection();

        String query = "UPDATE performer" +
                " SET performerName = ?, genre = ? ,performerType = ? ,description = ? ,performerStatus = ?" +
                " WHERE (`performerID` = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, performer.getPerformerName());
            preparedStatement.setString(2, performer.getGenre());
            preparedStatement.setString(3, performer.getPerformerType());
            preparedStatement.setString(4, performer.getDescription());
            preparedStatement.setString(5, performer.getPerformerStatus());
            preparedStatement.setString(6, performer.getPerformerID());
            preparedStatement.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }

    public static boolean havePurchased(String userID, String liveSetID) {

        ensureConnection();

        String query = "SELECT count(*) FROM purchased" +
                " INNER JOIN user ON user.userID = purchased.buyerID" +
                " INNER JOIN ticket ON purchased.ticketID = ticket.ticketID" +
                " INNER JOIN liveset ON liveset.liveSetID = ticket.liveSetID" +
                " WHERE purchased.buyerID = ? AND liveset.liveSetID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, liveSetID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getInt(1) >= 1;
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return true;
    }

    public static boolean addPurchase(String liveSetId, String buyerId) {

        String query = "INSERT INTO purchased (`purchasedID`, `date`, `time`, `buyerID`, `ticketID`) VALUES (?, ?,  ?, ?,  ?)";
        String newTicket = createNewTicket(liveSetId);
        String purchaseID = UtilityMethods.generateRandomID();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, purchaseID);
            statement.setDate(2, UtilityMethods.getCurrentDate());
            statement.setTime(3, UtilityMethods.getCurrentTime());
            statement.setString(4, buyerId);
            statement.setString(5, newTicket);
            statement.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }

    private static String createNewTicket(String liveSetID) {
        ensureConnection();
        String ticketId = UtilityMethods.generateRandomID();
        String query = "INSERT INTO ticket (`ticketID`, `liveSetID`) VALUES (?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ticketId);
            preparedStatement.setString(2, liveSetID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return ticketId;
    }

    public static LinkedList<Purchased> getMyPurchases(String userID) {
        ensureConnection();

        LinkedList<Purchased> purchasedLinkedList = new LinkedList<>();

        String query = "SELECT purchased.date, purchased.time, performer.performerName, liveset.price, liveset.thumbnail, ticket.ticketID, ticket.status, liveset.livesetID" +
                " FROM purchased" +
                " INNER JOIN user ON purchased.buyerID = user.userID" +
                " INNER JOIN ticket ON purchased.ticketID = ticket.ticketID" +
                " INNER JOIN liveset ON liveset.liveSetID = ticket.liveSetID" +
                " INNER JOIN performer ON liveset.performerID = performer.performerID" +
                " WHERE user.userID = ?" +
                " ORDER BY 3";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);

            ResultSet resultSet  = statement.executeQuery();

            while(resultSet.next()) {
                Date date = resultSet.getDate(1);
                Time time = resultSet.getTime(2);
                String performerName = resultSet.getString(3);
                int liveSetPrice = resultSet.getInt(4);
                String liveSetThumbnail = getImage(resultSet.getString(8), resultSet.getBlob(5));
                String ticketId = resultSet.getString(6);
                String ticketStatus = resultSet.getString(7);

                purchasedLinkedList.add(new Purchased(date, time, performerName, liveSetPrice, liveSetThumbnail, ticketId, ticketStatus));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return purchasedLinkedList;
    }

    public static LinkedList<LiveSet> getLiveSets() {

        ensureConnection();

        LinkedList<LiveSet> liveSets = new LinkedList<>();

        String query = "SELECT * from liveset";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
            System.out.println(e.getMessage());
//            System.err.println("Having error executing query " + query);
            return new LinkedList<>();
        }

        return liveSets;
    }

    public static LinkedList<LastWatched> getLastWatched() {

        ensureConnection();

        LinkedList<LastWatched> lastWatched = new LinkedList<>();

        String query = "SELECT * FROM lastwatched";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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



    public static LinkedList<Ticket> getTickets() {
        ensureConnection();

        LinkedList<Ticket> tickets = new LinkedList<>();

        String query = "SELECT * FROM ticket";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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

        String query = "SELECT * FROM user ORDER BY userID desc";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
           System.err.println("Having error converting image  on: " + liveSetID);
        }
        return "";
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
