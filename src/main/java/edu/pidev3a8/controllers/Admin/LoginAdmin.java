package edu.pidev3a8.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginAdmin {
    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private Text errorMessage;

    // Identifiants statiques
    private static final String ADMIN_USERNAME = "admin@admin.com";
    private static final String ADMIN_PASSWORD = "admin123";

    @FXML
    protected void handleTogglePasswordVisibility(ActionEvent event) {
        if (passwordField.isVisible()) {
            passwordTextField.setText(passwordField.getText());
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);
        } else {
            passwordField.setText(passwordTextField.getText());
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
        }
    }

    @FXML
    protected void handleLogin(ActionEvent event) {
        String enteredUsername = username.getText();
        String enteredPassword = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();

        if (ADMIN_USERNAME.equals(enteredUsername) && ADMIN_PASSWORD.equals(enteredPassword)) {
            // Redirection vers le tableau de bord
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/DashboardAdm.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setMaximized(true);
                stage.setFullScreen(true);
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Affichage du message d'erreur
            errorMessage.setText("Identifiants incorrects");
            errorMessage.setVisible(true);
        }
    }
}