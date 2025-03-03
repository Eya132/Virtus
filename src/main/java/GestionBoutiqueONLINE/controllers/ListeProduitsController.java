package GestionBoutiqueONLINE.controllers;


import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.services.ProduitService;
import GestionBoutiqueONLINE.tools.MyConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.FileChooser;


import java.io.FileOutputStream;

import javafx.scene.text.Text;


public class ListeProduitsController {


    @FXML
    private TableView<Produit> produitTable;

    @FXML
    private TableColumn<Produit, String> nomColumn;

    @FXML
    private TableColumn<Produit, Integer> prixColumn;

    @FXML
    private TableColumn<Produit, Integer> quantiteColumn;

    @FXML
    private TableColumn<Produit, String> imageColumn;

    @FXML
    private TableColumn<Produit, String> refColumn;
    @FXML
    private TextField searchField; // Champ de recherche

    @FXML
    private ListView<Produit> suggestionsList; // Liste des suggestions

    @FXML
    private TableColumn<Produit, Void> actionsColumn;


    private ProduitService produitService = new ProduitService();
    private ObservableList<Produit> produitsList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Configurer les colonnes du TableView
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prixProduit"));
        quantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteProduit"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageProduit"));
        refColumn.setCellValueFactory(new PropertyValueFactory<>("refProduit"));

        // Personnaliser la colonne "Image" pour afficher l'image
        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(imagePath);
                        imageView.setImage(image);
                        imageView.setFitWidth(150);
                        imageView.setFitHeight(150);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image : " + imagePath);
                        e.printStackTrace();
                        setGraphic(null);
                    }
                }
            }
        });

        // Ajouter les boutons d'actions (Modifier et Supprimer)
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style des boutons
                updateButton.setStyle("-fx-background-color: #10888D; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 80; -fx-min-height: 30;");
                deleteButton.setStyle("-fx-background-color: #F45E0C; -fx-text-fill: white; -fx-font-size: 14px; -fx-min-width: 80; -fx-min-height: 30;");

                // Action pour le bouton Modifier
                updateButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    ouvrirInterfaceUpdate(produit);
                });

                // Action pour le bouton Supprimer
                deleteButton.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    supprimerProduit(produit);
                });
                produitTable.setRowFactory(tv -> {
                    TableRow<Produit> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !row.isEmpty()) { // Double-clic
                            Produit produit = row.getItem();
                            afficherDetailsProduit(produit); // Afficher les détails du produit
                        }
                    });
                    return row;
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, updateButton, deleteButton));
                }
            }
        });

        // Charger les produits depuis la base de données
        loadProduits();

        // Configurer la recherche dynamique et les suggestions
        setupSearchAndSuggestions();

        // Personnaliser la ListView pour afficher l'image et le nom du produit
        suggestionsList.setCellFactory(param -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            private final Text nomProduit = new Text();

            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(Produit produit, boolean empty) {
                super.updateItem(produit, empty);
                if (empty || produit == null) {
                    setGraphic(null);
                } else {
                    // Charger l'image du produit
                    try {
                        Image image = new Image(produit.getImageProduit());
                        imageView.setImage(image);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image : " + produit.getImageProduit());
                        e.printStackTrace();
                    }

                    // Afficher le nom du produit
                    nomProduit.setText(produit.getNomProduit());

                    // Créer un conteneur pour l'image et le nom
                    HBox hbox = new HBox(10, imageView, nomProduit);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void loadProduits() {
        produitsList.setAll(produitService.getAllDataProduit()); // Charger tous les produits
        produitTable.setItems(produitsList); // Lier la liste à la TableView
    }

    private void setupSearchAndSuggestions() {
        // Créer une FilteredList pour filtrer les produits
        FilteredList<Produit> filteredList = new FilteredList<>(produitsList, p -> true);

        // Écouter les changements dans le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Si le champ de recherche est vide, afficher tous les produits
            if (newValue == null || newValue.isEmpty()) {
                suggestionsList.setVisible(false); // Masquer la liste des suggestions
                produitTable.setItems(produitsList); // Réafficher la liste initiale
                return;
            }

            // Convertir le texte de recherche en minuscules pour une recherche insensible à la casse
            String lowerCaseFilter = newValue.toLowerCase();

            // Appliquer le filtre
            filteredList.setPredicate(produit -> {
                // Vérifier si le nom du produit contient le texte de recherche
                if (produit.getNomProduit().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Vérifier si la référence du produit contient le texte de recherche
                if (produit.getRefProduit().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                // Si aucun critère n'est rempli, exclure le produit
                return false;
            });

            // Mettre à jour la ListView des suggestions
            suggestionsList.setItems(filteredList);
            suggestionsList.setVisible(true); // Afficher la liste des suggestions

            // Mettre à jour la TableView avec les résultats filtrés
            produitTable.setItems(filteredList);
        });

        // Gérer la sélection d'un produit dans la ListView
        suggestionsList.setOnMouseClicked(event -> {
            Produit selectedProduit = suggestionsList.getSelectionModel().getSelectedItem();
            if (selectedProduit != null) {
                searchField.setText(selectedProduit.getNomProduit()); // Mettre à jour le champ de recherche
                suggestionsList.setVisible(false); // Masquer la liste des suggestions
                filterAndDisplayProducts(selectedProduit.getNomProduit()); // Filtrer la TableView
            }
        });

        // Gérer l'appui sur Entrée dans le champ de recherche
        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String searchText = searchField.getText();
                suggestionsList.setVisible(false); // Masquer la liste des suggestions
                filterAndDisplayProducts(searchText); // Filtrer la TableView
            }
        });
    }

    private void filterAndDisplayProducts(String searchText) {
        FilteredList<Produit> filteredList = new FilteredList<>(produitsList, p -> true);
        filteredList.setPredicate(produit -> {
            if (searchText == null || searchText.isEmpty()) {
                return true; // Afficher tous les produits si le champ de recherche est vide
            }

            String lowerCaseFilter = searchText.toLowerCase();
            return produit.getNomProduit().toLowerCase().contains(lowerCaseFilter) ||
                    produit.getRefProduit().toLowerCase().contains(lowerCaseFilter);
        });

        // Lier la FilteredList à la TableView
        produitTable.setItems(filteredList);
    }
    @FXML
    private void ajouterProduitAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) produitTable.getScene().getWindow();
            stage.setTitle("Ajouter un produit");
            stage.setScene(scene);

            stage.setOnHidden(e -> loadProduits());
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface d'ajout de produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ouvrirInterfaceUpdate(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateProduit.fxml"));
            Parent root = loader.load();

            UpdateProduitController updateController = loader.getController();
            updateController.setProduit(produit);

            Stage stage = new Stage();
            stage.setTitle("Modifier le Produit");
            stage.setScene(new Scene(root));
            stage.show();
            stage.setOnHidden(e -> loadProduits());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerProduit(Produit produit) {
        produitService.deleteProduit(produit);
        loadProduits();
    }


    private void afficherDetailsProduit(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsProduit.fxml"));
            Parent root = loader.load();

            DetailsProduitController detailsController = loader.getController();
            detailsController.afficherDetails(produit); // Passer le produit au contrôleur des détails

            Stage stage = new Stage();
            stage.setTitle("Détails du Produit");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de l'interface des détails du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void afficherStatistiques() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiquesProduits.fxml"));
            Parent root = loader.load();

            // Passer les données au contrôleur des statistiques
            StatistiquesProduitsController statistiquesController = loader.getController();
            statistiquesController.setDonneesStatistiques(getProduitsLesPlusCommandes());

            Stage stage = new Stage();
            stage.setTitle("Statistiques des Produits les Plus Commandés");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible d'afficher les statistiques.");
        }
    }

    private Map<String, Integer> getProduitsLesPlusCommandes() {
        Map<String, Integer> produitsCommandes = new HashMap<>();
        try {
            String query = "SELECT p.nomProduit, SUM(c.quantiteCommande) as totalQuantite " +
                    "FROM commande c " +
                    "JOIN produit p ON c.idProduit = p.idProduit " +
                    "GROUP BY p.nomProduit " +
                    "ORDER BY totalQuantite DESC";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String nomProduit = rs.getString("nomProduit");
                int totalQuantite = rs.getInt("totalQuantite");
                produitsCommandes.put(nomProduit, totalQuantite);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits les plus commandés : " + e.getMessage());
        }
        return produitsCommandes;
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void exporterEnPDF() {
        // Créer un FileChooser pour permettre à l'utilisateur de choisir l'emplacement du fichier PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        java.io.File file = fileChooser.showSaveDialog(produitTable.getScene().getWindow());

        if (file != null) {
            try {
                // Créer un document PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                // Ajouter un titre au document
                document.add(new Paragraph("Liste des Produits"));

                // Créer une table PDF avec les colonnes nécessaires (sauf "Actions")
                PdfPTable pdfTable = new PdfPTable(5); // 5 colonnes : Référence, Nom, Prix, Quantité, Image
                pdfTable.setWidthPercentage(100);

                // Ajouter les en-têtes de colonnes
                pdfTable.addCell("Référence");
                pdfTable.addCell("Nom");
                pdfTable.addCell("Prix");
                pdfTable.addCell("Quantité");
                pdfTable.addCell("Image");

                // Ajouter les données de la TableView à la table PDF
                for (Produit produit : produitTable.getItems()) {
                    pdfTable.addCell(produit.getRefProduit());
                    pdfTable.addCell(produit.getNomProduit());
                    pdfTable.addCell(String.valueOf(produit.getPrixProduit()));
                    pdfTable.addCell(String.valueOf(produit.getQuantiteProduit()));

                    // Charger et ajouter l'image
                    try {
                        String imagePath = produit.getImageProduit();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            // Utiliser le nom complet de la classe Image d'iText
                            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(imagePath);
                            image.scaleToFit(50, 50); // Redimensionner l'image
                            pdfTable.addCell(image);
                        } else {
                            pdfTable.addCell("Pas d'image");
                        }
                    } catch (Exception e) {
                        pdfTable.addCell("Erreur de chargement de l'image");
                    }
                }

                // Ajouter la table au document
                document.add(pdfTable);

                // Fermer le document
                document.close();

                // Afficher une confirmation à l'utilisateur
                afficherAlerte("Succès", "Le fichier PDF a été exporté avec succès.");

                // Ouvrir le fichier PDF avec l'application par défaut
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                } else {
                    afficherAlerte("Erreur", "Impossible d'ouvrir le fichier PDF. L'action n'est pas supportée sur ce système.");
                }
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                afficherAlerte("Erreur", "Une erreur est survenue lors de l'exportation en PDF.");
            }
        }
    }
    }







