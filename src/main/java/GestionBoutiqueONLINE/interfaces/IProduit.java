package GestionBoutiqueONLINE.interfaces;

import java.util.List;

public interface IProduit<T> {

    void addProduit(T t);
    void deleteProduit(T t);
    void updateProduit(int id, T t);
    List<T> getAllDataProduit();
}
