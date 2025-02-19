
package gestion_match.controller;

import gestion_match.entites.Match;
import gestion_match.services.MatchService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

        public class ModifierMatchController {

            @FXML
            private DatePicker datePicker; // Sélecteur de date
            @FXML
            private TextField heureField; // Champ pour l'heure
            @FXML
            private TextField localisationField; // Champ pour la localisation
            @FXML
            private TextField terrainField; // Champ pour le terrain
            @FXML
            private ComboBox<String> typeSportComboBox; // ComboBox pour le type de sport

            private Match match; // Le match à modifier
            private MatchService matchService = new MatchService(); // Service pour gérer les matchs
            private Stage stage; // La fenêtre de modification
            private DashboardController dashboardController; // Référence au DashboardController

            public void setMatch(Match match) {
                this.match = match;

                // Convertir java.sql.Date en java.time.LocalDate
                if (match.getDateMatch() != null) {
                    java.util.Date utilDate = new java.util.Date(match.getDateMatch().getTime());
                    LocalDate localDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    datePicker.setValue(localDate);
                }

                // Remplir les champs avec les données du match
                heureField.setText(match.getHeure());
                localisationField.setText(match.getLocalisation());
                terrainField.setText(match.getTerrain());

                // Initialiser le ComboBox pour le type de sport
                typeSportComboBox.setValue(match.getTypeSport());
            }

            /**
             * Enregistre les modifications apportées au match.
             */
            @FXML
            private void enregistrerModifications() {
                // Debugging: Print out the current values
                System.out.println("Date: " + datePicker.getValue());
                System.out.println("Heure: " + heureField.getText());
                System.out.println("Localisation: " + localisationField.getText());
                System.out.println("Terrain: " + terrainField.getText());
                System.out.println("Type de sport: " + typeSportComboBox.getValue());

                // Validation des champs obligatoires
                if (datePicker.getValue() == null || heureField.getText().isEmpty() || localisationField.getText().isEmpty() || terrainField.getText().isEmpty() || typeSportComboBox.getValue() == null) {
                    showAlert("Veuillez remplir tous les champs obligatoires.");
                    return; // Ne pas enregistrer si les champs obligatoires sont vides
                }

                try {
                    // Mettre à jour les informations modifiables du match
                    LocalDate localDate = datePicker.getValue();
                    match.setDateMatch(Date.valueOf(localDate));
                    match.setHeure(heureField.getText());
                    match.setLocalisation(localisationField.getText());
                    match.setTerrain(terrainField.getText());
                    match.setTypeSport(typeSportComboBox.getValue()); // Ajout du type de sport

                    // Mettre à jour le match dans la base de données
                    matchService.updateEntity(match);

                    // Fermer la fenêtre de modification
                    if (stage != null) {
                        stage.close();
                    } else {
                        System.err.println("Stage is null!");
                    }

                    // Rafraîchir la liste des matchs dans le DashboardController
                    if (dashboardController != null) {
                        dashboardController.loadMatchsForUser();
                    } else {
                        System.err.println("DashboardController is null!");
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Print the full stack trace
                    showAlert("Erreur lors de la mise à jour du match : " + e.getMessage());
                }
            }

            /**
             * Affiche une boîte de dialogue d'alerte.
             *
             * @param message Le message d'erreur à afficher.
             */
            private void showAlert(String message) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            }

            public void setStage(Stage stage) {
                this.stage = stage;
            }

            public void setDashboardController(DashboardController dashboardController) {
                this.dashboardController = dashboardController;
            }
        }
