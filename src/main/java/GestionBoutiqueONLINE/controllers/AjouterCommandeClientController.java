package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class AjouterCommandeClientController {
    @FXML
    private Label nomProduitLabel;

    @FXML
    private TextField quantiteField;

    @FXML
    private ImageView imageView;

    @FXML
    private Label prixLabel;

    @FXML
    private Label descriptionLabel;

    private Produit produit; // Pour stocker le produit sélectionné

    // ID utilisateur statique pour le test
    private static final int ID_USER_STATIQUE = 19;

    public void setProduit(Produit produit) {
        this.produit = produit;
        nomProduitLabel.setText(produit.getNomProduit()); // Afficher le nom du produit
        prixLabel.setText(String.format("%d TND", produit.getPrixProduit())); // Afficher le prix
        descriptionLabel.setText(produit.getDescriptionProduit()); // Afficher la description
        imageView.setImage(new Image(produit.getImageProduit())); // Afficher l'image
    }

    @FXML
    private void validerCommande() {
        try {
            int quantite = Integer.parseInt(quantiteField.getText());

            // Vérifier si la quantité est valide
            if (quantite <= 0) {
                afficherAlerte("Erreur", "❌ La quantité doit être supérieure à 0.");
            } else if (quantite > produit.getQuantiteProduit()) {
                afficherAlerte("Erreur", "❌ Vous ne pouvez pas commander plus que " + produit.getQuantiteProduit());
            } else {
                // Créer une nouvelle commande
                Commande commande = new Commande(
                        LocalDateTime.now(), // Date actuelle
                        quantite,
                        produit.getIdProduit(),
                        ID_USER_STATIQUE, // ID utilisateur statique
                        Commande.StatusCommande.EN_ATTENTE // Statut par défaut
                );

                // Ajouter la commande à la base de données
                CommandeService commandeService = new CommandeService();
                commandeService.addCommande(commande);

                // Afficher un message de succès
                afficherAlerte("Succès", "✅ Commande validée avec succès !");
                // Redirection vers la liste des produits
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterCommandeClient.fxml"));
                    Parent root = loader.load();

                    // Récupérer le Stage actuel
                    Stage stage = (Stage) quantiteField.getScene().getWindow();
                    stage.setTitle("Liste des commandes"); // Définir le titre de la fenêtre

                    // Remplacer la scène actuelle
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    System.out.println("❌Erreur lors de la redirection : " + e.getMessage());
                    afficherAlerte("Erreur", "❌Impossible de charger la liste des commandes.");
                }

                // Fermer la fenêtre après validation
                //quantiteField.getScene().getWindow().hide();
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "❌ Veuillez entrer une quantité valide.");
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
