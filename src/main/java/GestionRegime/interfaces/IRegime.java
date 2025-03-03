package GestionRegime.interfaces;

import java.util.List;

public interface IRegime<T> {
    void addRegime(T t);
    void deleteRegime(T t);
    void updateRegime(int id, T t);
   public List<T> getAllDataRegime();
}
