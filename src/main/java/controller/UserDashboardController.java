package controller;

import Entites.Match1;
import Entites.ListInscri;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.MatchService;
import services.ListInscriService;

import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDashboardController {
    private int userId; // Variable pour stocker l'ID utilisateur

    // Méthode pour définir l'ID utilisateur
    public void setUserId(int userId) {
        this.userId = userId;

    }
    @FXML
    private TableView<Match1> matchTable;

    @FXML
    private TableColumn<Match1, String> dateColumn;

    @FXML
    private TableColumn<Match1, String> heureColumn;

    @FXML
    private TableColumn<Match1, String> localisationColumn;

    @FXML
    private TableColumn<Match1, String> terrainColumn;

    @FXML
    private TableColumn<Match1, String> typeSportColumn;

    @FXML
    private TableColumn<Match1, Void> actionColumn;

    private MatchService matchService = new MatchService();
    private ListInscriService listInscriService = new ListInscriService();

    private Map<String, List<String>> terrainsParLocalisation = new HashMap<>();

    @FXML
    public void initialize() {
        // Récupérer l'ID utilisateur depuis la classe Session
        int userId = Session.getUserId();

        // Configurer les colonnes
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        localisationColumn.setCellValueFactory(new PropertyValueFactory<>("localisation"));
        terrainColumn.setCellValueFactory(new PropertyValueFactory<>("terrain"));
        typeSportColumn.setCellValueFactory(new PropertyValueFactory<>("typeSport"));

        // Initialiser les terrains par localisation
        initialiserTerrainsParLocalisation();

        // Charger les matchs de l'utilisateur
        loadUserMatches(userId);

        // Ajouter une colonne pour les actions (Annuler et Modifier)
        actionColumn.setCellFactory(param -> new ActionTableCell(userId));
    }

    // Charger les matchs de l'utilisateur (planificateur et participant)
    private void loadUserMatches(int userId) {
        // Matchs créés par l'utilisateur
        ObservableList<Match1> userMatches = FXCollections.observableArrayList(matchService.getMatchesByUserId(userId));

        // Matchs auxquels l'utilisateur participe
        List<ListInscri> participations = listInscriService.getInscriptionsByUserId(userId);
        for (ListInscri inscription : participations) {
            Match1 match = matchService.getMatchById(inscription.getMatchId());
            if (match != null && !userMatches.contains(match)) {
                userMatches.add(match);
            }
        }

        // Afficher les matchs dans la TableView
        matchTable.setItems(userMatches);

        // Ajouter un log pour vérifier que la table est bien mise à jour
        System.out.println("Table des matchs mise à jour avec " + userMatches.size() + " matchs.");
    }



    // Classe interne pour les boutons d'action
    private class ActionTableCell extends TableCell<Match1, Void> {
        private final Button annulerButton = new Button("Annuler");
        private final Button modifierButton = new Button("Modifier");
        private final int userId; // ID de l'utilisateur connecté

        public ActionTableCell(int userId) {
            this.userId = userId;

            // Action pour le bouton "Annuler"
            annulerButton.setOnAction(event -> {
                Match1 match = getTableView().getItems().get(getIndex());
                annulerMatch(match);
            });

            // Action pour le bouton "Modifier"
            modifierButton.setOnAction(event -> {
                Match1 match = getTableView().getItems().get(getIndex());
                modifierMatch(match);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                Match1 match = getTableView().getItems().get(getIndex());

                // Afficher "Modifier" uniquement si l'utilisateur est le planificateur
                if (match.getUserId() == userId) {
                    setGraphic(new HBox(5, annulerButton, modifierButton));
                } else {
                    setGraphic(annulerButton); // Seul le bouton "Annuler" est visible
                }
            }
        }
    }

    // Méthode pour annuler un match ou une participation
    private void annulerMatch(Match1 match) {
        System.out.println("Tentative d'annulation du match avec ID: " + match.getId());

        // Récupérer l'ID utilisateur depuis la classe Session
        int userId = Session.getUserId();

        // Vérifier si l'utilisateur est le planificateur
        if (match.getUserId() == userId) {
            // L'utilisateur est le planificateur, on annule le match
            matchService.deleteMatch(match.getId());
            System.out.println("Match annulé avec succès !");
        } else {
            // L'utilisateur est un participant, annuler la participation
            ListInscri inscription = listInscriService.getInscriptionByMatchAndUser(match.getId(), userId);
            if (inscription != null) {
                // Annuler la participation
                listInscriService.cancelParticipation(match.getId(), userId);

                // Mettre à jour le statut du match à "en_attente"
                match.setStatut("en_attente");
                matchService.updateMatch(match);  // Mettre à jour le match dans la base de données
                System.out.println("Participation annulée, statut du match mis à jour en 'en_attente' !");
            } else {
                System.out.println("Aucune inscription trouvée pour l'utilisateur.");
            }
        }

        // Rafraîchir la TableView
        loadUserMatches(userId);
    }

    // Méthode pour modifier un match
    private void modifierMatch(Match1 match) {
        int userId = Session.getUserId(); // Récupérer l'ID utilisateur depuis la classe Session

        if (match.getUserId() == userId) {
            // Créer une boîte de dialogue
            Dialog<Match1> dialog = new Dialog<>();
            dialog.setTitle("Modifier le Match");

            // Charger le fichier CSS avec vérification
            URL cssResource = getClass().getResource("/styles/style4.css");
            if (cssResource != null) {
                dialog.getDialogPane().getStylesheets().add(cssResource.toExternalForm());
                System.out.println("Fichier CSS chargé avec succès : " + cssResource);
            } else {
                System.err.println("Le fichier CSS n'a pas été trouvé ! Vérifiez le chemin : /styles/style4.css");
                showErrorAlert("Erreur CSS", "Le fichier CSS n'a pas été trouvé. Vérifiez le chemin : /styles/style4.css");
            }

            // Ajouter une classe CSS personnalisée à la boîte de dialogue
            dialog.getDialogPane().getStyleClass().add("dialog-pane");

            // Champ pour l'heure de début
            String heureString = match.getHeure() != null ? match.getHeure().toString() : "";
            TextField heureField = new TextField(heureString);
            heureField.getStyleClass().add("text-field");

            // Champ pour l'heure de fin (calculée automatiquement)
            TextField heureFinField = new TextField();
            heureFinField.setEditable(false); // Non modifiable par l'utilisateur
            heureFinField.getStyleClass().add("text-field");

            // Calculer et afficher l'heure de fin initiale
            if (!heureString.isEmpty()) {
                LocalTime heureDebut = LocalTime.parse(heureString);
                LocalTime heureFin = heureDebut.plusMinutes(45);
                heureFinField.setText(heureFin.toString());
            }

            // Mettre à jour l'heure de fin automatiquement lorsque l'heure de début change
            heureField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    LocalTime heureDebut = LocalTime.parse(newValue);
                    LocalTime heureFin = heureDebut.plusMinutes(45);
                    heureFinField.setText(heureFin.toString());
                } catch (Exception e) {
                    heureFinField.setText(""); // Vider si l'heure est invalide
                }
            });

            // Liste des localisations
            ObservableList<String> localisations = FXCollections.observableArrayList(
                    "ARIANA", "BEJA", "BEN AROUS", "BIZERTE", "GABES", "GAFSA", "JENDOUBA", "KAIROUAN",
                    "KASSERINE", "KEBILI", "KEF", "MAHDIA", "MANOUBA", "MEDENINE", "MONASTIR", "NABEUL",
                    "SFAX", "SIDI BOUZID", "SILIANA", "SOUSSE", "TATAOUINE", "TOZEUR", "TUNIS", "ZAGHOUAN"
            );

            ComboBox<String> localisationComboBox = new ComboBox<>(localisations);
            localisationComboBox.setValue(match.getLocalisation());
            localisationComboBox.getStyleClass().add("combo-box");

            // ComboBox pour les terrains
            ComboBox<String> terrainComboBox = new ComboBox<>();
            terrainComboBox.setValue(match.getTerrain());
            terrainComboBox.getStyleClass().add("combo-box");

            // Mettre à jour la liste des terrains selon la localisation sélectionnée
            terrainComboBox.setItems(FXCollections.observableArrayList(terrainsParLocalisation.get(match.getLocalisation())));

            localisationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    // Mettre à jour la liste des terrains en fonction de la localisation choisie
                    terrainComboBox.setItems(FXCollections.observableArrayList(terrainsParLocalisation.get(newValue)));
                    terrainComboBox.setValue(terrainsParLocalisation.get(newValue).get(0)); // Mettre à jour le terrain par défaut
                }
            });

            // ComboBox pour le nombre de personnes
            ComboBox<Integer> nbPersonneComboBox = new ComboBox<>(FXCollections.observableArrayList(2, 4));
            nbPersonneComboBox.setValue(match.getNbPersonne());
            nbPersonneComboBox.getStyleClass().add("combo-box");

            // Zone de texte pour la description
            TextArea descriptionArea = new TextArea(match.getDescription());
            descriptionArea.getStyleClass().add("text-area");

            // Ajouter les éléments à la boîte de dialogue
            dialog.getDialogPane().setContent(new VBox(10,
                    new Label("Heure de début:"), heureField,
                    new Label("Heure de fin (calculée):"), heureFinField,
                    new Label("Localisation:"), localisationComboBox,
                    new Label("Terrain:"), terrainComboBox,
                    new Label("Nombre de personnes:"), nbPersonneComboBox,
                    new Label("Description:"), descriptionArea));

            // Ajouter les boutons Enregistrer et Annuler
            ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Appliquer une classe CSS au bouton Enregistrer
            Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
            saveButton.getStyleClass().add("button");

            // Gérer le résultat de la boîte de dialogue
            dialog.setResultConverter(buttonType -> {
                if (buttonType == saveButtonType) {
                    // Mettre à jour les propriétés du match
                    match.setLocalisation(localisationComboBox.getValue());
                    match.setTerrain(terrainComboBox.getValue());
                    match.setNbPersonne(nbPersonneComboBox.getValue());
                    match.setDescription(descriptionArea.getText());

                    // Mettre à jour l'heure de début
                    String heureText = heureField.getText();
                    if (!heureText.isEmpty()) {
                        try {
                            LocalTime heureDebut = LocalTime.parse(heureText);
                            match.setHeure(heureDebut);

                            // Enregistrer les modifications dans la base de données
                            matchService.updateMatch(match);
                            loadUserMatches(userId); // Recharger les matchs
                            matchTable.refresh(); // Rafraîchir la TableView
                            System.out.println("Match modifié avec succès !");
                        } catch (Exception e) {
                            System.out.println("Format d'heure invalide");
                            showErrorAlert("Erreur", "Veuillez entrer une heure valide (HH:MM).");
                        }
                    }
                }
                return null;
            });

            // Afficher la boîte de dialogue
            dialog.showAndWait();
        } else {
            showWarningAlert("Action non autorisée", "Vous ne pouvez modifier que les matchs que vous avez planifiés.");
        }
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

    // Méthode pour afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une alerte d'avertissement
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}