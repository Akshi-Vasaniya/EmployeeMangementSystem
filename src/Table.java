import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
    static final String DB_URL = "jdbc:mysql://localhost:3306/EMS";
    static final String username = "root";
    static final String pass = System.getenv("DB_Pass");;

    public static void main(String[] args) {
        try {
            // Load MySQL drivers
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (
                    Connection con = DriverManager.getConnection(DB_URL, username, pass);
                    Statement stm = con.createStatement();
                    ) {
                String sql = "CREATE TABLE EMS_Record(" +
                        "Id int PRIMARY KEY," +
                        "Name varchar(30)," +
                        "Code int," +
                        "Designation varchar(20)," +
                        "Exp int," +
                        "Age int" +
                        ")";

                stm.executeUpdate(sql);
                System.out.println("EMS_Record table created successfully!");

            } catch (SQLException ex) {
                System.out.println("Exception: "+ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Exception: "+ex.getMessage());
        }
    }
}
