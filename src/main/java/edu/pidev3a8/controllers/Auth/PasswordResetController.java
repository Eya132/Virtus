package edu.pidev3a8.controllers.Auth;

import edu.pidev3a8.services.EmailService;
import edu.pidev3a8.services.PasswordResetService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordResetController {

    @FXML private TextField emailField;
    @FXML private TextField tokenField;
    @FXML private TextField newPasswordField;
    @FXML private Label errorMessage;
    @FXML private Label tokenLabel;
    @FXML private Label newPasswordLabel;
    @FXML private Button verifyTokenButton;
    @FXML private Button resetPasswordButton;

    @FXML
    protected void initialize() {
        // Initialiser la visibilité des champs et boutons
        tokenField.setVisible(false);
        tokenLabel.setVisible(false);
        newPasswordField.setVisible(false);
        newPasswordLabel.setVisible(false);
        verifyTokenButton.setVisible(false);
        resetPasswordButton.setVisible(false);
    }

    @FXML
    protected void handleRequestToken() {
        String email = emailField.getText();

        if (email.isEmpty() || !isValidEmail(email)) {
            errorMessage.setText("Veuillez entrer une adresse e-mail valide.");
            errorMessage.setVisible(true);
            return;
        }

        // Générer un token et l'envoyer par e-mail
        String token = generateResetToken();
        boolean emailSent = sendResetEmail(email, token);

        if (emailSent) {
            // Stocker le token dans la base de données
            PasswordResetService.storeResetToken(email, token);
            errorMessage.setText("Token envoyé à votre adresse e-mail.");
            errorMessage.setVisible(true);

            // Montrer le champ pour entrer le token et le bouton de vérification
            tokenField.setVisible(true);
            tokenLabel.setVisible(true);
            verifyTokenButton.setVisible(true);
        } else {
            errorMessage.setText("Erreur lors de l'envoi de l'e-mail.");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    protected void handleVerifyToken() {
        String token = tokenField.getText();

        if (token.isEmpty()) {
            errorMessage.setText("Veuillez entrer le token reçu.");
            errorMessage.setVisible(true);
            return;
        }

        // Valider le token
        if (PasswordResetService.isValidToken(token)) {
            errorMessage.setText("Token valide. Vous pouvez maintenant réinitialiser votre mot de passe.");
            errorMessage.setVisible(true);

            // Montrer le champ pour entrer le nouveau mot de passe et le bouton de réinitialisation
            newPasswordField.setVisible(true);
            newPasswordLabel.setVisible(true);
            resetPasswordButton.setVisible(true);
        } else {
            errorMessage.setText("Token invalide ou expiré.");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    protected void handleResetPassword() {
        String newPassword = newPasswordField.getText();

        if (newPassword.isEmpty()) {
            errorMessage.setText("Veuillez entrer un nouveau mot de passe.");
            errorMessage.setVisible(true);
            return;
        }

        // Hacher le nouveau mot de passe
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // Réinitialiser le mot de passe
        boolean passwordUpdated = PasswordResetService.resetPassword(tokenField.getText(), hashedPassword);

        if (passwordUpdated) {
            errorMessage.setText("Mot de passe réinitialisé avec succès.");
            errorMessage.setVisible(true);
        } else {
            errorMessage.setText("Erreur lors de la réinitialisation du mot de passe.");
            errorMessage.setVisible(true);
        }
    }

    private String generateResetToken() {
        // Générer un token unique (par exemple, un UUID)
        return java.util.UUID.randomUUID().toString();
    }

    private boolean sendResetEmail(String email, String token) {
        try {
            EmailService.sendResetEmail(email, token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        // Une simple validation d'e-mail (peut être améliorée)
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}