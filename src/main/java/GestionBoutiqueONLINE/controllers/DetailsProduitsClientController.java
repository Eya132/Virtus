package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class DetailsProduitsClientController {

    @FXML
    private Label nomLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Label prixLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label refLabel;

    @FXML
    private Label stockLabel;

    private Produit produit; // Pour stocker le produit actuel
    private String deviseActuelle = "TND"; // Devise par défaut

    public void afficherDetails(Produit produit, String devise, int prix) {
        this.produit = produit; // Stocker le produit
        this.deviseActuelle = devise; // Stocker la devise actuelle

        nomLabel.setText(produit.getNomProduit());
        imageView.setImage(new Image(produit.getImageProduit()));
        descriptionLabel.setText(produit.getDescriptionProduit());
        refLabel.setText(produit.getRefProduit());

        // Afficher le prix tel quel avec la devise
        prixLabel.setText(String.format("%d %s", prix, deviseActuelle));

        // Indicateur de stock
        if (produit.getQuantiteProduit() > 0) {
            stockLabel.setText("En stock");
            stockLabel.setStyle("-fx-text-fill: green;");
        } else {
            stockLabel.setText("Rupture de stock");
            stockLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void commanderProduit() {
        if (produit.getQuantiteProduit() <= 0) {
            afficherAlerte("Rupture de stock", "Ce produit n'est plus disponible.");
            return; // Sortir de la méthode sans afficher les détails
        }
        try {
            // Charger l'interface d'ajout de commande
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCommandeClient.fxml"));
            Parent root = loader.load();

            // Passer les données du produit au contrôleur de l'interface d'ajout de commande
            AjouterCommandeClientController ajouterCommandeController = loader.getController();
            ajouterCommandeController.setProduit(produit);

            // Afficher la nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Ajouter une commande");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement de l'interface d'ajout de commande : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    void AnnulerAction() {
        nomLabel.getScene().getWindow().hide();
    }
}