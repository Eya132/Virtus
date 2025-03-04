package services;

import Entites.ListInscri;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListInscriService {

    private final MyConnection myConnection = MyConnection.getInstance();

    // Méthode pour ajouter une inscription à un match pour un utilisateur donné
    public void addInscription(ListInscri inscription) {
        String query = "INSERT INTO ListInscri (matchId, userId) VALUES (?, ?)";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, inscription.getMatchId());
            pstmt.setInt(2, inscription.getuserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'inscription : " + e.getMessage());
        }
    }

    // Méthode pour récupérer toutes les inscriptions de la table ListInscri
    public List<ListInscri> getAllInscriptions() {
        List<ListInscri> inscriptions = new ArrayList<>();
        String query = "SELECT * FROM ListInscri";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ListInscri inscription = new ListInscri(
                        rs.getInt("id"),
                        rs.getInt("matchId"),
                        rs.getInt("userId")
                );
                inscriptions.add(inscription);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des inscriptions : " + e.getMessage());
        }
        return inscriptions;
    }

    // Méthode pour récupérer une inscription par son ID
    public ListInscri getInscriptionById(int id) {
        String query = "SELECT * FROM ListInscri WHERE id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ListInscri(
                            rs.getInt("id"),
                            rs.getInt("matchId"),
                            rs.getInt("userId")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'inscription par ID : " + e.getMessage());
        }
        return null;
    }

    // Méthode pour mettre à jour une inscription existante
    public void updateInscription(ListInscri inscription) {
        String query = "UPDATE ListInscri SET matchId = ?, userId = ? WHERE id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, inscription.getMatchId());
            pstmt.setInt(2, inscription.getuserId());
            pstmt.setInt(3, inscription.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'inscription : " + e.getMessage());
        }
    }

    // Méthode pour supprimer une inscription par son ID
    public void deleteInscription(int id) {
        String query = "DELETE FROM ListInscri WHERE id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'inscription : " + e.getMessage());
        }
    }

    // Méthode pour récupérer toutes les inscriptions pour un match spécifique
    public List<ListInscri> getInscriptionsByMatchId(int matchId) {
        List<ListInscri> inscriptions = new ArrayList<>();
        String query = "SELECT * FROM ListInscri WHERE matchId = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, matchId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ListInscri inscription = new ListInscri(
                            rs.getInt("id"),
                            rs.getInt("matchId"),
                            rs.getInt("userId")
                    );
                    inscriptions.add(inscription);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des inscriptions par matchId : " + e.getMessage());
        }
        return inscriptions;
    }

    // Méthode pour récupérer toutes les inscriptions pour un utilisateur spécifique
    public List<ListInscri> getInscriptionsByUserId(int userId) {
        List<ListInscri> inscriptions = new ArrayList<>();
        String query = "SELECT * FROM ListInscri WHERE userId = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ListInscri inscription = new ListInscri(
                            rs.getInt("id"),
                            rs.getInt("matchId"),
                            rs.getInt("userId")
                    );
                    inscriptions.add(inscription);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des inscriptions par userId : " + e.getMessage());
        }
        return inscriptions;
    }

    // Méthode pour récupérer une inscription spécifique par matchId et userId
    public ListInscri getInscriptionByMatchAndUser(int matchId, int userId) {
        String query = "SELECT * FROM ListInscri WHERE matchId = ? AND userId = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, matchId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ListInscri(
                            rs.getInt("id"),
                            rs.getInt("matchId"),
                            rs.getInt("userId")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'inscription par matchId et userId : " + e.getMessage());
        }
        return null;
    }

    // Méthode pour annuler la participation d'un utilisateur à un match
    public void cancelParticipation(int matchId, int userId) {
        String query = "DELETE FROM ListInscri WHERE matchId = ? AND userId = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, matchId);
            pstmt.setInt(2, userId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Aucune inscription n'a été annulée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'annulation de l'inscription : " + e.getMessage());
        }
    }

    // Méthode pour récupérer le nombre de participants pour un match donné
    public int getNombreParticipants(int matchId) {
        String query = "SELECT COUNT(*) FROM ListInscri WHERE matchId = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, matchId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du nombre de participants : " + e.getMessage());
        }
        return 0;
    }

    // Méthode pour mettre à jour le statut d'un match
    public void updateStatutMatch(int matchId, String statut) {
        String query = "UPDATE Match1 SET statut = ? WHERE id = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, statut);
            pstmt.setInt(2, matchId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut du match : " + e.getMessage());
        }
    }

    // Méthode pour supprimer l'inscription d'un utilisateur pour un match spécifique
    public void deleteInscriptionByMatchAndUser(int matchId, int userId) {
        String query = "DELETE FROM ListInscri WHERE matchId = ? AND userId = ?";
        try (Connection conn = myConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, matchId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'inscription : " + e.getMessage());
        }
    }
}