package Module_6;

import java.sql.*;

public class DbSmokeTest {
    public static void main(String[] args) {
        String url  = "jdbc:mysql://localhost:3306/javabook?serverTimezone=UTC";
        String user = "scott";   // or ivytech_user if you prefer
        String pass = "tiger";   // matching password

        try {
            // Optional: explicitly load the driver (modern JDBC loads it automatically)
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(url, user, pass);
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT firstName, mi, lastName FROM Student WHERE deptId = 'CS'");
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    System.out.printf("%s %s %s%n",
                            rs.getString("firstName"),
                            rs.getString("mi"),
                            rs.getString("lastName"));
                }
                System.out.println("✅ Connection successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}