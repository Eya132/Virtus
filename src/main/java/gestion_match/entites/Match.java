package gestion_match.entites;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Match {
    private int idMatch;            // Identifiant unique du match
    private int planificateurId;    // ID du planificateur (administrateur ou utilisateur)
    private Date dateMatch;         // Date du match
    private String heure;           // Heure du match
    private String localisation;    // Localisation du match (ex: Tunis, Béja, etc.)
    private String terrain;         // Terrain où le match se déroule (ex: "Terrain Ariana")
    private String typeSport;       // Type de sport (ex: "Sport de raquette")
    private int nbPersonnes;        // Nombre de personnes pour le match
    private String description;     // Description optionnelle du match
    private String statut;          // Statut du match ("EN_ATTENTE", "VALIDE")

    // Constructeur avec un objet Date pour la dateMatch
    public Match(Date dateMatch, String heure, String localisation, String terrain,
                 String typeSport, int nbPersonnes, String description, String statut, int planificateurId) {
        this.dateMatch = dateMatch;
        this.heure = heure;
        this.localisation = localisation;
        this.terrain = terrain;
        this.typeSport = typeSport;
        this.nbPersonnes = nbPersonnes;
        this.description = description;
        this.statut = statut;
        this.planificateurId = planificateurId;
    }

    // Constructeur avec une date en String (qui sera convertie en Date)
    public Match(String date, String heure, String localisation, String terrain, String typeSport, int nbPersonnes, String description, String statut, int planificateurId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Format attendu : "2025-02-11"
        try {
            this.dateMatch = sdf.parse(date);  // Conversion de la chaîne en Date
        } catch (ParseException e) {
            System.err.println("Erreur lors de l'analyse de la date. Assurez-vous que le format est 'yyyy-MM-dd'.");
            e.printStackTrace();  // Gérer l'exception de parsing si le format de date est incorrect
        }
        this.heure = heure;
        this.localisation = localisation;
        this.terrain = terrain;
        this.typeSport = typeSport;
        this.nbPersonnes = nbPersonnes;
        this.description = description;
        this.statut = statut;
        this.planificateurId = planificateurId;
    }

    // Constructeur partiel
    public Match(String description, int nbPersonnes, String typeSport, String terrain, String localisation, Date dateMatch, String heure, int planificateurId) {
        this.description = description;
        this.nbPersonnes = nbPersonnes;
        this.typeSport = typeSport;
        this.terrain = terrain;
        this.localisation = localisation;
        this.dateMatch = dateMatch;
        this.heure = heure;
        this.planificateurId = planificateurId;
    }

    public Match(int idMatch, int planificateurId) {
        this.planificateurId=planificateurId;
        this.idMatch = idMatch;
    }

    public Match() {

    }

    public Match(int idMatch) {
        this.idMatch=idMatch;
    }

    public Match(int idMatch, java.sql.Date dateMatch, String heure, String localisation, String terrain, String typeSport, int nbPersonnes, String description, String statut, int planificateurId) {
        this.dateMatch = dateMatch;
        this.heure = heure;
    this.localisation = localisation;
    this.terrain = terrain;
    this.typeSport = typeSport;
    this.nbPersonnes = nbPersonnes;
    this.description = description;
    this.statut = statut;
    this.planificateurId = planificateurId;



    }

    // Getters et setters
    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getPlanificateurId() {
        return planificateurId;
    }

    public void setPlanificateurId(int planificateurId) {
        this.planificateurId = planificateurId;
    }

    public Date getDateMatch() {
        return dateMatch;
    }

    public void setDateMatch(Date dateMatch) {
        this.dateMatch = dateMatch;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public String getTypeSport() {
        return typeSport;
    }

    public void setTypeSport(String typeSport) {
        this.typeSport = typeSport;
    }

    public int getNbPersonnes() {
        return nbPersonnes;
    }

    public void setNbPersonnes(int nbPersonnes) {
        this.nbPersonnes = nbPersonnes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Match{" +
                "idMatch=" + idMatch +
                ", planificateurId=" + planificateurId +
                ", dateMatch=" + dateMatch +
                ", heure='" + heure + '\'' +
                ", localisation='" + localisation + '\'' +
                ", terrain='" + terrain + '\'' +
                ", typeSport='" + typeSport + '\'' +
                ", nbPersonnes=" + nbPersonnes +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }


}
