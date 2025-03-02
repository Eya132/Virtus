package Controller;

import Entities.Event;
import Services.EventService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {
    // Composants FXML
    @FXML private TableView<Event> eventTable;
    @FXML private TableColumn<Event, String> nameColumn;
    @FXML private TableColumn<Event, String> descriptionColumn;
    @FXML private TableColumn<Event, String> dateColumn;
    @FXML private TableColumn<Event, String> lieuColumn;
    @FXML private TableColumn<Event, Integer> capaciteColumn;
    @FXML private TextField searchField;
    @FXML private DatePicker filterDatePicker;

    private final EventService eventService = new EventService();
    private ObservableList<Event> events;

    @FXML
    public void initialize() {
        // Configuration des colonnes de la TableView
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        capaciteColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));

        // Charger les événements dans la TableView
        loadEvents();

        // Ajouter un écouteur pour la recherche automatique
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        // Ajouter un écouteur pour le filtrage automatique par date
        filterDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> handleFilterByDate());
    }

    // Charger les événements depuis la base de données
    private void loadEvents() {
        events = FXCollections.observableArrayList(eventService.getAllEvents());
        System.out.println("Événements chargés : " + events.size()); // Log pour vérifier
        eventTable.setItems(events);
    }

    // Gérer la recherche d'événements
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        List<Event> filteredEvents = events.stream()
                .filter(event -> event.getTitre().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        eventTable.setItems(FXCollections.observableArrayList(filteredEvents));
    }

    // Gérer le filtrage des événements par date
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

    // Gérer l'exportation des événements en PDF
    @FXML
    private void handleDownloadPDF() {
        // Recharger les événements depuis la base de données
        loadEvents();

        if (events.isEmpty()) {
            showAlert("Aucun événement", "Aucun événement à exporter.");
            return;
        }

        // Générer un nom de fichier unique
        String userHome = System.getProperty("user.home");
        String fileName = generateUniqueFileName();
        String downloadsPath = userHome + "/Downloads/" + fileName;

        File file = new File(downloadsPath);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Créer un document PDF en mode paysage
            Document document = new Document(PageSize.A4.rotate()); // Mode paysage
            PdfWriter.getInstance(document, fos);
            document.open();

            // Ajouter un titre stylisé
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.ORANGE);
            Paragraph title = new Paragraph("Liste des événements", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Créer un tableau avec toutes les colonnes
            PdfPTable table = new PdfPTable(5); // 5 colonnes
            table.setWidthPercentage(100); // Largeur à 100%
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // Définir les largeurs des colonnes (en pourcentage)
            float[] columnWidths = {20f, 40f, 15f, 15f, 10f}; // Titre, Description, Date, Lieu, Capacité
            table.setWidths(columnWidths);

            // Style des en-têtes de colonnes
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            BaseColor headerBackground = new BaseColor(64, 224, 208); // Turquoise

            // Ajouter les en-têtes de colonnes
            String[] headers = {"Titre", "Description", "Date", "Lieu", "Capacité"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerBackground);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Style des cellules de données
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
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

                // Ajouter les cellules avec retour à la ligne pour les descriptions longues
                PdfPCell titreCell = new PdfPCell(new Phrase(event.getTitre(), dataFont));
                titreCell.setPadding(5);
                table.addCell(titreCell);

                PdfPCell descriptionCell = new PdfPCell(new Phrase(event.getDescription(), dataFont));
                descriptionCell.setPadding(5);
                descriptionCell.setNoWrap(false); // Activer le retour à la ligne
                table.addCell(descriptionCell);

                PdfPCell dateCell = new PdfPCell(new Phrase(String.valueOf(event.getDate()), dataFont));
                dateCell.setPadding(5);
                table.addCell(dateCell);

                PdfPCell lieuCell = new PdfPCell(new Phrase(event.getLieu(), dataFont));
                lieuCell.setPadding(5);
                table.addCell(lieuCell);

                PdfPCell capaciteCell = new PdfPCell(new Phrase(String.valueOf(event.getCapacite()), dataFont));
                capaciteCell.setPadding(5);
                table.addCell(capaciteCell);
            }

            // Ajouter le tableau au document
            document.add(table);

            // Fermer le document
            document.close();

            // Afficher un message de confirmation
            showAlert("Succès", "Le fichier PDF a été téléchargé avec succès : " + file.getAbsolutePath());

            // Ouvrir le fichier PDF après sa création
            openPDFFile(file);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la génération du PDF.");
        }
    }

    // Générer un nom de fichier unique
    private String generateUniqueFileName() {
        String timestamp = LocalDate.now().toString() + "_" + System.currentTimeMillis();
        return "evenements_" + timestamp + ".pdf";
    }

    // Ouvrir le fichier PDF après sa création
    private void openPDFFile(File file) {
        if (file.exists()) {
            try {
                // Ouvrir le fichier PDF avec l'application par défaut
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(file);
                } else {
                    showAlert("Erreur", "Impossible d'ouvrir le fichier PDF. Aucune application par défaut trouvée.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir le fichier PDF.");
            }
        } else {
            showAlert("Erreur", "Le fichier PDF n'a pas été trouvé.");
        }
    }

    // Gérer l'ajout d'un événement
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
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout.");
        }
    }

    // Gérer la modification d'un événement
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
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible d'ouvrir le formulaire de modification.");
            }
        } else {
            showAlert("Aucun événement sélectionné", "Veuillez sélectionner un événement à modifier.");
        }
    }

    // Gérer la suppression d'un événement
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

    // Afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}