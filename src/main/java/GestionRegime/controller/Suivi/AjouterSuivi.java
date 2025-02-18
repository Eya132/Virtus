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

            // Récupérer les valeurs saisies par l'utilisateur
            int utilisateurId = 1; // À remplacer par l'ID de l'utilisateur connecté
            int regimeId = regimeComboBox.getValue(); // Récupérer l'ID du régime sélectionné
            double poids = Double.parseDouble(poidsField.getText());
            double tourDeTaille = Double.parseDouble(tourDeTailleField.getText());
            double imc = Double.parseDouble(imcField.getText());




            // Créer un nouvel objet Suivi
            Suivi nouveauSuivi = new Suivi(utilisateurId, regimeId, poids, tourDeTaille, imc);

            // Ajouter le suivi via le service
            suiviService.addSuivi(nouveauSuivi);


            // Afficher un message de succès
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Suivi ajouté");
            alert.setHeaderText(null);
            alert.setContentText("Le suivi a été ajouté avec succès !");
            alert.showAndWait();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListSuivi.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("liste suivi");
            stage.setScene(scene);
            stage.show();

            // Effacer les champs après l'ajout
            poidsField.clear();
            tourDeTailleField.clear();
            imcField.clear();

        } catch (NumberFormatException e) {
            // Afficher un message d'erreur si les valeurs saisies ne sont pas valides
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir des valeurs numériques valides.");
            alert.showAndWait();
        } catch (Exception e) {
            // Afficher un message d'erreur en cas de problème lors de l'ajout
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout du suivi : " + e.getMessage());
            alert.showAndWait();
        }

    }
}