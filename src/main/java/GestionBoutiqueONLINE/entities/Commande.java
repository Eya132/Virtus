package GestionBoutiqueONLINE.entities;

import java.time.LocalDateTime;

public class Commande {
    public enum StatusCommande {
        EN_ATTENTE, VALIDÉE, ANNULÉE
    }

    public Commande(int idProduit, int quantiteCommande) {
        this.idProduit = idProduit;
        this.quantiteCommande = quantiteCommande;
    }

    private int idCommande;
    private LocalDateTime dateCommande;
    private int quantiteCommande;
    private int idProduit;
    private String idUser;  // Ajout de l'ID utilisateur sans relation avec User
    private StatusCommande statusCommande;
    public Commande() {}

    public Commande(LocalDateTime dateCommande, StatusCommande statusCommande) {
        this.dateCommande = dateCommande;
        this.statusCommande = statusCommande;
    }

    public Commande(int idCommande, LocalDateTime dateCommande, int quantiteCommande, int idProduit, String idUser, StatusCommande statusCommande) {
        this.idCommande = idCommande;
        this.dateCommande = dateCommande;
        this.quantiteCommande = quantiteCommande;
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.statusCommande = statusCommande;
    }

    public Commande(int quantiteCommande, int idProduit, String idUser, StatusCommande statusCommande) {
        this.quantiteCommande = quantiteCommande;
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.statusCommande = statusCommande;
    }

    public Commande(LocalDateTime dateCommande, int quantiteCommande, int idProduit, String idUser, StatusCommande statusCommande) {
        this.dateCommande = dateCommande;
        this.quantiteCommande = quantiteCommande;
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.statusCommande = statusCommande;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public int getQuantiteCommande() {
        return quantiteCommande;
    }

    public void setQuantiteCommande(int quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public StatusCommande getStatusCommande() {
        return statusCommande;
    }

    public void setStatusCommande(StatusCommande statusCommande) {
        this.statusCommande = statusCommande;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", dateCommande=" + dateCommande +
                ", quantiteCommande=" + quantiteCommande +
                ", idProduit=" + idProduit +
                ", idUser=" + idUser +
                ", statusCommande=" + statusCommande +
                '}';
    }
}
