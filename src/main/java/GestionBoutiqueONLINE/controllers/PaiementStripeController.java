package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.EmailService;
import GestionBoutiqueONLINE.services.ProduitService;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class PaiementStripeController {
    @FXML
    private WebView stripeWebView;

    private String clientSecret; // ClientSecret du PaymentIntent
    private Commande commande;   // Référence à la commande
    private Produit produit;     // Référence au produit
    private double montant;

    // Méthode pour définir le clientSecret
    public void setClientSecret(String clientSecret, double montant) {
        this.montant = montant;
        this.clientSecret = clientSecret;
        chargerFormulaireStripe();
    }

    // Méthode pour définir la commande
    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    // Méthode pour définir le produit
    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    // Charger le formulaire Stripe
    private void chargerFormulaireStripe() {
        stripeWebView.getEngine().setOnError(event -> {
            System.err.println("Erreur WebView : " + event.getMessage());
        });

        stripeWebView.getEngine().setOnAlert(event -> {
            System.out.println("Alerte WebView : " + event.getData());
        });

        // Charger le fichier HTML externe
        String htmlPath = getClass().getResource("/paiement.html").toExternalForm();
        stripeWebView.getEngine().load(htmlPath);

        configurerConnecteurJavaJavaScript();
    }

    // Configurer le connecteur Java-JavaScript
    private void configurerConnecteurJavaJavaScript() {
        stripeWebView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) stripeWebView.getEngine().executeScript("window");
                window.setMember("javaConnector", this); // Utiliser "this" pour référencer le contrôleur
                System.out.println("✅ Connecteur Java-JavaScript configuré !");
            }
        });
    }

    // Méthode appelée depuis JavaScript pour ajouter la commande
    public void ajouterCommande() {
        System.out.println("✅ ajouterCommande() appelé !");
        Platform.runLater(() -> {
            try {
                if (commande == null || produit == null) {
                    afficherAlerte("Erreur", "❌ Commande ou produit est null !");
                    return;
                }

                // Ajouter la commande à la base de données
                CommandeService commandeService = new CommandeService();
                commandeService.addCommande(commande);
                System.out.println("✅ Commande ajoutée avec succès !");

                // Mettre à jour la quantité du produit
                ProduitService produitService = new ProduitService();
                produitService.updateQuantiteProduit(commande.getIdProduit(), commande.getQuantiteCommande());
                System.out.println("✅ Quantité du produit mise à jour !");


                // Envoyer un e-mail de confirmation
                String toEmail = "bellilmariem22@gmail.com";
                String orderDetails = "ID Commande: " + commande.getIdCommande() + "\n"
                        + "Produit: " + produit.getNomProduit() + "\n"
                        + "Quantité: " + commande.getQuantiteCommande() + "\n"
                        + "Prix unitaire: " + produit.getPrixProduit() + " TND\n"
                        + "TVA (7%): " + (produit.getPrixProduit() * commande.getQuantiteCommande() * 0.07) + " TND\n"
                        + "Frais de livraison: 7.00 TND\n"
                        + "Total TTC: " + (produit.getPrixProduit() * commande.getQuantiteCommande() * 1.07 + 7.0) + " TND";
                String paymentDetails = "Paiement effectué en ligne via Stripe.";

                EmailService.sendOrderConfirmationEmail(toEmail, orderDetails, paymentDetails);


                // Rediriger vers la page ConsulterCommandeClient
                redirigerVersConsulterCommandeClient();
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de l'ajout de la commande : " + e.getMessage());
                e.printStackTrace();
                afficherAlerte("Erreur", "❌ Erreur lors de l'ajout de la commande : " + e.getMessage());
            }
        });
    }

    // Méthode appelée lors du clic sur le bouton "Payer"
    @FXML
    private void confirmerPaiement() {
        // Exécuter le script JavaScript pour confirmer le paiement
        stripeWebView.getEngine().executeScript(
                "stripe.confirmCardPayment('" + clientSecret + "', {" +
                        "payment_method: {" +
                        "card: card," +
                        "billing_details: {" +
                        "name: document.getElementById('name-on-card').value" +
                        "}" +
                        "}" +
                        "}).then(function (result) {" +
                        "if (result.error) {" +
                        "console.error('Erreur de paiement :', result.error.message);" +
                        "alert('Erreur de paiement : ' + result.error.message);" +
                        "} else {" +
                        "if (result.paymentIntent.status === 'succeeded') {" +
                        "console.log('✅ Paiement réussi !');" +
                        "window.javaConnector.ajouterCommande();" + // Appel de la méthode Java
                        "} else {" +
                        "console.error('❌ Paiement non confirmé. Statut :', result.paymentIntent.status);" +
                        "alert('❌ Paiement non confirmé. Statut : ' + result.paymentIntent.status);" +
                        "}" +
                        "}" +
                        "});"
        );
    }

    // Rediriger vers la page ConsulterCommandeClient
    private void redirigerVersConsulterCommandeClient() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterCommandeClient.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Consulter Commande");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre de paiement
            Stage paiementStage = (Stage) stripeWebView.getScene().getWindow();
            paiementStage.close();
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la redirection : " + e.getMessage());
        }
    }

    // Afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
