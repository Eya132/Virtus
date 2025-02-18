package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class UpdateCommandeClientController {

    @FXML
    private TextField QuantiteTextField;

    @FXML
    private ImageView ImageViewProduit;

    @FXML
    private TextField NomProduitTextField;

    @FXML
    private TextField PrixProduitTextField;

    @FXML
    private TextField DescriptionProduitTextField;
    @FXML
    //private Produit produit;

    private Commande commande;
    private final ProduitService produitService = new ProduitService();
    private final CommandeService commandeService = new CommandeService();

    @FXML
    public void initialize() {
        restreindreSaisieChiffres(QuantiteTextField);

        // Désactiver la modification des champs non modifiables
        NomProduitTextField.setEditable(false);
        PrixProduitTextField.setEditable(false);
        DescriptionProduitTextField.setEditable(false);
    }

    private void restreindreSaisieChiffres(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtre = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, filtre));
    }

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
    }

    @FXML
    void UpdateAction() {
        String quantiteText = QuantiteTextField.getText();
        Produit produit = produitService.getProduitById(commande.getIdProduit());
        if (quantiteText.isEmpty()) {
            afficherAlerte("Erreur", "❌ Veuillez saisir une quantité.");
            return;
        }
        int quantite = Integer.parseInt(quantiteText);
        if (quantite <= 0) {
            afficherAlerte("Erreur", "❌ La quantité doit être supérieure à zéro.");
            return;
        }
        else if (quantite > produit.getQuantiteProduit()) {
            afficherAlerte("Erreur", "❌ Vous ne pouvez pas commander plus que " + produit.getQuantiteProduit());}
        else{commande.setQuantiteCommande(quantite);
        commandeService.updateCommande(commande.getIdCommande(), commande);
        afficherAlerte("Succès", "✅ Commande mise à jour avec succès !");
        QuantiteTextField.getScene().getWindow().hide();}
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void annulerAction() {
        QuantiteTextField.getScene().getWindow().hide();
    }
}
