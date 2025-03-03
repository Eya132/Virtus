package GestionRegime.entities;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Regime {
    private IntegerProperty regime_id;
    private IntegerProperty utilisateur_id;
    private IntegerProperty nutritionniste_id;
    private ObjectProperty<Objectif> objectif;
    private IntegerProperty caloriesJournalieres;
    private IntegerProperty proteines;
    private IntegerProperty glucides;
    private IntegerProperty lipides;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StringProperty status;

    public enum Objectif {
        PERTE_DE_POIDS,
        PRISE_DE_MASSE,
        MAINTIEN_DU_POIDS
    }

    // Constructeur
    public Regime() {
        this.regime_id = new SimpleIntegerProperty();
        this.utilisateur_id = new SimpleIntegerProperty();
        this.nutritionniste_id = new SimpleIntegerProperty();
        this.objectif = new SimpleObjectProperty<>();
        this.caloriesJournalieres = new SimpleIntegerProperty();
        this.proteines = new SimpleIntegerProperty();
        this.glucides = new SimpleIntegerProperty();
        this.lipides = new SimpleIntegerProperty();
        this.status = new SimpleStringProperty();
    }

    // Getters et Setters pour les propriétés
    public int getRegime_id() {
        return regime_id.get();
    }

    public IntegerProperty regime_idProperty() {
        return regime_id;
    }

    public void setRegime_id(int regime_id) {
        this.regime_id.set(regime_id);
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

    public int getNutritionniste_id() {
        return nutritionniste_id.get();
    }

    public IntegerProperty nutritionniste_idProperty() {
        return nutritionniste_id;
    }

    public void setNutritionniste_id(int nutritionniste_id) {
        this.nutritionniste_id.set(nutritionniste_id);
    }

    public Objectif getObjectif() {
        return objectif.get();
    }

    public ObjectProperty<Objectif> objectifProperty() {
        return objectif;
    }

    public void setObjectif(Objectif objectif) {
        this.objectif.set(objectif);
    }

    public int getCaloriesJournalieres() {
        return caloriesJournalieres.get();
    }

    public IntegerProperty caloriesJournalieresProperty() {
        return caloriesJournalieres;
    }

    public void setCaloriesJournalieres(int caloriesJournalieres) {
        this.caloriesJournalieres.set(caloriesJournalieres);
    }

    public int getProteines() {
        return proteines.get();
    }

    public IntegerProperty proteinesProperty() {
        return proteines;
    }

    public void setProteines(int proteines) {
        this.proteines.set(proteines);
    }

    public int getGlucides() {
        return glucides.get();
    }

    public IntegerProperty glucidesProperty() {
        return glucides;
    }

    public void setGlucides(int glucides) {
        this.glucides.set(glucides);
    }

    public int getLipides() {
        return lipides.get();
    }

    public IntegerProperty lipidesProperty() {
        return lipides;
    }

    public void setLipides(int lipides) {
        this.lipides.set(lipides);
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}