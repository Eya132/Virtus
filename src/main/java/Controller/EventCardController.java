package Controller;

import Entities.Event;
import Services.ParticipationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Random;

public class EventCardController {
    @FXML
    private Label eventName; // Label pour le nom de l'événement
    @FXML
    private Label eventDate; // Label pour la date de l'événement
    @FXML
    private Button participateButton; // Bouton "Participer"
    @FXML
    private ImageView eventImage; // ImageView pour l'image de l'événement

    private Event event; // L'événement associé à cette carte
    private final ParticipationService participationService = new ParticipationService();

    public void setEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("L'événement ne peut pas être null.");
        }
        this.event = event;

        // Afficher les données de l'événement dans la carte
        eventName.setText(event.getTitre());
        eventDate.setText(String.valueOf(event.getDate())); // Afficher la date de l'événement

        // Charger l'image depuis l'URL stockée dans la base de données
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            try {
                Image image = new Image(event.getImageUrl());
                eventImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                // Image par défaut en cas d'erreur
                eventImage.setImage(new Image(getClass().getResource("/Images/default_image.png").toExternalForm()));
            }
        } else {
            // Si aucune URL n'est disponible, afficher une image par défaut
            eventImage.setImage(new Image(getClass().getResource("/Images/default_image.png").toExternalForm()));
        }

        // Vérifier si l'événement est complet
        int participantsCount = participationService.getParticipationsByEvent(event.getIdevent()).size();
        if (participantsCount >= event.getCapacite()) {
            participateButton.setText("Complet");
            participateButton.setDisable(true); // Désactiver le bouton si l'événement est complet
        } else {
            participateButton.setText("Participer");
            participateButton.setDisable(false); // Activer le bouton si l'événement n'est pas complet
        }
    }

    @FXML
    private void handleParticiper() {
        if (event == null) {
            System.err.println("Aucun événement n'est défini.");
            return;
        }

        // Récupérer l'ID de l'utilisateur connecté (exemple temporaire)
        // Ajouter 100 participations avec des userId de 1 à 100
        Random random = new Random();
        int userId = random.nextInt(100) + 1; // Générer un userId aléatoire entre 1 et 100
        participationService.addParticipation(event.getIdevent(), userId);

        // Mettre à jour l'affichage
        setEvent(event); // Recharger les informations de l'événement
    }


    @FXML
    private void handleImageClick(MouseEvent event) {
        if (this.event != null) {
            try {
                // Charger la page des statistiques
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventStat.fxml"));
                Parent root = loader.load();

                // Passer l'événement au contrôleur des statistiques
                EventStatisticController controller = loader.getController();
                controller.setEvent(this.event);

                // Afficher la nouvelle fenêtre
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Statistiques de l'événement");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}