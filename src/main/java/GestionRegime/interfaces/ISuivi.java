package GestionRegime.interfaces;

import java.util.List;

public interface ISuivi <T>{
    void addSuivi(T t);
    void deleteSuivi(T t);
    void updateSuivi(int id, T t);
    List<T> getAllDataSuivi();
}

