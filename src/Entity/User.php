<?php

namespace App\Entity;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Validator\Constraints as Assert;

use Doctrine\ORM\Mapping as ORM;


#[ORM\Entity]
class User implements UserInterface, PasswordAuthenticatedUserInterface
{
      
    #[ORM\Id]
    #[ORM\Column(type: "string", length: 11, unique: true)]
    private ?string $id_user = null;

    public function getRoles(): array
    {
        // Return the roles or permissions granted to the user
        return [$this->role];
    }

    public function eraseCredentials(): void
    {
        // Clear any sensitive data if stored temporarily
    }

    public function getUserIdentifier(): string
    {
        // Return the unique identifier for the user (e.g., email)
        return $this->email_user;
    }

    public function getPassword(): string
    {
        // Return the hashed password
        return $this->password_user;
    }

    public function __toString(): string
{
    return sprintf('%s %s (%s)', 
        $this->getNomUser(), 
        $this->getPrenomUser(), 
        $this->getEmailUser()
    );
}

public function __construct()
{
    $this->generateCustomId();
}

private function generateCustomId(): void
{
    $prefix = '';
    $middle = '';
    
    // Détermine le préfixe en fonction du rôle
    switch ($this->role) {
        case 'PLAYER':
            $middle = ($this->sexe_user === 'M') ? 'JMT' : 'FMT';
            break;
        case 'NUTRITIONIST':
            $middle = 'NUT';
            break;
        case 'ADMIN':
            $middle = 'ADM';
            break;
        default:
            $middle = 'GEN'; // Générique si rôle non reconnu
    }
    
    // Génère les nombres aléatoires
    $randomPrefix = str_pad(rand(0, 999), 3, '0', STR_PAD_LEFT);
    $randomSuffix = str_pad(rand(0, 9999), 4, '0', STR_PAD_LEFT);
    
    $this->id_user = $randomPrefix . $middle . $randomSuffix;
}

        #[ORM\Column(type: "string", length: 255)]
        #[Assert\NotBlank(message: "L'email ne peut pas être vide")]
        #[Assert\Email(message: "L'email '{{ value }}' n'est pas valide")]
        private ?string $email_user = null; // Rendons nullable avec valeur par défaut null

        #[ORM\Column(type: "string", length: 255)]
        #[Assert\NotBlank(message: "Le mot de passe est obligatoire", groups: ["registration"])]
        #[Assert\Length(
            min: 6,
            minMessage: "Le mot de passe doit contenir au moins {{ limit }} caractères",
            groups: ["registration"]
        )]
        #[Assert\Regex(
            pattern: "/\d/",
            message: "Le mot de passe doit contenir au moins un chiffre",
            groups: ["registration"]
        )]
        private ?string $password_user = null;

        #[ORM\Column(type: "string", length: 255)]
        #[Assert\NotBlank(message: "Le nom ne peut pas être vide")]
        private ?string $nom_user = null;

        #[ORM\Column(type: "string", length: 255)]
        #[Assert\NotBlank(message: "Le prénom ne peut pas être vide")]
        private ?string $prenom_user = null;

        #[ORM\Column(type: "string", length: 255)]
        private ?string $sexe_user = "M";

        #[ORM\Column(type: "string", length: 15)]
        #[Assert\NotBlank(message: "Le téléphone ne peut pas être vide")]
        #[Assert\Regex(
            pattern: "/^[259][0-9]*$/",
            message: "Le téléphone doit commencer par 2, 5 ou 9"
        )]
        private ?string $telephone_user = null;

        #[ORM\Column(type: "string", length: 255)]
        #[Assert\NotBlank(message: "La description ne peut pas être vide")]
        private ?string $description_user = null;

        #[ORM\Column(type: "string", length: 255)]
        #[Assert\NotBlank(message: "L'adresse ne peut pas être vide")]
        private ?string $adresse_user = null;

        #[ORM\Column(type: "string")]
        private ?string $role = "PLAYER";

        #[ORM\Column(type: "string")]
        private ?string $experience = null;

        #[ORM\Column(type: "float")]
        #[Assert\PositiveOrZero(message: "Le salaire ne peut pas être négatif")]
        private ?float $salaire = null;

        #[ORM\Column(type: "string")]
        private ?string $niveau_joueur = null;

        #[ORM\Column(type: "integer")]
        #[Assert\PositiveOrZero(message: "La distance maximale ne peut pas être négative")]
        private ?int $max_distance_user = null;

        #[ORM\Column(type: "string", length: 1)]
        private ?string $is_premium = null;

        #[ORM\Column(type: "string", length: 255)]
        private ?string $photo_user = null;

        #[ORM\Column(type: "string", length: 255)]
        private ?string $piece_jointe = null;

        #[ORM\Column(type: "string", length: 255, nullable: true)]
        private ?string $reset_token = null;

        #[ORM\Column(type: "datetime", nullable: true)]
        private ?\DateTimeInterface $token_expiration = null;

        #[ORM\Column(type: "datetime", nullable: true)]
        #[Assert\NotBlank(message: "La date de naissance ne peut pas être vide")]
        private ?\DateTimeInterface $date_naissance_user = null;

    public function getIdUser(): ?string
    {
        return $this->id_user;
    }

    public function setIdUser($value)
    {
        $this->id_user = $value;
    }

    public function getEmailUser()
    {
        return $this->email_user;
    }

    public function setEmailUser($value)
    {
        $this->email_user = $value;
    }

    public function getPasswordUser()
    {
        return $this->password_user;
    }

    public function setPasswordUser($value)
    {
        $this->password_user = $value;
    }

    public function getNomUser()
    {
        return $this->nom_user;
    }

    public function setNomUser($value)
    {
        $this->nom_user = $value;
    }

    public function getPrenomUser()
    {
        return $this->prenom_user;
    }

    public function setPrenomUser($value)
    {
        $this->prenom_user = $value;
    }

    public function getSexeUser()
    {
        return $this->sexe_user;
    }

    public function setSexeUser($value)
    {
        $this->sexe_user = $value;
    }

    public function getTelephoneUser()
    {
        return $this->telephone_user;
    }

    public function setTelephoneUser($value)
    {
        $this->telephone_user = $value;
    }

    public function getDescriptionUser()
    {
        return $this->description_user;
    }

    public function setDescriptionUser($value)
    {
        $this->description_user = $value;
    }

    public function getAdresseUser()
    {
        return $this->adresse_user;
    }

    public function setAdresseUser($value)
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

    public function getNiveauJoueur()
    {
        return $this->niveau_joueur;
    }

    public function setNiveauJoueur($value)
    {
        $this->niveau_joueur = $value;
    }

    public function getMaxDistanceUser() : ?int
    {
        return $this->max_distance_user;
    }

    public function setMaxDistanceUser($value)
    {
        $this->max_distance_user = $value;
    }

    public function getIsPremium()
    {
        return $this->is_premium;
    }

    public function setIsPremium($value)
    {
        $this->is_premium = $value;
    }

    public function getPhotoUser()
    {
        return $this->photo_user;
    }

    public function setPhotoUser($value)
    {
        $this->photo_user = $value;
    }

    public function getPieceJointe()
    {
        return $this->piece_jointe;
    }

    public function setPieceJointe($value)
    {
        $this->piece_jointe = $value;
    }

    public function getResetToken()
    {
        return $this->reset_token ;
    }

    public function setResetToken($value)
    {
        $this->reset_token = $value;
    }

    public function getTokenExpiration()
    {
        return $this->token_expiration;
    }

    public function setTokenExpiration($value)
    {
        $this->token_expiration = $value;
    }

    public function getDateNaissanceUser()
    {
        return $this->date_naissance_user;
    }

    public function setDateNaissanceUser($value)
    {
        $this->date_naissance_user = $value;
    }
}
