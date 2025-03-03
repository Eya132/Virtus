package GestionRegime.controller;

import GestionRegime.entities.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private BorderPane mainBorderPane; // Référence au BorderPane principal

    @FXML
    private TableView<Player> playerTable;

    @FXML
    private TableColumn<Player, Integer> idColumn;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, Integer> ageColumn;

    @FXML
    private ListView<String> chatArea;

    @FXML
    private TextField inputField;

    @FXML
    public void initialize() {
        // Initialiser les colonnes du tableau
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        // Charger les données des joueurs
        playerTable.getItems().addAll(
                new Player(1, "Joueur 1", 25),
                new Player(2, "Joueur 2", 30),
                new Player(3, "Joueur 3", 22)
        );
    }

    private void loadView(String fxmlFile) {
        try {
            URL fxmlLocation = getClass().getResource(fxmlFile);
            if (fxmlLocation == null) {
                System.err.println("❌ Le fichier FXML n'a pas été trouvé : " + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent view = loader.load();

            if (mainBorderPane == null) {
                System.err.println("❌ mainBorderPane est null");
            } else {
                mainBorderPane.setCenter(view);
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de la vue : " + fxmlFile);
            e.printStackTrace();
        }
    }

    // Méthodes pour naviguer entre les vues
    @FXML
    private void switchToPlayerList() {
        loadView("/playerForm.fxml"); // Charger la vue de la liste des joueurs
    }

    @FXML
    private void switchToRegimeList() {
        loadView("/ListRegime.fxml"); // Charger la vue de la liste des régimes
    }

    @FXML
    private void switchToStatistics() {
        loadView("/StatistiquesRegime.fxml"); // Charger la vue des statistiques
    }

    @FXML
    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            chatArea.getItems().add("Nutritionniste : " + message);
            inputField.clear();
        }
    }
}