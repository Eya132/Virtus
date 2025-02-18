package GestionRegime.controller.Regime;

import GestionRegime.entities.Regime;
import GestionRegime.services.RegimeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ListeRegimeController {

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
    private TableColumn<Regime, String> dateDebutColumn;

    @FXML
    private TableColumn<Regime, String> dateFinColumn;

    @FXML
    private TableColumn<Regime, String> statusColumn;

    private RegimeService regimeService = new RegimeService();

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés de la classe Regime
        idColumn.setCellValueFactory(new PropertyValueFactory<>("regime_id"));
        objectifColumn.setCellValueFactory(new PropertyValueFactory<>("objectif"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("caloriesJournalieres"));
        proteinesColumn.setCellValueFactory(new PropertyValueFactory<>("proteines"));
        glucidesColumn.setCellValueFactory(new PropertyValueFactory<>("glucides"));
        lipidesColumn.setCellValueFactory(new PropertyValueFactory<>("lipides"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFinColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Charger les données dans la TableView
        loadRegimeData();
    }

    private void loadRegimeData() {
        RegimeService rs = new RegimeService();
        List<Regime> regimes = rs.getAllDataRegime(); // Appel de la méthode d'instance
        if (regimes.isEmpty()) {
            System.out.println("Aucun régime trouvé dans la base de données.");
        } else {
            System.out.println("Nombre de régimes chargés : " + regimes.size());
        }
        ObservableList<Regime> regimeObservableList = FXCollections.observableArrayList(regimes);
        regimeTable.setItems(regimeObservableList);
    }


    @FXML
    private void handleModifierRegimeButton(ActionEvent event) {
        Regime selectedRegime = regimeTable.getSelectionModel().getSelectedItem();
        if (selectedRegime == null) {
            System.out.println("❌ Aucun régime sélectionné !");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ModifierRegime.fxml"));
            Parent root = fxmlLoader.load();

            // Passer les données du régime sélectionné au contrôleur de modification
            ModifierRegimeController controller = fxmlLoader.getController();
            controller.loadRegimeData(selectedRegime);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la vue : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimerRegimeButton(ActionEvent event) {
        Regime selectedRegime = regimeTable.getSelectionModel().getSelectedItem();
        if (selectedRegime == null) {
            System.out.println("❌ Aucun régime sélectionné !");
            return;
        }

        // Confirmation de suppression (optionnel)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce régime ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer le régime de la base de données
            regimeService.deleteRegime(selectedRegime);

            // Recharger les données dans la TableView
            loadRegimeData();

            System.out.println("✅ Régime supprimé avec succès !");
        }
    }
    }
