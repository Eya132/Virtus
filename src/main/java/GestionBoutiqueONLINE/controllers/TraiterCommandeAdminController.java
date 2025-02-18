package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TraiterCommandeAdminController {



    @FXML
    private TextField NomProduitTextField;

    @FXML
    private TextField PrixProduitTextField;

    @FXML
    private TextField DescriptionProduitTextField;

    @FXML
    private TextField QuantiteTextField;

    @FXML
    private ImageView ImageViewProduit;

    @FXML
    private ComboBox<Commande.StatusCommande> StatusComboBox; // Utilisation de l'énumération StatusCommande

    private Commande commande;
    private final ProduitService produitService = new ProduitService();
    private final CommandeService commandeService = new CommandeService();

    public void setCommande(Commande commande) {
        this.commande = commande;
        chargerDonneesCommande();
    }

    private void chargerDonneesCommande() {
        Produit produit = produitService.getProduitById(commande.getIdProduit());
        if (produit != null) {
            NomProduitTextField.setText(produit.getNomProduit());
            PrixProduitTextField.setText(String.valueOf(produit.getPrixProduit()));
            DescriptionProduitTextField.setText(produit.getDescriptionProduit());
            if (produit.getImageProduit() != null) {
                ImageViewProduit.setImage(new Image(produit.getImageProduit()));
            }
        }
        QuantiteTextField.setText(String.valueOf(commande.getQuantiteCommande()));

        // Charger les statuts possibles dans la ComboBox
        StatusComboBox.getItems().setAll(Commande.StatusCommande.values()); // Récupérer toutes les valeurs de l'énumération
        StatusComboBox.setValue(commande.getStatusCommande()); // Définir le statut actuel de la commande
    }

    @FXML
    void UpdateAction() {
        Commande.StatusCommande nouveauStatut = StatusComboBox.getValue(); // Récupérer le statut sélectionné
        commande.setStatusCommande(nouveauStatut); // Mettre à jour le statut de la commande

        // Si le statut est "VALIDÉE", mettre à jour la quantité du produit
        if (nouveauStatut == Commande.StatusCommande.VALIDÉE) {
            Produit produit = produitService.getProduitById(commande.getIdProduit());
            produit.setQuantiteProduit(produit.getQuantiteProduit() - commande.getQuantiteCommande());
            produitService.updateProduit(produit.getIdProduit(),produit);
        }

        // Mettre à jour la commande dans la base de données
        commandeService.updateCommande(commande.getIdCommande(), commande);
        afficherAlerte("Succès", "✅ Commande mise à jour avec succès !");
        QuantiteTextField.getScene().getWindow().hide(); // Fermer la fenêtre
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void AnnulerAction() {
        QuantiteTextField.getScene().getWindow().hide();
    }

}
