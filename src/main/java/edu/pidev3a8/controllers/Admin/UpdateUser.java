package edu.pidev3a8.controllers.Admin;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.pidev3a8.entities.*;
import edu.pidev3a8.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class UpdateUser {

    @FXML
    private TextField idField; // Champ pour afficher l'ID

    @FXML
    private TextField nomField, prenomField, emailField, telephoneField, adresseField, salaireField, maxDistanceField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private ComboBox<String> sexeComboBox, roleComboBox, niveauComboBox, experienceComboBox;
    @FXML
    private CheckBox premiumCheckBox;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ImageView photoView;
    @FXML
    private Button choosePhotoButton, updateButton;
    @FXML
    private Button backButton;
    @FXML
    private FontAwesomeIconView iconView;

    private User user;

    public void initialize() {
        // Configurer le champ ID pour qu'il soit en lecture seule et gris
        idField.setEditable(false);
        idField.setStyle("-fx-background-color: lightgray;");
        iconView.setGlyphName("ARROW_LEFT");
        iconView.setSize("24");

        // Remplir les ComboBox avec les valeurs possibles
        sexeComboBox.getItems().addAll("MALE", "FEMALE");
        roleComboBox.getItems().addAll("PLAYER", "COACH", "NUTRITIONIST");
        niveauComboBox.getItems().addAll("BEGINNER", "INTERMEDIATE", "ADVANCED");
        experienceComboBox.getItems().addAll("JUNIOR", "MID", "SENIOR");
    }

    public void setUserData(User user) {
        if (user == null) {
            System.out.println("Erreur: utilisateur null");
            return;
        }

        this.user = user;

        // Remplir les champs avec les données utilisateur
        idField.setText(user.getId_user());
        nomField.setText(user.getNomUser());
        prenomField.setText(user.getPrenomUser());
        emailField.setText(user.getEmailUser());
        passwordField.setText(user.getPasswordUser());
        dateNaissancePicker.setValue(user.getDateNaissanceUser());

        // Gérer les valeurs null pour sexeUser
        if (user.getSexeUser() != null) {
            sexeComboBox.setValue(user.getSexeUser().name());
        } else {
            sexeComboBox.setValue(null);
        }

        telephoneField.setText(user.getTelephoneUser());
        adresseField.setText(user.getAdresseUser());

        // Gérer les valeurs null pour role
        if (user.getRole() != null) {
            roleComboBox.setValue(user.getRole().name());
        } else {
            roleComboBox.setValue(null);
        }

        // Gérer les valeurs null pour niveau_joueur
        if (user.getNiveau_joueur() != null) {
            niveauComboBox.setValue(user.getNiveau_joueur().name());
        } else {
            niveauComboBox.setValue(null);
        }

        // Gérer les valeurs null pour experience
        if (user.getExperience() != null) {
            experienceComboBox.setValue(user.getExperience().name());
        } else {
            experienceComboBox.setValue(null);
        }

        salaireField.setText(String.valueOf(user.getSalaire()));
        premiumCheckBox.setSelected(user.getIs_Premuim());
        descriptionField.setText(user.getDescriptionUser());
        maxDistanceField.setText(String.valueOf(user.getMaxDistanceUser()));

        // Charger la photo
        if (user.getPhotoUser() != null && !user.getPhotoUser().isEmpty()) {
            Image image = new Image(user.getPhotoUser());
            photoView.setImage(image);
        }

        // Débogage
        System.out.println("Données utilisateur chargées avec succès.");
    }

    @FXML
    private void updateUser() {
        if (user == null) {
            System.out.println("Aucun utilisateur à mettre à jour.");
            return;
        }

        try {
            // Récupérer les données des champs
            String email = emailField.getText();
            String password = passwordField.getText();
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            LocalDate dateNaissance = dateNaissancePicker.getValue();
            String telephone = telephoneField.getText();
            String description = descriptionField.getText();
            String maxDistanceText = maxDistanceField.getText();
            String adresse = adresseField.getText();
            String salaireText = salaireField.getText();

            // Validation des champs
            validateEmail(email);
            validatePassword(password);
            validateNom(nom);
            validatePrenom(prenom);
            validateDateNaissance(dateNaissance);
            validateTelephone(telephone);
            validateDescription(description);
            validateMaxDistance(maxDistanceText);
            validateAdresse(adresse);

            // Récupérer les valeurs des ComboBox avec gestion des null
            UserSexe sexe = sexeComboBox.getValue() != null ? UserSexe.valueOf(sexeComboBox.getValue()) : null;
            UserRole role = roleComboBox.getValue() != null ? UserRole.valueOf(roleComboBox.getValue()) : null;
            User_Niveau niveauJoueur = niveauComboBox.getValue() != null ? User_Niveau.valueOf(niveauComboBox.getValue()) : null;
            Experience experience = experienceComboBox.getValue() != null ? Experience.valueOf(experienceComboBox.getValue()) : null;

            // Validation du rôle
            if (role == null) {
                throw new IllegalArgumentException("Veuillez sélectionner un rôle.");
            }

            // Validation du salaire
            double salaire = salaireField.isDisabled() ? 0 : Double.parseDouble(salaireText);
            if (salaire < 600 || salaire > 4000) {
                throw new IllegalArgumentException("Le salaire doit être compris entre 600 et 4000.");
            }

            // Validation spécifique au rôle
            if (role == UserRole.PLAYER) {
                if (salaire != 0 || experience != null) {
                    throw new IllegalArgumentException("Un joueur ne peut pas remplir les champs salaire et expérience.");
                }
            } else if (role == UserRole.NUTRITIONIST) {
                if (niveauJoueur != null || premiumCheckBox.isSelected()) {
                    throw new IllegalArgumentException("Un nutritionniste ne peut pas remplir les champs niveau joueur et premium.");
                }
            }

            // Mettre à jour les champs de l'utilisateur
            user.setNomUser(nom);
            user.setPrenomUser(prenom);
            user.setEmailUser(email);
            user.setPasswordUser(password);
            user.setDateNaissanceUser(dateNaissance);
            user.setSexeUser(sexe);
            user.setTelephoneUser(telephone);
            user.setAdresseUser(adresse);
            user.setRole(role);
            user.setNiveau_joueur(niveauJoueur);
            user.setExperience(experience);
            user.setSalaire(salaire);
            user.setIs_Premuim(premiumCheckBox.isSelected());
            user.setDescriptionUser(description);
            user.setMaxDistanceUser(Integer.parseInt(maxDistanceText));

            // Mettre à jour l'utilisateur dans la base de données
            UserService userService = new UserService();
            userService.updateEntity(user);

            // Afficher une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise à jour réussie");
            alert.setHeaderText(null);
            alert.setContentText("L'utilisateur a été mis à jour avec succès !");
            alert.showAndWait();

            // Fermer la fenêtre actuelle
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();

            // Ouvrir la fenêtre ListUser.fxml
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
                Parent root = loader.load();

                Stage listUserStage = new Stage();
                listUserStage.setScene(new Scene(root));
                listUserStage.setFullScreen(true);
                listUserStage.setMaximized(true);
                listUserStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de l'interface ListUser.fxml");
            }
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs de validation
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de validation");
            alert.setHeaderText("Erreur de validation");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            // Gestion des autres erreurs
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de la mise à jour de l'utilisateur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Méthodes de validation
    private void validateEmail(String email) throws IllegalArgumentException {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir un email.");
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            throw new IllegalArgumentException("L'email n'est pas dans un format valide.");
        }
    }

    private void validatePassword(String password) throws IllegalArgumentException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir un mot de passe.");
        }
        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères et un chiffre.");
        }
    }

    private void validateNom(String nom) throws IllegalArgumentException {
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir un nom.");
        }
    }

    private void validatePrenom(String prenom) throws IllegalArgumentException {
        if (prenom == null || prenom.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir un prénom.");
        }
    }

    private void validateDateNaissance(LocalDate dateNaissance) throws IllegalArgumentException {
        if (dateNaissance == null) {
            throw new IllegalArgumentException("Veuillez sélectionner une date de naissance.");
        }
        LocalDate now = LocalDate.now();
        Period age = Period.between(dateNaissance, now);
        if (age.getYears() < 12) {
            throw new IllegalArgumentException("L'utilisateur doit avoir au moins 12 ans.");
        }
    }

    private void validateTelephone(String telephone) throws IllegalArgumentException {
        if (telephone == null || telephone.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir un numéro de téléphone.");
        }
        if (!telephone.matches("\\d{8}")) {
            throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 8 chiffres.");
        }
    }

    private void validateDescription(String description) throws IllegalArgumentException {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir une description.");
        }
    }

    private void validateMaxDistance(String maxDistanceText) throws IllegalArgumentException {
        if (maxDistanceText == null || maxDistanceText.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir une distance maximale.");
        }
        try {
            int maxDistance = Integer.parseInt(maxDistanceText);
            if (maxDistance <= 0) {
                throw new IllegalArgumentException("La distance maximale doit être un nombre positif.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La distance maximale doit être un nombre valide.");
        }
    }

    private void validateAdresse(String adresse) throws IllegalArgumentException {
        if (adresse == null || adresse.isEmpty()) {
            throw new IllegalArgumentException("Veuillez saisir une adresse.");
        }
    }

    @FXML
    private void choosePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une photo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            user.setPhotoUser(file.toURI().toString());
            photoView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Utilisateurs");
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setMaximized(true);

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}