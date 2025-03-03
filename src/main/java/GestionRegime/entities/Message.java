package GestionRegime.entities;

public class Message {
    private String sender; // "Joueur" ou "Nutritionniste"
    private String content; // Contenu du message
    private String timestamp; // Horodatage du message

    public Message(String sender, String content, String timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters et Setters
    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}