package GestionRegime.entities;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Suivi {

    // Propriétés JavaFX
    private IntegerProperty suivi_id;
    private IntegerProperty utilisateur_id;
    private IntegerProperty regime_id;
    private DoubleProperty poids;
    private DoubleProperty tour_de_taille;
    private DoubleProperty imc;
    private ObjectProperty<LocalDate> date_suivi; // Changé en LocalDate

    // Constructeurs
    public Suivi() {
        this.suivi_id = new SimpleIntegerProperty();
        this.utilisateur_id = new SimpleIntegerProperty();
        this.regime_id = new SimpleIntegerProperty();
        this.poids = new SimpleDoubleProperty();
        this.tour_de_taille = new SimpleDoubleProperty();
        this.imc = new SimpleDoubleProperty();
        this.date_suivi = new SimpleObjectProperty<>();
    }

    public Suivi(int utilisateur_id, int regime_id, double poids, double tour_de_taille, double imc) {
        this();
        this.utilisateur_id.set(utilisateur_id);
        this.regime_id.set(regime_id);
        this.poids.set(poids);
        this.tour_de_taille.set(tour_de_taille);
        this.imc.set(imc);
    }

    public Suivi(int suivi_id, int utilisateur_id, int regime_id, double poids, double tour_de_taille, double imc, LocalDate date_suivi) {
        this();
        this.suivi_id.set(suivi_id);
        this.utilisateur_id.set(utilisateur_id);
        this.regime_id.set(regime_id);
        this.poids.set(poids);
        this.tour_de_taille.set(tour_de_taille);
        this.imc.set(imc);
        this.date_suivi.set(date_suivi);
    }

    // Getters et Setters
    public int getSuivi_id() {
        return suivi_id.get();
    }

    public IntegerProperty suivi_idProperty() {
        return suivi_id;
    }

    public void setSuivi_id(int suivi_id) {
        this.suivi_id.set(suivi_id);
    }

    public int getUtilisateur_id() {
        return utilisateur_id.get();
    }

    public IntegerProperty utilisateur_idProperty() {
        return utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id.set(utilisateur_id);
    }

    public int getRegime_id() {
        return regime_id.get();
    }

    public IntegerProperty regime_idProperty() {
        return regime_id;
    }

    public void setRegime_id(int regime_id) {
        this.regime_id.set(regime_id);
    }

    public double getPoids() {
        return poids.get();
    }

    public DoubleProperty poidsProperty() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids.set(poids);
    }

    public double getTour_de_taille() {
        return tour_de_taille.get();
    }

    public DoubleProperty tour_de_tailleProperty() {
        return tour_de_taille;
    }

    public void setTour_de_taille(double tour_de_taille) {
        this.tour_de_taille.set(tour_de_taille);
    }

    public double getImc() {
        return imc.get();
    }

    public DoubleProperty imcProperty() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc.set(imc);
    }

    public LocalDate getDate_suivi() {
        return date_suivi.get();
    }

    public ObjectProperty<LocalDate> date_suiviProperty() {
        return date_suivi;
    }

    public void setDate_suivi(LocalDate date_suivi) {
        this.date_suivi.set(date_suivi);
    }

    @Override
    public String toString() {
        return "Suivi{" +
                "suivi_id=" + suivi_id.get() +
                ", utilisateur_id=" + utilisateur_id.get() +
                ", regime_id=" + regime_id.get() +
                ", poids=" + poids.get() +
                ", tour_de_taille=" + tour_de_taille.get() +
                ", imc=" + imc.get() +
                ", date_suivi=" + date_suivi.get() +
                '}';
    }
}