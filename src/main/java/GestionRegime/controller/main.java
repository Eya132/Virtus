package GestionRegime.controller;

import GestionRegime.controller.plus.ChatJoueurController;
import GestionRegime.controller.plus.ChatNutritionnisteController;
import GestionRegime.services.ChatManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    private ChatManager chatManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Instancier le ChatManager
        chatManager = new ChatManager();

        // Dans la méthode start() de MainApp
        FXMLLoader joueurLoader = new FXMLLoader(getClass().getResource("/ChatJoueur.fxml"));
        Parent joueurRoot = joueurLoader.load();
        ChatJoueurController joueurController = joueurLoader.getController();
        joueurController.setChatManager(chatManager); // Injecter le ChatManager
        chatManager.setJoueurController(joueurController);

        FXMLLoader nutritionnisteLoader = new FXMLLoader(getClass().getResource("/ChatNutritionniste.fxml"));
        Parent nutritionnisteRoot = nutritionnisteLoader.load();
        ChatNutritionnisteController nutritionnisteController = nutritionnisteLoader.getController();
        nutritionnisteController.setChatManager(chatManager); // Injecter le ChatManager
        chatManager.setNutritionnisteController(nutritionnisteController);


        // Afficher les interfaces
        Stage joueurStage = new Stage();
        joueurStage.setScene(new Scene(joueurRoot));
        joueurStage.setTitle("Chat du Joueur");
        joueurStage.show();

        Stage nutritionnisteStage = new Stage();
        nutritionnisteStage.setScene(new Scene(nutritionnisteRoot));
        nutritionnisteStage.setTitle("Chat du Nutritionniste");
        nutritionnisteStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}