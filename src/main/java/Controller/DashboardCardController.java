package Controller;

import Entities.Event;
import Services.EventService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class DashboardCardController {
    @FXML
    private GridPane eventContainer; // Doit correspondre au fx:id dans le FXML

    @FXML
    private Label titleLabel; // Doit correspondre au fx:id dans le FXML

    private final EventService eventService = new EventService();

    @FXML
    public void initialize() {
        titleLabel.setText("Nos Événements"); // Définir le titre
        loadEvents();
    }

    private void loadEvents() {
        List<Event> events = eventService.getAllEvents();
        int row = 0;
        int col = 0;

        for (Event event : events) {
            try {
                // Charger la carte d'événement
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventCard.fxml"));
                VBox eventCard = loader.load();

                // Définir l'événement dans la carte
                EventCardController controller = loader.getController();
                controller.setEvent(event);

                // Ajouter la carte au conteneur (GridPane)
                eventContainer.add(eventCard, col, row);

                // Passer à la colonne suivante
                col++;
                if (col == 4) { // 3 cartes par ligne
                    col = 0;
                    row++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}