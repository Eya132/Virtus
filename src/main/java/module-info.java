module gestion.match1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;
    requires java.mail;
    requires org.apache.poi.ooxml;
    requires java.desktop;
    requires activation;


    opens controller to javafx.fxml; // Ouvre le package controller à javafx.fxml
    opens Entites to javafx.base;   // Ouvre le package Entites à javafx.base
    exports controller;             // Exporte le package controller
}