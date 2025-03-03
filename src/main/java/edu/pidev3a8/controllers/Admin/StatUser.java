    package edu.pidev3a8.controllers.Admin;

    import com.itextpdf.text.*;
    import com.itextpdf.text.pdf.PdfContentByte;
    import edu.pidev3a8.tools.myConnection;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.chart.PieChart;
    import javafx.scene.control.Button;
    import javafx.scene.control.Tooltip;
    import javafx.scene.input.MouseEvent;

    import java.awt.*;
    import java.sql.*;
    import java.text.DecimalFormat;
    import java.util.HashMap;
    import java.util.Map;

    import com.itextpdf.text.pdf.PdfPTable;
    import com.itextpdf.text.pdf.PdfWriter;
    import javafx.fxml.FXML;
    import javafx.scene.control.Alert;
    import javafx.stage.FileChooser;
    import com.itextpdf.text.pdf.BaseFont;
    import com.itextpdf.text.pdf.PdfContentByte;
    import javafx.stage.Stage;

    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.util.Map;

    public class StatUser {
        @FXML
        private PieChart agePieChart;
        @FXML
        private Button btnExportPDF;
        @FXML
        private Button retour;


        public void initialize() {
            // Récupérer les données statistiques sur l'âge des utilisateurs
            Map<String, Integer> ageStatistics = getAgeStatistics();
           /* ageStatistics.put("12-17 ans", 5);
            ageStatistics.put("18-25 ans", 10);
            ageStatistics.put("26-35 ans", 8);
            ageStatistics.put("36-50 ans", 7);
            ageStatistics.put("50+ ans", 3);

            */

            // Afficher les données dans le PieChart
            setDonneesStatistiques(ageStatistics);
            btnExportPDF.setOnAction(event -> exporterEnPDF());

        }

        public void setDonneesStatistiques(Map<String, Integer> ageStatistics) {
            // Créer une liste de données pour le PieChart
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            // Calculer le nombre total d'utilisateurs
            int totalUtilisateurs = ageStatistics.values().stream().mapToInt(Integer::intValue).sum();

            // Ajouter les données au PieChart
            for (Map.Entry<String, Integer> entry : ageStatistics.entrySet()) {
                String trancheAge = entry.getKey();
                int nombreUtilisateurs = entry.getValue();
                pieChartData.add(new PieChart.Data(trancheAge, nombreUtilisateurs));

                // Afficher les données dans la console pour déboguer
                System.out.println("Ajout au PieChart : " + trancheAge + " - " + nombreUtilisateurs);
            }

            // Afficher les données dans le PieChart
            agePieChart.setData(pieChartData);
            agePieChart.setTitle("Répartition des Utilisateurs par Tranche d'Âge");

            // Ajouter des Tooltips pour afficher le pourcentage
            ajouterTooltips(pieChartData, totalUtilisateurs);
        }

        private void ajouterTooltips(ObservableList<PieChart.Data> pieChartData, int totalUtilisateurs) {
            DecimalFormat df = new DecimalFormat("#.##"); // Format pour afficher 2 décimales

            for (PieChart.Data data : pieChartData) {
                // Calculer le pourcentage
                double pourcentage = (data.getPieValue() / totalUtilisateurs) * 100;
                String tooltipText = data.getName() + " : " + df.format(pourcentage) + "%";

                // Créer un Tooltip avec le texte du pourcentage
                Tooltip tooltip = new Tooltip(tooltipText);

                // Gérer l'affichage du Tooltip lors du survol
                data.getNode().addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                    // Afficher le Tooltip
                    Tooltip.install(data.getNode(), tooltip);

                    // Optionnel : Ajouter un effet visuel lors du survol
                    data.getNode().setStyle("-fx-border-color: black; -fx-border-width: 2;");
                });

                data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                    // Supprimer le Tooltip
                    Tooltip.uninstall(data.getNode(), tooltip);

                    // Réinitialiser le style
                    data.getNode().setStyle("");
                });
            }
        }

        private Map<String, Integer> getAgeStatistics() {
            Map<String, Integer> ageStatistics = new HashMap<>();
            try {
                String query = "SELECT " +
                        "CASE " +
                        "    WHEN TIMESTAMPDIFF(YEAR, dateNaissance_user, CURDATE()) BETWEEN 12 AND 17 THEN '12-17 ans' " +
                        "    WHEN TIMESTAMPDIFF(YEAR, dateNaissance_user, CURDATE()) BETWEEN 18 AND 25 THEN '18-25 ans' " +
                        "    WHEN TIMESTAMPDIFF(YEAR, dateNaissance_user, CURDATE()) BETWEEN 26 AND 35 THEN '26-35 ans' " +
                        "    WHEN TIMESTAMPDIFF(YEAR, dateNaissance_user, CURDATE()) BETWEEN 36 AND 50 THEN '36-50 ans' " +
                        "    ELSE '50+ ans' " +
                        "END AS trancheAge, " +
                        "COUNT(*) AS nombreUtilisateurs " +
                        "FROM user " +
                        "GROUP BY trancheAge";

                Statement st = myConnection.getInstance().getCnx().createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    String trancheAge = rs.getString("trancheAge");
                    int nombreUtilisateurs = rs.getInt("nombreUtilisateurs");
                    ageStatistics.put(trancheAge, nombreUtilisateurs);

                    // Afficher les résultats dans la console pour déboguer
                    System.out.println("Tranche d'âge : " + trancheAge + ", Nombre d'utilisateurs : " + nombreUtilisateurs);
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors de la récupération des statistiques sur l'âge des utilisateurs : " + e.getMessage());
            }
            return ageStatistics;
        }

        @FXML
        private void exporterEnPDF() {
            // Créer un FileChooser pour permettre à l'utilisateur de choisir l'emplacement du fichier PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(agePieChart.getScene().getWindow());

            if (file != null) {
                try {
                    // Créer un document PDF
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                    document.open();

                    // Ajouter un titre au document
                    Paragraph title = new Paragraph("Statistiques sur l'âge des utilisateurs");
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(title);

                    // Ajouter un espace après le titre
                    document.add(new Paragraph("\n"));

                    // Créer une table PDF avec les colonnes nécessaires
                    PdfPTable pdfTable = new PdfPTable(2); // 2 colonnes : Tranche d'âge, Nombre d'utilisateurs
                    pdfTable.setWidthPercentage(100);

                    // Ajouter les en-têtes de colonnes
                    pdfTable.addCell("Tranche d'âge");
                    pdfTable.addCell("Nombre d'utilisateurs");

                    // Récupérer les données statistiques
                    Map<String, Integer> ageStatistics = getAgeStatistics();

                    // Ajouter les données de la PieChart à la table PDF
                    for (Map.Entry<String, Integer> entry : ageStatistics.entrySet()) {
                        pdfTable.addCell(entry.getKey()); // Tranche d'âge
                        pdfTable.addCell(String.valueOf(entry.getValue())); // Nombre d'utilisateurs
                    }

                    // Ajouter la table au document
                    document.add(pdfTable);

                    // Ajouter un espace après la table
                    document.add(new Paragraph("\n"));

                    // Dessiner le graphique en camembert
                    PdfContentByte canvas = writer.getDirectContent();
                    drawPieChart(canvas, ageStatistics, document);

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

        private void drawPieChart(PdfContentByte canvas, Map<String, Integer> ageStatistics, Document document) throws DocumentException {
            // Définir la position et la taille du graphique
            float x = document.left() + 50; // Ajustez la position horizontale
            float y = document.bottom() + 200; // Ajustez la position verticale
            float width = 300;
            float height = 300;

            // Calculer le total des utilisateurs
            int totalUtilisateurs = ageStatistics.values().stream().mapToInt(Integer::intValue).sum();

            // Dessiner le graphique en camembert
            float startAngle = 0;
            for (Map.Entry<String, Integer> entry : ageStatistics.entrySet()) {
                float angle = (entry.getValue() / (float) totalUtilisateurs) * 360;

                // Dessiner le segment du camembert
                canvas.setColorFill(getColorForTranche(entry.getKey()));
                canvas.arc(x, y, x + width, y + height, startAngle, angle);
                canvas.fillStroke();

                // Calculer le point central du segment pour dessiner une flèche
                float midAngle = startAngle + (angle / 2);
                float radius = width / 2;
                float labelX = x + radius + (float) (radius * 1.2 * Math.cos(Math.toRadians(midAngle)));
                float labelY = y + radius + (float) (radius * 1.2 * Math.sin(Math.toRadians(midAngle)));

                // Dessiner une ligne (flèche) du segment à l'étiquette
                canvas.moveTo(x + radius + (float) (radius * Math.cos(Math.toRadians(midAngle))),
                        y + radius + (float) (radius * Math.sin(Math.toRadians(midAngle))));
                canvas.lineTo(labelX, labelY);
                canvas.stroke();

                // Ajouter une étiquette avec le nom de la tranche d'âge
                try {
                    BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                    canvas.beginText();
                    canvas.setFontAndSize(baseFont, 12); // Use float for font size
                    canvas.showTextAligned(Element.ALIGN_LEFT, entry.getKey(), labelX + 5, labelY, 0);
                    canvas.endText();
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                    afficherAlerte("Erreur", "Une erreur est survenue lors de la création de la police.");
                }

                // Mettre à jour l'angle de départ pour le prochain segment
                startAngle += angle;
            }
        }

        private BaseColor getColorForTranche(String trancheAge) {
            // Associer une couleur à chaque tranche d'âge
            switch (trancheAge) {
                case "12-17 ans":
                    return BaseColor.RED;
                case "18-25 ans":
                    return BaseColor.BLUE;
                case "26-35 ans":
                    return BaseColor.GREEN;
                case "36-50 ans":
                    return BaseColor.ORANGE;
                case "50+ ans":
                    return BaseColor.PINK;
                default:
                    return BaseColor.GRAY;
            }
        }

        private void afficherAlerte(String titre, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titre);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        @FXML
        private void goBack() {
            try {
                System.out.println("Tentative de chargement de ListUser.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUser.fxml"));
                Parent root = loader.load();
                System.out.println("Fichier FXML chargé avec succès");

                Stage stage = new Stage();
                stage.setTitle("Liste des Utilisateurs");
                stage.setScene(new Scene(root));
                stage.setFullScreen(true);
                stage.setMaximized(true);

                // Fermer la fenêtre actuelle
                Stage currentStage = (Stage) retour.getScene().getWindow();
                currentStage.close();

                stage.show();
                System.out.println("Nouvelle fenêtre affichée");
            } catch (IOException e) {
                e.printStackTrace(); // Ajoutez cette ligne pour voir les erreurs
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors du chargement de la page");
                alert.setContentText("Impossible de charger la liste des utilisateurs.");
                alert.showAndWait();
            }
        }

    }
