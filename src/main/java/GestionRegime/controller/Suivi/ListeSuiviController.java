package GestionRegime.controller.Suivi;

import GestionRegime.entities.Suivi;
import GestionRegime.services.SuiviService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ListeSuiviController {

    @FXML
    private TableView<Suivi> suiviTable;

    @FXML
    private TableColumn<Suivi, Integer> idColumn;

    @FXML
    private TableColumn<Suivi, Integer> utilisateurIdColumn;

    @FXML
    private TableColumn<Suivi, Integer> regimeIdColumn;

    @FXML
    private TableColumn<Suivi, Double> poidsColumn;

    @FXML
    private TableColumn<Suivi, Double> tourDeTailleColumn;

    @FXML
    private TableColumn<Suivi, Double> imcColumn;

    @FXML
    private TableColumn<Suivi, LocalDate> dateSuiviColumn;

    private SuiviService suiviService = new SuiviService();



    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de la classe Suivi
        idColumn.setCellValueFactory(new PropertyValueFactory<>("suivi_id"));
        utilisateurIdColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur_id"));
        regimeIdColumn.setCellValueFactory(new PropertyValueFactory<>("regime_id"));
        poidsColumn.setCellValueFactory(new PropertyValueFactory<>("poids"));
        tourDeTailleColumn.setCellValueFactory(new PropertyValueFactory<>("tour_de_taille"));
        imcColumn.setCellValueFactory(new PropertyValueFactory<>("imc"));
        dateSuiviColumn.setCellValueFactory(new PropertyValueFactory<>("date_suivi"));

        // Formater la colonne dateSuiviColumn
        dateSuiviColumn.setCellFactory(column -> new TableCell<Suivi, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });

        // Charger les données dans la TableView
        loadSuiviData();
    }

    private void loadSuiviData() {
        List<Suivi> suivis = suiviService.getAllDataSuivi();
        ObservableList<Suivi> suiviObservableList = FXCollections.observableArrayList(suivis);
        suiviTable.setItems(suiviObservableList);
    }

    @FXML
    private void handleAjouterSuivi() {
        try {
            // Charger le fichier FXML de l'interface AjouterSuivi
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterSuivi.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (stage)
            Stage stage = new Stage();
            stage.setTitle("Ajouter un suivi");
            stage.setScene(scene);

            // Afficher la fenêtre
            stage.show();

        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement de l'interface AjouterSuivi : " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleModifierSuivi() {
        Suivi selectedSuivi = suiviTable.getSelectionModel().getSelectedItem();

        if (selectedSuivi == null) {
            System.out.println("❌ Erreur: Aucun suivi sélectionné !");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSuivi.fxml"));
            Parent root = loader.load();

            // Récupération du contrôleur et passage des données
            ModifierSuiviController modifierController = loader.getController();
            modifierController.setSuiviToUpdate(selectedSuivi);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Modifier un suivi");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement de l'interface ModifierSuivi : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSupprimerSuivi() {
        Suivi selectedSuivi = suiviTable.getSelectionModel().getSelectedItem();
        if (selectedSuivi == null) {
            System.out.println("❌ Aucun suivi sélectionné !");
            return;
        }

        // Confirmation de suppression (optionnel)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce suivi ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer le suivi de la base de données
            suiviService.deleteSuivi(selectedSuivi);

            // Recharger les données dans la TableView
            loadSuiviData();

            System.out.println("✅ Suivi supprimé avec succès !");
        }
    }
}