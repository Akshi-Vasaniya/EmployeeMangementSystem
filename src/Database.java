import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    static final String DB_URL = "jdbc:mysql://localhost:3306/";
    static final String User = "root";
    static final String Pass = System.getenv("DB_Pass");;

    public static void main(String[] args) {
        try {
            // Loading MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (// Establishing connection
                     Connection conn = DriverManager.getConnection(DB_URL, User, Pass);
                     Statement stm = conn.createStatement();
                 ) {
                String sql = "CREATE DATABASE EMS";
                // Executing statement
                stm.executeUpdate(sql);
                System.out.println("EMS database created successfully!!");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Exception Message: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
}
