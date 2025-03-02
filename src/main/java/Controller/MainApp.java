package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        //DashboardCard
        List<String> suggestions = LieuController.getLieux("M");
        System.out.println("Suggestions pour 'M' : " + suggestions);
        // Configurer la scène
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestion des événements");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
