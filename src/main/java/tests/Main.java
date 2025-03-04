package main;

import Entites.ListInscri;
import services.ListInscriService;

public class Main {
    public static void main(String[] args) {
        // Créer une instance de ListInscriService
        ListInscriService listInscriService = new ListInscriService();

        // Créer un objet ListInscri avec des données de test
        ListInscri inscription = new ListInscri();
        inscription.setMatchId(6); // Remplacez par un matchId existant dans votre base de données
        inscription.setuserId(10);  // Remplacez par un idUser existant dans votre base de données

        // Appeler la méthode addInscription pour ajouter l'inscription
        listInscriService.addInscription(inscription);

        // Afficher un message de succès
        System.out.println("Inscription ajoutée avec succès !");

        // Optionnel : Récupérer et afficher toutes les inscriptions pour vérifier l'ajout
        System.out.println("Liste des inscriptions après ajout :");
        listInscriService.getAllInscriptions().forEach(System.out::println);
    }
}