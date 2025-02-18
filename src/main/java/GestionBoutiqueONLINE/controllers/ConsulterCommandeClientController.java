package GestionBoutiqueONLINE.controllers;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.CommandeService;
import GestionBoutiqueONLINE.services.ProduitService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;



import java.util.List;

public class ConsulterCommandeClientController {


    @FXML
    private TableView<Commande> tableCommandes;

    @FXML
    private TableColumn<Commande, String> colDateCommande;

    @FXML
    private TableColumn<Commande, String> colStatus;

    @FXML
    private TableColumn<Commande, Integer> colQuantite;

    @FXML
    private TableColumn<Commande, String> colNomProduit;

    @FXML
    private TableColumn<Commande, ImageView> colImage;

    @FXML
    private TableColumn<Commande, Void> colActions;

    private ProduitService produitService = new ProduitService();

    public void initialize() {
        // Initialisation des colonnes de la TableView
        colDateCommande.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateCommande().toString()));  // Afficher la date sous forme de chaîne

        colStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatusCommande().toString()));  // Afficher le statut sous forme de chaîne

        colQuantite.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantiteCommande()).asObject());  // Afficher la quantité

        // Colonne pour le nom du produit
        colNomProduit.setCellValueFactory(cellData -> {
            Produit produit = produitService.getProduitById(cellData.getValue().getIdProduit());
            return new SimpleStringProperty(produit.getNomProduit());
        });

        // Colonne pour l'image du produit
        colImage.setCellValueFactory(cellData -> {
            Produit produit = produitService.getProduitById(cellData.getValue().getIdProduit());
            ImageView imageView = new ImageView(new Image(produit.getImageProduit()));
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
            return new javafx.beans.property.SimpleObjectProperty<>(imageView);
        });

        // Colonne pour les actions (Modifier et Supprimer)
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Commande, Void> call(final TableColumn<Commande, Void> param) {
                return new TableCell<>() {
                    private final Button modifierButton = new Button("Modifier");
                    private final Button supprimerButton = new Button("Supprimer");

                    {
                        // Style des boutons
                        modifierButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        supprimerButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                        // Action pour le bouton Modifier
                        modifierButton.setOnAction(event -> {
                            Commande commande = getTableView().getItems().get(getIndex());
                            modifierCommande(commande);
                        });

                        // Action pour le bouton Supprimer
                        supprimerButton.setOnAction(event -> {
                            Commande commande = getTableView().getItems().get(getIndex());
                            supprimerCommande(commande);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new HBox(10, modifierButton, supprimerButton));
                        }
                    }
                };
            }
        });

        // Charger les commandes de l'utilisateur
        CommandeService commandeService = new CommandeService();
        List<Commande> commandes = commandeService.getCommandesByUserId(19);  // Remplacez "1" par l'ID dynamique de l'utilisateur connecté
        tableCommandes.getItems().setAll(commandes);  // Ajouter les commandes à la TableView
    }

    private void modifierCommande(Commande commande) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCommandeClient.fxml"));
            if(loader.getLocation()==null)
            {
                System.err.println("ASBAAAAAAA");
                return;
            }

            Parent root = loader.load();
            UpdateCommandeClientController updateController = loader.getController();
            if(updateController==null)
            {
                System.err.println("HEDHA CONTROLERR");
                    return;
            }
            updateController.setCommande(commande);


            Stage stage = new Stage();
            stage.setTitle("Modifier la Commande");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface de modification de commande : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void supprimerCommande(Commande commande) {
        // Logique pour supprimer la commande
        CommandeService commandeService = new CommandeService();
        commandeService.deleteCommande(commande.getIdCommande());
        tableCommandes.getItems().remove(commande);  // Retirer la commande de la TableView
        System.out.println("Commande supprimée : " + commande.getIdCommande());
    }






}

