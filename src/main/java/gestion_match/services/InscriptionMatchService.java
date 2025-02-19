package gestion_match.services;

import gestion_match.entites.InscriptionMatch;
import gestion_match.entites.Match;
import gestion_match.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscriptionMatchService {

    // Ajouter une inscription
    public void addInscription(InscriptionMatch inscription) {
        // Vérification si le match existe
        Match match = getMatchById(inscription.getIdMatch());
        if (match == null) {
            System.out.println("Erreur : Le match spécifié n'existe pas !");
            return; // Sortir si le match n'existe pas
        }

        // Vérification si l'utilisateur est déjà inscrit au match
        if (isUserAlreadyRegistered(inscription.getIdUser(), inscription.getIdMatch())) {
            System.out.println("Erreur : L'utilisateur est déjà inscrit à ce match !");
            return;
        }

        // Ajouter l'inscription à la base de données
        String requete = "INSERT INTO inscriptionmatch (idMatch, idUser, role, statut) VALUES (?, ?, ?, ?)";
        try (Connection connection = MyConnection.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(requete)) {

            // Démarrer la transaction
            connection.setAutoCommit(false);

            pst.setInt(1, inscription.getIdMatch());
            pst.setInt(2, inscription.getIdUser());
            pst.setString(3, inscription.getRole());
            pst.setString(4, inscription.getStatut());

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Inscription ajoutée avec succès !");
                connection.commit();  // Valider la transaction
            } else {
                System.out.println("Erreur lors de l'ajout de l'inscription.");
                connection.rollback(); // Annuler la transaction en cas d'échec
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            try {
                MyConnection.getInstance().getConnection().rollback(); // Annuler la transaction en cas d'erreur
            } catch (SQLException ex) {
                System.out.println("Erreur lors du rollback : " + ex.getMessage());
            }
        }
    }

    // Vérifier si l'utilisateur est déjà inscrit à ce match
    private boolean isUserAlreadyRegistered(int idUser, int idMatch) {
        String requete = "SELECT * FROM inscriptionmatch WHERE idUser = ? AND idMatch = ?";
        try (Connection connection = MyConnection.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(requete)) {

            pst.setInt(1, idUser);
            pst.setInt(2, idMatch);
            ResultSet rs = pst.executeQuery();
            return rs.next();  // Si un résultat existe, l'utilisateur est déjà inscrit
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
            return false;
        }
    }

    // Récupérer toutes les inscriptions pour un match
    public List<InscriptionMatch> getInscriptionsByMatch(int idMatch) {
        List<InscriptionMatch> inscriptionsList = new ArrayList<>();
        String requete = "SELECT * FROM inscriptionmatch WHERE idMatch = ?";
        try (Connection connection = MyConnection.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(requete)) {

            pst.setInt(1, idMatch);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                InscriptionMatch inscription = new InscriptionMatch(
                        rs.getInt("idInscription"),
                        rs.getInt("idMatch"),
                        rs.getInt("idUser"),
                        rs.getString("role"),
                        rs.getString("statut")
                );
                inscriptionsList.add(inscription);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des inscriptions : " + e.getMessage());
        }
        return inscriptionsList;
    }

    // Récupérer un match par son ID
    private Match getMatchById(int idMatch) {
        String requete = "SELECT * FROM `match` WHERE idMatch = ?";
        try (Connection connection = MyConnection.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(requete)) {

            pst.setInt(1, idMatch);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Match(rs.getInt("idMatch"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return null;  // Retourne null si le match n'existe pas
    }

    // Mettre à jour l'inscription (par exemple, changer le statut de "en attente" à "confirmé")
    public void updateInscription(InscriptionMatch inscription) {
        String sql = "UPDATE inscriptionmatch SET statut = ? WHERE idMatch = ? AND idUser = ?";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inscription.getStatut());
            stmt.setInt(2, inscription.getIdMatch());
            stmt.setInt(3, inscription.getIdUser());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Statut mis à jour avec succès !");
            } else {
                System.out.println("Aucune inscription trouvée à mettre à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la mise à jour : " + e.getMessage());
        }
    }
    public int getNombreInscriptionsParMatch(int idMatch) {
        String requete = "SELECT COUNT(*) AS total FROM inscriptionmatch WHERE idMatch = ?";
        try (Connection connection = MyConnection.getInstance().getConnection();
             PreparedStatement pst = connection.prepareStatement(requete)) {

            pst.setInt(1, idMatch);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt("total"); // Retourne le nombre total d'inscriptions pour ce match
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération du nombre d'inscriptions : " + e.getMessage());
        }
        return 0; // Retourne 0 en cas d'erreur ou si aucune inscription n'est trouvée
    }
    public void updateStatutInscriptionsByMatch(int idMatch, String statut) {
        String sql = "UPDATE inscriptionmatch SET statut = ? WHERE idMatch = ?";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut);
            stmt.setInt(2, idMatch);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Statut des inscriptions mis à jour avec succès !");
            } else {
                System.out.println("Aucune inscription trouvée à mettre à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la mise à jour des inscriptions : " + e.getMessage());
        }
    }
    public void annulerParticipation(int userId, int matchId) {
        String sql = "DELETE FROM inscriptionmatch WHERE idUser = ? AND idMatch = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, matchId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Participation annulée avec succès !");
            } else {
                System.out.println("Aucune participation trouvée à annuler.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de l'annulation de la participation : " + e.getMessage());
        }
    }

}
