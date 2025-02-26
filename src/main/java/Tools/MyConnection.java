package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3306/matchmate";
    private String login = "root";
    private String pwd = "";
    private Connection cnx;
    private static MyConnection Instance;

    private MyConnection() {
        try {
            this.cnx = DriverManager.getConnection(this.url, this.login, this.pwd);
            System.out.println("Connection Established");
        } catch (SQLException var2) {
            SQLException er= var2;
            System.out.println("error,connection not established! / " + er.getMessage());
        }

    }

    public Connection getCnx() {
        return this.cnx;
    }

    public static MyConnection getInstance() {
        if (Instance == null) {
            Instance = new MyConnection();
        }

        return Instance;
    }
}
