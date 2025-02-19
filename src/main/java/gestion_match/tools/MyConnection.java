package gestion_match.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3307/matchmate";
    private String login = "root";
    private String pwd = "";
    private Connection con;
    private static MyConnection instance;

    // Constructeur privé pour l'instanciation unique
    private MyConnection() {
        try {
            con = DriverManager.getConnection(url, login, pwd);
            if (con != null && con.isValid(2)) {
                System.out.println("Connection Established!");
            } else {
                System.out.println("Erreur : La connexion est invalide.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'établissement de la connexion : " + e.getMessage());
        }
    }

    // Méthode pour obtenir la connexion
    public Connection getConnection() {
        try {
            // Vérifie si la connexion est toujours valide
            if (con == null || con.isClosed()) {
                con = DriverManager.getConnection(url, login, pwd);  // Tentative de nouvelle connexion
                System.out.println("Nouvelle connexion établie.");
            }
            return con;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour obtenir l'instance unique
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }
}
