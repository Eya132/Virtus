package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentArea; // Zone où les vues seront chargées

    // Méthode pour charger l'interface "Planifier match"
    @FXML
    private void loadPlanifierMatch() {
        loadView("/Planifier_match.fxml");
    }

    // Méthode pour charger l'interface "Tableau de bord"
    @FXML
    private void loadVoirPlanning() {
        loadView("/match_view.fxml");
    }

    // Méthode pour charger l'interface "Voir match dispo"
    @FXML
    private void loadVoirMatchDispo() {
        loadView("/ListeMatchs.fxml");
    }

    // Méthode générique pour charger une vue
    private void loadView(String fxmlFile) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();

            // Passer l'ID utilisateur au contrôleur de la vue chargée (si nécessaire)
            if (loader.getController() instanceof UserDashboardController) {
                ((UserDashboardController) loader.getController()).setUserId(Session.getUserId());
            } else if (loader.getController() instanceof PlanifierMatchController) {
                ((PlanifierMatchController) loader.getController()).setUserId(Session.getUserId());
            }

            // Remplacer le contenu actuel par la nouvelle vue
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Erreur lors du chargement de l'interface : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode appelée lors de l'initialisation du contrôleur
    @FXML
    public void initialize() {
        // Charger "Planifier match" par défaut au démarrage
        loadPlanifierMatch();
    }
}