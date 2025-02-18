package GestionRegime.services;

import GestionRegime.entities.Regime;
import GestionRegime.interfaces.IRegime;
import GestionRegime.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegimeService implements IRegime<Regime> {

    // Méthode pour ajouter un régime
    @Override
    public void addRegime(Regime regime) {
        String requete = "INSERT INTO regime (utilisateur_id, nutritionniste_id, objectif, calories_journalières, proteines, glucides, lipides, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, regime.getUtilisateur_id());
            pst.setInt(2, regime.getNutritionniste_id());
            pst.setString(3, regime.getObjectif().toString());
            pst.setInt(4, regime.getCaloriesJournalieres());
            pst.setInt(5, regime.getProteines());
            pst.setInt(6, regime.getGlucides());
            pst.setInt(7, regime.getLipides());
            pst.setDate(8, convertToSqlDate(regime.getDateDebut()));
            pst.setDate(9, convertToSqlDate(regime.getDateFin()));
            pst.setString(10, regime.getStatus());
            pst.executeUpdate();
            System.out.println(" ✅ Régime ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println(" ❌ Erreur lors de l'ajout du régime : " + e.getMessage());
        }
    }

    // Méthode pour convertir une date de type java.util.Date en java.sql.Date
    public java.sql.Date convertToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    // Méthode pour supprimer un régime
    @Override
    public void deleteRegime(Regime regime) {
        String requete = "DELETE FROM regime WHERE regime_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, regime.getRegime_id());
            pst.executeUpdate();
            System.out.println(" ✅ Régime supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println(" ❌ Erreur lors de la suppression du régime : " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour un régime
    @Override
    public void updateRegime(int regime_id, Regime regime) {
        String requete = "UPDATE regime SET utilisateur_id = ?, nutritionniste_id = ?, objectif = ?, calories_journalières = ?, proteines = ?, glucides = ?, lipides = ?, date_debut = ?, date_fin = ?, statut = ? WHERE regime_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, regime.getUtilisateur_id());
            pst.setInt(2, regime.getNutritionniste_id());
            pst.setString(3, regime.getObjectif().toString());
            pst.setInt(4, regime.getCaloriesJournalieres());
            pst.setInt(5, regime.getProteines());
            pst.setInt(6, regime.getGlucides());
            pst.setInt(7, regime.getLipides());
            pst.setDate(8, convertToSqlDate(regime.getDateDebut()));
            pst.setDate(9, convertToSqlDate(regime.getDateFin()));
            pst.setString(10, regime.getStatus());
            pst.setInt(11, regime_id);
            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println(" ✅ Régime mis à jour avec succès !");
            } else {
                System.out.println(" ❌ Problème lors de la mise à jour du régime !");
            }
        } catch (SQLException e) {
            System.out.println(" ❌ Erreur lors de la mise à jour du régime : " + e.getMessage());
        }
    }

    // Méthode pour récupérer tous les régimes
    @Override
    public List<Regime> getAllDataRegime() {
        List<Regime> result = new ArrayList<>();
        String requete = "SELECT * FROM regime";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Regime r = new Regime();
                r.setRegime_id(rs.getInt("regime_id"));
                r.setUtilisateur_id(rs.getInt("utilisateur_id"));
                r.setNutritionniste_id(rs.getInt("nutritionniste_id"));
                r.setObjectif(Regime.Objectif.valueOf(rs.getString("objectif")));
                r.setCaloriesJournalieres(rs.getInt("calories_journalières"));
                r.setProteines(rs.getInt("proteines"));
                r.setGlucides(rs.getInt("glucides"));
                r.setLipides(rs.getInt("lipides"));
                r.setDateDebut(rs.getDate("date_debut"));
                r.setDateFin(rs.getDate("date_fin"));
                r.setStatus(rs.getString("statut"));
                result.add(r);
            }
        } catch (SQLException e) {
            System.out.println(" ❌ Erreur lors de la récupération des régimes : " + e.getMessage());
        }
        return result;
    }

    // Méthode pour récupérer tous les IDs des régimes disponibles


}
