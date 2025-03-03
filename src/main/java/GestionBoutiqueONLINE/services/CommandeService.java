package GestionBoutiqueONLINE.services;

import GestionBoutiqueONLINE.entities.Commande;
import GestionBoutiqueONLINE.entities.Produit;
import GestionBoutiqueONLINE.interfaces.ICommande;
import GestionBoutiqueONLINE.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class CommandeService implements ICommande<Commande> {

    @Override
    public void addCommande(Commande commande) {
        String requete = "INSERT INTO commande (quantiteCommande, idProduit, idUser, statusCommande) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, commande.getQuantiteCommande());
            pst.setInt(2, commande.getIdProduit());
            pst.setInt(3, commande.getIdUser());
            pst.setString(4, commande.getStatusCommande().name()); // Convert enum to String
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Commande ajoutée avec succès !");
            } else {
                System.out.println("⚠️ Aucune commande insérée.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de la commande : " + e.getMessage());
        }

    }

    @Override
    public void deleteCommande(int idCommande) {
        String requete = "DELETE FROM commande WHERE idCommande = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, idCommande); // ID de la commande à supprimer

            int result = pst.executeUpdate();

            if (result > 0) {
                System.out.println("✅ Commande supprimée avec succès !");
            } else {
                System.out.println("⚠️ Aucune commande trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de la commande : " + e.getMessage());
        }


    }

    @Override
    public void updateCommande(int idC, Commande commande) {
        String requete = "UPDATE commande SET quantiteCommande = ?, statusCommande = ?, dateCommande = CURRENT_TIMESTAMP WHERE idCommande = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, commande.getQuantiteCommande());
            pst.setString(2, commande.getStatusCommande().toString()); // StatusCommande est un Enum, il faut le convertir en String
            pst.setInt(3, idC); // ID de la commande à mettre à jour

            int result = pst.executeUpdate();

            if (result > 0) {
                System.out.println("✅ Commande mise à jour avec succès !");
            } else {
                System.out.println("⚠️ Erreur : Aucune commande trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de la commande : " + e.getMessage());
        }
    }

    @Override
    public List<Commande> getAllDataCommande() {
        List<Commande> result = new ArrayList<>();
        String requete = "SELECT * FROM commande";

        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("idCommande"));
                commande.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime()); // Utilisation de Timestamp pour la date
                commande.setQuantiteCommande(rs.getInt("quantiteCommande"));
                commande.setIdProduit(rs.getInt("idProduit"));
                commande.setIdUser(rs.getInt("idUser"));
                commande.setStatusCommande(Commande.StatusCommande.valueOf(rs.getString("statusCommande"))); // Conversion du status en Enum

                result.add(commande); // Ajout de la commande à la liste
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des commandes : " + e.getMessage());
        }

        return result;
    }

    public List<Commande> getCommandesByUserId(int idUser) {
        List<Commande> commandes = new ArrayList<>();
        String requete = "SELECT * FROM commande WHERE idUser = ?";  // Requête pour récupérer les commandes de l'utilisateur

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, idUser);  // Assurez-vous d'utiliser l'ID de l'utilisateur pour filtrer les commandes
            ResultSet rs = pst.executeQuery();  // Exécution de la requête

            // Traitement des résultats
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setIdCommande(rs.getInt("idCommande"));  // Récupérer l'ID de la commande
                commande.setDateCommande(rs.getTimestamp("dateCommande").toLocalDateTime());  // Récupérer la date de la commande
                commande.setQuantiteCommande(rs.getInt("quantiteCommande"));  // Récupérer la quantité commandée
                commande.setIdProduit(rs.getInt("idProduit"));  // Récupérer l'ID du produit
                commande.setIdUser(rs.getInt("idUser"));  // Récupérer l'ID de l'utilisateur

                // Récupérer le statut de la commande et le convertir en type d'énumération
                String status = rs.getString("statusCommande");
                if (status != null) {
                    commande.setStatusCommande(Commande.StatusCommande.valueOf(status));  // Convertir en statut de commande (ENUM)
                }

                commandes.add(commande);  // Ajouter la commande à la liste
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des commandes de l'utilisateur : " + e.getMessage());
        }

        return commandes;  // Retourner la liste des commandes récupérées
    }

    private Map<String, Integer> getProduitsLesPlusCommandes() {
        Map<String, Integer> produitsCommandes = new HashMap<>();
        try {
            String query = "SELECT p.nomProduit, SUM(c.quantiteCommande) as totalQuantite " +
                    "FROM commande c " +
                    "JOIN produit p ON c.idProduit = p.idProduit " +
                    "GROUP BY p.nomProduit " +
                    "ORDER BY totalQuantite DESC";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String nomProduit = rs.getString("nomProduit");
                int totalQuantite = rs.getInt("totalQuantite");
                produitsCommandes.put(nomProduit, totalQuantite);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits les plus commandés : " + e.getMessage());
        }
        return produitsCommandes;
    }



}
