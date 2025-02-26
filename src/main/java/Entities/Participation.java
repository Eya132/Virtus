package Entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Participation {
    private final IntegerProperty idparticipation;
    private final IntegerProperty iduser;
    private final IntegerProperty idevent;

    public Participation() {
        this.idparticipation = new SimpleIntegerProperty();
        this.iduser = new SimpleIntegerProperty();
        this.idevent = new SimpleIntegerProperty();
    }

    public Participation(int idparticipation, int iduser, int idevent) {
        this.idparticipation = new SimpleIntegerProperty(idparticipation);
        this.iduser = new SimpleIntegerProperty(iduser);
        this.idevent = new SimpleIntegerProperty(idevent);
    }

    public int getIdparticipation() {
        return idparticipation.get();
    }

    public void setIdparticipation(int idparticipation) {
        this.idparticipation.set(idparticipation);
    }

    public IntegerProperty idparticipationProperty() {
        return idparticipation;
    }

    public int getIduser() {
        return iduser.get();
    }

    public void setIduser(int iduser) {
        this.iduser.set(iduser);
    }

    public IntegerProperty iduserProperty() {
        return iduser;
    }

    public int getIdevent() {
        return idevent.get();
    }

    public void setIdevent(int idevent) {
        this.idevent.set(idevent);
    }

    public IntegerProperty ideventProperty() {
        return idevent;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "idparticipation=" + idparticipation.get() +
                ", iduser=" + iduser.get() +
                ", idevent=" + idevent.get() +
                '}';
    }
}