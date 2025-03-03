package edu.pidev3a8.controllers;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class home  extends Application {


    public static void main(String[] args) {launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListUser" +
                    ".fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setMaximized(true);
            stage.show();
            System.out.println("test");
        } catch (IOException e) {
            System.out.println("Erreur de lecture : " + e.getMessage());
            e.printStackTrace(); // Afficher la stack trace complète
        }
    }
}
