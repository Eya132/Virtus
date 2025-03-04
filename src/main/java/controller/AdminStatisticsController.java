package controller;

import Entites.Match1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import services.MatchService;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdminStatisticsController implements Initializable {

    @FXML
    private PieChart mostUsedTerrainsChart; // Graphique des terrains les plus utilisés

    @FXML
    private PieChart mostPopularSportsChart; // Graphique des types de sport les plus populaires

    @FXML
    private BarChart<String, Number> mostActiveLocalisationsChart; // Graphique des localisations les plus actives

    private MatchService matchService = new MatchService();
    private List<Match1> allMatches; // Liste de tous les matchs

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger tous les matchs depuis la base de données
        allMatches = matchService.getAllMatches();

        // Afficher les statistiques
        showMostUsedTerrains();
        showMostPopularSports();
        showMostActiveLocalisations();
    }

    /**
     * Affiche les terrains les plus utilisés.
     */
    private void showMostUsedTerrains() {
        // Grouper les matchs par terrain et compter leur fréquence
        Map<String, Long> terrainUsage = allMatches.stream()
                .collect(Collectors.groupingBy(Match1::getTerrain, Collectors.counting()));

        // Créer une liste de données pour le PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        terrainUsage.forEach((terrain, count) ->
                pieChartData.add(new PieChart.Data(terrain + " (" + count + ")", count)));

        // Ajouter les données au PieChart
        mostUsedTerrainsChart.setData(pieChartData);
    }

    /**
     * Affiche les types de sport les plus populaires.
     */
    private void showMostPopularSports() {
        // Grouper les matchs par type de sport et compter leur fréquence
        Map<String, Long> sportPopularity = allMatches.stream()
                .collect(Collectors.groupingBy(Match1::getTypeSport, Collectors.counting()));

        // Créer une liste de données pour le PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        sportPopularity.forEach((sport, count) ->
                pieChartData.add(new PieChart.Data(sport + " (" + count + ")", count)));

        // Ajouter les données au PieChart
        mostPopularSportsChart.setData(pieChartData);
    }

    /**
     * Affiche les localisations les plus actives.
     */
    private void showMostActiveLocalisations() {
        // Grouper les matchs par localisation et compter leur fréquence
        Map<String, Long> localisationActivity = allMatches.stream()
                .collect(Collectors.groupingBy(Match1::getLocalisation, Collectors.counting()));

        // Créer une série de données pour le BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Matchs par localisation");

        // Ajouter les données au graphique
        localisationActivity.forEach((localisation, count) ->
                series.getData().add(new XYChart.Data<>(localisation, count)));

        // Ajouter la série au BarChart
        mostActiveLocalisationsChart.getData().add(series);
    }
}