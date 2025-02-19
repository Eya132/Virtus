package gestion_match.controller;

import gestion_match.entites.Match;
import gestion_match.services.InscriptionMatchService;
import gestion_match.services.MatchService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private TableView<Match> matchTableView; // TableView pour afficher les matchs
    @FXML
    private TableColumn<Match, String> dateColumn; // Colonne pour la date
    @FXML
    private TableColumn<Match, String> heureColumn; // Colonne pour l'heure
    @FXML
    private TableColumn<Match, String> localisationColumn; // Colonne pour la localisation
    @FXML
    private TableColumn<Match, String> typeSportColumn; // Colonne pour le type de sport
    @FXML
    private TableColumn<Match, String> roleColumn; // Colonne pour le rôle (planificateur/participant)
    @FXML
    private TableColumn<Match, String> actionColumn; // Colonne pour les actions (annuler/modifier)

    private MatchService matchService = new MatchService();
    private InscriptionMatchService inscriptionMatchService = new InscriptionMatchService();
    private int userId = 74; // ID utilisateur fictif (à remplacer par l'ID de l'utilisateur connecté)

    @FXML
    public void initialize() {
        // Configurer les colonnes de la TableView
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateMatch"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        localisationColumn.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        typeSportColumn.setCellValueFactory(new PropertyValueFactory<>("typeSport"));

        // Déterminer le rôle de l'utilisateur
        roleColumn.setCellValueFactory(cellData -> {
            Match match = cellData.getValue();
            if (match.getPlanificateurId() == userId) {
                return new SimpleStringProperty("Planificateur");
            } else {
                return new SimpleStringProperty("Participant");
            }
        });

        // Configurer la colonne des actions
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button annulerButton = new Button("Annuler");
            private final Button modifierButton = new Button("Modifier");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Match match = getTableView().getItems().get(getIndex());

                    // Afficher les boutons en fonction du rôle
                    if (match.getPlanificateurId() == userId) {
                        // L'utilisateur est le planificateur : il peut annuler ou modifier
                        annulerButton.setOnAction(event -> {
                            try {
                                annulerMatch(match);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showErrorAlert("Erreur de suppression", "Impossible de supprimer le match.");
                            }
                        });

                        modifierButton.setOnAction(event -> {
                            try {
                                modifierMatch(match);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showErrorAlert("Erreur de modification", "Impossible de modifier le match.");
                            }
                        });

                        setGraphic(new HBox(5, annulerButton, modifierButton));
                    } else {
                        // L'utilisateur est un participant : il peut seulement annuler
                        annulerButton.setOnAction(event -> {
                            try {
                                annulerParticipation(match);
                            } catch (Exception e) {
                                e.printStackTrace();
                                showErrorAlert("Erreur d'annulation", "Impossible d'annuler votre participation.");
                            }
                        });
                        setGraphic(annulerButton);
                    }
                }
            }
        });

        // Charger les matchs de l'utilisateur
        loadMatchsForUser();
    }

    @FXML
    void loadMatchsForUser() {
        try {
            List<Match> matchs = matchService.getMatchsByUser(userId);
            matchTableView.getItems().setAll(matchs);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Erreur de chargement", "Impossible de charger les matchs.");
        }
    }

    private void annulerMatch(Match match) {
        // Vérifier si l'utilisateur est le planificateur et si le statut du match est EN_ATTENTE
        if (match.getPlanificateurId() == userId && "EN_ATTENTE".equals(match.getStatut())) {
            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce match ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Supprimer le match
                        matchService.deleteMatch(match.getIdMatch());
                        // Recharger la liste des matchs
                        loadMatchsForUser();
                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorAlert("Erreur de suppression", "Impossible de supprimer le match.");
                    }
                }
            });
        } else {
            showErrorAlert("Erreur de suppression", "Vous ne pouvez pas supprimer ce match.");
        }
    }

    private void modifierMatch(Match match) {
        try {
            // Charger la vue de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Page4.fxml"));
            Parent root = loader.load();

            // Initialiser le contrôleur
            ModifierMatchController controller = loader.getController();
            controller.setMatch(match);
            controller.setDashboardController(this);

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier un match");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur de chargement", "Impossible de charger la page de modification.");
        }
    }

    private void annulerParticipation(Match match) {
        // Afficher une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'annulation");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir annuler votre participation à ce match ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Annuler la participation de l'utilisateur
                    inscriptionMatchService.annulerParticipation(userId, match.getIdMatch());
                    // Mettre à jour le statut du match à "EN_ATTENTE"
                    matchService.updateStatutMatch(match.getIdMatch(), "EN_ATTENTE");

                    // Recharger la liste des matchs
                    loadMatchsForUser();

                    // Afficher un message de succès
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Participation annulée");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Votre participation a été annulée avec succès.");
                    successAlert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorAlert("Erreur d'annulation", "Impossible d'annuler votre participation.");
                }
            }
        });
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
