package GestionRegime.controller.plus;

import GestionRegime.services.PlayerService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PlayerController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField poidsField;

    @FXML
    private Label messageLabel;

    private PlayerService playerService;

    public PlayerController() {
        this.playerService = new PlayerService();
    }

    @FXML
    public void createPlayerAccount() {
        try {
            // Récupère les valeurs saisies par l'utilisateur
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            int age = Integer.parseInt(ageField.getText());
            double poids = Double.parseDouble(poidsField.getText());

            // Crée un compte joueur et envoie un e-mail
            playerService.createPlayerAccount(nom, prenom, age, poids);

            // Affiche un message de succès
            messageLabel.setText("Compte créé avec succès ! Un e-mail a été envoyé.");
        } catch (NumberFormatException e) {
            // Gère les erreurs de saisie
            messageLabel.setText("Erreur : Veuillez saisir un âge et un poids valides.");
        } catch (Exception e) {
            // Gère les autres erreurs
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }
}