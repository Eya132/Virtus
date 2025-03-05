package controller;

import Entites.Match1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import services.MatchService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanifierMatchController {
    private int userId; // ID de l'utilisateur connecté

    // Méthode pour définir l'ID utilisateur
    public void setUserId(int userId) {
        this.userId = userId;
    }
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField heureField;

    @FXML
    private TextField heureFinField;

    @FXML
    private ComboBox<String> localisationComboBox;

    @FXML
    private ComboBox<String> terrainComboBox;

    @FXML
    private ComboBox<Integer> nbPersonneComboBox;

    @FXML
    private ComboBox<String> typeSportComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField idUserField;

    private MatchService matchService = new MatchService();
    private Map<String, List<String>> terrainsParLocalisation = new HashMap<>();

    @FXML
    public void initialize() {
        // Initialisation des options pour la ComboBox localisation
        ObservableList<String> localisations = FXCollections.observableArrayList(
                "ARIANA", "BEJA", "BEN AROUS", "BIZERTE", "GABES", "GAFSA", "JENDOUBA", "KAIROUAN",
                "KASSERINE", "KEBILI", "KEF", "MAHDIA", "MANOUBA", "MEDENINE", "MONASTIR", "NABEUL",
                "SFAX", "SIDI BOUZID", "SILIANA", "SOUSSE", "TATAOUINE", "TOZEUR", "TUNIS", "ZAGHOUAN"
        );
        localisationComboBox.setItems(localisations);

        // Initialisation des options pour la ComboBox nombre de personnes
        ObservableList<Integer> nbPersonnes = FXCollections.observableArrayList(2, 4);
        nbPersonneComboBox.setItems(nbPersonnes);

        // Initialisation des options pour la ComboBox type de sport
        ObservableList<String> sports = FXCollections.observableArrayList("Tennis", "Padel", "Ping Pong");
        typeSportComboBox.setItems(sports);

        // Initialisation des terrains par localisation
        initialiserTerrainsParLocalisation();

        // Ajouter un listener pour mettre à jour les terrains en fonction de la localisation sélectionnée
        localisationComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                List<String> terrains = terrainsParLocalisation.get(newVal);
                if (terrains != null) {
                    terrainComboBox.setItems(FXCollections.observableArrayList(terrains));
                    terrainComboBox.getSelectionModel().selectFirst(); // Sélectionner le premier terrain par défaut
                } else {
                    terrainComboBox.setItems(FXCollections.observableArrayList());
                }
            }
        });
    }

    private void initialiserTerrainsParLocalisation() {
        terrainsParLocalisation.put("ARIANA", Arrays.asList("Stade Olympique de Radès", "Terrain Municipal d'Ariana"));
        terrainsParLocalisation.put("BEJA", Arrays.asList("Stade Municipal de Béja", "Terrain de la Cité Sportive"));
        terrainsParLocalisation.put("BEN AROUS", Arrays.asList("Stade Bou Kornine", "Terrain de Mégrine"));
        terrainsParLocalisation.put("BIZERTE", Arrays.asList("Stade 15 Octobre", "Terrain de Menzel Bourguiba"));
        terrainsParLocalisation.put("GABES", Arrays.asList("Stade Municipal de Gabès", "Terrain de Chenini"));
        terrainsParLocalisation.put("GAFSA", Arrays.asList("Stade Municipal de Gafsa", "Terrain de Métlaoui"));
        terrainsParLocalisation.put("JENDOUBA", Arrays.asList("Stade Municipal de Jendouba", "Terrain de Tabarka"));
        terrainsParLocalisation.put("KAIROUAN", Arrays.asList("Stade de Kairouan", "Terrain de Bou Hajla"));
        terrainsParLocalisation.put("KASSERINE", Arrays.asList("Stade Municipal de Kasserine", "Terrain de Sbeitla"));
        terrainsParLocalisation.put("KEBILI", Arrays.asList("Stade Municipal de Kébili", "Terrain de Douz"));
        terrainsParLocalisation.put("KEF", Arrays.asList("Stade du Kef", "Terrain de Tajerouine"));
        terrainsParLocalisation.put("MAHDIA", Arrays.asList("Stade Municipal de Mahdia", "Terrain de Bou Merdes"));
        terrainsParLocalisation.put("MANOUBA", Arrays.asList("Stade de la Manouba", "Terrain de Den Den"));
        terrainsParLocalisation.put("MEDENINE", Arrays.asList("Stade Municipal de Médenine", "Terrain de Ben Guerdane"));
        terrainsParLocalisation.put("MONASTIR", Arrays.asList("Stade Mustapha Ben Jannet", "Terrain de Moknine"));
        terrainsParLocalisation.put("NABEUL", Arrays.asList("Stade Municipal de Nabeul", "Terrain de Hammamet"));
        terrainsParLocalisation.put("SFAX", Arrays.asList("Stade Taïeb Mhiri", "Terrain de Sakiet Ezzit"));
        terrainsParLocalisation.put("SIDI BOUZID", Arrays.asList("Stade Municipal de Sidi Bouzid", "Terrain de Meknassy"));
        terrainsParLocalisation.put("SILIANA", Arrays.asList("Stade Municipal de Siliana", "Terrain de Gaâfour"));
        terrainsParLocalisation.put("SOUSSE", Arrays.asList("Stade Olympique de Sousse", "Terrain de Hammam Sousse"));
        terrainsParLocalisation.put("TATAOUINE", Arrays.asList("Stade Municipal de Tataouine", "Terrain de Remada"));
        terrainsParLocalisation.put("TOZEUR", Arrays.asList("Stade Municipal de Tozeur", "Terrain de Nefta"));
        terrainsParLocalisation.put("TUNIS", Arrays.asList("Stade Chedly Zouiten", "Terrain d'El Menzah"));
        terrainsParLocalisation.put("ZAGHOUAN", Arrays.asList("Stade Municipal de Zaghouan", "Terrain de Bir Mcherga"));
    }

    @FXML
    public void planifierMatch() {
        try {
            // Vérifier que tous les champs obligatoires sont remplis
            if (!areRequiredFieldsFilled()) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
            }

            // Récupérer la date sélectionnée
            LocalDate selectedDate = datePicker.getValue();

            // Vérifier si la date est dans le passé
            if (selectedDate.isBefore(LocalDate.now())) {
                showWarningAlert("Date invalide", "La date sélectionnée est dans le passé. Veuillez choisir une date future.");
                return; // Arrêter l'exécution de la méthode si la date est dans le passé
            }

            // Récupérer les valeurs des champs
            String date = selectedDate.toString();
            String heure = heureField.getText();
            String localisation = localisationComboBox.getValue();
            String terrain = terrainComboBox.getValue();
            int nbPersonne = nbPersonneComboBox.getValue();
            String typeSport = typeSportComboBox.getValue();
            String description = descriptionArea.getText();

            // Récupérer l'ID utilisateur depuis le champ
            int idUtilisateur = parseUserId(idUserField.getText());

            // Vérifier l'unicité du match
            if (!matchService.isMatchUnique(date, heure, terrain)) {
                showWarningAlert("Match existant", "Un match avec la même date, heure et terrain existe déjà.");
                return; // Arrêter l'exécution de la méthode si le match n'est pas unique
            }
           // Stocker l'ID utilisateur dans la classe statique
            Session.setUserId(idUtilisateur);
            // Créer un nouvel objet Match1 avec le statut en_attente
            Match1 match = new Match1(0, date, heure, localisation, terrain, nbPersonne, description, typeSport, "en_attente", idUtilisateur);

            // Ajouter le match via le service
            matchService.addMatch(match);

            // Afficher un message de succès
            showSuccessAlert("Succès", "Le match a été planifié avec succès !");

            // Ne pas rediriger directement ici, laisser le navbar gérer la redirection
        } catch (IllegalArgumentException e) {
            showErrorAlert("Erreur", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Erreur", "Erreur lors de la planification du match : " + e.getMessage());
        }
    }
    private void redirectToUserDashboard(int userId) {
        try {
            // Charger le fichier FXML de l'interface UserDashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/match_view.fxml"));
            Parent root = loader.load();

            // Passer l'ID utilisateur au contrôleur UserDashboardController
            UserDashboardController controller = loader.getController();
            controller.setUserId(userId);

            // Remplacer la scène actuelle par la nouvelle scène
            Stage stage = (Stage) datePicker.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showErrorAlert("Erreur", "Erreur lors du chargement de l'interface : " + e.getMessage());
        }
    }

    private boolean areRequiredFieldsFilled() {
        return datePicker.getValue() != null
                && !heureField.getText().isEmpty()
                && localisationComboBox.getValue() != null
                && terrainComboBox.getValue() != null
                && nbPersonneComboBox.getValue() != null
                && typeSportComboBox.getValue() != null
                && !descriptionArea.getText().isEmpty()
                && !idUserField.getText().isEmpty();
    }

    private int parseUserId(String userIdText) {
        try {
            return Integer.parseInt(userIdText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID utilisateur doit être un nombre valide.");
        }
    }

    @FXML
    public void onHeureChange(KeyEvent event) {
        try {
            String heureDebutStr = heureField.getText();
            if (!heureDebutStr.isEmpty()) {
                // Vérifier le format de l'heure
                if (heureDebutStr.matches("^([01]?[0-9]|2[0-3]):([0-5][0-9])$")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime heureDebut = LocalTime.parse(heureDebutStr, formatter);

                    // Ajouter 45 minutes à l'heure de début pour calculer l'heure de fin
                    LocalTime heureFin = heureDebut.plus(45, ChronoUnit.MINUTES);

                    // Afficher l'heure de fin dans le champ heureFinField
                    heureFinField.setText(heureFin.format(formatter));
                } else {
                    heureFinField.clear();
                }
            }
        } catch (DateTimeParseException e) {
            heureFinField.clear();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetFields() {
        datePicker.setValue(null);
        heureField.clear();
        heureFinField.clear();
        localisationComboBox.getSelectionModel().clearSelection();
        terrainComboBox.getSelectionModel().clearSelection();
        nbPersonneComboBox.getSelectionModel().clearSelection();
        typeSportComboBox.getSelectionModel().clearSelection();
        descriptionArea.clear();
        idUserField.clear();
    }
}