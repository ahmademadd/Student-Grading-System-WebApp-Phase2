package Controllers.DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/localdatabase";
    private static final String USER = "Admin";
    private static final String PASSWORD = "0000";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // For MySQL 8+
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database connection failed!" + e.getMessage());
            return null;
        }
    }
}
