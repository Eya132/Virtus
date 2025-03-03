package GestionRegime.services;

import GestionRegime.entities.Regime;
import GestionRegime.interfaces.IRegime;
import GestionRegime.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegimeService implements IRegime<Regime> {

    // Constantes pour les noms de colonnes
    private static final String COLUMN_REGIME_ID = "regime_id";
    private static final String COLUMN_UTILISATEUR_ID = "utilisateur_id";
    private static final String COLUMN_NUTRITIONNISTE_ID = "nutritionniste_id";
    private static final String COLUMN_OBJECTIF = "objectif";
    private static final String COLUMN_CALORIES = "calories_journalières";
    private static final String COLUMN_PROTEINES = "proteines";
    private static final String COLUMN_GLUCIDES = "glucides";
    private static final String COLUMN_LIPIDES = "lipides";
    private static final String COLUMN_DATE_DEBUT = "date_debut";
    private static final String COLUMN_DATE_FIN = "date_fin";
    private static final String COLUMN_STATUT = "statut";

    // Méthode pour ajouter un régime
    @Override
    public void addRegime(Regime regime) {
        String requete = "INSERT INTO regime (utilisateur_id, nutritionniste_id, objectif, calories_journalières, proteines, glucides, lipides, date_debut, date_fin, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, regime.getUtilisateur_id());
            pst.setInt(2, regime.getNutritionniste_id());
            pst.setString(3, regime.getObjectif().toString());
            pst.setInt(4, regime.getCaloriesJournalieres());
            pst.setInt(5, regime.getProteines());
            pst.setInt(6, regime.getGlucides());
            pst.setInt(7, regime.getLipides());
            pst.setDate(8, java.sql.Date.valueOf(regime.getDateDebut()));
            pst.setDate(9, java.sql.Date.valueOf(regime.getDateFin()));
            pst.setString(10, regime.getStatus());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Régime ajouté avec succès !");
            } else {
                System.out.println("❌ Aucune ligne insérée.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de l'ajout du régime : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour supprimer un régime
    @Override
    public void deleteRegime(Regime regime) {
        String requete = "DELETE FROM regime WHERE regime_id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, regime.getRegime_id());
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Régime supprimé avec succès !");
            } else {
                System.out.println("❌ Aucun régime trouvé avec l'ID : " + regime.getRegime_id());
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la suppression du régime : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour mettre à jour un régime
    @Override
    public void updateRegime(int regime_id, Regime regime) {
        String requete = "UPDATE regime SET utilisateur_id = ?, nutritionniste_id = ?, objectif = ?, calories_journalières = ?, proteines = ?, glucides = ?, lipides = ?, date_debut = ?, date_fin = ?, statut = ? WHERE regime_id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, regime.getUtilisateur_id());
            pst.setInt(2, regime.getNutritionniste_id());
            pst.setString(3, regime.getObjectif().toString());
            pst.setInt(4, regime.getCaloriesJournalieres());
            pst.setInt(5, regime.getProteines());
            pst.setInt(6, regime.getGlucides());
            pst.setInt(7, regime.getLipides());
            pst.setDate(8, java.sql.Date.valueOf(regime.getDateDebut())); // Convertir LocalDate en java.sql.Date
            pst.setDate(9, java.sql.Date.valueOf(regime.getDateFin()));   // Convertir LocalDate en java.sql.Date
            pst.setString(10, regime.getStatus());
            pst.setInt(11, regime_id);
            pst.executeUpdate();
            System.out.println("✅ Régime mis à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la mise à jour du régime : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer tous les régimes
    public  List<Regime> getAllDataRegime() { // Supprimer @Override
        List<Regime> regimes = new ArrayList<>();
        String requete = "SELECT * FROM regime";
        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(requete)) {
            while (rs.next()) {
                Regime regime = new Regime();
                regime.setRegime_id(rs.getInt(COLUMN_REGIME_ID));
                regime.setUtilisateur_id(rs.getInt(COLUMN_UTILISATEUR_ID));
                regime.setNutritionniste_id(rs.getInt(COLUMN_NUTRITIONNISTE_ID));
                regime.setObjectif(Regime.Objectif.valueOf(rs.getString(COLUMN_OBJECTIF)));
                regime.setCaloriesJournalieres(rs.getInt(COLUMN_CALORIES));
                regime.setProteines(rs.getInt(COLUMN_PROTEINES));
                regime.setGlucides(rs.getInt(COLUMN_GLUCIDES));
                regime.setLipides(rs.getInt(COLUMN_LIPIDES));

                // Convertir java.sql.Date en LocalDate avec gestion des null
                java.sql.Date sqlDateDebut = rs.getDate(COLUMN_DATE_DEBUT);
                java.sql.Date sqlDateFin = rs.getDate(COLUMN_DATE_FIN);
                regime.setDateDebut(sqlDateDebut != null ? sqlDateDebut.toLocalDate() : null);
                regime.setDateFin(sqlDateFin != null ? sqlDateFin.toLocalDate() : null);

                regime.setStatus(rs.getString(COLUMN_STATUT));
                regimes.add(regime);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL lors de la récupération des régimes : " + e.getMessage());
            e.printStackTrace();
        }
        return regimes;
    }
}