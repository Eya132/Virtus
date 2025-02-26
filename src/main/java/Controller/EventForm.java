package Controller;

import Entities.Event;
import Services.EventService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;

public class EventForm {
    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField lieuField;
    @FXML private TextField iduserField;
    @FXML private ImageView imagePreview;
    @FXML private TextField imageUrlField;
    @FXML private TextField capaciteField; // Nouveau champ

    private Event event;
    private final EventService eventService = new EventService();

    @FXML
    public void initialize() {
        // Restreindre la sélection de dates passées dans le DatePicker
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now())); // Désactiver les dates passées
            }
        });

        // Valider que le champ iduserField ne contient que des nombres
        iduserField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            if (change.getControlNewText().matches("\\d*")) { // Accepter uniquement des chiffres
                return change;
            }
            return null; // Rejeter la modification si ce n'est pas un nombre
        }));

        // Valider que le champ capaciteField ne contient que des nombres
        capaciteField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, change -> {
            if (change.getControlNewText().matches("\\d*")) { // Accepter uniquement des chiffres
                return change;
            }
            return null; // Rejeter la modification si ce n'est pas un nombre
        }));
    }

    public void setEvent(Event event) {
        this.event = event;
        if (event != null) {
            titreField.setText(event.getTitre());
            descriptionField.setText(event.getDescription());
            datePicker.setValue(event.getDate().toLocalDate());
            lieuField.setText(event.getLieu());
            iduserField.setText(String.valueOf(event.getIduser()));
            imageUrlField.setText(event.getImageUrl());
            capaciteField.setText(String.valueOf(event.getCapacite())); // Nouveau champ
            if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
                imagePreview.setImage(new Image(event.getImageUrl()));
            }
        }
    }

    @FXML
    private void handleUploadImage() {
        // Ouvrir une boîte de dialogue pour sélectionner une image
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
            event.setCapacite(Integer.parseInt(capaciteField.getText())); // Nouveau champ

            if (event.getIdevent() == 0) {
                eventService.addEvent(event); // Ajouter un nouvel événement
            } else {
                eventService.updateEvent(event); // Mettre à jour un événement existant
            }

            closeWindow();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private boolean validateFields() {
        // Vérifier que tous les champs obligatoires sont remplis
        if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                lieuField.getText().isEmpty() || datePicker.getValue() == null ||
                iduserField.getText().isEmpty() || capaciteField.getText().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return false;
        }

        // Vérifier que l'ID utilisateur est un nombre valide
        try {
            Integer.parseInt(iduserField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID utilisateur doit être un nombre.");
            return false;
        }

        // Vérifier que la capacité est un nombre valide
        try {
            Integer.parseInt(capaciteField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre.");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        ((Stage) titreField.getScene().getWindow()).close();
    }
}