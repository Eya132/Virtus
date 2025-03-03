package GestionRegime.controller.Regime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.Map;

public class StatistiquesRegimeController {

    @FXML
    private PieChart statistiquesPieChart;

    public void setDonneesStatistiques(Map<String, Integer> statistiques) {
        // Créer une liste de données pour le PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Ajouter les données au PieChart
        for (Map.Entry<String, Integer> entry : statistiques.entrySet()) {
            String objectif = entry.getKey();
            int total = entry.getValue();
            pieChartData.add(new PieChart.Data(objectif + " (" + total + ")", total));
        }

        // Afficher les données dans le PieChart
        statistiquesPieChart.setData(pieChartData);
        statistiquesPieChart.setTitle("Statistiques par Objectif");
    }
}