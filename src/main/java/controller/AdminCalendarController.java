package controller;

import Entites.Match1;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.MatchService;
import services.ListInscriService;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AdminCalendarController implements Initializable {

    @FXML
    private ListView<Match1> matchList; // Liste des matchs

    @FXML
    private TextField searchField; // Champ de recherche

    @FXML
    private Button resetFilterButton; // Bouton pour réinitialiser le filtre

    @FXML
    private Button statisticsButton; // Bouton pour accéder aux statistiques

    @FXML
    private Button exportExcelButton; // Bouton pour exporter en Excel

    private MatchService matchService = new MatchService();
    private ListInscriService listInscriService = new ListInscriService();
    private List<Match1> allMatches; // Liste de tous les matchs

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ajouter un écouteur pour le bouton des statistiques
        statisticsButton.setOnAction(event -> openStatisticsView());

        // Charger tous les matchs depuis la base de données
        allMatches = matchService.getAllMatches();

        // Afficher tous les matchs dans la liste
        updateMatchList(allMatches);

        // Personnaliser l'affichage des éléments dans la ListView
        matchList.setCellFactory(param -> new ListCell<Match1>() {
            @Override
            protected void updateItem(Match1 match, boolean empty) {
                super.updateItem(match, empty);
                if (empty || match == null) {
                    setText(null);
                    setGraphic(null); // Réinitialiser le contenu
                    setStyle(""); // Réinitialiser le style
                } else {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                    String date = match.getDate().format(dateFormatter);
                    String heure = match.getHeure().format(timeFormatter);

                    // Créer un design moderne pour chaque élément de la liste
                    Label matchInfo = new Label(String.format("%s - %s à %s", date, heure, match.getLocalisation()));
                    matchInfo.setStyle("-fx-font-size: 14px;");

                    // Ajouter un bouton "Voir les participants"
                    Button viewParticipantsButton = new Button("Voir les participants");
                    viewParticipantsButton.setStyle(
                            "-fx-background-color: #007bff; " +
                                    "-fx-text-fill: white; " +
                                    "-fx-font-size: 12px; " +
                                    "-fx-font-weight: bold; " +
                                    "-fx-padding: 8px 16px; " +
                                    "-fx-border-radius: 5px; " +
                                    "-fx-background-radius: 5px; " +
                                    "-fx-cursor: hand;"
                    );

                    viewParticipantsButton.setOnAction(event -> openParticipantsView(match.getId())); // Passer l'ID du match

                    // Créer un conteneur pour le texte et le bouton
                    HBox hbox = new HBox();
                    hbox.setSpacing(10); // Espacement entre les éléments

                    // Ajouter un espace flexible pour pousser le bouton vers la droite
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS); // L'espace flexible occupe tout l'espace disponible

                    // Ajouter les éléments au HBox
                    hbox.getChildren().addAll(matchInfo, spacer, viewParticipantsButton);

                    setGraphic(hbox); // Définir le conteneur comme contenu de la cellule
                    setText(null); // Effacer le texte pour éviter la duplication
                }
            }
        });

        // Ajouter un écouteur pour la recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMatches(newValue);
        });

        // Ajouter un écouteur pour le bouton de réinitialisation du filtre
        resetFilterButton.setOnAction(event -> {
            searchField.clear(); // Effacer le champ de recherche
            updateMatchList(allMatches); // Afficher tous les matchs
        });

        // Ajouter un écouteur pour le bouton des statistiques
        statisticsButton.setOnAction(event -> openStatisticsView());

        // Ajouter un écouteur pour le bouton d'exportation en Excel
        exportExcelButton.setOnAction(event -> exportToExcel());
    }

    /**
     * Met à jour la liste des matchs.
     *
     * @param matches La liste des matchs à afficher.
     */
    private void updateMatchList(List<Match1> matches) {
        matchList.getItems().clear(); // Efface la liste actuelle
        matchList.getItems().addAll(matches); // Ajoute les matchs à la liste
    }

    /**
     * Filtre les matchs en fonction de la recherche.
     *
     * @param searchText Le texte de recherche.
     */
    private void filterMatches(String searchText) {
        List<Match1> filteredMatches = allMatches.stream()
                .filter(match -> match.getLocalisation().toLowerCase().contains(searchText.toLowerCase()))
                .toList();
        updateMatchList(filteredMatches); // Met à jour la liste avec les matchs filtrés
    }

    /**
     * Ouvre l'interface des statistiques.
     */
    private void openStatisticsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminStatisticsView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Statistiques des Matchs");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface des statistiques.");
        }
    }

    /**
     * Ouvre l'interface des participants pour un match donné.
     *
     * @param matchId L'ID du match sélectionné.
     */
    private void openParticipantsView(int matchId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ParticipantsView.fxml"));
            Parent root = loader.load();

            // Passer l'ID du match au contrôleur de la vue des participants
            ParticipantsController participantsController = loader.getController();
            participantsController.setMatchId(matchId);

            Stage stage = new Stage();
            stage.setTitle("Participants du Match");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface des participants.");
        }
    }

    /**
     * Exporte les données en Excel.
     */
    private void exportToExcel() {
        // Créer un FileChooser pour permettre à l'utilisateur de choisir l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                // Créer une feuille dans le classeur
                Sheet sheet = workbook.createSheet("Matchs");

                // Créer une ligne d'en-tête
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Date");
                headerRow.createCell(1).setCellValue("Heure");
                headerRow.createCell(2).setCellValue("Localisation");
                headerRow.createCell(3).setCellValue("Terrain");
                headerRow.createCell(4).setCellValue("Type de Sport");

                // Remplir les données des matchs
                int rowNum = 1;
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                for (Match1 match : allMatches) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(match.getDate().format(dateFormatter)); // Date
                    row.createCell(1).setCellValue(match.getHeure().format(timeFormatter)); // Heure
                    row.createCell(2).setCellValue(match.getLocalisation()); // Localisation
                    row.createCell(3).setCellValue(match.getTerrain()); // Terrain
                    row.createCell(4).setCellValue(match.getTypeSport()); // Type de sport
                }

                // Ajuster la largeur des colonnes automatiquement
                for (int i = 0; i < 5; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Enregistrer le fichier Excel
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    System.out.println("Fichier Excel exporté avec succès : " + file.getAbsolutePath());
                    showAlert("Succès", "Le fichier Excel a été exporté avec succès !");

                    // Ouvrir le fichier Excel avec l'application associée (comme Excel)
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (file.exists()) {
                            desktop.open(file); // Ouvre le fichier dans l'application par défaut (Excel ou autre)
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erreur lors de l'exportation en Excel : " + e.getMessage());
                showAlert("Erreur", "Erreur lors de l'exportation en Excel : " + e.getMessage());
            }
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     *
     * @param title   Le titre de l'alerte.
     * @param message Le message de l'alerte.
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}