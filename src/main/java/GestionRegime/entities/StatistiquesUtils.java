package GestionRegime.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatistiquesUtils {
    public static Map<String, Integer> calculerStatistiques(List<Regime> regimes) {
        Map<String, Integer> statistiques = new HashMap<>();

        for (Regime regime : regimes) {
            String objectif = regime.getObjectif().toString(); // Convertir l'enum en String
            statistiques.put(objectif, statistiques.getOrDefault(objectif, 0) + 1);
        }

        return statistiques;
    }
}
