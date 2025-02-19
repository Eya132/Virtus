package gestion_match.tests;

import gestion_match.entites.InscriptionMatch;
import gestion_match.services.InscriptionMatchService;

public class MainClass {
    public static void main(String[] args) {
        // Création d'une inscription avec idMatch = 28, idUser = 29, role = "Participant", et statut = "En attente"
       InscriptionMatch inscription = new InscriptionMatch(48, 28, "Participant", "EN_ATTENTE");

        // Création du service pour gérer l'inscription
       InscriptionMatchService inscriptionMatchService = new InscriptionMatchService();

       // Appel de la méthode addInscription pour tester
        inscriptionMatchService.addInscription(inscription);

    }
}
