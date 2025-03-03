package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FactureClientController {

    @FXML
    private ImageView qrCodeImageView;

    private ProduitService produitService = new ProduitService();

    public void afficherFacture(Commande commande) {
        // Récupérer le produit associé à la commande
        Produit produit = produitService.getProduitById(commande.getIdProduit());

        // Calculer les totaux
        double totalHT = produit.getPrixProduit() * commande.getQuantiteCommande();
        double tva = totalHT * 0.07; // TVA de 7%
        double totalTTC = totalHT + tva + 7.0; // Frais de livraison de 7 TND

        // Générer les données du QR code
        String qrCodeData = "ID Commande: " + commande.getIdCommande() + "\n"
                + "Référence Produit: " + produit.getRefProduit() + "\n"
                + "Produit: " + produit.getNomProduit() + "\n"
                + "Quantité: " + commande.getQuantiteCommande() + "\n"
                + "Prix unitaire: " + produit.getPrixProduit() + " TND\n"
                + "TVA (7%): " + tva + " TND\n"
                + "Frais de livraison: 7.00 TND\n"
                + "Total TTC: " + totalTTC + " TND";

        // Générer et afficher le QR code
        Image qrCodeImage = generateQRCode(qrCodeData);
        if (qrCodeImage != null) {
            qrCodeImageView.setImage(qrCodeImage);
        } else {
            System.err.println("Erreur : Impossible de générer le QR code.");
        }
    }

    private Image generateQRCode(String qrCodeData) {
        try {
            // Encoder les données pour l'URL
            String encodedData = URLEncoder.encode(qrCodeData, StandardCharsets.UTF_8.toString());
            String qrCodeUrl = "https://api.qrcode-monkey.com/qr/custom?data=" + encodedData + "&size=200";

            // Télécharger l'image du QR code
            URL url = new URL(qrCodeUrl);
            InputStream inputStream = url.openStream();
            return new Image(inputStream);
        } catch (IOException e) {
            System.err.println("Erreur lors de la génération du QR code : " + e.getMessage());
            return null;
        }
    }
}
