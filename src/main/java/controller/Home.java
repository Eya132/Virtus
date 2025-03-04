package controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Chargement du fichier FXML pour la première page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            // Création de la scène avec le root
            Scene scene = new Scene(root);

            // Configuration de la scène et affichage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion de Matchs"); // Optionnel : Titre de la fenêtre
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page FXML.");
        }
    }
}
