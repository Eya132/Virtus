package Entites;

public class ListInscri {
    private int id;
    private int matchId; // Référence au match
    private int userId; // Référence au joueur

    // Constructeur par défaut
    public ListInscri() {}

    // Constructeur avec paramètres
    public ListInscri(int id, int matchId, int userId) {
        this.id = id;
        this.matchId = matchId;
        this.userId = userId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getuserId() {
        return userId;
    }

    public void setuserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ListInscri{" +
                "id=" + id +
                ", matchId=" + matchId +
                ", userId=" + userId +
                '}';
    }
}
