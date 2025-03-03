package edu.pidev3a8.controllers.Auth;

import edu.pidev3a8.controllers.Admin.ListUser;
import edu.pidev3a8.entities.*;
import edu.pidev3a8.services.UserService;
import edu.pidev3a8.tools.myConnection;
import edu.pidev3a8.utils.EmailValidator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.CvType;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;


public class Register {

    // Champs FXML
    @FXML
    private TextField email;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField telephone;
    @FXML
    private TextField adresse;
    @FXML
    private DatePicker dateNaissancePicker;
    @FXML
    private ComboBox<UserSexe> sexeComboBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField confirmPasswordTextField;
    @FXML
    private Button togglePasswordButton;
    @FXML
    private VBox joueurCard;
    @FXML
    private VBox nutritionnisteCard;
    @FXML
    private ComboBox<User_Niveau> niveauComboBox;
    @FXML
    private ComboBox<Experience> experienceComboBox;
    @FXML
    private Button registerButton;
    @FXML
    private Text errorMessage;
    @FXML
    private Text passwordError;
    @FXML
    private Button selectImageButton;
    @FXML
    private ImageView userImageView;
    @FXML
    private Button openCameraButton; // Bouton pour ouvrir la caméra
    private String imagePath;
    @FXML
    private HBox pieceJointeBox;
    @FXML
    private Button selectPieceJointeButton;
    @FXML
    private TextField pieceJointeTextField;
    private File selectedPieceJointeFile;

    @FXML
    private CheckBox termsCheckBox;

    @FXML
    private WebView mapWebView;

    @FXML
    private Button LoginButton;





    // Rôle sélectionné

    private Stage mapStage;
    private UserRole selectedRole;
    private File selectedImageFile;

    private WebEngine webEngine;

    private double selectedLatitude;
    private double selectedLongitude;

    // Service pour gérer les utilisateurs
    private final UserService userService = new UserService();

    // Initialisation du contrôleur
    @FXML
    public void initialize() {
        // Initialisation des ComboBox avec les enums
        sexeComboBox.getItems().setAll(UserSexe.values());
        niveauComboBox.getItems().setAll(User_Niveau.values());
        experienceComboBox.getItems().setAll(Experience.values());
        pieceJointeBox.setVisible(false);
        selectPieceJointeButton.setOnAction(event -> handleSelectPieceJointe());

        webEngine = mapWebView.getEngine();
        webEngine.setJavaScriptEnabled(true);

        URL mapUrl = getClass().getResource("/map.html");
        if (mapUrl == null) {
            System.err.println("map.html not found! Vérifie son emplacement.");
        } else {
            System.out.println("map.html trouvé : " + mapUrl.toExternalForm());
            webEngine.load(mapUrl.toExternalForm());
        }

        webEngine.setOnError(event -> {
            System.err.println("Erreur JavaScript : " + event.getMessage());
        });

        webEngine.setOnAlert(event -> {
            System.out.println("alert webview : " + event.getData());
        });


        // Charger la carte OpenStreetMap avec Leaflet






        // Écouter les messages JavaScript pour récupérer les coordonnées
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this); // Définir l'objet Java dans le contexte JavaScript
                System.out.println("Objet Java défini dans JavaScript.");
            }
        });


        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("java", this);









        // Configuration des convertisseurs pour afficher les valeurs lisibles
        configureComboBoxConverters();

        // Gestion de la visibilité du mot de passe
        togglePasswordButton.setOnAction(event -> togglePasswordVisibility());

        // Gestion du bouton de sélection d'image
        selectImageButton.setOnAction(event -> handleSelectImage());

        // Gestion du bouton pour ouvrir la caméra
        openCameraButton.setOnAction(event -> handleOpenCamera());

        registerButton.setDisable(true);

        termsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(!newValue);
        });
    }

    // Configuration des convertisseurs pour les ComboBox
    private void configureComboBoxConverters() {
        sexeComboBox.setConverter(new StringConverter<UserSexe>() {
            @Override
            public String toString(UserSexe sexe) {
                return (sexe != null) ? sexe.name() : "";
            }

            @Override
            public UserSexe fromString(String string) {
                return UserSexe.valueOf(string);
            }
        });

        niveauComboBox.setConverter(new StringConverter<User_Niveau>() {
            @Override
            public String toString(User_Niveau niveau) {
                return (niveau != null) ? niveau.name() : "";
            }

            @Override
            public User_Niveau fromString(String string) {
                return User_Niveau.valueOf(string);
            }
        });

        experienceComboBox.setConverter(new StringConverter<Experience>() {
            @Override
            public String toString(Experience experience) {
                return (experience != null) ? experience.name() : "";
            }

            @Override
            public Experience fromString(String string) {
                return Experience.valueOf(string);
            }
        });
    }


    public void logFromJS(String message) {
        System.out.println("Message depuis JavaScript : " + message);
    }

    // Basculer la visibilité du mot de passe
    private void togglePasswordVisibility() {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordTextField.setVisible(true);
            passwordTextField.setText(passwordField.getText());
            confirmPasswordField.setVisible(false);
            confirmPasswordTextField.setVisible(true);
            confirmPasswordTextField.setText(confirmPasswordField.getText());
        } else {
            passwordTextField.setVisible(false);
            passwordField.setVisible(true);
            passwordField.setText(passwordTextField.getText());
            confirmPasswordTextField.setVisible(false);
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setText(confirmPasswordTextField.getText());
        }
    }


    // Sélection du rôle "Joueur"
    @FXML
    private void selectJoueur() {
        selectedRole = UserRole.PLAYER;
        joueurCard.setStyle("-fx-background-color: #d0f0d0; -fx-background-radius: 10; -fx-padding: 20;");
        nutritionnisteCard.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-padding: 20;");
        niveauComboBox.setVisible(true);
        experienceComboBox.setVisible(false);
        pieceJointeBox.setVisible(false);
    }

    // Sélection du rôle "Nutritionniste"
    @FXML
    private void selectNutritionniste() {
        selectedRole = UserRole.NUTRITIONIST;
        nutritionnisteCard.setStyle("-fx-background-color: #d0f0d0; -fx-background-radius: 10; -fx-padding: 20;");
        joueurCard.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 10; -fx-padding: 20;");
        experienceComboBox.setVisible(true);
        niveauComboBox.setVisible(false);
        pieceJointeBox.setVisible(true);
    }

    // Gestion de l'inscription
    @FXML
    private void handleRegister() {
        if (validateInputs()) {
            try {

                String query = "SELECT COUNT(*) FROM User WHERE email_user = ?";
                if (executeQueryAndCheckExistence(query, email.getText())) {
                    errorMessage.setText("Cet email est déjà utilisé.");
                    errorMessage.setVisible(true);
                    return; // Arrêter l'exécution si l'email existe déjà
                }

                // Créer un objet User avec les données
                User user = new User();
                user.setEmailUser(email.getText());
                user.setNomUser(nom.getText());
                user.setPrenomUser(prenom.getText());
                user.setTelephoneUser(telephone.getText());
                user.setAdresseUser(adresse.getText());
                user.setDateNaissanceUser(dateNaissancePicker.getValue());
                user.setSexeUser(sexeComboBox.getValue());
                user.setPasswordUser(passwordField.getText());
                user.setRole(selectedRole);
                user.setNiveau_joueur(niveauComboBox.getValue());
                user.setExperience(experienceComboBox.getValue());

                // Gestion de l'image de profil
                if (selectedImageFile != null) {
                    user.setPhotoUser(selectedImageFile.toURI().toString());
                }

                // Gestion de la pièce jointe (image du diplôme)
                if (selectedPieceJointeFile != null) {
                    user.setPiece_jointe(selectedPieceJointeFile.toURI().toString());
                }

                // Vérifier si le rôle est nutritionniste
                if (selectedRole == UserRole.NUTRITIONIST) {
                    // Vérifier si une image a été sélectionnée
                    if (selectedPieceJointeFile == null) {
                        errorMessage.setText("Veuillez sélectionner une image pour valider votre diplôme.");
                        errorMessage.setVisible(true);
                        return;
                    }

                    // Vérifier si le fichier est une image (optionnel)
                    String fileName = selectedPieceJointeFile.getName().toLowerCase();
                    if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png") && !fileName.endsWith(".jpeg")) {
                        errorMessage.setText("Veuillez sélectionner une image valide (JPG, PNG).");
                        errorMessage.setVisible(true);
                        return;
                    }

                    // Extraire le texte de l'image
                    String extractedText = extractTextFromImage(selectedPieceJointeFile);

                    // Afficher le texte extrait pour déboguer
                        System.out.println("Texte extrait de l'image : " + extractedText);

                    // Vérifier si le texte a été extrait avec succès
                    if (extractedText == null || extractedText.isEmpty()) {
                        errorMessage.setText("Impossible d'extraire le texte de l'image. Veuillez vérifier le fichier.");
                        errorMessage.setVisible(true);
                        return;
                    }

                    // Valider le texte extrait
                    UserStatus status = isDiplomaValid(extractedText) ? UserStatus.VALIDATED : UserStatus.PENDING;
                    user.setStatus(status); // Définir le statut en fonction de la validation du diplôme

                    // Ajouter l'utilisateur via le service
                    userService.addEntity(user, status);

                    // Rediriger en fonction du statut
                    if (status == UserStatus.VALIDATED) {
                        errorMessage.setFill(Color.GREEN);
                        errorMessage.setText("Diplôme validé ! Inscription réussie.");

                        // Rediriger vers l'interface "Bienvenue"
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardNut.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) registerButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } else {
                        errorMessage.setFill(Color.ORANGE);
                        errorMessage.setText("Création de compte effectuée, mais diplôme non reconnu.");

                        // Rediriger vers l'interface "Veuillez vérifier votre compte de nouveau"
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PendingValidation.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) registerButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                } else {
                    // Pour les autres rôles, le statut est VALIDATED par défaut
                    user.setStatus(UserStatus.VALIDATED);
                    userService.addEntity(user, UserStatus.VALIDATED);
                    errorMessage.setFill(Color.GREEN);
                    errorMessage.setText("Création de compte effectuée.");
                    errorMessage.setVisible(true);
                }

            } catch (Exception e) {
                // Afficher un message d'erreur en cas d'échec
                errorMessage.setFill(Color.RED);
                errorMessage.setText("Erreur lors de l'inscription : " + e.getMessage());
                errorMessage.setVisible(true);
                e.printStackTrace();
            }
        } else {
            // Afficher un message d'erreur si les entrées ne sont pas valides
            errorMessage.setFill(Color.RED);
            errorMessage.setVisible(true);
        }
    }

    // Validation des entrées
    private boolean validateInputs() {
        // Réinitialiser les messages d'erreur
        errorMessage.setVisible(false);
        passwordError.setVisible(false);

        // Validation de l'email
        if (email.getText() == null || email.getText().isEmpty()) {
            errorMessage.setText("Veuillez saisir un email.");
            errorMessage.setVisible(true);
            return false;
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email.getText())) {
            errorMessage.setText("L'email n'est pas dans un format valide.");
            errorMessage.setVisible(true);
            return false;
        }

        // Vérification de l'email via l'API EmailValidator
        if (!EmailValidator.isEmailValid(email.getText())) {
            errorMessage.setText("L'email n'est pas valide ou n'existe pas.");
            errorMessage.setVisible(true);
            return false;
        }


        // Validation du mot de passe
        if (passwordField.getText() == null || passwordField.getText().isEmpty()) {
            errorMessage.setText("Veuillez saisir un mot de passe.");
            errorMessage.setVisible(true);
            return false;
        }
        if (passwordField.getText().length() < 8 || !passwordField.getText().matches(".*\\d.*")) {
            errorMessage.setText("Le mot de passe doit contenir au moins 8 caractères et un chiffre.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation du nom
        if (nom.getText() == null || nom.getText().isEmpty()) {
            errorMessage.setText("Veuillez saisir un nom.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation du prénom
        if (prenom.getText() == null || prenom.getText().isEmpty()) {
            errorMessage.setText("Veuillez saisir un prénom.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation de la date de naissance
        if (dateNaissancePicker.getValue() == null) {
            errorMessage.setText("Veuillez sélectionner une date de naissance.");
            errorMessage.setVisible(true);
            return false;
        }
        LocalDate now = LocalDate.now();
        Period age = Period.between(dateNaissancePicker.getValue(), now);
        if (age.getYears() < 12) {
            errorMessage.setText("L'utilisateur doit avoir au moins 12 ans.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation du téléphone
        if (telephone.getText() == null || telephone.getText().isEmpty()) {
            errorMessage.setText("Veuillez saisir un numéro de téléphone.");
            errorMessage.setVisible(true);
            return false;
        }
        if (!telephone.getText().matches("\\d{8}")) {
            errorMessage.setText("Le numéro de téléphone doit contenir exactement 8 chiffres.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation de l'adresse
        if (adresse.getText() == null || adresse.getText().isEmpty()) {
            errorMessage.setText("Veuillez saisir une adresse.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation du sexe
        if (sexeComboBox.getValue() == null) {
            errorMessage.setText("Veuillez sélectionner un sexe.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation du rôle
        if (selectedRole == null) {
            errorMessage.setText("Veuillez sélectionner un rôle.");
            errorMessage.setVisible(true);
            return false;
        }

        // Validation de la confirmation du mot de passe
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            passwordError.setText("Les mots de passe ne correspondent pas.");
            passwordError.setVisible(true);
            return false;
        }

        // Validation spécifique au rôle
        if (selectedRole == UserRole.PLAYER && niveauComboBox.getValue() == null) {
            errorMessage.setText("Veuillez sélectionner un niveau pour le joueur.");
            errorMessage.setVisible(true);
            return false;
        }
        if (selectedRole == UserRole.NUTRITIONIST && experienceComboBox.getValue() == null) {
            errorMessage.setText("Veuillez sélectionner une expérience pour le nutritionniste.");
            errorMessage.setVisible(true);
            return false;
        }

        // Si toutes les validations passent
        return true;
    }

    // Ouvrir un sélecteur de fichier pour choisir une image
    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(selectImageButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedImageFile = selectedFile;
            imagePath = selectedFile.toURI().toString(); // Stockez le chemin sous forme d'URI
            displaySelectedImage();
        }
    }

    // Afficher l'image sélectionnée dans l'ImageView
    private void displaySelectedImage() {
        try {
            Image image = new Image(imagePath); // Chargez l'image à partir de l'URI
            userImageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gestion de la sélection de la pièce jointe (image du diplôme)
    @FXML
    private void handleSelectPieceJointe() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(selectPieceJointeButton.getScene().getWindow());
        if (selectedFile != null) {
            selectedPieceJointeFile = selectedFile;
            pieceJointeTextField.setText(selectedFile.getName());

            // Extraire le texte de l'image
            String extractedText = extractTextFromImage(selectedFile);

            // Afficher le texte extrait pour déboguer
            System.out.println("Texte extrait de l'image : " + extractedText);

            // Vérifier si le texte a été extrait avec succès
            if (extractedText == null || extractedText.isEmpty()) {
                errorMessage.setText("Impossible d'extraire le texte de l'image. Veuillez vérifier le fichier.");
                errorMessage.setVisible(true);
                return;
            }

            // Valider le texte extrait
            UserStatus status = isDiplomaValid(extractedText) ? UserStatus.VALIDATED : UserStatus.PENDING;
            if (status == UserStatus.VALIDATED) {
                errorMessage.setFill(Color.GREEN);
                errorMessage.setText("Diplôme validé ! Inscription réussie.");
            } else {
                errorMessage.setFill(Color.ORANGE);
                errorMessage.setText("Création de compte effectuée, mais diplôme non reconnu.");
            }
            errorMessage.setVisible(true);
        }
    }
    // Extraire le texte d'une image en utilisant Tesseract OCR
    private String extractTextFromImage(File imageFile) {
        Tesseract tesseract = new Tesseract();
        try {
            tesseract.setDatapath("C:\\Users\\hbibb\\Desktop\\pi\\src\\main\\resources\\tessdata"); // Chemin vers le dossier tessdata
            tesseract.setLanguage("fra"); // Langue française
            return tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new RuntimeException("Erreur lors de l'extraction du texte de l'image : " + e.getMessage(), e);
        }
    }

    // Vérifier si le texte extrait contient "diplôme nutritionniste"
    private boolean isDiplomaValid(String extractedText) {
        if (extractedText == null || extractedText.isEmpty()) {
            return false;
        }

        // Convertir le texte en minuscules pour une comparaison insensible à la casse
        String uppercaseText = extractedText.toUpperCase();

        // Vérifier la présence de mots-clés pertinents
        return uppercaseText.contains("diplome de nutritionniste") ||
                uppercaseText.contains("diplome nutritionniste") ||
                uppercaseText.contains("diplôme en nutrition") ||
                uppercaseText.contains("Le Certificat EN NUTRITION")||
                uppercaseText.contains("EQUILIBRE FORMATION");
    }
    // Gestion de l'ouverture de la caméra
    @FXML
    private void handleOpenCamera() {
        // Charger la bibliothèque OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Ouvrir la caméra
        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            errorMessage.setText("Impossible d'ouvrir la caméra.");
            errorMessage.setVisible(true);
            return;
        }

        // Capturer une image
        Mat frame = new Mat();
        capture.read(frame);
        capture.release();

        // Convertir l'image en format JavaFX
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".png", frame, buffer);
        byte[] byteArray = buffer.toArray();
        Image image = new Image(new ByteArrayInputStream(byteArray));

        // Afficher l'image dans l'ImageView
        userImageView.setImage(image);

        // Enregistrer l'image capturée dans un fichier temporaire
        try {
            File tempFile = File.createTempFile("captured_image", ".png");
            Imgcodecs.imwrite(tempFile.getAbsolutePath(), frame);
            selectedImageFile = tempFile;
            imagePath = tempFile.toURI().toString();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage.setText("Erreur lors de l'enregistrement de l'image.");
            errorMessage.setVisible(true);
        }
    }

    private boolean executeQueryAndCheckExistence(String query, String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Obtient une connexion à la base de données
            connection = myConnection.getInstance().getCnx(); // Utilisez votre singleton de connexion

            // Prépare la requête SQL
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email); // Remplace le paramètre "email" dans la requête

            // Exécute la requête
            resultSet = preparedStatement.executeQuery();

            // Vérifie si le résultat contient au moins une ligne
            if (resultSet.next()) {
                int count = resultSet.getInt(1); // Récupère le résultat de COUNT(*)
                return count > 0; // Retourne true si l'email existe
            }

            return false; // Si aucun résultat n'est trouvé
        } catch (Exception e) {
            e.printStackTrace();
            return false; // En cas d'erreur, retourne false
        } finally {
            // Ferme les ressources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Map

    @FXML
    private void handleOpenMap() {
        // Créer une nouvelle Stage (fenêtre)
        mapStage = new Stage();
        mapStage.setTitle("Sélectionner une adresse sur la carte");

        // Créer un WebView pour afficher la carte
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Charger le fichier map.html
        URL mapUrl = getClass().getResource("/map.html");
        if (mapUrl == null) {
            System.err.println("map.html non trouvé !");
            return;
        }
        webEngine.load(mapUrl.toExternalForm());

        // Configurer la scène et afficher la fenêtre
        Scene scene = new Scene(webView, 800, 600); // Taille de la fenêtre
        mapStage.setScene(scene);
        mapStage.show();

        // Définir l'objet Java dans le contexte JavaScript
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", this); // Définir l'objet Java dans le contexte JavaScript
                System.out.println("Objet Java défini dans JavaScript.");
            }
        });
    }
    public void setSelectedLocation(double latitude, double longitude) {
        System.out.println("Coordonnées reçues : " + latitude + ", " + longitude);

        // Convertir les coordonnées en adresse
        String address = getAddressFromCoordinates(latitude, longitude);
        adresse.setText(address); // Mettre à jour le champ adresse

        // Fermer la fenêtre de la carte
        if (mapStage != null) {
            mapStage.close();
        }
    }
    private String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            String url = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Extraire l'adresse de la réponse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONObject address = json.optJSONObject("address");

            if (address == null) {
                return "Adresse inconnue";
            }

            // Construire l'adresse en vérifiant la présence de chaque champ
            StringBuilder addressBuilder = new StringBuilder();

            if (address.has("road")) {
                addressBuilder.append(address.getString("road")).append(", ");
            }
            if (address.has("city")) {
                addressBuilder.append(address.getString("city")).append(", ");
            } else if (address.has("town")) {
                addressBuilder.append(address.getString("town")).append(", ");
            } else if (address.has("village")) {
                addressBuilder.append(address.getString("village")).append(", ");
            }
            if (address.has("country")) {
                addressBuilder.append(address.getString("country"));
            }

            // Si l'adresse est vide, retourner "Adresse inconnue"
            if (addressBuilder.length() == 0) {
                return "Adresse inconnue";
            }

            return addressBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Adresse inconnue";
        }
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de réinitialisation de mot de passe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) LoginButton.getScene().getWindow();

            // Changer la scène
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorMessage.setText("Erreur lors du chargement de la page Login.");
            errorMessage.setVisible(true);
        }
    }
}