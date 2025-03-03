package edu.pidev3a8.services;

import edu.pidev3a8.tools.myConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class PasswordResetService {

    public static void storeResetToken(String email, String token) {
        try (Connection conn = myConnection.getInstance().getCnx()) {
            String sql = "UPDATE User SET reset_token = ?, token_expiration = ? WHERE email_user = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, token);

                // Set expiration to 1 hour from now
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, 1);
                stmt.setTimestamp(2, new Timestamp(calendar.getTimeInMillis()));

                stmt.setString(3, email);
                stmt.executeUpdate();
                System.out.println("Reset token stored successfully for email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidToken(String token) {
        try (Connection conn = myConnection.getInstance().getCnx()) {
            String sql = "SELECT email_user FROM User WHERE reset_token = ? AND token_expiration > ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, token);
                stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
                ResultSet rs = stmt.executeQuery();

                return rs.next(); // Return true if the token is valid
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean resetPassword(String token, String newPassword) {
        try (Connection conn = myConnection.getInstance().getCnx()) {
            String sql = "UPDATE User SET password_user = ?, reset_token = NULL, token_expiration = NULL WHERE reset_token = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newPassword); // Hash the password
                stmt.setString(2, token);
                int rowsUpdated = stmt.executeUpdate();

                return rowsUpdated > 0; // Return true if the password was updated
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}