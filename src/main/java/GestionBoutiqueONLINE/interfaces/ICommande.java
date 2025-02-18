package GestionBoutiqueONLINE.interfaces;

import java.util.List;

public interface ICommande<T>{
    void addCommande(T t);
    void deleteCommande(int id);
    void updateCommande(int id, T t);
    List<T> getAllDataCommande();
}
