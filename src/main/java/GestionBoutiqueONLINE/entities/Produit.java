package GestionBoutiqueONLINE.entities;

public class Produit {


    private int idProduit;
    private String nomProduit;
    private String descriptionProduit;
    private int prixProduit;
    private int quantiteProduit;
    private String imageProduit;
    private String refProduit;

    public Produit() {}

    public Produit(String nomProduit, String descriptionProduit, int prixProduit, int quantiteProduit, String imageProduit) {
        this.nomProduit = nomProduit;
        this.descriptionProduit = descriptionProduit;
        this.prixProduit = prixProduit;
        this.quantiteProduit = quantiteProduit;
        this.imageProduit = imageProduit;
    }

    public Produit(String nomProduit, String descriptionProduit, int prixProduit, int quantiteProduit, String imageProduit, String refProduit) {
        this.nomProduit = nomProduit;
        this.descriptionProduit = descriptionProduit;
        this.prixProduit = prixProduit;
        this.quantiteProduit = quantiteProduit;
        this.imageProduit = imageProduit;
        this.refProduit = refProduit;
    }

    public Produit(int idProduit, String nomProduit, String descriptionProduit, int prixProduit, int quantiteProduit, String imageProduit, String refProduit) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.descriptionProduit = descriptionProduit;
        this.prixProduit = prixProduit;
        this.quantiteProduit = quantiteProduit;
        this.imageProduit = imageProduit;
        this.refProduit = refProduit;
    }

    public String getRefProduit() {
        return refProduit;
    }

    public void setRefProduit(String refProduit) {
        this.refProduit = refProduit;
    }

    public String getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(String imageProduit) {
        this.imageProduit = imageProduit;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public String getDescriptionProduit() {
        return descriptionProduit;
    }

    public void setDescriptionProduit(String descriptionProduit) {
        this.descriptionProduit = descriptionProduit;
    }

    public int getPrixProduit() {
        return prixProduit;
    }

    public void setPrixProduit(int prixProduit) {
        this.prixProduit = prixProduit;
    }

    public int getQuantiteProduit() {
        return quantiteProduit;
    }

    public void setQuantiteProduit(int quantiteProduit) {
        this.quantiteProduit = quantiteProduit;
    }
    public int getPrixCalcule() {
        return this.prixProduit * this.quantiteProduit;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "idProduit=" + idProduit +
                ", nomProduit='" + nomProduit + '\'' +
                ", descriptionProduit='" + descriptionProduit + '\'' +
                ", prixProduit=" + prixProduit +
                ", quantiteProduit=" + quantiteProduit +
                ", descriptionProduit='" + descriptionProduit +
                ", imageProduit='" + imageProduit + '\'' +
                ", refProduit='" + refProduit + '\'' +
                '}';
    }
}
