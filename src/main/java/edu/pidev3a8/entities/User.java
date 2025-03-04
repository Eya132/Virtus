package edu.pidev3a8.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

public class User {
    private String id_user;
    private String emailUser;
    private String passwordUser;
    private String nomUser;
    private String prenomUser;
    private LocalDate dateNaissanceUser;
    private UserSexe sexeUser;
    private String telephoneUser;
    private String photoUser;
    private String descriptionUser;
    private Integer maxDistanceUser = 0;
    private String adresseUser;
    private UserRole role;
    private Experience experience;
    private Double salaire = 0.0;
    private boolean is_Premuim;
    private User_Niveau niveau_joueur;
    private String piece_jointe;
    private UserStatus status;
    private String reset_token;
    private Timestamp token_expiration ;

    private static String current_user = null;

    public User() {
    }
    public User(String email, String password, String nom, String prenom, LocalDate dateNaissance, UserSexe sexe, String telephone, String description, int maxDistance, String adresse, UserRole role, User_Niveau niveauJoueur, Experience experience, double salaire, boolean isPremium , String piece_jointe) {
    }



    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", emailUser='" + emailUser + '\'' +
                ", passwordUser='" + passwordUser + '\'' +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", dateNaissanceUser=" + dateNaissanceUser +
                ", sexeUser='" + sexeUser + '\'' +
                ", telephoneUser='" + telephoneUser + '\'' +
                ", photoUser=" + photoUser +
                ", descriptionUser='" + descriptionUser + '\'' +
                ", maxDistanceUser=" + maxDistanceUser +
                ", adresseUser='" + adresseUser + '\'' +
                ", role=" + role +
                ", experience='" + experience + '\'' +
                ", salaire=" + salaire +
                ", is_Premuim=" + is_Premuim +
                ", niveau_joueur=" + niveau_joueur +
                ", piece_jointe='" + piece_jointe + '\'' +
                ", status=" + status +
                ", reset_token=" + reset_token +
                ", token_expiration=" + token_expiration +
                '}';
    }


    public User(String email, String password, String nom, String prenom, LocalDate dateNaissance, UserSexe sexe, String telephone, String description, int maxDistance, String adresse, UserRole role, User_Niveau niveauJoueur, Experience experience, double salaire, boolean isPremium , String piece_jointe , String reset_token, Timestamp token_expiration) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null.");
        }
        this.emailUser = email;
        this.passwordUser = password;
        this.nomUser = nom;
        this.prenomUser = prenom;
        this.dateNaissanceUser = dateNaissance;
        this.sexeUser = sexe;
        this.telephoneUser = telephone;
        this.descriptionUser = description;
        this.maxDistanceUser = maxDistance;
        this.adresseUser = adresse;
        this.role = role; // Ensure role is initialized
        this.niveau_joueur = niveauJoueur;
        this.experience = experience;
        this.salaire = salaire;
        this.is_Premuim = isPremium;
        this.piece_jointe =  piece_jointe;
        this.reset_token = reset_token;
        this.token_expiration = token_expiration;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public LocalDate getDateNaissanceUser() {
        return dateNaissanceUser;
    }

    public void setDateNaissanceUser(LocalDate dateNaissanceUser) {
        this.dateNaissanceUser = dateNaissanceUser;
    }

    public UserSexe getSexeUser() {
        return sexeUser;
    }

    public void setSexeUser(UserSexe sexeUser) {
        this.sexeUser = sexeUser;
    }

    public String getTelephoneUser() {
        return telephoneUser;
    }

    public void setTelephoneUser(String telephoneUser) {
        this.telephoneUser = telephoneUser;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }

    public String getDescriptionUser() {
        return descriptionUser;
    }

    public void setDescriptionUser(String descriptionUser) {
        this.descriptionUser = descriptionUser;
    }

    public Integer getMaxDistanceUser() {
        return maxDistanceUser;
    }

    public void setMaxDistanceUser(Integer maxDistanceUser) {
        this.maxDistanceUser = maxDistanceUser;
    }

    public String getAdresseUser() {
        return adresseUser;
    }

    public void setAdresseUser(String adresseUser) {
        this.adresseUser = adresseUser;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Double getSalaire() {
        return salaire;
    }

    public boolean getIs_Premuim() {
        return is_Premuim;
    }

    public void setIs_Premuim(boolean is_Premuim) {
        this.is_Premuim = is_Premuim;
    }

    public User_Niveau getNiveau_joueur() {
        return niveau_joueur;
    }

    public void setNiveau_joueur(User_Niveau niveau_joueur) {
        this.niveau_joueur = niveau_joueur;
    }
    public String getPiece_jointe() {return piece_jointe;
    }
    public void setPiece_jointe(String piece_jointe) {
        this.piece_jointe = piece_jointe;
    }

    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getReset_token() {
        return reset_token;
    }
    public void setReset_token(String reset_token) {
        this.reset_token =  reset_token;
    }

    public Timestamp getToken_expiration() {
        return token_expiration;
    }
    public void setToken_expiration(Timestamp token_expiration) {
        this.token_expiration = token_expiration;
    }
}
