package Services;

import Entities.Event;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    private Connection connection;

    public EventService() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/matchmate",
                    "root",
                    ""
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setIdevent(rs.getInt("idevent"));
                event.setTitre(rs.getString("titre"));
                event.setDate(rs.getDate("date"));
                event.setLieu(rs.getString("lieu"));
                event.setDescription(rs.getString("description"));
                event.setImageUrl(rs.getString("imageUrl"));
                event.setIduser(rs.getInt("iduser"));
                event.setCapacite(rs.getInt("capacite")); // Récupération de la capacité
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void addEvent(Event event) {
        String query = "INSERT INTO event (titre, date, lieu, description, imageUrl, iduser, capacite) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, event.getTitre());
            ps.setDate(2, event.getDate());
            ps.setString(3, event.getLieu());
            ps.setString(4, event.getDescription());
            ps.setString(5, event.getImageUrl());
            ps.setInt(6, event.getIduser());
            ps.setInt(7, event.getCapacite()); // Ajout de la capacité
            ps.executeUpdate();
            // Afficher une alerte de succès
            showAlert("Succès", "L'événement a été ajouter avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Afficher une alerte d'erreur
            showAlert("Erreur", "Une erreur s'est produite lors de ajout de l'événement.");
        }
    }

        public void updateEvent (Event event){
            String query = "UPDATE event SET titre = ?, date = ?, lieu = ?, description = ?, imageUrl = ?, iduser = ?, capacite = ? WHERE idevent = ?";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, event.getTitre());
                ps.setDate(2, event.getDate());
                ps.setString(3, event.getLieu());
                ps.setString(4, event.getDescription());
                ps.setString(5, event.getImageUrl());
                ps.setInt(6, event.getIduser());
                ps.setInt(7, event.getCapacite()); // Mise à jour de la capacité
                ps.setInt(8, event.getIdevent());
                ps.executeUpdate();
                showAlert("Succès", "L'événement a été mis à jour avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                // Afficher une alerte d'erreur
                showAlert("Erreur", "Une erreur s'est produite lors de la mise à jour de l'événement.");
            }
        }

        public void deleteEvent ( int id){
            String query = "DELETE FROM event WHERE idevent = ?";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                // Afficher une alerte de succès
                showAlert("Succès", "L'événement a été supprimé avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                // Afficher une alerte d'erreur
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de l'événement.");
            }

        }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    }