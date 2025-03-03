package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.EmailService;
import GestionBoutiqueONLINE.services.ProduitService;
import GestionBoutiqueONLINE.services.StripeService;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class PopupPaiementController {
    private Commande commande; // La commande à payer
    private Produit produit; // Le produit associé à la commande
    private AjouterCommandeClientController parentController; // Référence au contrôleur parent

    // Méthode pour définir la commande
    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    // Méthode pour définir le produit
    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    // Méthode pour définir le contrôleur parent
    public void setParentController(AjouterCommandeClientController parentController) {
        this.parentController = parentController;
    }

    // Action pour "Payer en espèces"
    @FXML
    private void payerEnEspece(MouseEvent event) {
        // Ajouter la commande à la base de données
        CommandeService commandeService = new CommandeService();
        commandeService.addCommande(commande);

        // Mettre à jour la quantité du produit
        ProduitService produitService = new ProduitService();
        produitService.updateQuantiteProduit(commande.getIdProduit(), commande.getQuantiteCommande());

        // Afficher un message de succès
        afficherAlerte("Succès", "✅ Commande validée avec succès !");
        // Envoyer un e-mail de confirmation
        String toEmail = "bellilmariem22@gmail.com"; // Remplacez par l'e-mail du client
        String orderDetails = "ID Commande: " + commande.getIdCommande() + "\n"
                + "Produit: " + produit.getNomProduit() + "\n"
                + "Quantité: " + commande.getQuantiteCommande() + "\n"
                + "Prix unitaire: " + produit.getPrixProduit() + " TND\n"
                + "TVA (7%): " + (produit.getPrixProduit() * commande.getQuantiteCommande() * 0.07) + " TND\n"
                + "Frais de livraison: 7.00 TND\n"
                + "Total TTC: " + (produit.getPrixProduit() * commande.getQuantiteCommande() * 1.07 + 7.0) + " TND";
        String paymentDetails = "Paiement effectué en espèces.";

        EmailService.sendOrderConfirmationEmail(toEmail, orderDetails, paymentDetails);

        // Rediriger vers ConsulterCommandeClient
        if (parentController != null) {
            parentController.redirectToConsulterCommandeClient();
        } else {
            System.out.println("❌ Erreur : parentController est null.");
        }

        // Fermer le popup
        Stage popupStage = (Stage) ((VBox) event.getSource()).getScene().getWindow();
        popupStage.close();
    }

    // Action pour "Payer en ligne"
    @FXML
    private void payerEnLigne(MouseEvent event) {
        try {
            // Calculer le montant total
            int montantTotal = commande.getQuantiteCommande() * produit.getPrixProduit();

            // Créer un PaymentIntent avec Stripe
            StripeService stripeService = new StripeService();
            String clientSecret = stripeService.createPaymentIntent(montantTotal, "eur");

            // Charger le formulaire de paiement Stripe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaiementStripe.fxml"));
            Parent root = loader.load();

            // Passer le clientSecret, la commande et le produit au contrôleur
            PaiementStripeController paiementController = loader.getController();
            paiementController.setClientSecret(clientSecret,montantTotal);
            paiementController.setCommande(commande);
            paiementController.setProduit(produit);

            // Afficher la fenêtre de paiement
            Stage stage = new Stage();
            stage.setTitle("Paiement en ligne");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer le popup
            Stage popupStage = (Stage) ((VBox) event.getSource()).getScene().getWindow();
            popupStage.close();
        } catch (Exception e) {
            afficherAlerte("Erreur", "❌ Erreur lors du paiement en ligne : " + e.getMessage());
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
