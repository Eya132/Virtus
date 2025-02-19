package gestion_match.services;

import gestion_match.entites.Match;
import gestion_match.interfaces.IService;
import gestion_match.tools.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchService implements IService<Match> {

    // Ajouter un match dans la base de données
    public void createEntity(Match match) {
        try {
            // On force le statut à "EN_ATTENTE" lors de la création
            String statutParDefaut = "EN_ATTENTE";
            String requete = "INSERT INTO `match`(`dateMatch`, `heure`, `localisation`, `terrain`, `typeSport`, `nbPersonnes`, `description`, `statut`, `planificateurId`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = MyConnection.getInstance().getConnection().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS);

            pst.setDate(1, new java.sql.Date(match.getDateMatch().getTime()));
            pst.setString(2, match.getHeure());
            pst.setString(3, match.getLocalisation());
            pst.setString(4, match.getTerrain());
            pst.setString(5, match.getTypeSport());
            pst.setInt(6, match.getNbPersonnes());
            pst.setString(7, match.getDescription());
            pst.setString(8, statutParDefaut); // Utilisation du statut par défaut
            pst.setInt(9, match.getPlanificateurId()); // Ajout de l'ID du planificateur

            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        match.setIdMatch(generatedKeys.getInt(1)); // Récupérer l'ID généré
                        System.out.println("Match ajouté avec succès, ID généré: " + match.getIdMatch());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du match: " + e.getMessage());
        }
    }



    // Mettre à jour un match
    public void updateEntity(Match match) {
        try {
            String requete = "UPDATE `match` SET `dateMatch` = ?, `heure` = ?, `localisation` = ?, `terrain` = ?, `typeSport` = ?, `nbPersonnes` = ?, `description` = ?, `statut` = ?, `planificateurId` = ? WHERE `idMatch` = ?";
            PreparedStatement pst = MyConnection.getInstance().getConnection().prepareStatement(requete);

            pst.setDate(1, new java.sql.Date(match.getDateMatch().getTime()));
            pst.setString(2, match.getHeure());
            pst.setString(3, match.getLocalisation());
            pst.setString(4, match.getTerrain());
            pst.setString(5, match.getTypeSport());
            pst.setInt(6, match.getNbPersonnes());
            pst.setString(7, match.getDescription());
            pst.setString(8, match.getStatut());
            pst.setInt(9, match.getPlanificateurId()); // Mise à jour de l'ID du planificateur
            pst.setInt(10, match.getIdMatch());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Match mis à jour avec succès");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du match: " + e.getMessage());
        }
    }

    // Récupérer tous les matchs
    public List<Match> getAllEntities() {
        List<Match> result = new ArrayList<>();
        String requete = "SELECT * FROM `match`";

        try (Connection conn = MyConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                Match match = new Match();
                match.setIdMatch(rs.getInt("idMatch"));
                match.setDateMatch(rs.getDate("dateMatch"));
                match.setHeure(rs.getString("heure"));
                match.setLocalisation(rs.getString("localisation"));
                match.setTerrain(rs.getString("terrain"));
                match.setTypeSport(rs.getString("typeSport"));
                match.setNbPersonnes(rs.getInt("nbPersonnes"));
                match.setDescription(rs.getString("description"));
                match.setStatut(rs.getString("statut"));
                match.setPlanificateurId(rs.getInt("planificateurId")); // Récupérer l'ID du planificateur

                result.add(match);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des matchs: " + e.getMessage());
        }

        return result;
    }

    // Récupérer les matchs par statut
    public List<Match> getMatchsParStatut(String statut) {
        List<Match> matchs = new ArrayList<>();
        String query = "SELECT * FROM `match` WHERE statut = ?"; // Assurez-vous que le nom de la table est correct

        try (Connection connection = MyConnection.getInstance().getConnection(); // Utiliser l'instance MyConnection
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, statut); // Définir le paramètre pour la requête
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Mapping du résultat vers un objet Match
                Date date = resultSet.getDate("dateMatch");
                String heure = resultSet.getString("heure");
                String localisation = resultSet.getString("localisation");
                String terrain = resultSet.getString("terrain");
                String typeSport = resultSet.getString("typeSport");
                int nbPersonnes = resultSet.getInt("nbPersonnes");
                String description = resultSet.getString("description");
                String statutMatch = resultSet.getString("statut");
                int planificateurId = resultSet.getInt("planificateurId"); // Récupérer l'ID du planificateur

                // Créer un nouvel objet Match
                Match match = new Match(date, heure, localisation, terrain, typeSport, nbPersonnes, description, statutMatch, planificateurId);
                matchs.add(match); // Ajouter le match à la liste
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des matchs par statut: " + e.getMessage());
        }

        return matchs;
    }

    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        String requete = "SELECT * FROM match";

        try (Connection connection = MyConnection.getInstance().getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                Match match = new Match(
                        rs.getInt("idMatch")
                );
                matches.add(match);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des matchs : " + e.getMessage());
        }
        return matches;
    }

    public void updateMatch(Match match) {
        String sql = "UPDATE `match` SET statut = ? WHERE idMatch = ?";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, match.getStatut());
            stmt.setInt(2, match.getIdMatch());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Statut du match mis à jour avec succès !");
            } else {
                System.out.println("Aucun match trouvé à mettre à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la mise à jour du match : " + e.getMessage());
        }
    }

    public List<Match> getMatchsByUser(int userId) {
        List<Match> matchs = new ArrayList<>();
        String sql = "SELECT * FROM `match` WHERE planificateurId = ? OR idMatch IN (SELECT idMatch FROM inscriptionmatch WHERE idUser = ?)";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Match match = new Match();
                match.setIdMatch(rs.getInt("idMatch"));

                // Récupérer la date en tant que java.sql.Date et la convertir en java.util.Date
                java.sql.Date sqlDate = rs.getDate("dateMatch");
                if (sqlDate != null) {
                    match.setDateMatch(new java.util.Date(sqlDate.getTime()));
                }

                match.setHeure(rs.getString("heure"));
                match.setLocalisation(rs.getString("localisation"));
                match.setTypeSport(rs.getString("typeSport"));
                match.setPlanificateurId(rs.getInt("planificateurId"));
                match.setStatut(rs.getString("statut")); // Récupérer le statut
                matchs.add(match);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la récupération des matchs : " + e.getMessage());
        }
        return matchs;
    }
    public void deleteMatch(int idMatch) {
        String sql = "DELETE FROM `match` WHERE idMatch = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMatch);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Match supprimé avec succès !");
            } else {
                System.out.println("Aucun match trouvé à supprimer.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la suppression du match : " + e.getMessage());
        }
    }
    public void updateStatutMatch(int matchId, String statut) {
        String sql = "UPDATE `match` SET statut = ? WHERE idMatch = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statut);
            stmt.setInt(2, matchId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Statut du match mis à jour avec succès !");
            } else {
                System.out.println("Aucun match trouvé à mettre à jour.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL lors de la mise à jour du statut du match : " + e.getMessage());
        }
    }
}