package GestionBoutiqueONLINE.services;

import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.interfaces.IProduit;
import GestionBoutiqueONLINE.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IProduit<Produit> {
    @Override
    public void addProduit(Produit produit) {
        String requete = "INSERT INTO produit (nomProduit, descriptionProduit, prixProduit, quantiteProduit,imageProduit) VALUES (?, ?, ?, ?,?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, produit.getNomProduit());
            pst.setString(2, produit.getDescriptionProduit());
            pst.setInt(3, produit.getPrixProduit());
            pst.setInt(4, produit.getQuantiteProduit());
            pst.setString(5, produit.getImageProduit());
            pst.executeUpdate();
            System.out.println(" ✅Produit ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(" ❌Erreur lors de l'ajout du produit : " + e.getMessage());
        }

    }

    @Override
    public void deleteProduit( Produit produit) {
        String requete = "DELETE FROM produit WHERE idProduit = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, produit.getIdProduit());
            pst.executeUpdate();
            System.out.println("✅ Produit supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println(" ❌Erreur lors de la suppression du produit : " + e.getMessage());
        }

    }

    @Override
    public void updateProduit(int id, Produit produit) {
        String requete = "UPDATE produit SET nomProduit=?, descriptionProduit=?, prixProduit=?, quantiteProduit=?,imageProduit=? WHERE idProduit=?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, produit.getNomProduit());
            pst.setString(2, produit.getDescriptionProduit());
            pst.setInt(3, produit.getPrixProduit());
            pst.setInt(4, produit.getQuantiteProduit());
            pst.setString(5, produit.getImageProduit());
            pst.setInt(6, id);
            int test=pst.executeUpdate();
            if (test>0) {
                System.out.println(" ✅Produit mis à jour avec succès !");
            }
            else{
                System.out.println(" ❌PROBLEEMEE  !");

            }


        } catch (SQLException e) {
            System.out.println(" ❌Erreur lors de la mise à jour du produit : " + e.getMessage());
        }

    }

    @Override
    public List<Produit> getAllDataProduit() {

        List<Produit> result = new ArrayList<>();
        String requete = "SELECT * FROM produit";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Produit p = new Produit();
                p.setIdProduit(rs.getInt("idProduit"));
                p.setNomProduit(rs.getString("nomProduit"));
                p.setDescriptionProduit(rs.getString("descriptionProduit"));
                p.setPrixProduit(rs.getInt("prixProduit"));
                p.setQuantiteProduit(rs.getInt("quantiteProduit"));
                p.setImageProduit(rs.getString("imageProduit"));
                result.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des produits : " + e.getMessage());
        }
        return result;
    }

    public Produit getProduitById(int idProduit) {
        String requete = "SELECT * FROM produit WHERE idProduit = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, idProduit);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Produit produit = new Produit();
                produit.setIdProduit(rs.getInt("idProduit"));
                produit.setNomProduit(rs.getString("nomProduit"));
                produit.setDescriptionProduit(rs.getString("descriptionProduit"));
                produit.setPrixProduit(rs.getInt("prixProduit"));
                produit.setQuantiteProduit(rs.getInt("quantiteProduit"));
                produit.setImageProduit(rs.getString("imageProduit"));
                return produit;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du produit : " + e.getMessage());
        }
        return null;
    }

}

