package gestion_match.interfaces;

import gestion_match.entites.InscriptionMatch;
import gestion_match.entites.Match;

import java.util.List;

public interface IServiceInscriptionMatch<T> {

    // Ajouter une inscription
    void addInscription(InscriptionMatch inscription);

    // Vérifier si l'utilisateur est déjà inscrit à ce match
  //  boolean isUserAlreadyRegistered(int idUser, int idMatch);

    // Récupérer toutes les inscriptions pour un match
    //List<InscriptionMatch> getInscriptionsByMatch(int idMatch);

    // Récupérer un match par son ID
  //  Match getMatchById(int idMatch);

    // Mettre à jour l'inscription (par exemple, changer le statut de "en attente" à "confirmé")
//    void updateInscription(InscriptionMatch inscription);

    // Récupérer le nombre d'inscriptions pour un match
  //  int getNombreInscriptionsParMatch(int idMatch);

    // Mettre à jour le statut de toutes les inscriptions pour un match
  //  void updateStatutInscriptionsByMatch(int idMatch, String statut);

    // Annuler la participation d'un utilisateur à un match
   // void annulerParticipation(int userId, int matchId);

}
