package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.util.function.UnaryOperator;

public class UpdateProduitController {
    @FXML
    private TextField NomTextField;

    @FXML
    private TextField PrixTextField;

    @FXML
    private TextField QuantiteTextField;

    @FXML
    private TextField DescriptionTextField;

    @FXML
    private ImageView ImageViewProduit;

    private Produit produit;
    private String imagePath;

    // Méthode pour initialiser les restrictions de saisie
    @FXML
    public void initialize() {
        // Restreindre la saisie du prix et de la quantité à des chiffres uniquement
        restreindreSaisieChiffres(PrixTextField);
        restreindreSaisieChiffres(QuantiteTextField);
    }

    // Méthode pour restreindre la saisie à des chiffres uniquement
    private void restreindreSaisieChiffres(TextField textField) {
        UnaryOperator<TextFormatter.Change> filtre = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) { // Autoriser uniquement les chiffres
                return change;
            }
            return null; // Rejeter la saisie si ce n'est pas un chiffre
        };

        // Appliquer le filtre au TextField
        TextFormatter<Integer> textFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filtre);
        textField.setTextFormatter(textFormatter);
    }

    // Méthode pour valider le nom
    private boolean validerNom(String nom) {
        // Vérifie que le nom ne commence pas par un symbole et ne contient pas uniquement des symboles
        return nom.matches("^[a-zA-Z0-9][a-zA-Z0-9\\s]*$");
    }

    // Méthode pour charger les données du produit à mettre à jour
    public void setProduit(Produit produit) {
        this.produit = produit;
        chargerDonneesProduit();
    }

    private void chargerDonneesProduit() {
        NomTextField.setText(produit.getNomProduit());
        PrixTextField.setText(String.valueOf(produit.getPrixProduit()));
        QuantiteTextField.setText(String.valueOf(produit.getQuantiteProduit()));
        DescriptionTextField.setText(produit.getDescriptionProduit());
        if (produit.getImageProduit() != null) {
            ImageViewProduit.setImage(new Image(produit.getImageProduit()));
            imagePath = produit.getImageProduit(); // Conserver le chemin de l'image existante
        }
    }

    // Méthode pour ajouter une image
    @FXML
    void AjouterImageAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.toURI().toString();
            ImageViewProduit.setImage(new Image(imagePath));
        }
    }

    // Méthode pour mettre à jour un produit
    @FXML
    void UpdateAction() {
        String nom = NomTextField.getText();
        String description = DescriptionTextField.getText();
        String prixText = PrixTextField.getText();
        String quantiteText = QuantiteTextField.getText();

        // Validation du nom
        if (!validerNom(nom)) {
            afficherAlerte("Erreur de saisie", "❌ Le nom ne doit pas commencer par des symboles ni contenir uniquement des symboles.");
            return;
        }

        // Vérifier que les champs ne sont pas vides
        if (nom.isEmpty() || description.isEmpty() || prixText.isEmpty() || quantiteText.isEmpty()) {
            afficherAlerte("Erreur", "❌ Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier qu'une image a été sélectionnée
        if (imagePath == null || imagePath.isEmpty()) {
            afficherAlerte("Erreur", "❌ Veuillez ajouter une image pour le produit.");
            return;
        }

        // Vérifier l'unicité du produit (en excluant le produit actuel)
        ProduitService produitService = new ProduitService();
        if (produitService.produitExisteDeja(produit.getNomProduit(),produit.getIdProduit()))
        {
            afficherAlerte("Erreur", "❌ Un produit avec ce nom existe déjà.");
            return;
        }

        int prix = Integer.parseInt(prixText);
        int quantite = Integer.parseInt(quantiteText);

        // Mettre à jour les propriétés du produit
        produit.setNomProduit(nom);
        produit.setDescriptionProduit(description);
        produit.setPrixProduit(prix);
        produit.setQuantiteProduit(quantite);
        produit.setImageProduit(imagePath);

        // Mettre à jour le produit dans la base de données
        produitService.updateProduit(produit.getIdProduit(), produit);

        // Affichage d'un message de succès
        afficherAlerte("Succès", "✅ Produit mis à jour avec succès !");

        // Fermer la fenêtre de mise à jour
        NomTextField.getScene().getWindow().hide();
    }

    // Méthode pour afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
