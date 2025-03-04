package controller;

import Entites.ListInscri;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import services.ListInscriService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ParticipantsController implements Initializable {

    @FXML
    private ListView<String> participantsList; // Liste des participants

    private int matchId; // ID du match sélectionné
    private ListInscriService listInscriService = new ListInscriService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cette méthode est appelée après que le FXML a été chargé
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
        loadParticipants();
    }

    private void loadParticipants() {
        // Récupérer les inscriptions pour ce match
        List<ListInscri> inscriptions = listInscriService.getInscriptionsByMatchId(matchId);

        // Afficher les participants dans la ListView
        for (ListInscri inscription : inscriptions) {
            participantsList.getItems().add("Utilisateur ID: " + inscription.getuserId());
        }
    }
}