package GestionRegime.controller.Regime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import GestionRegime.entities.Regime;
import GestionRegime.services.RegimeService;

import java.io.IOException;
import java.time.LocalDate;

public class AjouterRegimeController {

    @FXML
    private ComboBox<Regime.Objectif> objectifComboBox;

    @FXML
    private TextField caloriesTextField;

    @FXML
    private TextField proteinesTextField;

    @FXML
    private TextField glucidesTextField;

    @FXML
    private TextField lipidesTextField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField statusTextField;

    @FXML
    public Button ajouterRegimeButton;

    @FXML
    private Button voirListButton;

    @FXML
    public void initialize() {
        // Initialiser le ComboBox avec les valeurs de l'enum
        objectifComboBox.getItems().setAll(Regime.Objectif.values());
    }

    @FXML
    void ajouterRegimeAction(ActionEvent event) {
        try {
            // Vérifier que tous les champs sont remplis
            if (objectifComboBox.getValue() == null || caloriesTextField.getText().isEmpty() ||
                    proteinesTextField.getText().isEmpty() || glucidesTextField.getText().isEmpty() ||
                    lipidesTextField.getText().isEmpty() || dateDebutPicker.getValue() == null ||
                    dateFinPicker.getValue() == null || statusTextField.getText().isEmpty()) {
                afficherAlerte("Erreur de saisie", "Tous les champs doivent être remplis !");
                return;
            }

            // Récupérer les valeurs des champs
            Regime.Objectif objectif = objectifComboBox.getValue();
            int calories = Integer.parseInt(caloriesTextField.getText());
            int proteines = Integer.parseInt(proteinesTextField.getText());
            int glucides = Integer.parseInt(glucidesTextField.getText());
            int lipides = Integer.parseInt(lipidesTextField.getText());
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String status = statusTextField.getText();

            // Créer un nouvel objet Regime
            Regime regime = new Regime();
            regime.setObjectif(objectif);
            regime.setCaloriesJournalieres(calories);
            regime.setProteines(proteines);
            regime.setGlucides(glucides);
            regime.setLipides(lipides);
            regime.setDateDebut(dateDebut);
            regime.setDateFin(dateFin);
            regime.setStatus(status);

            // Ajouter le régime via le service
            RegimeService regimeService = new RegimeService();
            regimeService.addRegime(regime);

            // Rediriger vers la liste des régimes
            handleVoirListeButton(event);
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de saisie", "Les calories, protéines, glucides et lipides doivent être des nombres entiers.");
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur s'est produite lors de l'ajout du régime.");
            e.printStackTrace();
        }
    }

    // Méthode pour afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleVoirListeButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) voirListButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}