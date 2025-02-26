package Controller;

import Entities.Event;
import Services.EventService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> nameColumn;
    @FXML private TableColumn<Event, String> descriptionColumn;
    @FXML private TableColumn<Event, String> dateColumn;
    @FXML private TableColumn<Event, String> lieuColumn;
    @FXML private TableColumn<Event, Integer> capaciteColumn; // Nouvelle colonne
    @FXML private TextField searchField;
    @FXML private DatePicker filterDatePicker;

    private final EventService eventService = new EventService();
    private ObservableList<Event> events;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite")); // Nouvelle colonne

        // Charger les données
        loadEvents();
    }

    private void loadEvents() {
        events = FXCollections.observableArrayList(eventService.getAllEvents());
        eventTable.setItems(events);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        List<Event> filteredEvents = events.stream()
                .filter(event -> event.getTitre().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        eventTable.setItems(FXCollections.observableArrayList(filteredEvents));
    }

    @FXML
    private void handleFilterByDate() {
        LocalDate selectedDate = filterDatePicker.getValue();
        if (selectedDate != null) {
            List<Event> filteredEvents = events.stream()
                    .filter(event -> event.getDate().toLocalDate().equals(selectedDate))
                    .collect(Collectors.toList());
            eventTable.setItems(FXCollections.observableArrayList(filteredEvents));
        } else {
            eventTable.setItems(events);
        }
    }

    @FXML
    private void handleDownloadPDF() {
        // Ouvrir une boîte de dialogue pour choisir l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(eventTable.getScene().getWindow());

        if (file != null) {
            try {
                // Créer un document PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));

                // Ouvrir le document
                document.open();

                // Ajouter un titre stylisé
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.ORANGE);
                Paragraph title = new Paragraph("Liste des événements", titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Ajouter un tableau stylisé
                PdfPTable table = new PdfPTable(5); // 5 colonnes (ajout de la capacité)
                table.setWidthPercentage(100); // Largeur à 100%
                table.setSpacingBefore(10);
                table.setSpacingAfter(10);

                // Style des en-têtes de colonnes
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.WHITE);
                BaseColor headerBackground = new BaseColor(64, 224, 208); // Turquoise

                // Ajouter les en-têtes de colonnes
                String[] headers = {"Titre", "Description", "Date", "Lieu", "Capacité"}; // Nouvelle colonne
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setBackgroundColor(headerBackground);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(10);
                    table.addCell(cell);
                }

                // Style des cellules de données
                Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 13, BaseColor.DARK_GRAY);
                BaseColor rowBackground = new BaseColor(248, 248, 248); // Gris clair pour les lignes alternées

                // Ajouter les données du tableau
                for (int i = 0; i < events.size(); i++) {
                    Event event = events.get(i);

                    // Alterner les couleurs de fond des lignes
                    if (i % 2 == 0) {
                        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    } else {
                        table.getDefaultCell().setBackgroundColor(rowBackground);
                    }

                    // Ajouter les cellules
                    table.addCell(new Phrase(event.getTitre(), dataFont));
                    table.addCell(new Phrase(event.getDescription(), dataFont));
                    table.addCell(new Phrase(String.valueOf(event.getDate()), dataFont));
                    table.addCell(new Phrase(event.getLieu(), dataFont));
                    table.addCell(new Phrase(String.valueOf(event.getCapacite()), dataFont)); // Nouvelle colonne
                }

                // Ajouter le tableau au document
                document.add(table);

                // Fermer le document
                document.close();

                // Afficher un message de confirmation
                showAlert("Succès", "Le fichier PDF a été téléchargé avec succès : " + file.getAbsolutePath());
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la génération du PDF.");
            }
        }
    }

    @FXML
    private void handleAddEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventForm.fxml"));
            Parent root = loader.load();
            EventForm controller = loader.getController();
            controller.setEvent(null); // Mode ajout

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un événement");
            stage.showAndWait();
            loadEvents(); // Recharger les événements après fermeture du formulaire
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }

    @FXML
    private void handleEditEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventForm.fxml"));
                Parent root = loader.load();
                EventForm controller = loader.getController();
                controller.setEvent(selectedEvent); // Mode modification

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier un événement");
                stage.showAndWait();
                loadEvents(); // Recharger les événements après fermeture du formulaire
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            eventService.deleteEvent(selectedEvent.getIdevent());
            loadEvents(); // Recharger les événements après suppression
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}