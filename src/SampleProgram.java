import java.sql.*;

public class SampleProgram {

    private static Connection connection;

    public static void main(String[] args) {
        setUpConnection();
        showData();
    }

    private static void setUpConnection() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vanniinfoinfo?user=root&password=password");
        } catch (SQLException e) {
            System.out.println("Set up Connector/J or open Database Server or password is wrong");
        }

    }

    private static void showData() {
        String query = "SELECT userID from user";

        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
