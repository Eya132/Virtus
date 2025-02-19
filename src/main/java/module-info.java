module Gestion.match {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Ouvrir les packages nécessaires à JavaFX
    opens gestion_match.entites to javafx.base, javafx.fxml;
    opens gestion_match.controller to javafx.fxml;

    // Exporter les packages nécessaires
    exports gestion_match.controller;
    exports gestion_match.entites;
}