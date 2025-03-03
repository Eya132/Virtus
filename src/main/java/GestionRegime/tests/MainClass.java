package GestionRegime.tests;

import GestionRegime.entities.Suivi;
import GestionRegime.services.RegimeService;
import GestionRegime.services.SuiviService;

public class MainClass {
    public static void main(String[] args) {
        //RegimeService rs = new RegimeService();
        SuiviService suiviService = new SuiviService();


        // ➤ Ajouter un nouveau suivi
        Suivi newSuivi = new Suivi(2, 2, 70.5, 85.0, 23.5);
        suiviService.addSuivi(newSuivi);
        System.out.println("✅ Suivi ajouté avec succès !");

    }
    
}

        // ➤ Mettre à jour un suivi (ex: changer le poids)
        //Suivi updatedSuivi = new Suivi(2, 2, 68.0, 83.0, 22.8, Date.valueOf("2025-03-01"));
        //updatedSuivi.setSuivi_id(4); // Remplace 1 par l'ID correct du suivi existant
        // suiviService.updateSuivi(updatedSuivi.getSuivi_id(), updatedSuivi);


        // ➤ Supprimer un suivi (ex: ID = 1)
        // Suivi suiviToDelete = new Suivi();
        //suiviToDelete.setSuivi_id(3);
        // suiviService.deleteSuivi(suiviToDelete);

        // ➤ Récupérer et afficher tous les suivis
        //System.out.println("Liste des suivis:");
        // for (Suivi s : suiviService.getAllDataSuivi(1)) {System.out.println(s);}



//**************************************************************************//

        // 🔹 Ajouter un nouveau régime
        //Regime r1 = new Regime(1, 2, Regime.Objectif.PERTE_DE_POIDS, 2000, 150, 250, 50, Date.valueOf("2025-02-01"), Date.valueOf("2025-04-01"), "En cours");
        // rs.addRegime(r1);
        // Regime r2 = new Regime(2, 4, Regime.Objectif.PERTE_DE_POIDS, 2000, 150, 250, 50, Date.valueOf("2025-02-01"), Date.valueOf("2025-04-01"), "En cours");
        //rs.addRegime(r1);
        // Regime r4 = new Regime(7, 5, Regime.Objectif.PERTE_DE_POIDS, 2000, 150, 250, 50, Date.valueOf("2025-02-01"), Date.valueOf("2025-04-01"), "En cours");
        // rs.addRegime(r1);


//**********************************************************************//


        // 🔹 Mettre à jour un régime
        //Regime rUpdate = new Regime(2, 2, Regime.Objectif.MAINTIEN_DU_POIDS, 2500, 180, 300, 70, Date.valueOf("2025-02-10"), Date.valueOf("2025-05-10"), "En cours");
        // rs.updateRegime(2, rUpdate);


//***************************************************************************//


        // 🔹 Afficher tous les régimes
       // System.out.println(rs.getAllDataRegime());


        //***************************************************************************//



        // 🔹 Supprimer un régime
        // Regime rDelete = new Regime();
        // rDelete.setRegime_id(1);
        // rs.deleteRegime(rDelete);


