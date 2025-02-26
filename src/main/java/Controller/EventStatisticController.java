package Controller;

import Entities.Event;
import Services.ParticipationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class EventStatisticController {
    @FXML private Label eventTitle;
    @FXML private Label participantsCount;
    @FXML private Label capacityLabel;
    @FXML private Label statusLabel;
    @FXML private PieChart participationChart;

    private Event event;
    private final ParticipationService participationService = new ParticipationService();

    @FXML
    public void initialize() {
        // Initialisation pour éviter un PieChart vide
        participationChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("En attente", 1)
        ));
    }

    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            eventTitle.setText(event.getTitre());
            capacityLabel.setText("Capacité : " + event.getCapacite());

            // Récupérer le nombre de participants
            int participants = participationService.getParticipationsByEvent(event.getIdevent()).size();
            participantsCount.setText("Nombre de participants : " + participants);

            // Vérifier si l'événement est complet
            if (participants >= event.getCapacite()) {
                statusLabel.setText("Statut : Complet");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else {
                statusLabel.setText("Statut : Disponible");
                statusLabel.setStyle("-fx-text-fill: green;");
            }

            // Mettre à jour le PieChart (appelé dans tous les cas)
            updatePieChart(participants, event.getCapacite());
        }
    }

    private void updatePieChart(int participants, int capacity) {
        int remainingPlaces = Math.max(capacity - participants, 0); // Éviter les valeurs négatives

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Participants", participants),
                new PieChart.Data("Places restantes", remainingPlaces)
        );

        participationChart.setData(pieChartData);
    }
}
