package edu.pidev3a8.controllers.Auth;

import edu.pidev3a8.controllers.Admin.ListUser;
import edu.pidev3a8.controllers.Dashbord.DashboardNut;
import edu.pidev3a8.controllers.Dashbord.DashboardUser;
import edu.pidev3a8.entities.User;
import edu.pidev3a8.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Login {

    @FXML private TextField username;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordTextField;
    @FXML private Button togglePasswordButton;
    @FXML private Button loginButton;
    @FXML private Text errorMessage;
    @FXML
    private Button forgotPasswordButton;

    @FXML
    private Button registerButton;

    private UserService userService = new UserService();

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
    protected void handleLoginButtonAction(ActionEvent event) {
        String email = username.getText();
        String pass = passwordField.isVisible() ? passwordField.getText() : passwordTextField.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            errorMessage.setText("Veuillez remplir tous les champs.");
            errorMessage.setVisible(true);
            return;
        }

        User user = userService.getUserByEmail(email);

        if (user == null) {
            errorMessage.setText("Utilisateur non trouvé.");
            errorMessage.setVisible(true);
            return;
        }

        if (BCrypt.checkpw(pass, user.getPasswordUser())) {
            errorMessage.setVisible(false);
            navigateToDashboard(user);
        } else {
            errorMessage.setText("Mot de passe incorrect.");
            errorMessage.setVisible(true);
        }
    }





        private void navigateToDashboard(User user) {
        String userId = user.getId_user();

        if (userId.matches(".*JMT.*") || userId.matches(".*JFT.*")) {
            navigateToDashboardUser(user);
        } else if (userId.matches(".*NUT.*")) {
            navigateToDashboardNut(user);
        } else if (userId.matches(".*ADM.*")) {
            navigateToListUser(user);
        } else {
            errorMessage.setText("Format d'ID non reconnu.");
            errorMessage.setVisible(true);
        }
    }

    private void navigateToDashboardUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardUser.fxml"));
            Parent root = loader.load();
            DashboardUser controller = loader.getController();
            controller.setLoggedInUser(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de bord Utilisateur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToDashboardNut(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardNut.fxml"));
            Parent root = loader.load();
            DashboardNut controller = loader.getController();
            controller.setLoggedInUser(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tableau de bord Nutritionniste");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void navigateToListUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
            Parent root = loader.load();
            ListUser controller = loader.getController();
            controller.setLoggedInUser(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Utilisateurs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de réinitialisation de mot de passe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) registerButton.getScene().getWindow();

            // Changer la scène
            stage.setScene(scene);
            stage.setTitle("Register");
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage.setText("Erreur lors du chargement de la page Login.");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    protected void handleForgotPasswordButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de réinitialisation de mot de passe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PasswordReset.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) forgotPasswordButton.getScene().getWindow();

            // Changer la scène
            stage.setScene(scene);
            stage.setTitle("Réinitialisation de mot de passe");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage.setText("Erreur lors du chargement de la page de réinitialisation.");
            errorMessage.setVisible(true);
        }
    }
}
