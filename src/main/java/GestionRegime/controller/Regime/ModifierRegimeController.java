package GestionRegime.controller.Regime;

import GestionRegime.entities.Regime;
import GestionRegime.services.RegimeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ModifierRegimeController {

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
    private Button modifierRegimeButton;

    @FXML
    private Button annulerButton;

    private Regime regimeToUpdate; // Régime à modifier

    @FXML
    public void initialize() {
        // Initialiser le ComboBox avec les valeurs de l'enum
        objectifComboBox.getItems().setAll(Regime.Objectif.values());
    }

    // Méthode pour charger les données du régime à modifier
    public void loadRegimeData(Regime regime) {
        this.regimeToUpdate = regime;

        objectifComboBox.setValue(regime.getObjectif());
        caloriesTextField.setText(String.valueOf(regime.getCaloriesJournalieres()));
        proteinesTextField.setText(String.valueOf(regime.getProteines()));
        glucidesTextField.setText(String.valueOf(regime.getGlucides()));
        lipidesTextField.setText(String.valueOf(regime.getLipides()));
        dateDebutPicker.setValue(regime.getDateDebut());
        dateFinPicker.setValue(regime.getDateFin());
        statusTextField.setText(regime.getStatus()); // <-- Ici, statusTextField est null
    }

    @FXML
    void modifierRegimeAction(ActionEvent event) {
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
            int calories = Integer.parseInt(caloriesTextField.getText());
            int proteines = Integer.parseInt(proteinesTextField.getText());
            int glucides = Integer.parseInt(glucidesTextField.getText());
            int lipides = Integer.parseInt(lipidesTextField.getText());

            // Récupérer les dates sélectionnées
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            // Vérifier que la date de début est antérieure à la date de fin
            if (dateDebut.isAfter(dateFin)) {
                afficherAlerte("Erreur de saisie", "La date de début doit être antérieure à la date de fin.");
                return;
            }

            // Mettre à jour l'objet Regime
            regimeToUpdate.setObjectif(objectifComboBox.getValue());
            regimeToUpdate.setCaloriesJournalieres(calories);
            regimeToUpdate.setProteines(proteines);
            regimeToUpdate.setGlucides(glucides);
            regimeToUpdate.setLipides(lipides);
            regimeToUpdate.setDateDebut(dateDebut);
            regimeToUpdate.setDateFin(dateFin);
            regimeToUpdate.setStatus(statusTextField.getText());

            // Mettre à jour le régime via le service
            RegimeService regimeService = new RegimeService();
            regimeService.updateRegime(regimeToUpdate.getRegime_id(), regimeToUpdate);

            // Afficher un message de confirmation
            afficherAlerte("Succès", "Le régime a été modifié avec succès !");

            // Rediriger vers la liste des régimes
            handleAnnulerButton(event);
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de saisie", "Les calories, protéines, glucides et lipides doivent être des nombres entiers.");
        } catch (Exception e) {
            afficherAlerte("Erreur", "Une erreur s'est produite lors de la modification du régime.");
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
    private void handleAnnulerButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListRegime.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
