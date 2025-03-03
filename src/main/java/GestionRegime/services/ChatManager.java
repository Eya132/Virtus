package GestionRegime.services;

import GestionRegime.controller.plus.ChatJoueurController;
import GestionRegime.controller.plus.ChatNutritionnisteController;

public class ChatManager {
    private ChatJoueurController joueurController;
    private ChatNutritionnisteController nutritionnisteController;

    public void setJoueurController(ChatJoueurController joueurController) {
        this.joueurController = joueurController;
    }

    public void setNutritionnisteController(ChatNutritionnisteController nutritionnisteController) {
        this.nutritionnisteController = nutritionnisteController;
    }

    public void sendMessageToNutritionist(String message) {
        if (nutritionnisteController != null) {
            nutritionnisteController.receiveMessage(message);
        }
    }

    public void sendMessageToPlayer(String message) {
        if (joueurController != null) {
            joueurController.receiveMessage(message);
        }
    }
}