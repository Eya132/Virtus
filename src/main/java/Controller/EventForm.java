package Controller;

import Entities.Event;
import Services.EventService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class EventForm {
    // Composants FXML
    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField lieuField;
    @FXML private TextField iduserField;
    @FXML private ImageView imagePreview;
    @FXML private TextField imageUrlField;
    @FXML private TextField capaciteField;
    @FXML private ListView<String> lieuSuggestions;
    private WebView mapView;
    private Event event; // Entité Event
    private final EventService eventService = new EventService(); // Service pour gérer les événements
    private JavaConnector javaConnector;

    // Initialisation du formulaire
    @FXML
    public void initialize() {
        // Désactiver les dates passées dans le DatePicker
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now())); // Désactiver les dates antérieures à aujourd'hui
            }
        });

        // Écouter les changements dans le champ lieuField
        lieuField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 1) {
                // Récupérer les suggestions de lieux
                List<String> suggestions = LieuController.getLieux(newValue.substring(0, 1));
                // Mettre à jour la ListView
                ObservableList<String> observableSuggestions = FXCollections.observableArrayList(suggestions);
                lieuSuggestions.setItems(observableSuggestions);
            } else {
                // Effacer les suggestions si le champ est vide
                lieuSuggestions.setItems(FXCollections.emptyObservableList());
            }
        });

        // Écouter la sélection d'un élément dans la ListView
        lieuSuggestions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Remplir le champ lieuField avec l'élément sélectionné
                lieuField.setText(newValue);
            }
        });

        // Appliquer un style CSS à la ListView (fond rouge)
        lieuSuggestions.setStyle("-fx-control-inner-background: green; -fx-text-fill: white;");

        // Valider que l'ID utilisateur et la capacité ne contiennent que des nombres
        iduserField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change; // Accepter uniquement des chiffres
            }
            return null; // Rejeter la modification si ce n'est pas un nombre
        }));

// Initialiser le connecteur Java-JavaScript
        javaConnector = new JavaConnector();

        // Charger la carte dans le WebView
        WebEngine webEngine = mapView.getEngine();
        webEngine.load(getClass().getResource("/map.html").toExternalForm());

        // Écouter les changements dans la propriété addressProperty
        javaConnector.addressProperty().addListener((observable, oldValue, newValue) -> {
            lieuField.setText(newValue); // Mettre à jour le champ lieuField
        });

        // Exposer le connecteur Java à JavaScript
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaConnector", javaConnector);
            }
        });
        capaciteField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change; // Accepter uniquement des chiffres
            }
            return null; // Rejeter la modification si ce n'est pas un nombre
        }));
    }

    // Remplir le formulaire avec les données d'un événement existant
    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            titreField.setText(event.getTitre());
            descriptionField.setText(event.getDescription());
            datePicker.setValue(event.getDate().toLocalDate());
            lieuField.setText(event.getLieu());
            iduserField.setText(String.valueOf(event.getIduser()));
            imageUrlField.setText(event.getImageUrl());
            capaciteField.setText(String.valueOf(event.getCapacite()));
            if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
                imagePreview.setImage(new Image(event.getImageUrl())); // Afficher l'image
            }
        }
    }

    // Gérer l'upload d'une image
    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            imageUrlField.setText(imagePath); // Mettre à jour le champ de l'URL de l'image
            imagePreview.setImage(new Image(imagePath)); // Afficher l'image dans l'ImageView
        }
    }

    // Générer une description avec l'API Hugging Face
    @FXML
    private void handleGenerateDescription() {
        String eventTitle = titreField.getText();
        String eventDate = datePicker.getValue().toString();
        String eventLocation = lieuField.getText();
        int eventCapacity = Integer.parseInt(capaciteField.getText());

        String generatedDescription = generateSportEventDescription(eventTitle, eventDate, eventLocation, eventCapacity);

        if (generatedDescription != null && !generatedDescription.isEmpty()) {
            descriptionField.setText(generatedDescription); // Mettre à jour le champ de description
        } else {
            showAlert("Erreur", "La génération de la description a échoué.");
        }
    }

    // Générer une description pour un événement sportif
    private String generateSportEventDescription(String eventTitle, String date, String location, int capacity) {
        String prompt = String.format(
                "Un événement de sport de raquette intitulé : %s. " +
                        "L'événement aura lieu le %s à %s. " +
                        "Il est ouvert à tous les niveaux, des débutants aux joueurs expérimentés. " +
                        "Des raquettes et des balles seront fournies sur place. " +
                        "L'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette. " +
                        "La capacité est limitée à %d participants. " +
                        "Inscrivez-vous dès maintenant pour garantir votre place !",
                eventTitle, date, location, capacity
        );

        return generateDescriptionWithHuggingFace(prompt);
    }

    // Interagir avec l'API Hugging Face pour générer une description
    private String generateDescriptionWithHuggingFace(String prompt) {
        String apiKey = loadApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            showAlert("Erreur", "La clé API Hugging Face n'est pas configurée.");
            return null;
        }

        String apiUrl = "https://api-inference.huggingface.co/models/gpt2";
        int maxRetries = 3; // Nombre maximum de tentatives
        int retryDelay = 2000; // Délai de 2 secondes entre les tentatives

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Créer le corps de la requête JSON
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("inputs", prompt);
                requestBody.addProperty("max_length", 150); // Limite de tokens

                // Créer la requête HTTP
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                        .build();

                // Envoyer la requête et récupérer la réponse
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Vérifier le code de statut de la réponse
                if (response.statusCode() == 200) {
                    // Analyser la réponse JSON
                    JsonElement jsonElement = JsonParser.parseString(response.body());

                    // Vérifier si la réponse est un tableau
                    if (jsonElement.isJsonArray()) {
                        JsonArray jsonArray = jsonElement.getAsJsonArray();
                        if (jsonArray.size() > 0) {
                            JsonObject jsonResponse = jsonArray.get(0).getAsJsonObject();
                            if (jsonResponse.has("generated_text")) {
                                // Nettoyer et retourner le texte généré
                                String generatedText = jsonResponse.get("generated_text").getAsString();
                                return cleanGeneratedText(generatedText); // Nettoyer le texte
                            }
                        }
                    } else if (jsonElement.isJsonObject()) {
                        // Gérer les réponses d'erreur
                        JsonObject jsonResponse = jsonElement.getAsJsonObject();
                        if (jsonResponse.has("error")) {
                            String errorMessage = jsonResponse.get("error").getAsString();
                            showAlert("Erreur de l'API Hugging Face", errorMessage);
                            return null;
                        }
                    }
                } else if (response.statusCode() == 503 && attempt < maxRetries) {
                    // Attendre avant de réessayer en cas d'erreur 503
                    Thread.sleep(retryDelay);
                    continue;
                } else {
                    // Afficher une alerte en cas d'erreur HTTP
                    showAlert("Erreur de l'API", "Code de statut : " + response.statusCode() + "\nRéponse : " + response.body());
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de générer la description avec l'IA.");
                return null;
            }
        }
        return null; // Retourner null si toutes les tentatives échouent
    }


    // Nettoyer le texte généré pour supprimer les incohérences
    private String cleanGeneratedText(String generatedText) {
        // Supprimer les phrases incohérentes ou hors sujet
        String[] sentences = generatedText.split("\\. "); // Séparer les phrases
        StringBuilder cleanedText = new StringBuilder();

        for (String sentence : sentences) {
            // Conserver uniquement les phrases pertinentes
            if (sentence.toLowerCase().contains("tennis") || sentence.toLowerCase().contains("sport") || sentence.toLowerCase().contains("événement") ||sentence.toLowerCase().contains("raquettes")) {
                cleanedText.append(sentence).append(". ");
            }
        }

        // Retourner le texte nettoyé
        return cleanedText.toString().trim();
    }

    // Charger la clé API depuis les variables d'environnement ou un fichier de configuration
    private String loadApiKey() {
        String apiKey = System.getenv("HUGGING_FACE_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            return apiKey;
        }

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                return properties.getProperty("huggingface.api.key");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Retourner null si la clé n'est pas trouvée
    }

    // Gérer l'enregistrement de l'événement
    @FXML
    private void handleSave() {
        if (validateFields()) {
            if (event == null) {
                event = new Event();
            }

            event.setTitre(titreField.getText());
            event.setDescription(descriptionField.getText());
            event.setLieu(lieuField.getText());
            event.setDate(Date.valueOf(datePicker.getValue()));
            event.setImageUrl(imageUrlField.getText());
            event.setIduser(Integer.parseInt(iduserField.getText()));
            event.setCapacite(Integer.parseInt(capaciteField.getText()));

            if (event.getIdevent() == 0) {
                eventService.addEvent(event); // Ajouter un nouvel événement
            } else {
                eventService.updateEvent(event); // Mettre à jour un événement existant
            }

            closeWindow();
        }
    }

    // Gérer l'annulation
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    // Valider les champs du formulaire
    private boolean validateFields() {
        if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                lieuField.getText().isEmpty() || datePicker.getValue() == null ||
                iduserField.getText().isEmpty() || capaciteField.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return false;
        }

        try {
            Integer.parseInt(iduserField.getText());
            Integer.parseInt(capaciteField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur et la capacité doivent être des nombres.");
            return false;
        }

        return true;
    }

    // Afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Fermer la fenêtre
    private void closeWindow() {
        ((Stage) titreField.getScene().getWindow()).close();
    }
}