package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientProduitController {

    @FXML
    private FlowPane produitsContainer;

    private final ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        chargerProduits();
    }

    private void chargerProduits() {
        List<Produit> produits = produitService.getAllDataProduit();

        for (Produit produit : produits) {
            VBox produitBox = creerProduitBox(produit);
            produitsContainer.getChildren().add(produitBox);
        }
    }

    private VBox creerProduitBox(Produit produit) {
        VBox produitBox = new VBox(10);
        produitBox.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-padding: 10;");
        produitBox.setPrefWidth(200);
        produitBox.setPrefHeight(300);

        ImageView imageView = new ImageView(new Image(produit.getImageProduit()));
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        Label nomLabel = new Label(produit.getNomProduit());
        nomLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label prixLabel = new Label(String.format("%d TND", produit.getPrixProduit()));
        Label descriptionLabel = new Label(produit.getDescriptionProduit());
        descriptionLabel.setWrapText(true);

        Label stockLabel = new Label(produit.getQuantiteProduit() > 0 ? "En stock" : "Rupture de stock");
        stockLabel.setStyle(produit.getQuantiteProduit() > 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;");

        produitBox.getChildren().addAll(imageView, nomLabel, prixLabel, descriptionLabel, stockLabel);
        produitBox.setOnMouseClicked(event -> afficherDetailsProduit(produit));

        return produitBox;
    }

    private void afficherDetailsProduit(Produit produit) {


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsProduitsClient.fxml"));
            Parent root = loader.load();

            DetailsProduitsClientController detailsController = loader.getController();
            detailsController.afficherDetails(produit);

            Stage stage = new Stage();
            stage.setTitle("Détails du Produit");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'afficher les détails du produit.");
        }
    }

    @FXML
    private void consulterCommandes() {
        System.out.println("Bouton 'Consulter mes commandes' cliqué");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterCommandeClient.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Mes Commandes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible de charger la liste des commandes.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}




