package Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LieuController {

    // Liste statique des lieux disponibles
    private static final List<String> LIEUX = Arrays.asList(
            "Manzah", "Megrine", "Marsa", "Monastir", "Mahdia", "Menzel Bourguiba","Gammarth","Djerba","Bizerte","Ghazela"
    );

    /**
     * Récupérer les lieux commençant par une lettre donnée.
     *
     * @param lettre La lettre de départ pour filtrer les lieux.
     * @return Une liste de lieux commençant par la lettre spécifiée.
     */
    public static List<String> getLieux(String lettre) {
        // Vérifier si la lettre est vide ou null
        if (lettre == null || lettre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le paramètre 'lettre' ne peut pas être vide.");
        }

        // Filtrer les lieux qui commencent par la lettre donnée (insensible à la casse)
        return LIEUX.stream()
                .filter(lieu -> lieu.toLowerCase().startsWith(lettre.toLowerCase()))
                .collect(Collectors.toList());

    }

}