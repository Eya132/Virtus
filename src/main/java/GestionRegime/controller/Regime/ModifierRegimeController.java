package GestionRegime.controller.Regime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import GestionRegime.entities.Regime;
import GestionRegime.services.RegimeService;

import java.io.IOException;

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
    private TextField dateDebutTextField;

    @FXML
    private TextField dateFinTextField;

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

        // Remplir les champs avec les données du régime
        objectifComboBox.setValue(regime.getObjectif());
        caloriesTextField.setText(String.valueOf(regime.getCaloriesJournalieres()));
        proteinesTextField.setText(String.valueOf(regime.getProteines()));
        glucidesTextField.setText(String.valueOf(regime.getGlucides()));
        lipidesTextField.setText(String.valueOf(regime.getLipides()));
        dateDebutTextField.setText(regime.getDateDebut().toString());
        dateFinTextField.setText(regime.getDateFin().toString());
        statusTextField.setText(regime.getStatus());
    }

    @FXML
    void modifierRegimeAction(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (objectifComboBox.getValue() == null || caloriesTextField.getText().isEmpty() ||
                proteinesTextField.getText().isEmpty() || glucidesTextField.getText().isEmpty() ||
                lipidesTextField.getText().isEmpty() || dateDebutTextField.getText().isEmpty() ||
                dateFinTextField.getText().isEmpty() || statusTextField.getText().isEmpty()) {
            System.out.println("❌ Tous les champs doivent être remplis !");
            return;
        }

        // Récupérer les valeurs des champs
        Regime.Objectif objectif = objectifComboBox.getValue();
        int calories = Integer.parseInt(caloriesTextField.getText());
        int proteines = Integer.parseInt(proteinesTextField.getText());
        int glucides = Integer.parseInt(glucidesTextField.getText());
        int lipides = Integer.parseInt(lipidesTextField.getText());
        String dateDebut = dateDebutTextField.getText();
        String dateFin = dateFinTextField.getText();
        String status = statusTextField.getText();

        // Convertir les dates en java.sql.Date
        try {
            java.sql.Date sqlDateDebut = java.sql.Date.valueOf(dateDebut); // Format AAAA-MM-JJ
            java.sql.Date sqlDateFin = java.sql.Date.valueOf(dateFin);     // Format AAAA-MM-JJ

            // Mettre à jour l'objet Regime
            regimeToUpdate.setObjectif(objectif);
            regimeToUpdate.setCaloriesJournalieres(calories);
            regimeToUpdate.setProteines(proteines);
            regimeToUpdate.setGlucides(glucides);
            regimeToUpdate.setLipides(lipides);
            regimeToUpdate.setDateDebut(sqlDateDebut);
            regimeToUpdate.setDateFin(sqlDateFin);
            regimeToUpdate.setStatus(status);

            // Mettre à jour le régime via le service
            RegimeService regimeService = new RegimeService();
            regimeService.updateRegime(regimeToUpdate.getRegime_id(), regimeToUpdate);

            // Rediriger vers la liste des régimes
            handleAnnulerButton(event);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Format de date invalide. Utilisez le format AAAA-MM-JJ.");
        }
    }

    @FXML
    private void handleAnnulerButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ListRegime.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue : " + e.getMessage());
            e.printStackTrace();
        }
    }
}