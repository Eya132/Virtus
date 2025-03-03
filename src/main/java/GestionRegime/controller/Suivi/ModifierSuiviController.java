package GestionRegime.controller.Suivi;

import GestionRegime.entities.Suivi;
import GestionRegime.services.SuiviService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierSuiviController {
    @FXML
    private TextField poidsTextField;

    @FXML
    private TextField tourDeTailleTextField;

    @FXML
    private TextField imcTextField;

    @FXML
    private DatePicker dateSuiviDatePicker;

    @FXML
    private Button modifierSuiviButton;

    @FXML
    private Button annulerButton;

    private Suivi suiviToUpdate; // Suivi à modifier

    // Méthode pour charger les données du suivi sélectionné depuis la liste
    public void setSuiviToUpdate(Suivi suivi) {
        if (suivi == null) {
            System.out.println("❌ Erreur: Aucun suivi sélectionné!");
            return;
        }
        this.suiviToUpdate = suivi;

        // Remplir les champs avec les données du suivi
        poidsTextField.setText(String.valueOf(suivi.getPoids()));
        tourDeTailleTextField.setText(String.valueOf(suivi.getTour_de_taille()));
        imcTextField.setText(String.valueOf(suivi.getImc()));
        dateSuiviDatePicker.setValue(suivi.getDate_suivi()); // Utilisez DatePicker pour la date
    }

    @FXML
    public void modifierSuiviAction(ActionEvent event) {
        if (suiviToUpdate == null) {
            System.out.println("❌ Erreur: Aucun suivi sélectionné pour modification.");
            return;
        }

        if (poidsTextField.getText().isEmpty() || tourDeTailleTextField.getText().isEmpty() ||
                imcTextField.getText().isEmpty() || dateSuiviDatePicker.getValue() == null) {
            System.out.println("❌ Tous les champs doivent être remplis !");
            return;
        }

        try {
            double poids = Double.parseDouble(poidsTextField.getText());
            double tourDeTaille = Double.parseDouble(tourDeTailleTextField.getText());
            double imc = Double.parseDouble(imcTextField.getText());
            LocalDate dateSuivi = dateSuiviDatePicker.getValue(); // Récupérer la date du DatePicker

            // Mise à jour des valeurs
            suiviToUpdate.setPoids(poids);
            suiviToUpdate.setTour_de_taille(tourDeTaille);
            suiviToUpdate.setImc(imc);
            suiviToUpdate.setDate_suivi(dateSuivi);

            // Mise à jour en base de données
            SuiviService suiviService = new SuiviService();
            suiviService.updateSuivi(suiviToUpdate.getSuivi_id(), suiviToUpdate);

            System.out.println("✅ Suivi mis à jour avec succès!");
            handleAnnulerButton(event);
        } catch (NumberFormatException e) {
            System.out.println("❌ Erreur: Veuillez entrer des valeurs numériques valides.");
        } catch (Exception e) {
            System.out.println("❌ Une erreur inattendue s'est produite: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAnnulerButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ListSuivi.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) annulerButton.getScene().getWindow();
            stage.close();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
