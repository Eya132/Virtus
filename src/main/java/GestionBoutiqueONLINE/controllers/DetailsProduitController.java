package GestionBoutiqueONLINE.controllers;


import GestionBoutiqueONLINE.entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DetailsProduitController {
    @FXML
    private Label NomLabel;

    @FXML
    private Label PrixLabel;

    @FXML
    private Label QuantiteLabel;

    @FXML
    private Label DescriptionLabel;

    @FXML
    private ImageView ImageViewProduit;

    public void afficherDetails(Produit produit) {
        NomLabel.setText(produit.getNomProduit());
        PrixLabel.setText(String.valueOf(produit.getPrixProduit()) + " TND");
        QuantiteLabel.setText("Stock: " + produit.getQuantiteProduit());
        DescriptionLabel.setText(produit.getDescriptionProduit());
        if (produit.getImageProduit() != null) {
            ImageViewProduit.setImage(new Image(produit.getImageProduit()));
        }
    }
}
