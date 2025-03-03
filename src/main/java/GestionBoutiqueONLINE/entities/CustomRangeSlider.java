package GestionBoutiqueONLINE.entities;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomRangeSlider extends Application {

    @Override
    public void start(Stage primaryStage) {
        Slider minSlider = new Slider(0, 10000, 70);
        Slider maxSlider = new Slider(0, 10000, 80000);

        Label minLabel = new Label("Min: " + minSlider.getValue());
        Label maxLabel = new Label("Max: " + maxSlider.getValue());

        minSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > maxSlider.getValue()) {
                minSlider.setValue(maxSlider.getValue());
            }
            minLabel.setText("Min: " + String.format("%.0f DT", minSlider.getValue()));
        });

        maxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() < minSlider.getValue()) {
                maxSlider.setValue(minSlider.getValue());
            }
            maxLabel.setText("Max: " + String.format("%.0f DT", maxSlider.getValue()));
        });

        VBox root = new VBox(10, minLabel, minSlider, maxLabel, maxSlider);
        root.setPadding(new Insets(20));
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Custom Range Slider");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Appel correct de la méthode launch
    }
}
