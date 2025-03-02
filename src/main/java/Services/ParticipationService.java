package Services;

import Entities.Participation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipationService {
    private Connection connection;
    private static final Logger logger = Logger.getLogger(ParticipationService.class.getName());

    public ParticipationService() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/matchmate", "root", "");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la connexion à la base de données", e);
        }
    }

    /**
     * Ajoute une participation à la base de données.
     *
     * @param idevent L'ID de l'événement.
     * @param iduser  L'ID de l'utilisateur.
     */
    public void addParticipation(int idevent, int iduser) {
        String query = "INSERT INTO participation (iduser, idevent) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, iduser);
            pst.setInt(2, idevent);
            pst.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de l'ajout de la participation", e);
        }
    }

    /**
     * Récupère toutes les participations pour un événement donné.
     *
     * @return Une liste de participations pour l'événement.
     */
    public List<Participation> getParticipationsByEvent(int eventId) {
        List<Participation> participations = new ArrayList<>();
        try {
            String query = "SELECT * FROM participation WHERE idevent = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                participations.add(new Participation(rs.getInt("idparticipation"), rs.getInt("iduser"), rs.getInt("idevent")));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des participations : " + ex.getMessage());
        }
        return participations;
    }

    /**
     * Récupère toutes les participations de la base de données.
     *
     * @return Une liste de toutes les participations.
     */
    public List<Participation> getAllParticipations() {
        List<Participation> participations = new ArrayList<>();
        String query = "SELECT * FROM participation";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Participation participation = new Participation(
                        rs.getInt("idparticipation"),
                        rs.getInt("iduser"),
                        rs.getInt("idevent")
                );
                participations.add(participation);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération de toutes les participations", e);
        }
        return participations;
    }
    // Dans ParticipationService
    public boolean hasUserParticipated(int eventId, int userId) {
        // Vérifier si un utilisateur avec userId a déjà participé à l'événement eventId
        // Retourne true si l'utilisateur a déjà participé, false sinon
        return getParticipationsByEvent(eventId).stream()
                .anyMatch(participation -> participation.getIduser() == userId);
    }

}