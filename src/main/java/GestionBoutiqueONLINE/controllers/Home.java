package GestionBoutiqueONLINE.controllers;

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
    public void start(Stage stage)  {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ListeCommandesAdmin.fxml"));
            Scene scene = new Scene(root);//objet qui presente le fichier fxml
            stage.setScene(scene);//presenter cette scene
            //stage.setFullScreen(true);
            //stage.setMaximized(true);
            //stage.setTitle("A des produits");

            stage.setTitle("Liste des produits");
           // stage.setTitle("Liste des commandes");
            //stage.setTitle("Ajouter une commande");

            stage.show();//l'afficher ou la montrert


        } catch (IOException e) {

            System.out.println("Erreur de lecture"+e.getMessage());
            e.printStackTrace();

        }


    }
}
