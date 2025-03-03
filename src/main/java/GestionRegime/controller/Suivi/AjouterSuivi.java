package GestionRegime.controller.Suivi;

import GestionRegime.entities.Regime;
import GestionRegime.entities.Suivi;
import GestionRegime.services.RegimeService;
import GestionRegime.services.SuiviService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.List;

public class AjouterSuivi {

    @FXML
    private ComboBox<Integer> regimeComboBox;

    @FXML
    private TextField objectifField;

    @FXML
    private TextField caloriesField;

    @FXML
    private TextField proteinesField;

    @FXML
    private TextField glucidesField;

    @FXML
    private TextField lipidesField;

    @FXML
    private TextField poidsField;

    @FXML
    private TextField tourDeTailleField;

    @FXML
    private TextField imcField;

    private SuiviService suiviService = new SuiviService();
    private RegimeService regimeService = new RegimeService();

    @FXML
    public void initialize() {
        // Remplir le ComboBox avec les IDs des régimes disponibles
        List<Integer> regimeIds = suiviService.getAllRegimeIds();
        ObservableList<Integer> regimeIdsObservableList = FXCollections.observableArrayList(regimeIds);
        regimeComboBox.setItems(regimeIdsObservableList);

        // Ajouter un écouteur pour afficher les détails du régime sélectionné
        regimeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Regime regime = suiviService.getRegimeById(newValue);
                if (regime != null) {
                    objectifField.setText(regime.getObjectif().toString());
                    caloriesField.setText(String.valueOf(regime.getCaloriesJournalieres()));
                    proteinesField.setText(String.valueOf(regime.getProteines()));
                    glucidesField.setText(String.valueOf(regime.getGlucides()));
                    lipidesField.setText(String.valueOf(regime.getLipides()));
                }
            }
        });
    }

    @FXML
    private void handleAjouterSuivi() {
        try {
            // Vérifier que tous les champs sont remplis
            if (poidsField.getText().isEmpty() || tourDeTailleField.getText().isEmpty() || imcField.getText().isEmpty()) {
                afficherAlerte("Erreur de saisie", "Tous les champs doivent être remplis.");
                return;
            }

            // Récupérer les valeurs saisies par l'utilisateur
            int utilisateurId = 1; // À remplacer par l'ID de l'utilisateur connecté
            int regimeId = regimeComboBox.getValue(); // Récupérer l'ID du régime sélectionné

            // Vérifier que le poids est un entier positif
            int poids;
            try {
                poids = Integer.parseInt(poidsField.getText());
                if (poids <= 0) {
                    afficherAlerte("Erreur de saisie", "Le poids doit être un entier positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                afficherAlerte("Erreur de saisie", "Le poids doit être un entier positif.");
                return;
            }

            // Vérifier que le tour de taille est entre 40 et 95
            double tourDeTaille = Double.parseDouble(tourDeTailleField.getText());
            if (tourDeTaille < 40 || tourDeTaille > 95) {
                afficherAlerte("Erreur de saisie", "Le tour de taille doit être compris entre 40 et 95.");
                return;
            }

            // Vérifier que l'IMC est entre 18 et 25
            double imc = Double.parseDouble(imcField.getText());
            if (imc < 18 || imc > 25) {
                afficherAlerte("Erreur de saisie", "L'IMC doit être compris entre 18 et 25.");
                return;
            }

            // Créer un nouvel objet Suivi
            Suivi nouveauSuivi = new Suivi(utilisateurId, regimeId, poids, tourDeTaille, imc);

            // Ajouter le suivi via le service
            suiviService.addSuivi(nouveauSuivi);

            // Afficher un message de succès
            afficherAlerte("Suivi ajouté", "Le suivi a été ajouté avec succès !");

            // Rediriger vers la liste des suivis
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListSuivi.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Liste des suivis");
            stage.setScene(scene);
            stage.show();

            // Effacer les champs après l'ajout
            poidsField.clear();
            tourDeTailleField.clear();
            imcField.clear();

        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de saisie", "Veuillez saisir des valeurs numériques valides.");
        } catch (Exception e) {
            afficherAlerte("Erreur", "Erreur lors de l'ajout du suivi : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}