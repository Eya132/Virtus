package Controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;

public class MainNavigationController {
    @FXML
    private Parent adminView;
    @FXML private Parent cardView;

    @FXML
    private void showAdminView() {
        adminView.setVisible(true);
        cardView.setVisible(false);
    }

    @FXML
    private void showCardView() {
        adminView.setVisible(false);
        cardView.setVisible(true);
    }
}
