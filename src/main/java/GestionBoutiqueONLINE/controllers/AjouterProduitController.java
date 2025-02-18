package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;

import java.io.File;
import java.io.IOException;
import java.util.function.UnaryOperator;

public class AjouterProduitController {
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

    private String imagePath;

    // Méthode pour initialiser les restrictions de saisie
    @FXML
    public void initialize() {
        restreindreSaisieChiffres(PrixTextField);
        restreindreSaisieChiffres(QuantiteTextField);
    }

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

    // Méthode pour ajouter une image
    @FXML
    void AjouterImageAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.toURI().toString();
            ImageViewProduit.setImage(new Image(imagePath));
        }
    }

    // Méthode pour ajouter un produit
    @FXML
    void AjoutAction(ActionEvent event) {
        String nom = NomTextField.getText();
        String description = DescriptionTextField.getText();
        String prixText = PrixTextField.getText();
        String quantiteText = QuantiteTextField.getText();

        // Validation du nom
        if (!validerNom(nom)) {
            afficherAlerte("Erreur de saisie", "❌Le nom ne doit pas commencer par des symboles ni contenir uniquement des symboles.");
            return;
        }

        // Vérifier que les champs ne sont pas vides
        if (nom.isEmpty() || description.isEmpty() || prixText.isEmpty() || quantiteText.isEmpty()) {
            afficherAlerte("Erreur", "❌Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier qu'une image a été sélectionnée
        if (imagePath == null || imagePath.isEmpty()) {
            afficherAlerte("Erreur", " ❌ Veuillez ajouter une image pour le produit.");
            return;
        }

        int prix = Integer.parseInt(prixText);
        int quantite = Integer.parseInt(quantiteText);

        // Création du produit
        Produit produit = new Produit(nom, description, prix, quantite, imagePath);
        ProduitService produitService = new ProduitService();
        produitService.addProduit(produit);

        // Affichage d'un message de succès
        afficherAlerte("Succès", "Produit ajouté avec succès !");

        // Redirection vers la liste des produits
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeProduits.fxml"));
            Parent root = loader.load();

            // Récupérer le Stage actuel
            Stage stage = (Stage) NomTextField.getScene().getWindow();
            stage.setTitle("Liste des produits"); // Définir le titre de la fenêtre

            // Remplacer la scène actuelle
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("❌Erreur lors de la redirection : " + e.getMessage());
            afficherAlerte("Erreur", "❌Impossible de charger la liste des produits.");
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


