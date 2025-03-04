package edu.pidev3a8.controllers.Admin;

import edu.pidev3a8.entities.User;
import edu.pidev3a8.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListUser {

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TableColumn<User, String> nomColumn;

    @FXML
    private TableColumn<User, String> prenomColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private TableColumn<User, String> photoColumn;

    @FXML
    private TableColumn<User, Void> actionColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnAddUser;

    @FXML
    private Button btnStats;

    private User loggedInUser;
    private UserService userService = new UserService();
    private ObservableList<User> originalUserList;

    // Method to set the logged-in user
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        System.out.println("Utilisateur connecté (ListUser): " + loggedInUser.getNomUser());
    }

    public void initialize() {
        // Configuration des colonnes
        btnAddUser.setOnAction(event -> handleAddUser());
        btnStats.setOnAction(event -> handleStats());
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomUser"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenomUser"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        photoColumn.setCellValueFactory(new PropertyValueFactory<>("photoUser"));

        // Configuration de la colonne photo
        photoColumn.setCellFactory(column -> new TableCell<User, String>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(150); // Ajustez la taille de l'image
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true); // Conserver le ratio de l'image
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);

                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null); // Afficher rien si l'URL est vide
                    System.out.println("Image URL is empty or null");
                } else {
                    try {
                        // Charger l'image depuis l'URL
                        System.out.println("Attempting to load image from URL: " + imageUrl);
                        Image image = new Image(imageUrl, true); // Utilisez le deuxième paramètre pour le chargement en arrière-plan
                        imageView.setImage(image);
                        setGraphic(imageView); // Afficher l'image dans la cellule
                        System.out.println("Image loaded successfully: " + imageUrl);
                    } catch (Exception e) {
                        setGraphic(null); // Afficher rien en cas d'erreur
                        System.out.println("Error loading image: " + e.getMessage());
                    }
                }
            }
        });

        // Configuration de la colonne action avec les boutons
        actionColumn.setCellFactory(new Callback<TableColumn<User, Void>, TableCell<User, Void>>() {
            @Override
            public TableCell<User, Void> call(final TableColumn<User, Void> param) {
                return new TableCell<User, Void>() {

                    private final Button btnEdit = new Button("Modifier");
                    private final Button btnDelete = new Button("Supprimer");
                    private final Button btnView = new Button("Voir détails");
                    private final HBox pane = new HBox(btnEdit, btnDelete, btnView);

                    {
                        // Style des boutons (optionnel)
                        btnEdit.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                        btnView.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                        // Actions des boutons
                        btnEdit.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            editUser(user);
                        });

                        btnDelete.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            deleteUser(user);
                        });

                        btnView.setOnAction(event -> {
                            User user = getTableView().getItems().get(getIndex());
                            viewUser(user);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        });

        // Charger les utilisateurs
        loadUsers();

        // Initialiser la liste originale des utilisateurs
        originalUserList = FXCollections.observableArrayList(userTable.getItems());

        // Ajouter un écouteur sur le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterUsers(newValue);
        });

        // Appliquer le style CSS
        userTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }
        });
    }

    private void loadUsers() {
        List<User> users = userService.ListEntities();
        userTable.getItems().setAll(users);
    }

    private void filterUsers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            userTable.getItems().setAll(originalUserList);
        } else {
            List<User> filteredList = originalUserList.stream()
                    .filter(user -> (user.getNomUser() != null && user.getNomUser().toLowerCase().contains(searchText.toLowerCase())) ||
                            (user.getPrenomUser() != null && user.getPrenomUser().toLowerCase().contains(searchText.toLowerCase())))
                    .collect(Collectors.toList());
            userTable.getItems().setAll(filteredList);
        }
    }

    private void editUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierUser.fxml"));
            Parent root = loader.load();

            UpdateUser controller = loader.getController();
            User fullUser = userService.getUserById(user.getId_user()); // Récupérer toutes les données
            controller.setUserData(fullUser);

            Stage stage = new Stage();
            stage.setTitle("Modifier Utilisateur");
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setMaximized(true);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) userTable.getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(User user) {
        // Boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet utilisateur ?");
        alert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Supprimer l'utilisateur de la base de données
                userService.deleteEntity(user);

                // Supprimer l'utilisateur de la TableView
                userTable.getItems().remove(user);

                System.out.println("Utilisateur supprimé avec succès : " + user.getNomUser());
            } catch (Exception e) {
                System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
                e.printStackTrace(); // Afficher la stack trace complète

                // Afficher un message d'erreur à l'utilisateur
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Échec de la suppression");
                errorAlert.setContentText("Une erreur s'est produite lors de la suppression de l'utilisateur.");
                errorAlert.showAndWait();
            }
        }
    }

    private void viewUser(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherUser.fxml"));
            Parent root = loader.load();

            AfficherUser controller = loader.getController();
            User fullUser = userService.getUserById(user.getId_user()); // Récupérer toutes les données
            controller.setUserData(fullUser);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'utilisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        try {
            // Charger la vue AjouterUser.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterUser.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Ajouter Utilisateur");
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setMaximized(true);

            // Fermer la fenêtre actuelle (la liste des utilisateurs)
            Stage currentStage = (Stage) userTable.getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleStats() {
        try {
            // Charger la vue StatistiquesAgeUtilisateurs.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatUser.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Statistiques sur l'âge des utilisateurs");
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setMaximized(true);

            // Fermer la fenêtre actuelle (la liste des utilisateurs)
            Stage currentStage = (Stage) userTable.getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}