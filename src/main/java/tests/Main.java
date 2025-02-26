/*package tests;

import Entities.Event;
import Services.EventService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Définir une date au format "yyyy-MM-dd"
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // Créer un nouvel événement
        Event newEvent = new Event(
        );

        // Créer une instance d'EventService
        EventService eventService = new EventService();

        // Ajouter l'événement à la base de données
        eventService.addEvent(newEvent);

        System.out.println("Événement ajouté avec succès!");

        // Fermer la connexion à la base de données
        eventService.closeConnection();
    }
}
*/