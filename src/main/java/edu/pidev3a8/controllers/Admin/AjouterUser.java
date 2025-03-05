package edu.pidev3a8.controllers.Admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.pidev3a8.entities.*;
import edu.pidev3a8.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class AjouterUser {

    // Références aux champs du formulaire FXML
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField maxDistanceField;
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<UserRole> roleCombo;
    @FXML
    private ComboBox<User_Niveau> niveauCombo;
    @FXML
    private ComboBox<Experience> experienceCombo;
    @FXML
    private TextField salaryField;
    @FXML
    private CheckBox premiumCheck;
    @FXML
    private Button backButton;
    @FXML
    private ImageView imageView;
    @FXML
    private FontAwesomeIconView iconView;

    @FXML
    private HBox pieceJointeBox;
    @FXML
    private TextField pieceJointeField;
    private File selectedPieceJointeFile;




    private String imagePath;

    // Méthode pour initialiser les ComboBox et autres éléments
    @FXML
    public void initialize() {
        // Remplir les ComboBox avec les valeurs des énumérations
        roleCombo.getItems().setAll(UserRole.values());
        niveauCombo.getItems().setAll(User_Niveau.values());
        experienceCombo.getItems().setAll(Experience.values());
        pieceJointeBox.setVisible(false);

        // Définir une valeur par défaut pour les ComboBox
        roleCombo.setValue(UserRole.PLAYER); // Par exemple, PLAYER par défaut
        niveauCombo.setValue(User_Niveau.Debutant); // Par exemple, DEBUTANT par défaut
        experienceCombo.setValue(Experience.FOUR_YEARS); // Par exemple, AMATEUR par défaut

        // Ajouter un écouteur pour le changement de rôle
        roleCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateFormBasedOnRole(newValue);
        });
    }

    // Méthode pour mettre à jour le formulaire en fonction du rôle sélectionné
    private void updateFormBasedOnRole(UserRole role) {
        if (role == UserRole.PLAYER) {
            salaryField.setDisable(true);
            experienceCombo.setDisable(true);
            pieceJointeBox.setVisible(false);
            salaryField.clear();
            experienceCombo.getSelectionModel().clearSelection();
        } else if (role == UserRole.NUTRITIONIST) {
            niveauCombo.setDisable(true);
            premiumCheck.setDisable(true);
            pieceJointeBox.setVisible(true);
            niveauCombo.getSelectionModel().clearSelection();
            premiumCheck.setSelected(false);
        } else {
            salaryField.setDisable(false);
            experienceCombo.setDisable(false);
            niveauCombo.setDisable(false);
            premiumCheck.setDisable(false);
            pieceJointeBox.setVisible(false);
        }
    }

    // Méthode pour gérer l'ajout d'un utilisateur
    @FXML
    void AjouterUser(ActionEvent event) {
        try {
            // Récupérer les données des champs
            String email = emailField.getText();
            String password = passwordField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            LocalDate dateNaissance = dobPicker.getValue();
            String telephone = phoneField.getText();
            String description = descriptionArea.getText();
            String maxDistanceText = maxDistanceField.getText();
            String adresse = addressField.getText();
            UserRole role = roleCombo.getValue();
            String salaireText = salaryField.getText();
            String pieceJointe = selectedPieceJointeFile != null ? selectedPieceJointeFile.toURI().toString() : null;

            // Validation séquentielle des champs
            // 1. Vérification de l'email
            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir un email.");
            }
            if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
                throw new IllegalArgumentException("L'email n'est pas dans un format valide.");
            }

            // 2. Vérification du mot de passe
            if (password == null || password.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir un mot de passe.");
            }
            if (password.length() < 8 || !password.matches(".*\\d.*")) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères et un chiffre.");
            }

            // 3. Vérification du nom
            if (nom == null || nom.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir un nom.");
            }

            // 4. Vérification du prénom
            if (prenom == null || prenom.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir un prénom.");
            }

            // 5. Vérification de la date de naissance
            if (dateNaissance == null) {
                throw new IllegalArgumentException("Veuillez sélectionner une date de naissance.");
            }
            LocalDate now = LocalDate.now();
            Period age = Period.between(dateNaissance, now);
            if (age.getYears() < 12) {
                throw new IllegalArgumentException("L'utilisateur doit avoir au moins 12 ans.");
            }

            // 6. Vérification du téléphone
            if (telephone == null || telephone.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir un numéro de téléphone.");
            }
            if (!telephone.matches("\\d{8}")) {
                throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 8 chiffres.");
            }

            // 7. Vérification de la description
            if (description == null || description.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir une description.");
            }

            // 8. Vérification de la distance maximale
            if (maxDistanceText == null || maxDistanceText.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir une distance maximale.");
            }
            int maxDistance = Integer.parseInt(maxDistanceText);

            // 9. Vérification de l'adresse
            if (adresse == null || adresse.isEmpty()) {
                throw new IllegalArgumentException("Veuillez saisir une adresse.");
            }

            // 10. Vérification du rôle
            if (role == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un rôle.");
            }

            // 11. Vérification du salaire (si applicable)


            double salaire = 0;
            if (role == UserRole.NUTRITIONIST) {
                if (salaireText == null || salaireText.isEmpty()) {
                    throw new IllegalArgumentException("Veuillez saisir un salaire pour le nutritionniste.");
                }
                salaire = Double.parseDouble(salaireText);
                if (salaire < 600 || salaire > 4000) {
                    throw new IllegalArgumentException("Le salaire doit être compris entre 600 et 4000.");
                }
            } else {
                // Si le rôle n'est pas NUTRITIONIST, le salaire doit être 0
                salaire = 0;
            }

            // Autres validations spécifiques au rôle
            UserSexe sexe = maleRadio.isSelected() ? UserSexe.M : UserSexe.F;
            User_Niveau niveauJoueur = niveauCombo.isDisabled() ? null : niveauCombo.getValue();
            Experience experience = experienceCombo.isDisabled() ? null : experienceCombo.getValue();
            boolean isPremium = premiumCheck.isDisabled() ? false : premiumCheck.isSelected();

            if (role == UserRole.PLAYER) {
                if (salaire != 0 || experience != null) {
                    throw new IllegalArgumentException("Un joueur ne peut pas remplir les champs salaire et expérience.");
                }
            } else if (role == UserRole.NUTRITIONIST) {
                if (niveauJoueur != null || isPremium) {
                    throw new IllegalArgumentException("Un nutritionniste ne peut pas remplir les champs niveau joueur et premium.");
                }
            }

            // Créer un objet User avec les données
            User user = new User(email, password, nom, prenom, dateNaissance, sexe, telephone, description, maxDistance, adresse, role, niveauJoueur, experience, salaire, isPremium, pieceJointe);
            user.setPhotoUser(imagePath);

            // Ajouter l'utilisateur via le service
            UserService userService = new UserService();
            userService.addEntity(user);
            System.out.println("Role selected: " + role);

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Utilisateur ajouté avec succès !");
            alert.setContentText("L'utilisateur " + nom + " " + prenom + " a été ajouté.");
            alert.showAndWait();

            // Rediriger vers la liste des utilisateurs
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
            Parent root = fxmlLoader.load();
            nomField.getScene().setRoot(root);

        } catch (IllegalArgumentException e) {
            // Gestion des erreurs de validation
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de validation");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Gestion des autres erreurs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout de l'utilisateur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void goBack() {
        try {
            System.out.println("Tentative de chargement de ListUser.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
            Parent root = loader.load();
            System.out.println("Fichier FXML chargé avec succès");

            Stage stage = new Stage();
            stage.setTitle("Liste des Utilisateurs");
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setMaximized(true);

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            stage.show();
            System.out.println("Nouvelle fenêtre affichée");
        } catch (IOException e) {
            e.printStackTrace(); // Ajoutez cette ligne pour voir les erreurs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors du chargement de la page");
            alert.setContentText("Impossible de charger la liste des utilisateurs.");
            alert.showAndWait();
        }
    }
    @FXML
    void AjouterImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null); // Ouvrir la boîte de dialogue pour sélectionner un fichier

        if (file != null) {
            // Stocker le chemin de l'image
            imagePath = file.toURI().toString();

            // Afficher l'image dans l'ImageView
            Image image = new Image(imagePath);
            imageView.setImage(image);
        }
    }

    @FXML
    private void handleSelectPieceJointe(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Document Files", "*.pdf", "*.doc", "*.docx"));
        File selectedFile = fileChooser.showOpenDialog(pieceJointeBox.getScene().getWindow());
        if (selectedFile != null) {
            selectedPieceJointeFile = selectedFile;
            pieceJointeField.setText(selectedFile.getName());
        }
    }
}