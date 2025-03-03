package edu.pidev3a8.controllers.Admin;

import edu.pidev3a8.entities.User;
import edu.pidev3a8.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ValidationNut implements Initializable {

    @FXML
    private ListView<User> validationListView;

    private final UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPendingNutritionists();
        setupListView();
    }

    private void loadPendingNutritionists() {
        List<User> pendingNutritionists = userService.getAllNutritionists();
        System.out.println("Nutritionnistes en attente : " + pendingNutritionists);
        validationListView.getItems().addAll(pendingNutritionists);
    }

    private void setupListView() {
        validationListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    VBox vbox = new VBox(5);

                    Text emailText = new Text(user.getEmailUser());
                    emailText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                    Text statusText = new Text(user.getStatus().toString());
                    statusText.setStyle("-fx-font-size: 12px; -fx-fill: #666;");

                    ImageView statusIcon = new ImageView();
                    statusIcon.setFitHeight(20);
                    statusIcon.setFitWidth(20);

                    if (user.getStatus().toString().equals("VALIDATED")) {
                        statusIcon.setImage(new Image(getClass().getResourceAsStream("/icones/green_check.png")));
                    } else {
                        statusIcon.setImage(new Image(getClass().getResourceAsStream("/icones/orange_pending.png")));
                    }

                    vbox.getChildren().addAll(emailText, statusText);
                    hbox.getChildren().addAll(statusIcon, vbox);
                    setGraphic(hbox);
                }
            }
        });
    }

}