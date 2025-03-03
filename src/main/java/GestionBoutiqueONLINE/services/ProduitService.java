package GestionBoutiqueONLINE.services;

import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.interfaces.IProduit;
import GestionBoutiqueONLINE.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProduitService implements IProduit<Produit> {



    public String generateRefProduit(String nomProduit) {
        // Extraire les 3 premières lettres du nom du produit
        String troisPremieresLettres = nomProduit.substring(0, Math.min(3, nomProduit.length())).toUpperCase();

        // Générer deux nombres aléatoires de 4 chiffres
        int nombreAleatoire1 = (int) (Math.random() * 9000) + 1000; // Nombre entre 1000 et 9999
        int nombreAleatoire2 = (int) (Math.random() * 9000) + 1000; // Nombre entre 1000 et 9999

        // Combiner pour former la référence au format ####-XXX-####
        return String.format("%04d-%s-%04d", nombreAleatoire1, troisPremieresLettres, nombreAleatoire2);
    }


    @Override
    public void addProduit(Produit produit) {
        String requete = "INSERT INTO produit (nomProduit, descriptionProduit, prixProduit, quantiteProduit,imageProduit,refProduit) VALUES (?, ?, ?, ?,?,?)";

            String refProduit = generateRefProduit(produit.getNomProduit());

            if (produitExisteDeja(refProduit)) {
                System.out.println("❌ Un produit avec la référence " + refProduit + " existe déjà.");
                return; // Ne pas ajouter le produit si la référence existe déjà
            }
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, produit.getNomProduit());
            pst.setString(2, produit.getDescriptionProduit());
            pst.setInt(3, produit.getPrixProduit());
            pst.setInt(4, produit.getQuantiteProduit());
            pst.setString(5, produit.getImageProduit());
            pst.setString(6,refProduit);
            pst.executeUpdate();
            System.out.println(" ✅Produit ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(" ❌Erreur lors de l'ajout du produit : " + e.getMessage());
        }

    }

    @Override
    public void deleteProduit(Produit produit) {
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
        String requete = "UPDATE produit SET nomProduit=?, descriptionProduit=?, prixProduit=?, quantiteProduit=?,imageProduit=? ,refProduit=? WHERE idProduit=?";

            String refProduit = generateRefProduit(produit.getNomProduit());

            if (produitExisteDeja(refProduit, id)) {
                System.out.println("❌ Un produit avec la référence " + refProduit + " existe déjà.");
                return; // Ne pas mettre à jour le produit si la référence existe déjà
            }
        try {

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, produit.getNomProduit());
            pst.setString(2, produit.getDescriptionProduit());
            pst.setInt(3, produit.getPrixProduit());
            pst.setInt(4, produit.getQuantiteProduit());
            pst.setString(5, produit.getImageProduit());
            pst.setString(6, refProduit);
            pst.setInt(7, id);
            int test = pst.executeUpdate();
            if (test > 0) {
                System.out.println(" ✅Produit mis à jour avec succès !");
            } else {
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
                p.setRefProduit(rs.getString("refProduit"));
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
                produit.setRefProduit(rs.getString("refProduit"));
                return produit;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération du produit : " + e.getMessage());
        }
        return null;
    }

    public boolean produitExisteDeja(String refProduit) {
        String requete = "SELECT COUNT(*) FROM produit WHERE refProduit = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, refProduit);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retourne true si un produit avec la même référence existe déjà
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification de l'unicité du produit par référence : " + e.getMessage());
        }
        return false;
    }

    public boolean produitExisteDeja(String refProduit, int idProduitActuel) {
        String requete = "SELECT COUNT(*) FROM produit WHERE refProduit = ? AND idProduit != ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, refProduit);
            pst.setInt(2, idProduitActuel);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retourne true si un produit avec la même référence existe déjà (en excluant le produit actuel)
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification de l'unicité du produit par référence : " + e.getMessage());
        }
        return false;
    }

    public void updateQuantiteProduit(int idProduit, int quantiteCommandee) {
        String requete = "UPDATE produit SET quantiteProduit = quantiteProduit - ? WHERE idProduit = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, quantiteCommandee);
            pst.setInt(2, idProduit);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de la quantité du produit : " + e.getMessage());
        }
    }



    public List<Produit> getProduitsLesPlusCommandes() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT p.*, SUM(c.quantiteCommande) as total " +
                "FROM produit p LEFT JOIN commande c " +
                "ON p.idProduit = c.idProduit " +
                "GROUP BY p.idProduit " +
                "ORDER BY total DESC";

        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                produits.add(mapResultSetToProduit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return produits;
    }

    // Récupérer les produits par mots-clés (catégorie)
    public List<Produit> getProduitsByKeywords(String... keywords) {
        List<Produit> produits = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM produit WHERE ");

        for (int i = 0; i < keywords.length; i++) {
            query.append("descriptionProduit LIKE ?");
            if (i < keywords.length - 1) query.append(" OR ");
        }

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query.toString())) {
            for (int i = 0; i < keywords.length; i++) {
                pst.setString(i + 1, "%" + keywords[i] + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                produits.add(mapResultSetToProduit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return produits;
    }

    // Mapper un ResultSet à un objet Produit
    private Produit mapResultSetToProduit(ResultSet rs) throws SQLException {
        Produit p = new Produit();
        p.setIdProduit(rs.getInt("idProduit"));
        p.setNomProduit(rs.getString("nomProduit"));
        p.setDescriptionProduit(rs.getString("descriptionProduit"));
        p.setPrixProduit(rs.getInt("prixProduit"));
        p.setQuantiteProduit(rs.getInt("quantiteProduit"));
        p.setImageProduit(rs.getString("imageProduit"));
        p.setRefProduit(rs.getString("refProduit"));
        return p;
    }

    public List<Produit> getProduitsLesPlusCommandesEpingle() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT p.*, SUM(c.quantiteCommande) as total " +
                "FROM produit p LEFT JOIN commande c " +
                "ON p.idProduit = c.idProduit " +
                "WHERE c.quantiteCommande IS NOT NULL " + // Exclure les produits jamais commandés
                "GROUP BY p.idProduit " +
                "ORDER BY total DESC " +
                "LIMIT 4"; // Limiter à 4 produits

        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                produits.add(mapResultSetToProduit(rs));
            }
        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        return produits;
    }
}



