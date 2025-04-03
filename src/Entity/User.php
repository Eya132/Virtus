<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;


#[ORM\Entity]
class User
{

    #[ORM\Id]
    #[ORM\Column(type: "string", length: 11)]
    private string $id_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $email_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $password_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $nom_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $prenom_user;

    #[ORM\Column(type: "string")]
    private string $sexe_user;

    #[ORM\Column(type: "string", length: 15)]
    private string $telephone_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $description_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $adresse_user;

    #[ORM\Column(type: "string")]
    private string $role;

    #[ORM\Column(type: "string")]
    private string $experience;

    #[ORM\Column(type: "float")]
    private float $salaire;

    #[ORM\Column(type: "string")]
    private string $niveau_joueur;

    #[ORM\Column(type: "integer")]
    private int $maxDistance_user;

    #[ORM\Column(type: "string", length: 1)]
    private string $is_premium;

    #[ORM\Column(type: "string", length: 255)]
    private string $photo_user;

    #[ORM\Column(type: "string", length: 255)]
    private string $piece_jointe;

    #[ORM\Column(type: "string", length: 255)]
    private string $reset_token;

    #[ORM\Column(type: "datetime")]
    private \DateTimeInterface $token_expiration;

    #[ORM\Column(type: "datetime")]
    private \DateTimeInterface $dateNaissance_user;

    public function getId_user()
    {
        return $this->id_user;
    }

    public function setId_user($value)
    {
        $this->id_user = $value;
    }

    public function getEmail_user()
    {
        return $this->email_user;
    }

    public function setEmail_user($value)
    {
        $this->email_user = $value;
    }

    public function getPassword_user()
    {
        return $this->password_user;
    }

    public function setPassword_user($value)
    {
        $this->password_user = $value;
    }

    public function getNom_user()
    {
        return $this->nom_user;
    }

    public function setNom_user($value)
    {
        $this->nom_user = $value;
    }

    public function getPrenom_user()
    {
        return $this->prenom_user;
    }

    public function setPrenom_user($value)
    {
        $this->prenom_user = $value;
    }

    public function getSexe_user()
    {
        return $this->sexe_user;
    }

    public function setSexe_user($value)
    {
        $this->sexe_user = $value;
    }

    public function getTelephone_user()
    {
        return $this->telephone_user;
    }

    public function setTelephone_user($value)
    {
        $this->telephone_user = $value;
    }

    public function getDescription_user()
    {
        return $this->description_user;
    }

    public function setDescription_user($value)
    {
        $this->description_user = $value;
    }

    public function getAdresse_user()
    {
        return $this->adresse_user;
    }

    public function setAdresse_user($value)
    {
        $this->adresse_user = $value;
    }

    public function getRole()
    {
        return $this->role;
    }

    public function setRole($value)
    {
        $this->role = $value;
    }

    public function getExperience()
    {
        return $this->experience;
    }

    public function setExperience($value)
    {
        $this->experience = $value;
    }

    public function getSalaire()
    {
        return $this->salaire;
    }

    public function setSalaire($value)
    {
        $this->salaire = $value;
    }

    public function getNiveau_joueur()
    {
        return $this->niveau_joueur;
    }

    public function setNiveau_joueur($value)
    {
        $this->niveau_joueur = $value;
    }

    public function getMaxDistance_user()
    {
        return $this->maxDistance_user;
    }

    public function setMaxDistance_user($value)
    {
        $this->maxDistance_user = $value;
    }

    public function getIs_premium()
    {
        return $this->is_premium;
    }

    public function setIs_premium($value)
    {
        $this->is_premium = $value;
    }

    public function getPhoto_user()
    {
        return $this->photo_user;
    }

    public function setPhoto_user($value)
    {
        $this->photo_user = $value;
    }

    public function getPiece_jointe()
    {
        return $this->piece_jointe;
    }

    public function setPiece_jointe($value)
    {
        $this->piece_jointe = $value;
    }

    public function getReset_token()
    {
        return $this->reset_token;
    }

    public function setReset_token($value)
    {
        $this->reset_token = $value;
    }

    public function getToken_expiration()
    {
        return $this->token_expiration;
    }

    public function setToken_expiration($value)
    {
        $this->token_expiration = $value;
    }

    public function getDateNaissance_user()
    {
        return $this->dateNaissance_user;
    }

    public function setDateNaissance_user($value)
    {
        $this->dateNaissance_user = $value;
    }
}
