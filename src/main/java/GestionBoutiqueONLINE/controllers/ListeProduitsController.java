package GestionBoutiqueONLINE.controllers;


import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;

public class ListeProduitsController {


    @FXML
    private TableView<Produit> produitTable;

    @FXML
    private TableColumn<Produit, String> nomColumn;

    @FXML
    private TableColumn<Produit, Integer> prixColumn;

    @FXML
    private TableColumn<Produit, Integer> quantiteColumn;

    @FXML
    private TableColumn<Produit, String> imageColumn;

    @FXML
    private TableColumn<Produit, Void> actionsColumn;

    private ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        // Configurer les colonnes du TableView
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixProduit"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteProduit"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageProduit"));

        // Personnaliser la colonne "Image" pour afficher l'image
        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(imagePath);
                        imageView.setImage(image);
                        imageView.setFitWidth(150);
                        imageView.setFitHeight(150);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image : " + imagePath);
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });

        // Ajouter les boutons d'actions (Modifier et Supprimer)
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier"); // Texte pour Modifier
            private final Button deleteButton = new Button("Supprimer"); // Texte pour Supprimer

            {
                // Style des boutons
                updateButton.setStyle("-fx-background-color: #10888D; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 80; -fx-min-height: 30;");
                deleteButton.setStyle("-fx-background-color: #F45E0C; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 80; -fx-min-height: 30;");

                // Action pour le bouton Modifier
                updateButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    ouvrirInterfaceUpdate(produit);
                });

                // Action pour le bouton Supprimer
                deleteButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    supprimerProduit(produit);
                });
                produitTable.setRowFactory(tv -> {
                    TableRow<Produit> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !row.isEmpty()) { // Double-clic
                            Produit produit = row.getItem();
                            afficherDetailsProduit(produit); // Afficher les détails du produit
                        }
                    });
                    return row;
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, updateButton, deleteButton));
                }
            }
        });

        // Charger les produits depuis la base de données
        loadProduits();
    }

    @FXML
    private void ajouterProduitAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) produitTable.getScene().getWindow();
            stage.setTitle("Ajouter un produit");
            stage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface d'ajout de produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadProduits() {
        List<Produit> produits = produitService.getAllDataProduit();
        produitTable.getItems().setAll(produits);
    }

    private void ouvrirInterfaceUpdate(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProduit.fxml"));
            Parent root = loader.load();

            UpdateProduitController updateController = loader.getController();
            updateController.setProduit(produit);

            Stage stage = new Stage();
            stage.setTitle("Modifier le Produit");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerProduit(Produit produit) {
        produitService.deleteProduit(produit);
        loadProduits();
    }
    private void afficherDetailsProduit(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsProduit.fxml"));
            Parent root = loader.load();

            DetailsProduitController detailsController = loader.getController();
            detailsController.afficherDetails(produit); // Passer le produit au contrôleur des détails

            Stage stage = new Stage();
            stage.setTitle("Détails du Produit");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface des détails du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
