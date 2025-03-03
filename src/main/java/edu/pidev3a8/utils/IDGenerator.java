package edu.pidev3a8.utils;

import edu.pidev3a8.entities.UserSexe;
import edu.pidev3a8.entities.UserRole;

import java.security.SecureRandom;

public class IDGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCustomID(UserSexe sexe, UserRole role) {
        int firstPart = RANDOM.nextInt(900) + 100;  // 3 chiffres (100-999)
        int secondPart = RANDOM.nextInt(9000) + 1000; // 4 chiffres (1000-9999)

        String middlePart;

        // Déterminer la partie centrale en fonction du rôle
        switch (role) {
            case PLAYER:
                middlePart = (sexe == UserSexe.M) ? "JMT" : "JFT"; // Joueur
                break;
            case NUTRITIONIST:
                middlePart = "NUT"; // Nutritionniste
                break;
            case ADMIN:
                middlePart = "ADM"; // Administrateur
                break;
            default:
                throw new IllegalArgumentException("Rôle non pris en charge : " + role);
        }

        return firstPart + middlePart + secondPart;
    }
}