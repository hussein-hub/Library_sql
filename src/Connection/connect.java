package Connection;

import java.sql.*;

public class connect {
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/library_main";
        String uname = "yourUsername";
        String pass = "yourPassword";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url,uname,pass);
        return con;
    }
}
