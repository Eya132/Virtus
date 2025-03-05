package services;

import Entites.Match1;
import tools.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MatchService {

    private static final Logger logger = Logger.getLogger(MatchService.class.getName());

    // Requêtes SQL
    private static final String INSERT_QUERY = "INSERT INTO Match1 (date, heure, localisation, terrain, nbPersonne, description, typeSport, statut, userId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM Match1";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM Match1 WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE Match1 SET date = ?, heure = ?, localisation = ?, terrain = ?, nbPersonne = ?, description = ?, typeSport = ?, statut = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM Match1 WHERE id = ?";
    private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM Match1 WHERE userId = ?";
    private static final String SELECT_BY_STATUT_QUERY = "SELECT * FROM Match1 WHERE statut = ?";
    private static final String UPDATE_STATUT_QUERY = "UPDATE Match1 SET statut = ? WHERE id = ?";
    private static final String COUNT_MATCHES_QUERY = "SELECT COUNT(*) FROM Match1";

    // Ajouter un match et retourner l'ID généré
    public int addMatch(Match1 match) {
        validateMatch(match);

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, match.getDate().toString());
            pstmt.setString(2, match.getHeure().toString());
            pstmt.setString(3, match.getLocalisation());
            pstmt.setString(4, match.getTerrain());
            pstmt.setInt(5, match.getNbPersonne());
            pstmt.setString(6, match.getDescription());
            pstmt.setString(7, match.getTypeSport());
            pstmt.setString(8, match.getStatut());
            pstmt.setInt(9, match.getUserId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("L'ajout du match a échoué, aucune ligne affectée.");
            }

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    // ogger.info("Match ajouté avec succès : " + generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("L'ajout du match a échoué, aucun ID généré.");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout du match : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'ajout du match", e);
        }
    }

    // Récupérer tous les matchs
    public List<Match1> getAllMatches() {
        List<Match1> matches = new ArrayList<>();
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_QUERY);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                matches.add(createMatchFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des matchs : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des matchs", e);
        }
        return matches;
    }

    // Récupérer un match par son ID
    public Match1 getMatchById(int id) {
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_ID_QUERY)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createMatchFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération du match par ID : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération du match par ID", e);
        }
        return null;
    }

    public void updateMatch(Match1 match) {
        String updateQuery = "UPDATE Match1 SET date = ?, heure = ?, localisation = ?, terrain = ?, nbPersonne = ?, description = ?, statut = ? WHERE id = ?";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            // Remplir les paramètres de la requête
            pstmt.setDate(1, java.sql.Date.valueOf(match.getDate())); // Mettre à jour la date
            pstmt.setTime(2, java.sql.Time.valueOf(match.getHeure())); // Mettre à jour l'heure
            pstmt.setString(3, match.getLocalisation()); // Mettre à jour la localisation
            pstmt.setString(4, match.getTerrain()); // Mettre à jour le terrain
            pstmt.setInt(5, match.getNbPersonne()); // Mettre à jour le nombre de personnes
            pstmt.setString(6, match.getDescription()); // Mettre à jour la description
            pstmt.setString(7, match.getStatut()); // Mettre à jour le statut
            pstmt.setInt(8, match.getId()); // Utiliser l'ID du match pour l'identifier

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Aucun match n'a été mis à jour.");
            } else {
                System.out.println("Match mis à jour avec succès !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du match : " + e.getMessage());
        }
    }


    // Méthode pour supprimer un match
    public void deleteMatch(int matchId) {
        String deleteQuery = "DELETE FROM Match1 WHERE id = ?";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setInt(1, matchId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Aucun match n'a été supprimé.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du match : " + e.getMessage());
        }
    }

    // Récupérer les matchs par utilisateur
    public List<Match1> getMatchesByUserId(int userId) {
        List<Match1> matches = new ArrayList<>();
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_USER_ID_QUERY)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(createMatchFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des matchs par utilisateur : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des matchs par utilisateur", e);
        }
        return matches;
    }

    // Récupérer les matchs par statut
    public List<Match1> getMatchesByStatut(String statut) {
        List<Match1> matches = new ArrayList<>();
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_BY_STATUT_QUERY)) {

            pstmt.setString(1, statut);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(createMatchFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des matchs par statut : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des matchs par statut", e);
        }
        return matches;
    }

    // Mettre à jour le statut d'un match
    public void updateStatutMatch(int matchId, String statut) {
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_STATUT_QUERY)) {

            pstmt.setString(1, statut);
            pstmt.setInt(2, matchId);
            pstmt.executeUpdate();
           // logger.info("Statut du match mis à jour avec succès : " + matchId);
        } catch (SQLException e) {
         //   logger.log(Level.SEVERE, "Erreur lors de la mise à jour du statut du match : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la mise à jour du statut du match", e);
        }
    }

    // Récupérer le nombre total de matchs
    public int getTotalMatches() {
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(COUNT_MATCHES_QUERY);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération du nombre total de matchs : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération du nombre total de matchs", e);
        }
        return 0;
    }

    // Méthode utilitaire pour créer un objet Match1 à partir d'un ResultSet
    private Match1 createMatchFromResultSet(ResultSet rs) throws SQLException {
        Match1 match = new Match1(
                rs.getInt("id"),
                rs.getString("date"),
                rs.getString("heure"),
                rs.getString("localisation"),
                rs.getString("terrain"),
                rs.getInt("nbPersonne"),
                rs.getString("description"),
                rs.getString("typeSport"),
                rs.getString("statut"),
                rs.getInt("userId")
        );
        // Ajouter le nombre de participants si la colonne existe
        try {
            match.setNbPersonne(rs.getInt("nbParticipants"));
        } catch (SQLException e) {
            // La colonne n'existe pas dans le ResultSet, ignorer
        }
        return match;
    }

    // Méthode pour valider les données d'un match
    private void validateMatch(Match1 match) {
        if (match.getDate() == null || match.getHeure() == null || match.getLocalisation() == null ||
                match.getTerrain() == null || match.getDescription() == null || match.getTypeSport() == null ||
                match.getStatut() == null) {
            throw new IllegalArgumentException("Tous les champs du match doivent être renseignés.");
        }
    }

    // Vérifier si un match existe
    public boolean matchExists(int matchId) {
        String query = "SELECT COUNT(*) FROM Match1 WHERE id = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, matchId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la vérification de l'existence du match : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la vérification de l'existence du match", e);
        }
        return false;
    }

    // Récupérer les matchs par statut et utilisateur
    public List<Match1> getMatchesByStatutAndUser(String statut, int userId) {
        List<Match1> matches = new ArrayList<>();
        String query = "SELECT * FROM Match1 WHERE statut = ? AND userId = ?";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, statut);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    matches.add(createMatchFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des matchs par statut et utilisateur : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des matchs par statut et utilisateur", e);
        }
        return matches;
    }

    // Annuler un match
    public void cancelMatch(int matchId) {
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Match1 SET statut = 'annulé' WHERE id = ?")) {

            pstmt.setInt(1, matchId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aucun match trouvé avec l'ID : " + matchId);
            }
            logger.info("Match annulé avec succès : " + matchId);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'annulation du match : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'annulation du match", e);
        }
    }

    // Récupérer les matchs avec le nombre de participants
    public List<Match1> getMatchesWithParticipantCount() {
        List<Match1> matches = new ArrayList<>();
        String query = "SELECT m.*, COUNT(l.userId) AS nbParticipants " +
                "FROM Match1 m LEFT JOIN ListInscri l ON m.id = l.matchId " +
                "GROUP BY m.id";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Match1 match = createMatchFromResultSet(rs);
                match.setNbPersonne(rs.getInt("nbParticipants")); // Ajouter un attribut nbParticipants à Match1
                matches.add(match);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des matchs avec le nombre de participants : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la récupération des matchs avec le nombre de participants", e);
        }
        return matches;
    }
    /**
     * Vérifie si un match avec la même date, heure et terrain existe déjà.
     *
     * @param date     La date du match.
     * @param heure    L'heure du match.
     * @param terrain  Le terrain du match.
     * @return true si le match est unique, false sinon.
     */
    public boolean isMatchUnique(String date, String heure, String terrain) {
        String query = "SELECT COUNT(*) FROM Match1 WHERE date = ? AND heure = ? AND terrain = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, date);
            pstmt.setString(2, heure);
            pstmt.setString(3, terrain);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // Retourne true si aucun match n'existe avec ces critères
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la vérification de l'unicité du match : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la vérification de l'unicité du match", e);
        }
        return true; // Par défaut, considérer que le match est unique en cas d'erreur
    }
}