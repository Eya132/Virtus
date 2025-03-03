package GestionRegime.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection
{
    private String url="jdbc:mysql://localhost:3306/db3a8";
    private String login="root"; //privileges administratif
    private String pwd="";
    private Connection cnx;
    public static MyConnection instance;//implémenter le design pattern Singleton
    public MyConnection() {
        try {
           cnx= DriverManager.getConnection(url,login,pwd) ; //CHECED
            System.out.println("Connection Established!");
        } catch (SQLException e) {
            System.out.println("erreur, conncetion not esstablished!/" +e.getMessage());
        }
    }

    public static MyConnection getInstance()
    {
        if(instance == null)
        {
            instance = new MyConnection();
        }
        return instance;
    }


    public Connection getCnx() {
        return cnx;
    }


}



