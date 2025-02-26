module Gestion.event {
    // Modules requis
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop; // Pour iText (si utilisé)
    requires itextpdf; // Pour iTextPDF

    // Ouvrir les packages pour JavaFX
    opens Controller to javafx.fxml;
    opens Entities to javafx.base, javafx.fxml;

    // Exporter les packages
    exports Controller;
    exports Entities;
}