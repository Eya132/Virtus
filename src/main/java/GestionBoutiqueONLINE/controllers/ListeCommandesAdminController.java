    package GestionBoutiqueONLINE.controllers;


    import GestionBoutiqueONLINE.entities.Commande;
    import GestionBoutiqueONLINE.entities.Produit;
    import GestionBoutiqueONLINE.services.CommandeService;
    import GestionBoutiqueONLINE.services.ProduitService;
    import javafx.beans.property.SimpleObjectProperty;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.HBox;
    import javafx.stage.Stage;
    import javafx.util.Callback;

    import java.io.IOException;
    import java.util.List;

    public class ListeCommandesAdminController {
        @FXML
        private TableView<Commande> tableCommandes;

        @FXML
        private TableColumn<Commande, Integer> colIdUser;

        @FXML
        private TableColumn<Commande, Integer> colIdCommande;

        @FXML
        private TableColumn<Commande, String> colStatus;

        @FXML
        private TableColumn<Commande, Integer> colQuantite;

        @FXML
        private TableColumn<Commande, String> colNomProduit;

        @FXML
        private TableColumn<Commande, ImageView> colImageProduit;

        @FXML
        private TableColumn<Commande, Void> colActions;

        private final CommandeService commandeService = new CommandeService();
        private final ProduitService produitService = new ProduitService();


        @FXML
        public void initialize() {
            // Initialisation des colonnes
            colIdUser.setCellValueFactory(new PropertyValueFactory<>("idUser"));
            colIdCommande.setCellValueFactory(new PropertyValueFactory<>("idCommande"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("statusCommande"));
            colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantiteCommande"));
            colNomProduit.setCellValueFactory(cellData -> {
                Produit produit = produitService.getProduitById(cellData.getValue().getIdProduit());
                return new SimpleStringProperty(produit.getNomProduit());
            });
            colImageProduit.setCellValueFactory(cellData -> {
                Produit produit = produitService.getProduitById(cellData.getValue().getIdProduit());
                ImageView imageView = new ImageView(new Image(produit.getImageProduit()));
                imageView.setFitWidth(150);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                return new SimpleObjectProperty<>(imageView);
            });

            // Définir la fabrique de cellules pour la colonne "Actions"
            colActions.setCellFactory(new Callback<>() {
                @Override
                public TableCell<Commande, Void> call(TableColumn<Commande, Void> param) {
                    return new TableCell<>() {
                        //private final Button traiterButton = new Button("Traiter");
                        private final Button supprimerButton = new Button("Supprimer");

                        {
                            // Style des boutons
                            //traiterButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                            supprimerButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                            // Actions des boutons
                            /*traiterButton.setOnAction(event -> {
                                Commande commande = getTableView().getItems().get(getIndex());
                                traiterCommande(commande);
                            });*/

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
                                setGraphic(new HBox(10, supprimerButton));
                            }
                        }
                    };
                }
            });

            // Charger les commandes
            chargerCommandes();
        }

        private void chargerCommandes() {
            List<Commande> commandes = commandeService.getAllDataCommande();
            tableCommandes.getItems().setAll(commandes);
        }
/*
        private void traiterCommande(Commande commande) {
            try {
                // Charger le fichier FXML de l'interface de traitement de commande
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TraiterCommandeAdmin.fxml"));
                Parent root = loader.load();

                // Obtenir le contrôleur de l'interface de traitement de commande
                TraiterCommandeAdminController traiterController = loader.getController();
                traiterController.setCommande(commande); // Passer la commande sélectionnée au contrôleur

                // Créer une nouvelle scène et l'afficher dans une nouvelle fenêtre
                Stage stage = new Stage();
                stage.setTitle("Traiter la Commande");
                stage.setScene(new Scene(root));
                stage.setOnHidden(e -> chargerCommandes());

                stage.show();
            } catch (IOException e) {
                System.err.println("Erreur lors du chargement de l'interface de traitement de commande : " + e.getMessage());
                e.printStackTrace();
            }
        }*/

        private void supprimerCommande(Commande commande) {
            CommandeService commandeService = new CommandeService();
            commandeService.deleteCommande(commande.getIdCommande());
            tableCommandes.getItems().remove(commande);  // Retirer la commande de la TableView
            System.out.println("Commande supprimée : " + commande.getIdCommande());
            chargerCommandes();
    }

    }
