package gestion_match.interfaces;

import java.util.List;

public interface IService<T> {
    // Ajouter une entité
    void createEntity(T entity);

    // Mettre à jour une entité
  //  void updateEntity(T entity);

    // Récupérer toutes les entités
    //List<T> getAllEntities();

    // Supprimer une entité
   //void deleteEntity(int id);

    // Mettre à jour le statut d'une entité
   // void updateStatutEntity(int id, String statut);
}
