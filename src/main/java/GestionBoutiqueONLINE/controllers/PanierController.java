package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class PanierController {


    @FXML
    private TableView<Produit> panierTableView;

    @FXML
    private TableColumn<Produit, String> imageColumn;

    @FXML
    private TableColumn<Produit, String> nomColumn;

    @FXML
    private TableColumn<Produit, String> descriptionColumn;

    @FXML
    private TableColumn<Produit, Integer> prixColumn;

    @FXML
    private TableColumn<Produit, Void> supprimerColumn;

    @FXML
    private TableColumn<Produit, Void> commanderColumn;

    private ObservableList<Produit> panier;

    private static final int ID_USER_STATIQUE = 1; // ID utilisateur statique

    @FXML
    public void initialize() {
        // Configurer les colonnes
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageProduit"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionProduit"));
        prixColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPrixProduit()).asObject());

        // Configurer la cellule personnalisée pour l'image
        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(imageUrl);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image : " + imageUrl);
                        setGraphic(null);
                    }
                }
            }
        });

        // Configurer la cellule personnalisée pour le bouton "Supprimer"
        supprimerColumn.setCellFactory(column -> new TableCell<>() {
            private final Button supprimerButton = new Button("Supprimer");

            {
                supprimerButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());

                    // Afficher une alerte de confirmation
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmer la suppression");
                    alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce produit du panier ?");
                    alert.setContentText(produit.getNomProduit());

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        panier.remove(produit); // Supprimer le produit du panier
                        panierTableView.refresh(); // Rafraîchir la TableView
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(supprimerButton);
                }
            }
        });

        // Configurer la cellule personnalisée pour le bouton "Commander"
        commanderColumn.setCellFactory(column -> new TableCell<>() {
            private final Button commanderButton = new Button("Commander");

            {
                commanderButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    ouvrirAjouterCommandeClient(produit); // Rediriger vers AjouterCommandeClient
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(commanderButton);
                }
            }
        });
    }

    /**
     * Définit la liste des produits dans le panier.
     *
     * @param panier La liste des produits dans le panier.
     */
    public void setPanier(ObservableList<Produit> panier) {
        this.panier = panier;
        panierTableView.setItems(panier); // Afficher les produits du panier
    }

    /**
     * Ouvre la page AjouterCommandeClient pour un produit spécifique.
     *
     * @param produit Le produit à commander.
     */
    private void ouvrirAjouterCommandeClient(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCommandeClient.fxml"));
            Parent root = loader.load();

            // Passer le produit au contrôleur AjouterCommandeClient
            AjouterCommandeClientController ajouterCommandeClientController = loader.getController();
            ajouterCommandeClientController.setProduit(produit);

            // Afficher la nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Commander " + produit.getNomProduit());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir la page de commande : " + e.getMessage());
        }
    }

    /**
     * Affiche une alerte avec un message d'erreur.
     *
     * @param titre   Le titre de l'alerte.
     * @param message Le message à afficher.
     */
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
/*
    @FXML
    private void commander() {
        for (Produit produit : panier) {
            // Créer une nouvelle commande avec une quantité par défaut (par exemple, 1)
            Commande commande = new Commande(
                    LocalDateTime.now(), // Date actuelle
                    1, // Quantité par défaut
                    produit.getIdProduit(),
                    ID_USER_STATIQUE, // ID utilisateur statique
                    Commande.StatusCommande.VALIDÉE // Statut par défaut
            );

            // Ouvrir la popup de paiement
            ouvrirPopupPaiement(commande, produit);
        }
    }


 */
    /**
     * Ouvre une popup de paiement pour une commande spécifique.
     *
     * @param commande La commande à payer.
     * @param produit  Le produit associé à la commande.
     */
    private void ouvrirPopupPaiement(Commande commande, Produit produit) {
        try {
            // Charger la vue de la popup de paiement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupPaiement.fxml"));
            Parent root = loader.load();

            // Passer la commande et le produit au contrôleur de la popup
            PopupPaiementController popupController = loader.getController();
            popupController.setCommande(commande);
            popupController.setProduit(produit);

            // Afficher la popup
            Stage stage = new Stage();
            stage.setTitle("Paiement");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'ouvrir la popup de paiement : " + e.getMessage());
        }
    }
}