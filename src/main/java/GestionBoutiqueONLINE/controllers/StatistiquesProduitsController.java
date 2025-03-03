package GestionBoutiqueONLINE.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Tooltip;
import javafx.application.Platform;

import javafx.scene.input.MouseEvent;

import java.text.DecimalFormat;
import java.util.Map;


import java.util.Map;
public class StatistiquesProduitsController {

    @FXML
    private PieChart produitsPieChart;

    public void setDonneesStatistiques(Map<String, Integer> produitsCommandes) {
        // Créer une liste de données pour le PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Calculer la quantité totale commandée
        int totalQuantite = produitsCommandes.values().stream().mapToInt(Integer::intValue).sum();

        // Ajouter les données au PieChart
        for (Map.Entry<String, Integer> entry : produitsCommandes.entrySet()) {
            String nomProduit = entry.getKey();
            int quantite = entry.getValue();
            pieChartData.add(new PieChart.Data(nomProduit, quantite));
        }

        // Afficher les données dans le PieChart
        produitsPieChart.setData(pieChartData);
        produitsPieChart.setTitle("Produits les Plus Commandés");

        // Ajouter des Tooltips pour afficher le pourcentage
        ajouterTooltips(pieChartData, totalQuantite);
    }

    private void ajouterTooltips(ObservableList<PieChart.Data> pieChartData, int totalQuantite) {
        DecimalFormat df = new DecimalFormat("#.##"); // Format pour afficher 2 décimales

        for (PieChart.Data data : pieChartData) {
            // Calculer le pourcentage
            double pourcentage = (data.getPieValue() / totalQuantite) * 100;
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
}
