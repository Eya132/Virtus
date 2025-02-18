package GestionRegime.entities;

import java.time.LocalDateTime;
import java.util.Date;

public class Suivi {

    private int suivi_id;
    private int utilisateur_id;
    private int regime_id;
    private double poids;
    private double tour_de_taille;
    private double imc;
    private LocalDateTime date_suivi; // Date de suivi


    public Suivi(int utilisateur_id, int regime_id, double poids, double tour_de_taille, double imc) {
        this.utilisateur_id = utilisateur_id;
        this.regime_id = regime_id;
        this.poids = poids;
        this.tour_de_taille = tour_de_taille;
        this.imc = imc;
    }

    public Suivi(int suivi_id, int utilisateur_id, int regime_id, double poids, double tour_de_taille, double imc, LocalDateTime date_suivi) {
        this.suivi_id = suivi_id;
        this.utilisateur_id = utilisateur_id;
        this.regime_id = regime_id;
        this.poids = poids;
        this.tour_de_taille = tour_de_taille;
        this.imc = imc;
        this.date_suivi = date_suivi;
    }

    public Suivi(int utilisateur_id, int regime_id, double poids, double tour_de_taille, double imc, LocalDateTime date_suivi) {
        this.utilisateur_id = utilisateur_id;
        this.regime_id = regime_id;
        this.poids = poids;
        this.tour_de_taille = tour_de_taille;
        this.imc = imc;
        this.date_suivi = date_suivi;
    }

    public int getSuivi_id() {
        return suivi_id;
    }

    public void setSuivi_id(int suivi_id) {
        this.suivi_id = suivi_id;
    }

    public int getUtilisateur_id() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public int getRegime_id() {
        return regime_id;
    }

    public void setRegime_id(int regime_id) {
        this.regime_id = regime_id;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public double getTour_de_taille() {
        return tour_de_taille;
    }

    public void setTour_de_taille(double tour_de_taille) {
        this.tour_de_taille = tour_de_taille;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public LocalDateTime getDate_suivi() {
        return date_suivi;
    }

    public void setDate_suivi(LocalDateTime date_suivi) {
        this.date_suivi = date_suivi;
    }

    @Override
    public String toString() {
        return "Suivi{" +
                "suivi_id=" + suivi_id +
                ", utilisateur_id=" + utilisateur_id +
                ", regime_id=" + regime_id +
                ", poids=" + poids +
                ", tour_de_taille=" + tour_de_taille +
                ", imc=" + imc +
                ", date_suivi=" + date_suivi +
                '}';
    }
}