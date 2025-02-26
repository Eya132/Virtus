package Entities;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Event {
    private int idevent;
    private String titre;
    private String description;
    private Date date;
    private String lieu;
    private int iduser;
    private String imageUrl;
    private int capacite; // Nouveau champ

    // Constructeurs
    public Event() {}

    public Event(String titre, String description, Date date, String lieu, int iduser, String imageUrl, int capacite) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.lieu = lieu;
        this.iduser = iduser;
        this.imageUrl = imageUrl;
        this.capacite = capacite;
    }

    // Getters et Setters
    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    @Override
    public String toString() {
        return "Evenement{" +
                "idevent=" + idevent +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", lieu='" + lieu + '\'' +
                ", iduser=" + iduser +
                ", imageUrl='" + imageUrl + '\'' +
                ", capacite=" + capacite +
                '}';
    }
}