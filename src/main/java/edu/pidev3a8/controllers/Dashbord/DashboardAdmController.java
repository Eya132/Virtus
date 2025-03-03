package edu.pidev3a8.controllers.Dashbord;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardAdmController {

    @FXML
    private StackPane contentPane; // Conteneur pour le contenu dynamique

    @FXML
    private ToggleGroup navbarToggleGroup; // Groupe pour les boutons du navbar

    // Méthode pour charger le fichier FXML dans le contenu dynamique
    private void loadFXML(String fxmlFile) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load(); // Utiliser Parent au lieu de BorderPane

            // Effacer le contenu actuel et ajouter le nouveau
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement du fichier FXML : " + fxmlFile);
        }
    }

    // Gestionnaire pour le bouton "Liste Utilisateurs"
    @FXML
    private void handleListUsers(ActionEvent event) {
        loadFXML("/ListUser.fxml"); // Charger ListUser.fxml
    }

    // Gestionnaire pour le bouton "Validation des Nutritionnistes"
    @FXML
    private void handleValidationNut(ActionEvent event) {
        loadFXML("/ValidationNut.fxml"); // Charger ValidationNut.fxml
    }
    @FXML
    private void handleListCommande(ActionEvent event) {
        loadFXML("/ListeCommandeAdmin.fxml"); // Charger ValidationNut.fxml
    }
}