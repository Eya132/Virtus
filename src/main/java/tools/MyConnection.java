package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3307/gestion_match";
    private String login = "root";
    private String pwd = "";
    private Connection con;
    private static MyConnection instance;

    // Constructeur privé pour l'instanciation unique
    private MyConnection() {
        initializeConnection();
    }

    // Méthode pour initialiser la connexion
    private void initializeConnection() {
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
            if (con == null || con.isClosed() || !con.isValid(2)) {
                if (con != null && !con.isClosed()) {
                    con.close(); // Fermer la connexion existante avant d'en créer une nouvelle
                }
                con = DriverManager.getConnection(url, login, pwd);
                // System.out.println("Nouvelle connexion établie."); // Supprimer ou commenter cette ligne
            }
            return con;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la connexion : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour fermer la connexion
    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Connexion fermée avec succès.");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }

    // Méthode pour obtenir l'instance unique (Singleton)
    public static MyConnection getInstance() {
        if (instance == null) {
            synchronized (MyConnection.class) {
                if (instance == null) {
                    instance = new MyConnection();
                }
            }
        }
        return instance;
    }
}