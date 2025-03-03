package GestionBoutiqueONLINE.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url="jdbc:mysql://localhost:3306/db3A8";
    private String login="root";
    private String pwd="";

    private Connection cnx;
    private static MyConnection instance;

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    private MyConnection() {     //private pour limiter la connextion  creation d'un patron d'instanciation Singleton
        try {
            cnx= DriverManager.getConnection(url, login, pwd);
            System.out.println("connection established");
        }
        catch (SQLException e) {
            System.out.println("Error, connection not established/"+e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }
}
