package shared.model;

import shared.referenceClasses.*;
import shared.utilityClasses.UtilityMethods;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Optional;

public class Database {

    private static Connection connection;

    private static boolean setConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/updatedvaniinfofo", "root", "");
                return true;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.exit(0);
            }
        }

        return false;
    }

    public static Response<String> signUp(String firstName, String lastName, String userName, String email, String password) {
        ensureConnection();
    
        String query = "INSERT INTO user(firstName, lastName, userName, email, password) VALUES (?,?,?,?,?)";
    
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, userName);
            statement.setString(4, email);
            statement.setString(5, password);
            statement.execute();
            return new Response<>("User signed up successfully", true);
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }
    
        return new Response<>("Error occurred while signing up user", false);
    }
    public static Response<Optional<User>> logIn(String giveUserName, String givenPassword) {

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
        return new Response<>(Optional.empty(), false);
    }

    public static Response<User> getTicketUser(String ticketId, String userId) {

        ensureConnection();

        String query = "SELECT * FROM user" +
                " INNER JOIN ticket ON user.userID = ticket.userID" +
                " WHERE ticket.ticketID = ? AND user.userID = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ticketId);
            statement.setString(2, userId);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Optional<User> user = toUser(resultSet).getPayload();
                if(user.isPresent()) {
                    return new Response<>(user.get(), true);
                }
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }
        return new Response<>(new User("","","","","","",0,"",false,""), false);
    }

    public static boolean addLiveSet(LiveSet liveSet) {
        ensureConnection();

        String query = "INSERT INTO liveset(liveSetID, status, date, time, thumbnail, streamLinkURL, performerID, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSet.getLiveSetID());
            preparedStatement.setString(2, liveSet.getStatus());
            preparedStatement.setDate(3, liveSet.getDate());
            preparedStatement.setTime(4, liveSet.getTime());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (InputStream fis = new FileInputStream(liveSet.getThumbnail())) {
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                preparedStatement.setBinaryStream(5, new ByteArrayInputStream(outputStream.toByteArray()));
            }

            preparedStatement.setString(6, liveSet.getStreamLinkURL());
            preparedStatement.setString(7, liveSet.getPerformerID());
            preparedStatement.setInt(8, liveSet.getPrice());
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static boolean editLiveSet(LiveSet liveSet) {
        ensureConnection();
        String query = "UPDATE liveset SET status=?, date=?, time=?, thumbnail=?, streamLinkURL=?, performerID=?, price=? WHERE liveSetID=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSet.getStatus());
            preparedStatement.setDate(2, liveSet.getDate());
            preparedStatement.setTime(3, liveSet.getTime());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (InputStream fis = new FileInputStream(liveSet.getThumbnail())) {
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                preparedStatement.setBinaryStream(4, new ByteArrayInputStream(outputStream.toByteArray()));
            } catch (IOException e) {
                // In case thumbnail is not updated, handle it here.
                System.err.println("Error reading file: " + e.getMessage());
                preparedStatement.setBytes(4, null); // Set thumbnail to null in case of error
            }

            preparedStatement.setString(5, liveSet.getStreamLinkURL());
            preparedStatement.setString(6, liveSet.getPerformerID());
            preparedStatement.setInt(7, liveSet.getPrice());
            preparedStatement.setString(8, liveSet.getLiveSetID());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public static Response<String> accessLiveSet(String ticketId, String userId, String liveSetBeingAccessedID) {

        if(isTicketUsed(ticketId)) {
            return new Response<>("The ticket was already used", false);
        }

       Optional<Ticket> temp =  getTickets().getPayload().stream().filter(ticket -> ticket.getTicketID().equals(ticketId) && ticket.getLiveSetID().equals(liveSetBeingAccessedID)).findFirst();
       if(temp.isEmpty()) {
            return new Response<>("The ticket entered was not for this live set", false);
       }

       if(updateTicketUser(ticketId, userId) && addLastWatched(userId, liveSetBeingAccessedID)) {
           return new Response<>("Success", true);
       }else {
           return new Response<>("Having error accessing the liveset", false);
       }
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
        String newTicket = createNewTicket(liveSetId).getPayload();
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

    private static Response<String> createNewTicket(String liveSetID) {
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

        return new Response<>(ticketId, true);
    }

    public static Response<LinkedList<Purchased>> getMyPurchases(String userID) {
        ensureConnection();

        LinkedList<Purchased> purchasedLinkedList = new LinkedList<>();

        String query = "SELECT purchased.date, purchased.time, performer.performerName, liveset.price, liveset.thumbnail, ticket.ticketID, ticket.status, liveset.livesetID, concat(user.firstName, user.lastName)" +
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
                String liveSetThumbnail = getImage(resultSet.getString(8), resultSet.getBlob(5)).getPayload();
                String ticketId = resultSet.getString(6);
                String ticketStatus = resultSet.getString(7);
                String userName = resultSet.getString(9);

                purchasedLinkedList.add(new Purchased(date, time, performerName, liveSetPrice, liveSetThumbnail, ticketId, ticketStatus, userName));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return new Response<>(purchasedLinkedList, true);
    }

    public static Response<LinkedList<LiveSet>> getLiveSets() {

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
                String thumbnail = getImage(liveSetID, result.getBlob(5)).getPayload();
                String streamLinkUrl = result.getString(6);
                String performerID = result.getString(7);
                int price = result.getInt(8);

                liveSets.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }

        } catch (SQLException e) {
           System.err.println("Having error executing query " + query);
           return new Response<>(new LinkedList<>(), false);
        }

        return new Response<>(liveSets, true);
    }

    public static Response<LinkedList<LastWatched>> getLastWatched() {

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
            return new Response<>(new LinkedList<>(), false);
        }

        return new Response<>(lastWatched, true);
    }

    public static Response<LinkedList<LoyaltyCard>> getLoyaltyCards() {
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
            return new Response<>(new LinkedList<>(), false);
        }


        return new Response<>(loyaltyCards, true);
    }

    public static Response<LinkedList<Performer>> getPerformers() {
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
            return new Response<>(new LinkedList<>(), false);
        }


        return new Response<>(performers, true);
    }

    public static Response<LinkedList<Genre>> getGenres() {
        ensureConnection();

        LinkedList<Genre> genres = new LinkedList<>();

        String query = "SELECT * FROM genre";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {

                int genreID = Integer.parseInt(resultSet.getString(1));
                String genreName = resultSet.getString(2);

                genres.add(new Genre(genreID, genreName));
            }
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }


        return new Response<>(genres, true);
    }



    public static Response<LinkedList<Ticket>> getTickets() {
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
            return new Response<>(new LinkedList<>(), false);
        }


        return new Response<>(tickets, true);
    }

    public static Response<LinkedList<User>> getUsers() {
        ensureConnection();

        LinkedList<User> users = new LinkedList<>();

        String query = "SELECT * FROM user ORDER BY userID desc";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                Optional<User> user = toUser(resultSet).getPayload();
                user.ifPresent(users::add);
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }


        return new Response<>(users, true);
    }



    private static Response<String> getImage(String liveSetID, Blob blob) {
        String path = "resources/images/" + liveSetID + ".jpg";

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            fileOutputStream.write(bytes);
            return new Response<>(path, true);
        } catch (SQLException | IOException e) {
           System.err.println("Having error converting image  on: " + liveSetID);
        }
        return new Response<>("", false);
    }

    private static void ensureConnection() {
      setConnection();
    }

    private static Response<Optional<User>> toUser(ResultSet resultSet) {
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
            return new Response<>(Optional.of(new User(userId, firstName, lastName, userName, email, password, watchedCons, userStatus, haveEarnedLoyalty, userType)), true);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return new Response<>(Optional.empty(), false);
    }

    private static boolean isTicketUsed(String ticketID) {
        ensureConnection();

        String query = "SELECT isnull(userID) FROM ticket where ticketID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ticketID);
            ResultSet set = preparedStatement.executeQuery();

            if(set.next()){
                return set.getInt(1) == 0;
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }

    private static boolean updateTicketUser(String ticketID, String userID) {

        ensureConnection();

        String query = "UPDATE ticket SET status = 'Used', userID = ? WHERE (ticketID = ?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userID);
            statement.setString(2, ticketID);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }

    private static boolean addLastWatched(String userId, String liveSetId) {

        ensureConnection();

        String query = "INSERT INTO lastwatched (lastWatchedID, userID, liveSetID) VALUES (?,? ,? )";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UtilityMethods.generateRandomID());
            statement.setString(2, userId);
            statement.setString(3, liveSetId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }

    public static Response<LinkedList<Performer>> searchPerformers(String searchTerm) {
        ensureConnection();

        LinkedList<Performer> matchingPerformers = new LinkedList<>();

        String query = "SELECT * FROM performer WHERE performerName LIKE ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String performerID = resultSet.getString(1);
                String performerName = resultSet.getString(2);
                String genre = resultSet.getString(3);
                String performerType = resultSet.getString(4);
                String description = resultSet.getString(5);
                String performerStatus = resultSet.getString(6);

                matchingPerformers.add(new Performer(performerID, performerName, genre, performerType, description, performerStatus));
            }

            if (matchingPerformers.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No performer '" + searchTerm + "' found", "No Results", JOptionPane.ERROR_MESSAGE);
            }

            return new Response<>(matchingPerformers, true);

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }
    }

}
