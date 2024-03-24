package shared;

import shared.referenceClasses.LiveSet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;

public class Database {

    public static void main(String[] args) {
        Database.setConnection();
        Database.getLiveSets();
    }

    private static Connection connection;


    private static void setConnection() {

        if(connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/vanniinfoinfo", "root", "password");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public static LinkedList<LiveSet> getLiveSets() {

        if(connection == null) setConnection();

        LinkedList<LiveSet> liveSets = new LinkedList<>();

        String query = "SELECT * from liveset";

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            while(result.next()) {
                String liveSetID = result.getString(1);
                String status = result.getString(2);
                Date date = result.getDate(3);
                Time time = result.getTime(4);
                String thumbnail =  getImage(liveSetID, result.getBlob(5));
                String streamLinkUrl = result.getString(6);
                String performerID = result.getString(7);
                int price = result.getInt(8);

                liveSets.add(new LiveSet(liveSetID, status, price, date, time, thumbnail, streamLinkUrl, performerID));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return liveSets;
    }

    private static String getImage(String liveSetID, Blob blob) {
        String path = "resources/images/"+liveSetID+".jpg";

        try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            fileOutputStream.write(bytes);
            return path;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: add other methods

}
