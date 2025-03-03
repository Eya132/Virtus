package GestionRegime.controller.plus;

import GestionRegime.services.ChatManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatNutritionnisteController {
    @FXML
    private ListView<String> chatArea;
    @FXML
    private TextField inputField;

    private ChatManager chatManager;

    // Méthode pour injecter le ChatManager
    public void setChatManager(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @FXML
    private void sendMessage() {
        String messageText = inputField.getText();
        if (!messageText.isEmpty()) {
            // Ajouter le message à la conversation
            chatArea.getItems().add("Nutritionniste: " + messageText);
            inputField.clear();

            // Envoyer le message au joueur via le ChatManager
            if (chatManager != null) {
                chatManager.sendMessageToPlayer(messageText);
            }
        }
    }

    public void receiveMessage(String message) {
        // Ajouter le message reçu à la conversation
        chatArea.getItems().add("Joueur: " + message);
    }
}