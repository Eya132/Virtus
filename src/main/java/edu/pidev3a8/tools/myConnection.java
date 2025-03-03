package edu.pidev3a8.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myConnection {
    private String url = "jdbc:mysql://localhost:3306/g_user";
    private String login = "root";
    private String password = "";
    private Connection con;
    private static myConnection instance;

    // Private constructor to enforce singleton pattern
    public myConnection() {
        establishConnection();
    }

    // Singleton instance getter
    public static myConnection getInstance() {
        if (instance == null) {
            instance = new myConnection();
        }
        return instance;
    }

    // Establish a new connection
    private void establishConnection() {
        try {
            con = DriverManager.getConnection(url, login, password);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.err.println("Error, connection not established: " + e.getMessage());
        }
    }

    // Get the connection, ensuring it is valid
    public Connection getCnx() {
        try {
            // Check if the connection is closed or null
            if (con == null || con.isClosed()) {
                System.out.println("Connection is closed or null. Re-establishing connection...");
                establishConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
            establishConnection(); // Attempt to re-establish the connection
        }
        return con;
    }

    // Close the connection (use sparingly, only when absolutely necessary)
    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}