package Controller;

import Entities.Participation;
import Services.ParticipationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ParticipationController {

    @FXML
    private TableView<Participation> participationTable;

    @FXML
    private TableColumn<Participation, Integer> idParticipationColumn;

    @FXML
    private TableColumn<Participation, Integer> idUserColumn;

    private ParticipationService participationService = new ParticipationService();

    public void initialize() {
        System.out.println("Controller chargé, initialisation en cours...");

        // Configuration des colonnes de la TableView
        idParticipationColumn.setCellValueFactory(new PropertyValueFactory<>("idParticipation"));
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("idUser"));

        // Charger les participations dans la TableView
        loadParticipations();
    }

    private void loadParticipations() {
        // Récupère la liste des participations en utilisant la méthode getAllParticipations()
        List<Participation> participationList = participationService.getAllParticipations();

        // Convertit la liste en ObservableList pour lier à la TableView
        ObservableList<Participation> observableList = FXCollections.observableArrayList(participationList);

        // Assigne la liste à la TableView
        participationTable.setItems(observableList);
    } }
