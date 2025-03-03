package GestionRegime.controller.Regime;
import GestionRegime.services.PDFGeneratorRegime;
import GestionRegime.entities.Regime;
import GestionRegime.services.RegimeService;
import GestionRegime.tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ListeRegimeController implements Initializable {

    @FXML
    private TableView<Regime> regimeTable;

    @FXML
    private TableColumn<Regime, Integer> idColumn;

    @FXML
    private TableColumn<Regime, String> objectifColumn;

    @FXML
    private TableColumn<Regime, Integer> caloriesColumn;

    @FXML
    private TableColumn<Regime, Integer> proteinesColumn;

    @FXML
    private TableColumn<Regime, Integer> glucidesColumn;

    @FXML
    private TableColumn<Regime, Integer> lipidesColumn;

    @FXML
    private TableColumn<Regime, LocalDate> dateDebutColumn;

    @FXML
    private TableColumn<Regime, LocalDate> dateFinColumn;

    @FXML
    private TableColumn<Regime, String> statusColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lier les colonnes aux propriétés de la classe Regime
        idColumn.setCellValueFactory(new PropertyValueFactory<>("regime_id"));
        objectifColumn.setCellValueFactory(new PropertyValueFactory<>("objectif"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("caloriesJournalieres"));
        proteinesColumn.setCellValueFactory(new PropertyValueFactory<>("proteines"));
        glucidesColumn.setCellValueFactory(new PropertyValueFactory<>("glucides"));
        lipidesColumn.setCellValueFactory(new PropertyValueFactory<>("lipides"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Configurer les colonnes de date pour afficher le format "dd/MM/yyyy"
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateDebutColumn.setCellFactory(column -> new TableCell<Regime, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        dateFinColumn.setCellFactory(column -> new TableCell<Regime, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        // Charger les données dans la TableView
        loadRegimeData();
    }
    @FXML
    private void handleModifierRegimeButton(ActionEvent event) {
        // Récupérer le régime sélectionné dans la TableView
        Regime selectedRegime = regimeTable.getSelectionModel().getSelectedItem();

        // Vérifier si un régime est sélectionné
        if (selectedRegime == null) {
            afficherAlerte("Aucun régime sélectionné", "Veuillez sélectionner un régime à modifier.");
            return;
        }

        try {
            // Charger le fichier FXML de la page de modification
            System.out.println("Chargement du fichier FXML : /ModifierRegime.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierRegime.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la page de modification
            ModifierRegimeController controller = loader.getController();
            System.out.println("Contrôleur initialisé : " + controller);

            // Passer les données du régime sélectionné au contrôleur de modification
            controller.loadRegimeData(selectedRegime);

            // Afficher la page de modification
            Stage stage = (Stage) regimeTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de la vue de modification : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimerRegimeButton(ActionEvent event) {
        Regime selectedRegime = regimeTable.getSelectionModel().getSelectedItem();
        if (selectedRegime != null) {
            RegimeService regimeService = new RegimeService();
            regimeService.deleteRegime(selectedRegime);
            loadRegimeData(); // Recharger les données après suppression
        } else {
            afficherAlerte("Aucun régime sélectionné", "Veuillez sélectionner un régime à supprimer.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadRegimeData() {
        Task<List<Regime>> task = new Task<>() {
            @Override
            protected List<Regime> call() throws Exception {
                RegimeService regimeService = new RegimeService();
                return regimeService.getAllDataRegime();
            }
        };

        task.setOnSucceeded(event -> {
            List<Regime> regimes = task.getValue();
            regimeTable.getItems().setAll(regimes);
            System.out.println("✅ Données chargées avec succès : " + regimes.size() + " régimes trouvés.");
        });

        task.setOnFailed(event -> {
            System.err.println("❌ Erreur lors du chargement des régimes : " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
    @FXML
    private void handleExportPDF() {
        // Créer une instance de RegimeService
        RegimeService regimeService = new RegimeService();

        // Récupérer la liste des régimes
        List<Regime> regimes = regimeService.getAllDataRegime();

        if (regimes.isEmpty()) {
            System.out.println("❌ Erreur: Aucun régime disponible pour l'exportation.");
            return;
        }

        // Ouvrir un FileChooser pour choisir l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.setInitialFileName("Liste_Regimes.pdf");

        Stage stage = (Stage) regimeTable.getScene().getWindow(); // Récupérer la fenêtre actuelle
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String filePath = file.getAbsolutePath();

            try {
                // Générer le PDF
                PDFGeneratorRegime.generateRegimeListPDF(regimes, filePath);
                System.out.println("✅ PDF généré avec succès : " + filePath);

                // Afficher un message de confirmation
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exportation réussie");
                alert.setHeaderText(null);
                alert.setContentText("Le PDF a été généré avec succès : " + filePath);
                alert.showAndWait();
            } catch (IOException e) {
                System.out.println("❌ Erreur lors de la génération du PDF : " + e.getMessage());
                e.printStackTrace();

                // Afficher un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'exportation");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur s'est produite lors de la génération du PDF.");
                alert.showAndWait();
            }
        }
    }
    @FXML
    private void handleVoirStatistiquesGlobales(ActionEvent event) {
        // Récupérer tous les régimes de la TableView
        List<Regime> regimes = regimeTable.getItems();

        // Vérifier si la liste des régimes est vide
        if (regimes.isEmpty()) {
            afficherAlerte("Aucun régime disponible", "Il n'y a aucun régime à afficher.");
            return;
        }

        // Afficher les statistiques globales
        afficherStatistiques(regimes);
    }

    private void afficherStatistiques(List<Regime> regimes) {
        try {
            // Calculer les statistiques globales
            Map<String, Integer> statistiques = new HashMap<>();

            for (Regime regime : regimes) {
                String objectif = regime.getObjectif().toString(); // Convertir l'enum en String
                statistiques.put(objectif, statistiques.getOrDefault(objectif, 0) + 1);
            }

            // Charger l'interface des statistiques
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiquesRegime.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur des statistiques
            StatistiquesRegimeController controller = loader.getController();

            // Passer les données au contrôleur des statistiques
            controller.setDonneesStatistiques(statistiques);

            // Afficher la nouvelle interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques Globales");
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de l'interface des statistiques : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Map<String, Integer> getStatistiquesParObjectif() {
        Map<String, Integer> statistiques = new HashMap<>();
        try {
            String query = "SELECT objectif, COUNT(*) as total " +
                    "FROM regime " +
                    "GROUP BY objectif " +
                    "ORDER BY total DESC";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String objectif = rs.getString("objectif");
                int total = rs.getInt("total");
                statistiques.put(objectif, total);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des statistiques par objectif : " + e.getMessage());
        }
        return statistiques;
    }
}
