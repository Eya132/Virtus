package GestionRegime.services;

import GestionRegime.entities.Regime;
import GestionRegime.entities.Suivi;
import GestionRegime.interfaces.ISuivi;
import GestionRegime.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuiviService implements ISuivi<Suivi> {

    @Override
    public void addSuivi(Suivi suivi) {
        if (!isRegimeIdExist(suivi.getRegime_id())) {
            System.out.println("❌ Le régime avec l'ID " + suivi.getRegime_id() + " n'existe pas.");
            return;
        }

        String requete = "INSERT INTO `suivi`(`utilisateur_id`, `regime_id`, `poids`, `tour_de_taille`, `imc`) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, suivi.getUtilisateur_id());
            pst.setInt(2, suivi.getRegime_id());
            pst.setDouble(3, suivi.getPoids());
            pst.setDouble(4, suivi.getTour_de_taille());
            pst.setDouble(5, suivi.getImc());

            pst.executeUpdate();
            System.out.println("✅ Suivi ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout du suivi : " + e.getMessage());
        }
    }

    private boolean isRegimeIdExist(int regimeId) {
        String requete = "SELECT 1 FROM regime WHERE regime_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, regimeId);
            ResultSet rs = pst.executeQuery();
            return rs.next(); // Si une ligne est trouvée, cela signifie que le regime_id existe
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification du regime_id : " + e.getMessage());
            return false;
        }
    }

    public static java.sql.Date convertToSqlDate(java.util.Date date_suivi) {
        if (date_suivi == null) {
            return null;
        }
        return new java.sql.Date(date_suivi.getTime());
    }

    @Override
    public void deleteSuivi(Suivi suivi) {
        String requete = "DELETE FROM suivi WHERE suivi_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, suivi.getSuivi_id());
            pst.executeUpdate();
            System.out.println("✅ Suivi supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression du suivi : " + e.getMessage());
        }
    }

    public void updateSuivi(int suiviId, Suivi suivi) {
        String requete = "UPDATE suivi SET poids=?, tour_de_taille=?, imc=?, date_suivi=? WHERE suivi_id=?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setDouble(1, suivi.getPoids());
            pst.setDouble(2, suivi.getTour_de_taille());
            pst.setDouble(3, suivi.getImc());
            pst.setDate(4, java.sql.Date.valueOf(suivi.getDate_suivi())); // Conversion en java.sql.Date
            pst.setInt(5, suiviId); // Utiliser l'ID du suivi pour la clause WHERE

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Suivi mis à jour avec succès !");
            } else {
                System.out.println("❌ Aucun suivi trouvé avec l'ID : " + suiviId);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour du suivi : " + e.getMessage());
        }
    }

    @Override
    public List<Suivi> getAllDataSuivi() {
        List<Suivi> suivis = new ArrayList<>();
        String requete = "SELECT * FROM suivi";
        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(requete)) {
            while (rs.next()) {
                Suivi suivi = new Suivi();
                suivi.setSuivi_id(rs.getInt("suivi_id"));
                suivi.setUtilisateur_id(rs.getInt("utilisateur_id"));
                suivi.setRegime_id(rs.getInt("regime_id"));
                suivi.setPoids(rs.getDouble("poids"));
                suivi.setTour_de_taille(rs.getDouble("tour_de_taille"));
                suivi.setImc(rs.getDouble("imc"));
                suivi.setDate_suivi(rs.getDate("date_suivi").toLocalDate()); // Conversion en LocalDate
                suivis.add(suivi);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération des suivis : " + e.getMessage());
            e.printStackTrace();
        }
        return suivis;
    }

    public List<Integer> getAllRegimeIds() {
        List<Integer> regimeIds = new ArrayList<>();
        String requete = "SELECT regime_id FROM regime";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                regimeIds.add(rs.getInt("regime_id"));
            }
        } catch (SQLException e) {
            System.out.println(" ❌ Erreur lors de la récupération des IDs des régimes : " + e.getMessage());
        }
        return regimeIds;
    }

    // Méthode pour récupérer un régime par son ID
    public Regime getRegimeById(int regimeId) {
        String requete = "SELECT * FROM regime WHERE regime_id = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, regimeId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Regime regime = new Regime();

                // Correctif : Convertir la String en Objectif
                regime.setRegime_id(rs.getInt("regime_id"));
                regime.setObjectif(Regime.Objectif.valueOf(rs.getString("objectif"))); // Correctif ici

                // Correctif : Convertir les valeurs double en int
                regime.setCaloriesJournalieres(rs.getInt("calories_journalières")); // Utiliser getInt pour récupérer directement en int
                regime.setProteines(rs.getInt("proteines"));
                regime.setGlucides(rs.getInt("glucides"));
                regime.setLipides(rs.getInt("lipides"));

                return regime;
            }
        } catch (SQLException e) {
            System.out.println(" ❌ Erreur lors de la récupération du régime : " + e.getMessage());
        }
        return null;
    }


}