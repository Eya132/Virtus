package Entites;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Classe représentant un match sportif.
 */
public class Match1 {
    private int id;
    private LocalDate date;
    private LocalTime heure;
    private String localisation;
    private String terrain; // Terrain disponible
    private int nbPersonne; // 2 ou 4
    private String description;
    private String typeSport; // Type de sport (ex: Tennis, Padel)
    private String statut; // Statut du match (ex: Planifié, En cours, Terminé)
    private int userId;

    // Constructeur par défaut
    public Match1() {}

    /**
     * Constructeur avec paramètres (accepte LocalDate et LocalTime).
     *
     * @param id           l'identifiant du match
     * @param date         la date du match
     * @param heure        l'heure du match
     * @param localisation la localisation du match
     * @param terrain      le terrain du match
     * @param nbPersonne   le nombre de personnes (2 ou 4)
     * @param description  la description du match
     * @param typeSport    le type de sport
     * @param statut       le statut du match
     * @param userId       l'identifiant de l'utilisateur
     */
    public Match1(int id, LocalDate date, LocalTime heure, String localisation, String terrain, int nbPersonne, String description, String typeSport, String statut, int userId) {
        this.id = id;
        this.date = Objects.requireNonNull(date, "La date ne peut pas être nulle.");
        this.heure = Objects.requireNonNull(heure, "L'heure ne peut pas être nulle.");
        this.localisation = validateString(localisation, "La localisation ne peut pas être nulle ou vide.");
        this.terrain = validateString(terrain, "Le terrain ne peut pas être nul ou vide.");
        this.setNbPersonne(nbPersonne); // Validation de nbPersonne
        this.description = validateString(description, "La description ne peut pas être nulle ou vide.");
        this.typeSport = validateString(typeSport, "Le type de sport ne peut pas être nul ou vide.");
        this.statut = validateString(statut, "Le statut ne peut pas être nul ou vide.");
        this.userId = userId;
    }

    /**
     * Constructeur avec paramètres (accepte des String pour date et heure).
     *
     * @param id           l'identifiant du match
     * @param date         la date du match (format AAAA-MM-JJ)
     * @param heure        l'heure du match (format HH:MM)
     * @param localisation la localisation du match
     * @param terrain      le terrain du match
     * @param nbPersonne   le nombre de personnes (2 ou 4)
     * @param description  la description du match
     * @param typeSport    le type de sport
     * @param statut       le statut du match
     * @param userId       l'identifiant de l'utilisateur
     */
    public Match1(int id, String date, String heure, String localisation, String terrain, int nbPersonne, String description, String typeSport, String statut, int userId) {
        this(id, parseDate(date), parseTime(heure), localisation, terrain, nbPersonne, description, typeSport, statut, userId);
    }

    public Match1(int id, String date, String heure, String localisation, String terrain, int nbPersonne, String description, String typeSport, String statut) {
        this.id = id;
        this.date = parseDate(date);
        this.heure = parseTime(heure);
        this.localisation=localisation;
        this.terrain=terrain;
        this.nbPersonne=nbPersonne;
        this.description=description;
        this.typeSport=typeSport;
        this.statut=statut;
    }

    /**
     * Valide qu'une chaîne de caractères n'est pas nulle ou vide.
     *
     * @param value   la chaîne à valider
     * @param message le message d'erreur à afficher si la validation échoue
     * @return la chaîne validée
     * @throws IllegalArgumentException si la chaîne est nulle ou vide
     */
    private static String validateString(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /**
     * Parse une chaîne de caractères en LocalDate.
     *
     * @param date la chaîne de caractères représentant la date (format AAAA-MM-JJ)
     * @return la date parsée
     * @throws IllegalArgumentException si le format de la date est invalide
     */
    private static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez le format AAAA-MM-JJ.", e);
        }
    }

    /**
     * Parse une chaîne de caractères en LocalTime.
     *
     * @param time la chaîne de caractères représentant l'heure (format HH:MM)
     * @return l'heure parsée
     * @throws IllegalArgumentException si le format de l'heure est invalide
     */
    private static LocalTime parseTime(String time) {
        try {
            return LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez le format HH:MM.", e);
        }
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "La date ne peut pas être nulle.");
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = Objects.requireNonNull(heure, "L'heure ne peut pas être nulle.");
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = validateString(localisation, "La localisation ne peut pas être nulle ou vide.");
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = validateString(terrain, "Le terrain ne peut pas être nul ou vide.");
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
        if (nbPersonne != 2 && nbPersonne != 4) {
            throw new IllegalArgumentException("Le nombre de personnes doit être 2 ou 4.");
        }
        this.nbPersonne = nbPersonne;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = validateString(description, "La description ne peut pas être nulle ou vide.");
    }

    public String getTypeSport() {
        return typeSport;
    }

    public void setTypeSport(String typeSport) {
        this.typeSport = validateString(typeSport, "Le type de sport ne peut pas être nul ou vide.");
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = validateString(statut, "Le statut ne peut pas être nul ou vide.");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Match1{" +
                "id=" + id +
                ", date=" + date +
                ", heure=" + heure +
                ", localisation='" + localisation + '\'' +
                ", terrain='" + terrain + '\'' +
                ", nbPersonne=" + nbPersonne +
                ", description='" + description + '\'' +
                ", typeSport='" + typeSport + '\'' +
                ", statut='" + statut + '\'' +
                ", userId=" + userId +
                '}';
    }
}