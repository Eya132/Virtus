package edu.pidev3a8.controllers.Admin;

import edu.pidev3a8.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AfficherUser {
    //dddddd
    //sss
    @FXML
    private Label idLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label roleLabel;

    @FXML
    private Label dateNaissanceLabel;

    @FXML
    private Label sexeLabel;

    @FXML
    private Label telephoneLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private Label salaireLabel;

    @FXML
    private Label niveauJoueurLabel;

    @FXML
    private Label experienceLabel;

    @FXML
    private Label photoLabel;

    @FXML
    private Button backButton;

    @FXML
    private ImageView photoImageView;

    public void setUserData(User user) {
        idLabel.setText("ID: " + user.getId_user());
        nomLabel.setText("Nom: " + user.getNomUser());
        prenomLabel.setText("Prénom: " + user.getPrenomUser());
        emailLabel.setText("Email: " + user.getEmailUser());
        roleLabel.setText("Rôle: " + user.getRole());
        dateNaissanceLabel.setText("Date de naissance: " + user.getDateNaissanceUser());
        sexeLabel.setText("Sexe: " + user.getSexeUser());
        telephoneLabel.setText("Téléphone: " + user.getTelephoneUser());
        descriptionLabel.setText("Description: " + user.getDescriptionUser());
        adresseLabel.setText("Adresse: " + user.getAdresseUser());
        salaireLabel.setText("Salaire: " + user.getSalaire());
        niveauJoueurLabel.setText("Niveau joueur: " + user.getNiveau_joueur());
        experienceLabel.setText("Expérience: " + user.getExperience());

        // Charger l'image de l'utilisateur
        if (user.getPhotoUser() != null && !user.getPhotoUser().isEmpty()) {
            try {
                Image image = new Image(user.getPhotoUser(), true); // Charger l'image en arrière-plan
                photoImageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image: " + e.getMessage());
            }
        } else {
            photoLabel.setText("Photo: Non disponible");
        }
    }

    @FXML
    private void goBack() {
        try {
            // Charger la vue de la liste des utilisateurs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Liste des Utilisateurs");
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setMaximized(true);

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}