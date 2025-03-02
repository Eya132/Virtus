module Gestion.event {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop; // Pour iText (si utilisé)
    requires itextpdf;
    requires java.net.http; // Pour les requêtes HTTP
    requires com.google.gson;
    requires spring.web;
    requires jdk.jsobject; // Pour Gson
    requires javafx.web;
    opens Controller to javafx.fxml;
    opens Entities to javafx.base, javafx.fxml;

    exports Controller;
    exports Entities;
}