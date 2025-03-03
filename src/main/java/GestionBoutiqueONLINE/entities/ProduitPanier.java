package GestionBoutiqueONLINE.entities;

public class ProduitPanier {

    private Produit produit;
    private int quantiteCommande;

    public ProduitPanier(Produit produit, int quantiteCommande) {
        this.produit = produit;
        this.quantiteCommande = quantiteCommande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantiteCommande() {
        return quantiteCommande;
    }

    public void setQuantiteCommande(int quantiteCommande) {
        this.quantiteCommande = quantiteCommande;
    }
}
