package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class AjouterCommandeClientController {


    @FXML
    private Label nomProduitLabel;

    @FXML
    private Spinner<Integer> quantiteSpinner;

   /* @FXML
    private TextField quantiteField;

    */

    @FXML
    private ImageView imageView;

    @FXML
    private Label prixLabel;

    @FXML
    private Label descriptionLabel;

    private Produit produit; // Pour stocker le produit sélectionné

    // ID utilisateur statique pour le test
    private static final String ID_USER_STATIQUE = "666JJJ11111";

    public void initialize() {
        // Configuration du Spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1); // Min: 1, Max: 100, Valeur initiale: 1
        quantiteSpinner.setValueFactory(valueFactory);
    }

    // Méthode pour définir le produit
    public void setProduit(Produit produit) {
        this.produit = produit;
        nomProduitLabel.setText(produit.getNomProduit()); // Afficher le nom du produit
        prixLabel.setText(String.format("%d TND", produit.getPrixProduit())); // Afficher le prix
        descriptionLabel.setText(produit.getDescriptionProduit()); // Afficher la description
        imageView.setImage(new Image(produit.getImageProduit())); // Afficher l'image
    }

    // Action pour valider la commande
    @FXML
    private void validerCommande() {
        try {
            int quantite = quantiteSpinner.getValue();
            System.out.println("Quantité sélectionnée : " + quantite);


            // Vérifier si la quantité est valide
            if (quantite <= 0) {
                afficherAlerte("Erreur", "❌ La quantité doit être supérieure à 0.");
            } else if (quantite > produit.getQuantiteProduit()) {
                afficherAlerte("Erreur", "❌ Vous ne pouvez pas commander plus que " + produit.getQuantiteProduit());
            } else {
                // Créer une nouvelle commande (sans l'ajouter encore)
                Commande commande = new Commande(
                        LocalDateTime.now(), // Date actuelle
                        quantite,
                        produit.getIdProduit(),
                        ID_USER_STATIQUE, // ID utilisateur statique
                        Commande.StatusCommande.VALIDÉE // Statut par défaut
                );

                // Afficher la popup de paiement
                afficherPopupPaiement(commande);
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "❌ Veuillez entrer une quantité valide.");
        }
    }

    private void afficherPopupPaiement(Commande commande) {
        try {
            // Charger la popup de paiement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupPaiement.fxml"));
            Parent root = loader.load();

            // Passer la commande, le produit et le contrôleur parent à la popup
            PopupPaiementController popupController = loader.getController();
            popupController.setCommande(commande);
            popupController.setProduit(produit);
            popupController.setParentController(this); // Passer la référence du contrôleur parent

            // Afficher la popup
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Choisir un mode de paiement");
            popupStage.show();
        } catch (IOException e) {
            System.out.println("❌ Erreur lors du chargement de la popup de paiement : " + e.getMessage());
            afficherAlerte("Erreur", "❌ Impossible d'afficher la popup de paiement.");
        }
    }

    public void redirectToConsulterCommandeClient() {
        try {
            // Charger la vue ConsulterCommandeClient.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/ConsulterCommandeClient.fxml"));

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);

            // Récupérer le Stage actuel
            Stage stage = (Stage) quantiteSpinner.getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.setTitle("Consulter les commandes"); // Titre de la fenêtre
            stage.show(); // Afficher la nouvelle scène
        } catch (IOException e) {
            System.out.println("❌ Erreur lors de la redirection : " + e.getMessage());
            afficherAlerte("Erreur", "❌ Impossible de charger la page des commandes.");
        }
    }

    // Méthode pour afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
