package gestion_match.controller;

import gestion_match.entites.Match;
import gestion_match.services.MatchService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PremierPageController {

    @FXML
    private TextField lbHeure;
    @FXML
    private TextField lbHeureFin; // Champ pour afficher l'heure de fin
    @FXML
    private TextField lbIdPlanificateur; // Nouveau champ pour l'ID du planificateur
    @FXML
    private ComboBox<String> comboLocalisation;
    @FXML
    private ComboBox<String> comboTerrain;
    @FXML
    private ComboBox<String> lbTypeSport; // Changez de TextField à ComboBox si vous utilisez un ComboBox pour le sport
    @FXML
    private ComboBox<Integer> comboNbPersonnes; // Changement ici, ComboBox pour le nombre de personnes
    @FXML
    private TextArea lbdescription;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button btnAjouterMatch;


    private MatchService matchService;

    // Map pour associer chaque localisation à une liste de terrains
    private Map<String, List<String>> terrainsParLocalisation;

    public PremierPageController() {
        this.matchService = new MatchService();
        this.terrainsParLocalisation = new HashMap<>();
    }

    // Cette méthode initialise les ComboBox et autres composants
    @FXML
    private void initialize() {
        // Initialisation des localisations
        ObservableList<String> localisations = FXCollections.observableArrayList(
                "Tunis", "Sfax", "Sousse", "Gabès", "Nabeul", "Kairouan", "Bizerte",
                "Ariana", "Monastir", "Kasserine", "Gafsa", "Ben Arous", "Médenine",
                "Kebili", "Mahdia", "Jendouba", "Manouba", "Zaghouan", "Siliana",
                "Tozeur", "Beja", "Tataouine", "Le Kef", "Sidi Bouzid"
        );
        comboLocalisation.setItems(localisations);

        // Remplir la Map avec les terrains pour chaque localisation
        terrainsParLocalisation.put("Tunis", List.of("Terrain Tunis 1", "Terrain Tunis 2"));
        terrainsParLocalisation.put("Sfax", List.of("Terrain Sfax 1", "Terrain Sfax 2"));
        // Ajoutez les autres localisations et leurs terrains ici

        // Ajouter un écouteur pour mettre à jour les terrains lorsque la localisation change
        comboLocalisation.setOnAction(event -> updateTerrains());

        // Initialiser le ComboBox pour le nombre de personnes
        ObservableList<Integer> nbPersonnesList = FXCollections.observableArrayList(2, 4);
        comboNbPersonnes.setItems(nbPersonnesList);

        // Ajouter un écouteur pour calculer l'heure de fin lorsque l'heure de début est saisie
        lbHeure.textProperty().addListener((observable, oldValue, newValue) -> calculateEndTime(newValue));
    }

    // Méthode pour calculer et afficher l'heure de fin
    private void calculateEndTime(String startTime) {
        try {
            // Formater l'heure de début au format HH:mm
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime start = LocalTime.parse(startTime, timeFormatter);

            // Ajouter 45 minutes
            LocalTime end = start.plusMinutes(45);

            // Afficher l'heure de fin
            lbHeureFin.setText(end.format(timeFormatter));
        } catch (Exception e) {
            lbHeureFin.clear(); // Si l'heure de début n'est pas valide, vider l'heure de fin
        }
    }

    // Met à jour le ComboBox des terrains en fonction de la localisation sélectionnée
    private void updateTerrains() {
        String localisation = comboLocalisation.getValue();
        if (localisation != null && terrainsParLocalisation.containsKey(localisation)) {
            List<String> terrains = terrainsParLocalisation.get(localisation);
            ObservableList<String> terrainsObservable = FXCollections.observableArrayList(terrains);
            comboTerrain.setItems(terrainsObservable);
        } else {
            comboTerrain.setItems(FXCollections.observableArrayList());
        }
    }


    @FXML
    private void createEntity() {
        try {
            // Vérifier que tous les champs sont remplis
            if (lbHeure.getText().isEmpty() || comboLocalisation.getValue() == null || comboTerrain.getValue() == null ||
                    lbTypeSport.getValue() == null || comboNbPersonnes.getValue() == null || lbdescription.getText().isEmpty() ||
                    datePicker.getValue() == null || lbIdPlanificateur.getText().isEmpty()) {
                showErrorMessage("Tous les champs sont obligatoires.");
                return;
            }

            // Vérifier que la date choisie est dans le futur
            Date dateMatch = Date.valueOf(datePicker.getValue());
            if (dateMatch.before(new Date(System.currentTimeMillis()))) {
                showErrorMessage("La date du match doit être dans le futur.");
                return;
            }

            // Vérifier que l'heure de fin est après l'heure de début
            String startTime = lbHeure.getText();
            String endTime = lbHeureFin.getText();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            try {
                LocalTime start = LocalTime.parse(startTime, timeFormatter);
                LocalTime end = LocalTime.parse(endTime, timeFormatter);

                if (end.isBefore(start)) {
                    showErrorMessage("L'heure de fin doit être après l'heure de début.");
                    return;
                }
            } catch (Exception e) {
                showErrorMessage("Les heures doivent être au format HH:mm.");
                return;
            }

            // Récupérer les valeurs des champs
            String heure = lbHeure.getText();
            String heureFin = lbHeureFin.getText(); // Heure de fin récupérée
            String localisation = comboLocalisation.getValue();
            String terrain = comboTerrain.getValue();
            String typeSport = lbTypeSport.getValue(); // Correction : utiliser getValue() pour un ComboBox
            int nbPersonnes = comboNbPersonnes.getValue(); // Récupérer le nombre de personnes
            String description = lbdescription.getText();
            String idPlanificateur = lbIdPlanificateur.getText(); // ID du planificateur récupéré

            // Définir le statut par défaut à "EN_ATTENTE"
            String statut = "EN_ATTENTE";

            // Créer un objet Match avec les valeurs récupérées
            Match match = new Match(dateMatch, heure, localisation, terrain, typeSport, nbPersonnes, description, statut, Integer.parseInt(idPlanificateur));

            // Appeler le service pour ajouter le match dans la base de données
            matchService.createEntity(match);

            // Afficher un message de succès
            showSuccessMessage("Match ajouté avec succès !");
            clearFields();
        } catch (Exception e) {
            showErrorMessage("Erreur lors de l'ajout du match : " + e.getMessage());
        }
    }


    // Méthode pour vider les champs du formulaire
    @FXML
    private void clearFields() {
        lbHeure.clear();
        lbHeureFin.clear(); // Vider également le champ de l'heure de fin
        comboLocalisation.setValue(null);
        comboTerrain.setValue(null);
        lbTypeSport.setValue(null); // Assurez-vous que le type de sport est vide également
        comboNbPersonnes.setValue(null); // Vider le nombre de personnes
        lbdescription.clear();
        lbIdPlanificateur.clear();  // Vider le champ de l'ID du planificateur
    }

    // Méthodes pour afficher des messages d'erreur et de succès
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
