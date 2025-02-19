package gestion_match.controller;

import gestion_match.entites.InscriptionMatch;
import gestion_match.entites.Match;
import gestion_match.services.InscriptionMatchService;
import gestion_match.services.MatchService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class ListMatchController {

    @FXML
    private ListView<Match> matchListView; // ListView to display matches
    @FXML
    private TextField locationFilterTextField; // TextField for location filter
    @FXML
    private ComboBox<String> sportFilterComboBox; // ComboBox for sport type filter
    @FXML
    private Button filterButton; // Button to trigger filtering

    private MatchService matchService = new MatchService();
    private InscriptionMatchService inscriptionMatchService = new InscriptionMatchService(); // Create an instance of the service

    @FXML
    public void initialize() {
        // Load all matches on initialization
        loadAllMatches();

        // Initialize sport filter options (this could be dynamic based on the available sports)
        sportFilterComboBox.getItems().addAll("Tennis", "Padel", "Ping-Pong");

        // Set event handler when an item is selected in the list
        matchListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Display match details in TextArea when selected
                displayMatchDetails(newValue);
            }
        });
    }

    private void loadAllMatches() {
        // Get all matches from MatchService
        List<Match> matches = matchService.getAllEntities();

        // Set the items in the ListView
        matchListView.getItems().setAll(matches);

        // Customize the ListView to include a Participate button and show sport type and location
        matchListView.setCellFactory(param -> new MatchCell());
    }

    private void displayMatchDetails(Match match) {
        // Display match details in the TextArea
        String details = "Match ID: " + match.getIdMatch() + "\n" +
                "Date: " + match.getDateMatch() + "\n" +
                "Time: " + match.getHeure() + "\n" +
                "Location: " + match.getLocalisation() + "\n" +
                "Field: " + match.getTerrain() + "\n" +
                "Sport: " + match.getTypeSport() + "\n" +
                "Participants: " + match.getNbPersonnes() + "\n" +
                "Description: " + match.getDescription();
    }

    @FXML
    private void filterMatches() {
        String locationFilter = locationFilterTextField.getText().toLowerCase();
        String sportTypeFilter = sportFilterComboBox.getValue() != null ? sportFilterComboBox.getValue().toLowerCase() : "";

        // Get all matches from the service
        List<Match> allMatches = matchService.getAllEntities();

        // Filter matches based on the location and sport type
        List<Match> filteredMatches = allMatches.stream()
                .filter(match -> match.getLocalisation().toLowerCase().contains(locationFilter))
                .filter(match -> match.getTypeSport().toLowerCase().contains(sportTypeFilter))
                .collect(Collectors.toList());

        // Update ListView with filtered matches
        matchListView.getItems().setAll(filteredMatches);
    }

    // Custom cell to add a Participate button and show sport type and location
    private class MatchCell extends javafx.scene.control.ListCell<Match> {
        private Text matchText;

        @Override
        protected void updateItem(Match match, boolean empty) {
            super.updateItem(match, empty);

            if (empty || match == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create the layout for the cell
                HBox hbox = new HBox();
                matchText = new Text(match.getTypeSport() + " - " + match.getLocalisation());
                Button participateButton = new Button("Participer");

                // Handle the button click event
                participateButton.setOnAction(event -> {
                    // Add logic for participating, for example:
                    System.out.println("Participating in match ID: " + match.getIdMatch());
                    inscrireUtilisateur(match); // Call the method here
                });

                // Show detailed information on hover
                hbox.setOnMouseEntered(event -> {
                    String details = "Date: " + match.getDateMatch() + "\n" +
                            "Time: " + match.getHeure() + "\n" +
                            "Field: " + match.getTerrain() + "\n" +
                            "Participants: " + match.getNbPersonnes() + "\n" +
                            "Description: " + match.getDescription() + "\n" +
                            "Status: " + match.getStatut() + "\n" +
                            "Planner ID: " + match.getPlanificateurId();
                    matchText.setText(match.getTypeSport() + " - " + match.getLocalisation() + "\n" + details);
                });

                // Hide detailed information when mouse leaves
                hbox.setOnMouseExited(event -> matchText.setText(match.getTypeSport() + " - " + match.getLocalisation()));

                // Add the elements to the HBox
                hbox.getChildren().addAll(matchText, participateButton);
                setGraphic(hbox);
            }

        }

        private void inscrireUtilisateur(Match match) {
            // Utiliser l'ID utilisateur actuel
            int userId = 73; // ID utilisateur fictif

            // Vérifier si l'utilisateur est déjà le planificateur
            if (userId == match.getPlanificateurId()) {
                // L'utilisateur est le planificateur, afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'inscription");
                alert.setHeaderText(null);
                alert.setContentText("Vous êtes le planificateur de ce match, vous êtes déjà inscrit!");
                alert.showAndWait();
                return; // Ne pas procéder à l'inscription
            }

            // Récupérer le nombre actuel de participants inscrits (y compris le planificateur)
            int nbParticipantsActuels = inscriptionMatchService.getNombreInscriptionsParMatch(match.getIdMatch());

            // Définir le nombre maximum de participants en fonction de nbPersonnes
            int maxParticipants;
            if (match.getNbPersonnes() == 2) {
                maxParticipants = 1; // 1 planificateur + 1 participant
            } else if (match.getNbPersonnes() == 4) {
                maxParticipants = 3; // 1 planificateur + 3 participants
            } else {
                // Cas par défaut (si nbPersonnes a une autre valeur)
                maxParticipants = match.getNbPersonnes();
            }

            // Vérifier si le nombre de participants actuels dépasse la limite
            if (nbParticipantsActuels >= maxParticipants) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'inscription");
                alert.setHeaderText(null);
                alert.setContentText("Le nombre maximum de participants pour ce match a été atteint.");
                alert.showAndWait();
                return; // Ne pas procéder à l'inscription
            }

            // Créer un objet InscriptionMatch
            InscriptionMatch inscription = new InscriptionMatch();
            inscription.setIdMatch(match.getIdMatch());
            inscription.setIdUser(userId);
            inscription.setRole("participant");
            inscription.setStatut("en attente");

            // Ajouter l'inscription via le service
            inscriptionMatchService.addInscription(inscription); // Utilisation de l'instance du service

            // Vérifier si le nombre de participants atteint la limite après l'inscription
            nbParticipantsActuels = inscriptionMatchService.getNombreInscriptionsParMatch(match.getIdMatch());

            if (nbParticipantsActuels >= maxParticipants) {
                // Mettre à jour le statut du match à "confirmé" dans la table `match`
                match.setStatut("confirmé");
                matchService.updateMatch(match); // Mettre à jour le match dans la base de données

                // Mettre à jour le statut de toutes les inscriptions associées à ce match dans la table `inscriptionmatch`
                inscriptionMatchService.updateStatutInscriptionsByMatch(match.getIdMatch(), "confirmé");
            }

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription réussie");
            alert.setHeaderText(null);
            alert.setContentText("Vous êtes inscrit au match!");
            alert.showAndWait();
        }
    }
}

