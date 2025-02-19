package gestion_match.entites;

public class InscriptionMatch {
    private int idInscription;    // Identifiant unique de l'inscription
    private int idMatch;          // Identifiant du match
    private int idUser;           // Identifiant de l'utilisateur (lien vers la table User)
    private String role;          // Rôle de l'utilisateur (planificateur ou participant)
    private String statut;        // Statut de l'inscription (par exemple: "EN_ATTENTE", "VALIDE", "ANNULE")

    // Constructeur avec idInscription, idMatch, idUser, role et statut
    public InscriptionMatch(int idInscription, int idMatch, int idUser, String role, String statut) {
        this.idInscription = idInscription;
        this.idMatch = idMatch;
        this.idUser = idUser;
        this.role = role;
        this.statut = statut;
    }

    // Constructeur sans idInscription (lorsque l'ID est généré automatiquement)
    public InscriptionMatch(int idMatch, int idUser, String role, String statut) {
        this.idMatch = idMatch;
        this.idUser = idUser;
        this.role = role;
        this.statut = statut;
    }

    // Constructeur par défaut
    public InscriptionMatch() {
    }

    public InscriptionMatch(int idInscription, int idMatch, int idUser, String statut) {
        this.idInscription = idInscription;
        this.idMatch = idMatch;
        this.idUser = idUser;
        this.statut = statut;
    }
    public InscriptionMatch(int idMatch, int idUser, String statut) {
        this.idMatch = idMatch;
        this.idUser = idUser;
        this.statut = statut;
    }


    // Getters et Setters
    public int getIdInscription() {
        return idInscription;
    }

    public void setIdInscription(int idInscription) {
        this.idInscription = idInscription;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "InscriptionMatch{" +
                "idInscription=" + idInscription +
                ", idMatch=" + idMatch +
                ", idUser=" + idUser +
                ", role='" + role + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
