package shared.model;

import shared.referenceClasses.*;
import shared.utilityClasses.UtilityMethods;

import java.io.*;
import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Optional;

public class Database {

    private static Connection connection;

    private static void setConnection() {

        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/asc", "root", "password");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                System.exit(0);
            }
        }

    }


    public static Response<String> signUp(User user) {

        if(userNameExist(user.getUserName())) {
            return new Response<>("User name " + user.getUserName() + " already exist", false);
        }

        ensureConnection();
    
        String query = "INSERT INTO user (userID, firstName, lastName,userName, email, password, watchedConsecShows, userStatus, loyaltyCardID, userType) VALUES (?, ?, ?, ?, ?, ?, ?,?, ?, ?)";
    
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUserID());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getUserName());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword());
            statement.setInt(7, user.getWatchedConsecutiveShows());
            statement.setString(8, user.getUserStatus());
            statement.setString(9, null);
            statement.setString(10, user.getUserType());
            statement.execute();
            return new Response<>("User signed up successfully", true);
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }
    
        return new Response<>("Error occurred while signing up user", false);
    }

    public static Response<Optional<User>> logIn(String giveUserName, String givenPassword) {

        ensureConnection();

        String query = "SELECT * FROM user WHERE userStatus = 'Active' AND userName = ? AND password = ?";

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


    public static void updateCompletedLiveSets() {

        ensureConnection();
        LinkedList<LiveSet> liveSets = getLiveSets().getPayload();

        String query = "UPDATE liveset SET status = 'Finished' WHERE liveSetID = ?";

        for (LiveSet liveSet : liveSets) {

            Date dateNow = new Date(Calendar.getInstance().getTime().getTime());

            if (liveSet.getDate().toLocalDate().isBefore(dateNow.toLocalDate())) {

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, liveSet.getLiveSetID());
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }


    public static Response<String> addLiveSet(Performer performer, LiveSet liveSet) {
        ensureConnection();

        Date dateNow = new Date(Calendar.getInstance().getTime().getTime());

        if(liveSet.getDate().toLocalDate().isBefore(dateNow.toLocalDate())) {
            return new Response<>("The given date is a past date", false);
        }

        if(performerConflictSchedule(performer, liveSet)) {
            return new Response<>("Given date will have performer have a conflicting schedule date", false);
        }

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
            return new Response<>("", rowsAffected > 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }




    public static boolean editLiveSet(LiveSet liveSet) {
        ensureConnection();

        if(liveSet.getStatus().equals("Canceled")) {
            refund(liveSet.getLiveSetID());
        }

        String query = "UPDATE liveset SET status=?, date=?, time=?, thumbnail=?, streamLinkURL=?, price=? WHERE liveSetID=?";
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
                System.err.println("Error reading file: " + e.getMessage());
                preparedStatement.setBytes(4, null);
            }

            preparedStatement.setString(5, liveSet.getStreamLinkURL());
            preparedStatement.setInt(6, liveSet.getPrice());
            preparedStatement.setString(7, liveSet.getLiveSetID());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addPerformer(Performer performer) {

        ensureConnection();

        int genreIndex = getGenres().getPayload().stream().filter(genre -> genre.getGenreName().equals(performer.getGenre())).findFirst().get().getGenreID();
        int performerTypeIndex =  getPerformerTypes().stream().filter(performerType -> performerType.getTypeName().equals(performer.getPerformerType())).findFirst().get().getId();

        String query = "INSERT INTO performer(performerID, performerName, genre, performerType, description, performerStatus)" + " VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, performer.getPerformerID());
            preparedStatement.setString(2, performer.getPerformerName());
            preparedStatement.setInt(3, genreIndex);
            preparedStatement.setInt(4, performerTypeIndex);
            preparedStatement.setString(5, performer.getDescription());
            preparedStatement.setString(6, performer.getPerformerStatus());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static LinkedList<User> getViewers(String liveSetID) {
        ensureConnection();

        LinkedList<User> viewers = new LinkedList<>();
        String query = "SELECT user.* FROM user INNER JOIN lastwatched ON lastwatched.userID = user.userID WHERE lastwatched.lastWatchedID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSetID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                Optional<User> user = toUser(resultSet).getPayload();
                user.ifPresent(viewers::add);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return viewers;
    }

    public static boolean updatePerformer(Performer performer) {

        ensureConnection();

        int genreIndex = getGenres().getPayload().stream().filter(genre -> genre.getGenreName().equals(performer.getGenre())).findFirst().get().getGenreID();
        int performerTypeIndex =  getPerformerTypes().stream().filter(performerType -> performerType.getTypeName().equals(performer.getPerformerType())).findFirst().get().getId();

        String query = "UPDATE performer" +
                " SET performerName = ?, genre = ? ,performerType = ? ,description = ? ,performerStatus = ?" +
                " WHERE (performerID = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, performer.getPerformerName());
            preparedStatement.setInt(2, genreIndex);
            preparedStatement.setInt(3, performerTypeIndex);
            preparedStatement.setString(4, performer.getDescription());
            preparedStatement.setString(5, performer.getPerformerStatus());
            preparedStatement.setString(6, performer.getPerformerID());
            preparedStatement.execute();

            cancelPerformersLiveSets(performer.getPerformerID());
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    private static void cancelPerformersLiveSets(String performerId) {

        LinkedList<String> performerLiveSets = getLiveSetIDofPerformer(performerId);

        if(performerLiveSets.isEmpty()){
            return;
        }

        ensureConnection();

        String query = "UPDATE liveset SET `status` = 'Canceled' WHERE (liveSetID = ?)";

        try {
            for(String s : performerLiveSets) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, s);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                refund(s);
            }

        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }
    }

    private static void refund(String liveSetID) {

        ensureConnection();

        LinkedList<RefundInformation> refundInformations = getRefundInformations(liveSetID);
        refundInformations.forEach(r -> updateTicketToRefunded(r.getTicketID()));

        String query = "INSERT INTO refund (refundID, amountRefunded, userID, date) VALUES (?, ?, ?, ?)";

        try {

            for (RefundInformation refundInformation : refundInformations) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, UtilityMethods.generateRandomID());
                preparedStatement.setInt(2, refundInformation.getAmount());
                preparedStatement.setString(3, refundInformation.getBuyerID());
                preparedStatement.setDate(4, new Date(Calendar.getInstance().getTime().getTime()));
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }

        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }

    }

    private static LinkedList<RefundInformation> getRefundInformations(String liveSetID) {
        ensureConnection();

        LinkedList<RefundInformation> refundInformations = new LinkedList<>();

        String query = "SELECT purchased.buyerID, ticket.ticketID, liveset.price " +
                "from purchased " +
                "INNER JOIN ticket ON purchased.ticketID = ticket.ticketID " +
                "INNER JOIN liveset ON liveset.liveSetID = ticket.liveSetID " +
                "WHERE ticket.userID is NULL AND liveset.liveSetID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSetID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                refundInformations.add(new RefundInformation(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3)));
            }

            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }

        return refundInformations;
    }



    private static void updateTicketToRefunded(String ticketID) {

        ensureConnection();

        String query = "UPDATE ticket SET status = 'Refunded' WHERE (ticketID = ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ticketID);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }

    }

    private static LinkedList<String> getLiveSetIDofPerformer(String performerId){
        ensureConnection();

        LinkedList<String> ids = new LinkedList<>();

        String query = "SELECT liveset.livesetID " +
                "FROM liveset " +
                "INNER JOIN performer ON liveset.performerID = performer.performerID " +
                "WHERE liveset.performerID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, performerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                ids.add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }

        return ids;
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

           int userWatch = getWatchesCount(userId);
           updateUserConsecutiveWatchesCount(userId, userWatch);

           if(userWatch >= 30){
                addLoyaltyCard(userId);
               return new Response<>("You have earned a loyalty card", true);
           }

           return new Response<>("Success", true);
       }else {
           return new Response<>("Having error accessing the live set", false);
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

    public static Response<String> addPurchase(String liveSetId, String buyerId) {

        if(!isLiveSetOpen(liveSetId)){
            return new Response<>("Live set was already canceled due to performers inactive", false);
        }

        String query = "INSERT INTO purchased (`purchasedID`, `date`, `time`, `buyerID`, `ticketID`) VALUES (?, ?,  ?, ?,  ?)";
        Response<String> newTicket = createNewTicket(liveSetId);

        String purchaseID = UtilityMethods.generateRandomID();

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, purchaseID);
            statement.setDate(2, UtilityMethods.getCurrentDate());
            statement.setTime(3, UtilityMethods.getCurrentTime());
            statement.setString(4, buyerId);
            statement.setString(5, newTicket.getPayload());
            statement.execute();
            return new Response<>("You have successfully bought a ticket for this liveset", true);
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return new Response<>("Purchase failed", false);
    }



    public static Response<LinkedList<Purchased>> getMyPurchases(String userID) {
        ensureConnection();

        LinkedList<Purchased> purchasedLinkedList = new LinkedList<>();

        String query = "SELECT purchased.date, purchased.time, performer.performerName, liveset.price, liveset.thumbnail, ticket.ticketID, ticket.status, liveset.livesetID, ticket.userID, liveset.status" +
                " FROM purchased" +
                " INNER JOIN user ON purchased.buyerID = user.userID" +
                " INNER JOIN ticket ON purchased.ticketID = ticket.ticketID" +
                " INNER JOIN liveset ON liveset.liveSetID = ticket.liveSetID" +
                " INNER JOIN performer ON liveset.performerID = performer.performerID" +
                " WHERE user.userID = ?" +
                " ORDER BY 1 desc";

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
                String userName = getUser(resultSet.getString(9));
                String status = resultSet.getString(10);

                purchasedLinkedList.add(new Purchased(date, time, performerName, liveSetPrice, liveSetThumbnail, ticketId, ticketStatus, userName, status));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return new Response<>(purchasedLinkedList, true);
    }



    public static Response<LinkedList<LiveSet>> getLiveSets() {

        ensureConnection();

        LinkedList<LiveSet> liveSets = new LinkedList<>();

        String query = "SELECT * from liveset ORDER BY 3, 4 desc";

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



    public static Response<LinkedList<Performer>> getPerformers() {
        ensureConnection();

        LinkedList<Performer> performers = new LinkedList<>();

        String query = "SELECT * FROM performer ORDER BY 6";

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


        String query = "SELECT * FROM genre ORDER BY 1";

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


    public static String getPerformerName(String liveSetId) {
        ensureConnection();

        String p = "Performer Name";

        String query = "SELECT performer.performerName FROM performer " +
                "INNER JOIN liveset ON liveset.performerID = performer.performerID " +
                "WHERE liveset.liveSetID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSetId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                p = resultSet.getString(1);
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return p;
    }

    public static LinkedList<User> getLiveSetPurchasers(String liveSetId){

        ensureConnection();
        LinkedList<User> users = new LinkedList<>();

        String query = "SELECT user.* " +
                "FROM user " +
                "INNER JOIN purchased ON purchased.buyerID = user.userID " +
                "INNER JOIN ticket ON ticket.ticketID = purchased.ticketID " +
                "WHERE ticket.liveSetID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSetId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Optional<User> user =  toUser(resultSet).getPayload();
                user.ifPresent(users::add);
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return users;
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
        return new Response<>(new User("","","","","","",0,"", Optional.empty() ,""), false);
    }

    public static LinkedList<PerformerType> getPerformerTypes() {

        ensureConnection();

        String query = "SELECT * FROM performerType ORDER BY 1";

        LinkedList<PerformerType> performerTypes = new LinkedList<>();
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet set = statement.executeQuery(query);

            while(set.next()) {
                performerTypes.add(new PerformerType(set.getInt(1), set.getString(2)));
            }

        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }

        return performerTypes;
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

                System.out.println(userId);
                tickets.add(new Ticket(ticketId, liveSetId, status, userId));
            }

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }


        return new Response<>(tickets, true);
    }

    public static Response<LinkedList<Performer>> searchPerformers(String searchTerm) {
        ensureConnection();

        LinkedList<Performer> matchingPerformers = new LinkedList<>();

        String query = "SELECT performer.* FROM performer " +
                "INNER JOIN genre ON genre.genreID = performer.genre " +
                "INNER JOIN performertype ON performertype.performerTypeID = performer.performerType "+
                "WHERE performer.performerName LIKE ? " +
                "OR performer.performerStatus LIKE ? " +
                "OR genre.genreName LIKE ? " +
                "OR performertype.pTypes LIKE ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm + "%");
            statement.setString(2, "%" + searchTerm + "%");
            statement.setString(3, "%" + searchTerm + "%");
            statement.setString(4, "%" + searchTerm + "%");

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
            
            return new Response<>(matchingPerformers, true);

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            System.out.println("Cause: " + e.getMessage());
            return new Response<>(new LinkedList<>(), false);
        }
    }

    public static Response<LinkedList<LiveSet>> searchLiveSetsAdmin(String searchTerm) {
        ensureConnection();

        LinkedList<LiveSet> matchingLiveSet = new LinkedList<>();

        String query = "SELECT ls.* FROM liveset ls " +
                "LEFT OUTER JOIN performer p ON ls.performerID = p.performerID " +
                "WHERE LOWER(ls.liveSetID) LIKE LOWER(?) " +
                "OR LOWER(p.performerName) LIKE LOWER(?) " +
                "OR ls.date LIKE ? " +
                "OR ls.time LIKE ? " +
                "OR LOWER(ls.status) LIKE LOWER(?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            String likeParam = "%" + searchTerm + "%";
            statement.setString(1, likeParam);
            statement.setString(2, likeParam);
            statement.setString(3, likeParam);
            statement.setString(4, likeParam);
            statement.setString(5, likeParam);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String liveSetID = resultSet.getString(1);
                String status = resultSet.getString(2);
                Date date = resultSet.getDate(3);
                Time time = resultSet.getTime(4);
                String thumbnail = getImage(liveSetID, resultSet.getBlob(5)).getPayload();
                String streamLinkUrl = resultSet.getString(6);
                String performerID = resultSet.getString(7);
                int price = resultSet.getInt(8);

                matchingLiveSet.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }

            return new Response<>(matchingLiveSet, true);
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }
    }

    public static Response<LinkedList<LiveSet>> searchLiveSets(String searchTerm) {
        ensureConnection();

        LinkedList<LiveSet> matchingLiveSet = new LinkedList<>();

        String query = "SELECT ls.* FROM liveset ls LEFT OUTER JOIN performer p ON ls.performerID = p.performerID WHERE LOWER(p.performerName) LIKE LOWER(?)";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + searchTerm + "%" );

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String liveSetID = resultSet.getString(1);
                String status = resultSet.getString(2);
                Date date = resultSet.getDate(3);
                Time time = resultSet.getTime(4);
                String thumbnail = getImage(liveSetID, resultSet.getBlob(5)).getPayload();
                String streamLinkUrl = resultSet.getString(6);
                String performerID = resultSet.getString(7);
                int price = resultSet.getInt(8);

                matchingLiveSet.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }


            return new Response<>(matchingLiveSet, true);

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }
    }

    public static Response<LinkedList<LiveSet>> sortByName(String condition) {
        ensureConnection();

        LinkedList<LiveSet> sortLiveSet = new LinkedList<>();

        String query = "SELECT ls.* FROM liveset ls LEFT OUTER JOIN performer p ON ls.performerID = p.performerID ORDER BY p.performerName " + condition;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String liveSetID = resultSet.getString(1);
                String status = resultSet.getString(2);
                Date date = resultSet.getDate(3);
                Time time = resultSet.getTime(4);
                String thumbnail = getImage(liveSetID, resultSet.getBlob(5)).getPayload();
                String streamLinkUrl = resultSet.getString(6);
                String performerID = resultSet.getString(7);
                int price = resultSet.getInt(8);

                sortLiveSet.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }


            return new Response<>(sortLiveSet, true);

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }
    }

    public static Response<LinkedList<LiveSet>> sortByDate(String condition) {
        ensureConnection();

        LinkedList<LiveSet> sortLiveSet = new LinkedList<>();

        String query = "SELECT ls.* FROM liveset ls ORDER BY ls.date " + condition;

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String liveSetID = resultSet.getString(1);
                String status = resultSet.getString(2);
                Date date = resultSet.getDate(3);
                Time time = resultSet.getTime(4);
                String thumbnail = getImage(liveSetID, resultSet.getBlob(5)).getPayload();
                String streamLinkUrl = resultSet.getString(6);
                String performerID = resultSet.getString(7);
                int price = resultSet.getInt(8);

                sortLiveSet.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }


            return new Response<>(sortLiveSet, true);

        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
            return new Response<>(new LinkedList<>(), false);
        }
    }




    /**
     *  UTILITY METHODS ON THIS CLASS (private methods)
     * */

    private static boolean performerConflictSchedule(Performer performer, LiveSet liveSet) {
        ensureConnection();

        System.out.println(liveSet.getDate());

        String query = "SELECT count(*) FROM liveset " +
                "INNER JOIN performer ON performer.performerID = liveset.performerID " +
                "WHERE liveset.status = 'Open' AND performer.performerID = ? AND liveset.date = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, performer.getPerformerID());
            preparedStatement.setDate(2, liveSet.getDate());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println("Having error executing the query: " + query);
            System.out.println("Cause: " + e.getMessage());
        }
        return false;
    }



    private static boolean userNameExist(String userName) {
        ensureConnection();

        String query = "SELECT userName FROM user WHERE userName = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }

    private static LoyaltyCard newLoyaltyCard() {

        String query = "INSERT INTO loyaltycard (loyaltyCardID, receiveDate) VALUES (?, ?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String loyaltyCardID = UtilityMethods.generateRandomID();
            Date dateNow =  new Date(Calendar.getInstance().getTime().getTime());
            preparedStatement.setString(1, loyaltyCardID);
            preparedStatement.setDate(2,dateNow);

            if(preparedStatement.executeUpdate() > 0) {
                return new LoyaltyCard(loyaltyCardID, dateNow);
            }
        } catch (SQLException e) {
            return null;
        }

        return null;
    }

    private static void addLoyaltyCard(String userId) {

        LoyaltyCard newLoyaltyCard = newLoyaltyCard();
        if(newLoyaltyCard == null) return;

        String query = "UPDATE user SET loyaltyCardID = ? WHERE (userID = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, newLoyaltyCard.getLoyaltyCardID());
            preparedStatement.setString(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateUserConsecutiveWatchesCount(String userId, int count) {
        String query = "UPDATE user SET watchedConsecShows = ? WHERE (userID = ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, count);
            preparedStatement.setString(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getWatchesCount(String userId) {

        ensureConnection();

        Date dateNow = new Date(Calendar.getInstance().getTime().getTime());
        LinkedList<Date> allLiveSetDates = getAllLiveSetDate();
        LinkedList<Date> userWatchDates = userWatchDates(userId);

        int consecutiveWatches = 0;
        for(int i = 0; i < allLiveSetDates.size(); i++) {

            Date currentDate = allLiveSetDates.get(i);

            if(currentDate.toLocalDate().isAfter(dateNow.toLocalDate())) break;
            else {

                if(currentDate.toLocalDate().isEqual(userWatchDates.get(i).toLocalDate())) {
                    consecutiveWatches++;
                }else {
                    consecutiveWatches = 0;
                    break;
                }

            }


        }

        return consecutiveWatches;
    }






    private static LinkedList<Date> userWatchDates(String userID) {

        LinkedList<Date> dates = new LinkedList<>();
        String query = "SELECT distinct(date) from lastwatched where userID = ? order by 1;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                dates.add(resultSet.getDate(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dates;
    }

    private static LinkedList<Date> getAllLiveSetDate(){

        LinkedList<Date> dates = new LinkedList<>();
        String query = "SELECT distinct(date) from liveset order by 1";
        try {
            Statement preparedStatement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = preparedStatement.executeQuery(query);

            while(resultSet.next()) {
                dates.add(resultSet.getDate(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dates;
    }

    private static String getUser(String userId) {
        ensureConnection();

        String query = "SELECT concat(firstName, ' ', lastName) from user WHERE userID = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return "";
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

    private static boolean isLiveSetOpen(String liveSetId) {

        ensureConnection();

        System.out.println(liveSetId);
        String query = "SELECT count(*) FROM liveset WHERE status = 'Open' AND livesetID = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, liveSetId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt(1) == 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private static Response<String> getImage(String liveSetID, Blob blob) {
        String path = "resources/thumbnails/" + liveSetID + ".jpg";

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
            Optional<LoyaltyCard> loyaltyCard = getLoyaltyCard(resultSet.getString(9));
            String userType = resultSet.getString(10);
            return new Response<>(Optional.of(new User(userId, firstName, lastName, userName, email, password, watchedCons, userStatus, loyaltyCard, userType)), true);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return new Response<>(Optional.empty(), false);
    }

    private static Optional<LoyaltyCard> getLoyaltyCard(String loyaltyCardId) {
        ensureConnection();

        String query = "SELECT * FROM loyaltycard WHERE loyaltyCardID = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, loyaltyCardId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String id = resultSet.getString(1);
                Date date = resultSet.getDate(2);
                return Optional.of(new LoyaltyCard(id, date));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        String query = "INSERT INTO lastwatched (lastWatchedID, userID, liveSetID, date) VALUES (?,? ,?, ? )";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, UtilityMethods.generateRandomID());
            statement.setString(2, userId);
            statement.setString(3, liveSetId);
            statement.setDate(4, new Date(Calendar.getInstance().getTime().getTime()));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Having error executing query " + query);
        }

        return false;
    }



}
