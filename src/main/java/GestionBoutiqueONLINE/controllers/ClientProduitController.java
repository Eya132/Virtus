    package GestionBoutiqueONLINE.controllers;

    import GestionBoutiqueONLINE.entities.Produit;
    import GestionBoutiqueONLINE.services.ProduitService;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.collections.transformation.FilteredList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.layout.FlowPane;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;
    import javafx.scene.input.KeyCode;

    import org.controlsfx.control.RangeSlider;
    import javafx.scene.control.Label;

    import java.io.IOException;
    import java.util.List;
    import java.util.stream.Collectors;
    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.Scanner;
    import org.json.JSONObject;



    public class ClientProduitController {


        @FXML
        private FlowPane produitsContainer;

        @FXML
        private TextField searchField;


        @FXML
        private Label minPriceLabel;

        @FXML
        private Label maxPriceLabel;
        @FXML
        private RangeSlider priceRangeSlider; // Remplacez Slider par RangeSlider
        private String deviseActuelle = "TND"; // Par défaut, la devise est TND
        @FXML
        private Button voirPanierButton;


        @FXML
        private ListView<Produit> suggestionsList; // Assurez-vous que cet élément est bien défini dans le FXML

        private final ProduitService produitService = new ProduitService();
        private ObservableList<Produit> produitsList = FXCollections.observableArrayList();
        private ObservableList<Produit> panier = FXCollections.observableArrayList(); // Liste des produits dans le panier
        @FXML
        private Label panierCountLabel;

        @FXML
        public void initialize() {
            if (suggestionsList == null) {
                System.err.println("Erreur : suggestionsList est null. Vérifiez le fichier FXML.");
                return;
            }


            chargerProduits();
            setupSearchAndSuggestions();
            configurerSlider();

            panierCountLabel.setText("Panier (" + panier.size() + ")");


        }

        private void chargerProduits() {
            List<Produit> produits = produitService.getAllDataProduit();
            produitsList.setAll(produits);
            afficherProduits(produits);
        }



        private void configurerSlider() {
            priceRangeSlider.setMin(0);
            priceRangeSlider.setMax(1000);
            priceRangeSlider.setLowValue(0);
            priceRangeSlider.setHighValue(1000);

            priceRangeSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> {
                minPriceLabel.setText(String.format("%.0f TND", newVal));
                filtrerProduitsParPrix();
            });

            priceRangeSlider.highValueProperty().addListener((obs, oldVal, newVal) -> {
                maxPriceLabel.setText(String.format("%.0f TND", newVal));
                filtrerProduitsParPrix();
            });
        }
        @FXML
        private void convertirEnEUR(ActionEvent event) {
            convertirPrix("EUR");
        }

        @FXML
        private void convertirEnUSD(ActionEvent event) {
            convertirPrix("USD");
        }

        @FXML
        private void revenirEnTND(ActionEvent event) {
            deviseActuelle = "TND";
            afficherProduits(produitsList);
        }


        private void convertirPrix(String deviseCible) {
            try {
                double taux = getTauxDeConversion("TND", deviseCible);

                // Create a new list with updated prices
                List<Produit> produitsConvertis = produitsList.stream()
                        .map(p -> {
                            // Create a copy of the product to avoid modifying the original
                            Produit produitConverti = new Produit();
                            produitConverti.setIdProduit(p.getIdProduit());
                            produitConverti.setNomProduit(p.getNomProduit());
                            produitConverti.setDescriptionProduit(p.getDescriptionProduit());
                            produitConverti.setImageProduit(p.getImageProduit());
                            produitConverti.setQuantiteProduit(p.getQuantiteProduit());
                            produitConverti.setPrixProduit((int) Math.round(p.getPrixProduit() * taux)); // Round to avoid decimal issues
                            return produitConverti;
                        })
                        .collect(Collectors.toList());

                // Update the current currency
                deviseActuelle = deviseCible;

                // Display the converted products
                afficherProduits(produitsConvertis);
            } catch (Exception e) {
                System.err.println("Erreur lors de la conversion des prix : " + e.getMessage());
                afficherAlerte("Erreur", "Impossible de convertir les prix. Vérifiez votre connexion Internet ou réessayez plus tard.");
            }
        }

        private double getTauxDeConversion(String deviseSource, String deviseCible) throws Exception {
            // Validate input parameters
            if (deviseSource == null || deviseSource.isEmpty()) {
                throw new IllegalArgumentException("La devise source ne peut pas être nulle ou vide.");
            }
            if (deviseCible == null || deviseCible.isEmpty()) {
                throw new IllegalArgumentException("La devise cible ne peut pas être nulle ou vide.");
            }

            String apiKey = "49e0c7cfd3119ad1a0c634f1"; // Remplacez par votre clé API
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalArgumentException("La clé API ne peut pas être nulle ou vide.");
            }

            // Construct the URL
            String urlString = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + deviseSource;
            System.out.println("URL de l'API : " + urlString); // Debug the URL

            // Validate the URL
            if (urlString == null || urlString.isEmpty()) {
                throw new IllegalArgumentException("L'URL de l'API ne peut pas être nulle ou vide.");
            }

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try {
                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    throw new RuntimeException("Erreur HTTP : " + responseCode);
                }

                StringBuilder response = new StringBuilder();
                try (Scanner scanner = new Scanner(conn.getInputStream())) {
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                }

                System.out.println("Réponse de l'API : " + response);

                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("error")) {
                    throw new RuntimeException("API Error: " + jsonResponse.getString("error"));
                }

                if (!jsonResponse.has("conversion_rates")) {
                    throw new RuntimeException("Réponse API invalide : clé 'conversion_rates' manquante.");
                }

                JSONObject rates = jsonResponse.getJSONObject("conversion_rates");
                if (!rates.has(deviseCible)) {
                    throw new RuntimeException("Devise cible introuvable : " + deviseCible);
                }

                return rates.getDouble(deviseCible);

            } catch (Exception e) {
                System.err.println("Erreur lors de la récupération du taux : " + e.getMessage());
                e.printStackTrace();
                throw e;
            } finally {
                conn.disconnect();
            }
        }



        private void filtrerProduitsParPrix() {
            double minPrice = priceRangeSlider.getLowValue();
            double maxPrice = priceRangeSlider.getHighValue();

            List<Produit> filteredList = produitsList.stream()
                    .filter(p -> p.getPrixProduit() >= minPrice && p.getPrixProduit() <= maxPrice)
                    .collect(Collectors.toList());

            afficherProduits(filteredList);
        }

        private void afficherProduits(List<Produit> produits) {
            produitsContainer.getChildren().clear();
            List<Produit> produitsEpingles = produitService.getProduitsLesPlusCommandesEpingle(); // Récupérer les 4 produits épinglés

            for (Produit produit : produits) {
                VBox produitBox = creerProduitBox(produit, produitsEpingles); // Passer la liste des produits épinglés
                produitsContainer.getChildren().add(produitBox);
            }
        }
        private void ajouterAuPanier(Produit produit) {
            panier.add(produit); // Ajouter le produit au panier
            panierCountLabel.setText("Panier (" + panier.size() + ")"); // Mettre à jour le compteur
            afficherAlerte("Succès", "Le produit a été ajouté au panier.");
        }

        private VBox creerProduitBox(Produit produit, List<Produit> produitsEpingles) {
            VBox produitBox = new VBox(10);
            produitBox.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-padding: 10;");
            produitBox.setPrefWidth(200);
            produitBox.setPrefHeight(300);

            ImageView imageView = new ImageView(new Image(produit.getImageProduit()));
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);

            Label nomLabel = new Label(produit.getNomProduit());
            nomLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            // Afficher le prix dans la devise actuelle
            Label prixLabel = new Label(String.format("%d %s", produit.getPrixProduit(), deviseActuelle));

            Label descriptionLabel = new Label(produit.getDescriptionProduit());
            descriptionLabel.setWrapText(true);

            Label stockLabel = new Label(produit.getQuantiteProduit() > 0 ? "En stock" : "Rupture de stock");
            stockLabel.setStyle(produit.getQuantiteProduit() > 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;");

            // Bouton "Ajouter au panier"
            Button ajouterAuPanierButton = new Button();
            ajouterAuPanierButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

            // Charger l'icône depuis le répertoire des ressources
            ImageView iconePanier = new ImageView(new Image(getClass().getResourceAsStream("/icones/ajouterPanier.png")));
            iconePanier.setFitWidth(40);
            iconePanier.setFitHeight(40);
            ajouterAuPanierButton.setGraphic(iconePanier);

            ajouterAuPanierButton.setOnAction(event -> ajouterAuPanier(produit));

            if (estParmiLesPlusCommandes(produit, produitsEpingles)) {
                ImageView pinIcon = new ImageView(new Image("/icones/epingle.png"));
                pinIcon.setFitWidth(20);
                pinIcon.setFitHeight(20);
                produitBox.getChildren().add(pinIcon);
            }

            produitBox.getChildren().addAll(imageView, nomLabel, prixLabel, descriptionLabel, stockLabel, ajouterAuPanierButton);
            produitBox.setOnMouseClicked(event -> afficherDetailsProduit(produit));

            return produitBox;
        }
        @FXML
        private void afficherPanier() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Panier.fxml"));
                Parent root = loader.load();

                PanierController panierController = loader.getController();
                panierController.setPanier(panier); // Passer la liste du panier

                Stage stage = new Stage();
                stage.setTitle("Panier");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible d'afficher le panier.");
            }
        }
        /*
        @FXML
        private void handlePriceFilter() {
            String minPriceText = minPriceField.getText();
            String maxPriceText = maxPriceField.getText();

            if (minPriceText.isEmpty() && maxPriceText.isEmpty()) {
                // Si les champs de prix sont vides, afficher tous les produits
                afficherProduits(produitsList);
                return;
            }

            try {
                int minPrice = minPriceText.isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minPriceText);
                int maxPrice = maxPriceText.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPriceText);

                List<Produit> filteredList = produitsList.stream()
                        .filter(p -> p.getPrixProduit() >= minPrice && p.getPrixProduit() <= maxPrice)
                        .collect(Collectors.toList());

                afficherProduits(filteredList);
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer des valeurs valides pour le prix.");
            }
        }


         */
        @FXML
        private void handleCategoryFilter(ActionEvent event) {
            CheckBox selectedCheckBox = (CheckBox) event.getSource();
            boolean isSelected = selectedCheckBox.isSelected(); // Vérifie si la case est cochée ou décochée
            String category = selectedCheckBox.getUserData().toString();

            List<Produit> filteredList;
            if (!isSelected) {
                // Si la case est décochée, afficher tous les produits
                filteredList = produitsList;
            } else {
                // Sinon, appliquer le filtre
                switch (category) {
                    case "VETEMENTS":
                        filteredList = produitService.getProduitsByKeywords("vetement", "tenue","jupe","short","T-shirt","top");
                        break;
                    case "NUTRITION":
                        filteredList = produitService.getProduitsByKeywords("nutrition", "alimentaire", "protéines");
                        break;
                    case "SPORT":
                        filteredList = produitService.getProduitsByKeywords("sport", "raquette", "balle");
                        break;
                    default:
                        filteredList = produitsList; // Afficher tous les produits
                        break;
                }
            }

            afficherProduits(filteredList);
        }

        private void setupSearchAndSuggestions() {
            FilteredList<Produit> filteredList = new FilteredList<>(produitsList, p -> true);

            suggestionsList.setCellFactory(param -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                private final Label nomLabel = new Label();
                private final Label prixLabel = new Label();
                private final HBox hbox = new HBox(10, imageView, nomLabel, prixLabel);

                {
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    imageView.setPreserveRatio(true);

                    nomLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                    prixLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");

                    hbox.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 5;");
                }

                @Override
                protected void updateItem(Produit produit, boolean empty) {
                    super.updateItem(produit, empty);
                    if (empty || produit == null) {
                        setGraphic(null);
                    } else {
                        try {
                            Image image = new Image(produit.getImageProduit());
                            imageView.setImage(image);
                        } catch (Exception e) {
                            System.err.println("Erreur lors du chargement de l'image : " + produit.getImageProduit());
                            e.printStackTrace();
                        }

                        nomLabel.setText(produit.getNomProduit());
                        prixLabel.setText(String.format("%d TND", produit.getPrixProduit()));

                        setGraphic(hbox);
                    }
                }
            });

            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == null || newValue.isEmpty()) {
                    suggestionsList.setVisible(false);
                    afficherProduits(produitsList); // Recharger tous les produits
                    return;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                filteredList.setPredicate(produit ->
                        produit.getNomProduit().toLowerCase().contains(lowerCaseFilter) ||
                                produit.getDescriptionProduit().toLowerCase().contains(lowerCaseFilter)
                );

                suggestionsList.setItems(filteredList);
                suggestionsList.setVisible(true);

                afficherProduits(filteredList);
            });

            suggestionsList.setOnMouseClicked(event -> {
                Produit selectedProduit = suggestionsList.getSelectionModel().getSelectedItem();
                if (selectedProduit != null) {
                    searchField.setText(selectedProduit.getNomProduit());
                    suggestionsList.setVisible(false);
                    filterAndDisplayProducts(selectedProduit.getNomProduit());
                }
            });

            searchField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String searchText = searchField.getText();
                    suggestionsList.setVisible(false);
                    filterAndDisplayProducts(searchText);
                }
            });
        }

        private void filterAndDisplayProducts(String searchText) {
            produitsContainer.getChildren().clear();
            List<Produit> produitsEpingles = produitService.getProduitsLesPlusCommandesEpingle(); // Récupérer les 4 produits épinglés

            for (Produit produit : produitsList) {
                if (produit.getNomProduit().toLowerCase().contains(searchText.toLowerCase()) ||
                        produit.getDescriptionProduit().toLowerCase().contains(searchText.toLowerCase())) {
                    VBox produitBox = creerProduitBox(produit, produitsEpingles); // Passer la liste des produits épinglés
                    produitsContainer.getChildren().add(produitBox);
                }
            }
        }

        private void afficherDetailsProduit(Produit produit) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsProduitsClient.fxml"));
                Parent root = loader.load();

                DetailsProduitsClientController detailsController = loader.getController();

                // Passer le produit, la devise actuelle et le prix converti (si applicable)
                detailsController.afficherDetails(produit, deviseActuelle, produit.getPrixProduit());

                Stage stage = new Stage();
                stage.setTitle("Détails du Produit");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible d'afficher les détails du produit.");
            }
        }

        @FXML
        private void consulterCommandes() {
            System.out.println("Bouton 'Consulter mes commandes' cliqué");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterCommandeClient.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Mes Commandes");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                afficherAlerte("Erreur", "Impossible de charger la liste des commandes.");
            }
        }

        private void afficherAlerte(String titre, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titre);
            alert.setHeaderText(message);
            alert.showAndWait();
        }

        private boolean estParmiLesPlusCommandes(Produit produit, List<Produit> produitsEpingles) {
            return produitsEpingles.stream()
                    .anyMatch(p -> p.getIdProduit() == produit.getIdProduit());
        }

    }




