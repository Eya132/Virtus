# 🎾 MatchMate

**MatchMate** est une application web développée avec le framework Symfony. Elle vise à simplifier la gestion et l'organisation d'événements sportifs, en particulier pour les amateurs de sports de raquette (tennis, padel, badminton, etc.).

## 📌 Description du projet

MatchMate est une plateforme complète qui offre une expérience enrichie aux sportifs. Elle intègre un large éventail de fonctionnalités pour favoriser l'organisation, la participation et le suivi de l'activité sportive.

### Fonctionnalités principales :

- **Authentification** : Création de compte, connexion et gestion des rôles (`ROLE_USER`, `ROLE_ADMIN`).
- **Participation à des événements** : Inscription à des événements sportifs organisés par l' administrateur avec gestion des participants.
- **Participation à des matchs** : Engagement direct dans des matchs sportifs, avec gestion des participants et des matches .
- **Suivi alimentaire personnalisé** : Suivie alimentaire permettant aux utilisateurs de suivre leurs habitudes nutritionnelles et d’améliorer leur hygiène de vie.
- **Boutique e-commerce intégrée** : Vente de produits liés au sport (équipements, accessoires, etc.).
- **Espace réclamations** : Les utilisateurs peuvent soumettre des réclamations, assurant un canal de communication avec les administrateurs.
- **Espace administrateur** : Tableau de bord centralisé pour gérer les utilisateurs, événements, produits, réclamations, etc.

### Points forts :

- **Interface moderne** : Design attrayant basé sur Twig et CSS personnalisé avec des couleurs vives (turquoise et orangé).
- **Navigation fluide** : Interface utilisateur ergonomique et responsive pour une meilleure expérience.
- **Sécurité renforcée** : Accès sécurisé et gestion précise des droits des utilisateurs.

## 🗂 Table des matières

- [Installation](#installation)
- [Utilisation](#utilisation)
  - [Gestion des utilisateurs](#gestion-des-utilisateurs)
  - [Fonctionnalités](#fonctionnalités)
- [Technologies utilisées](#technologies-utilisées)
- [Contribution](#contribution)
- [Licence](#licence)

## ⚙️ Installation

1. **Cloner le dépôt Git**
```bash
git clone https://github.com/votre-utilisateur/matchmate.git
cd matchmate
```

2. **Installer les dépendances PHP**
```bash
composer install
```

3. **Configurer l'environnement**
Créer un fichier `.env.local` et y ajouter vos paramètres :
```dotenv
DATABASE_URL="mysql://user:password@127.0.0.1:3306/matchmate"
```

4. **Créer la base de données et exécuter les migrations**
```bash
php bin/console doctrine:database:create
php bin/console doctrine:migrations:migrate
```

5. **Installer les dépendances front-end (optionnel)**
```bash
npm install
npm run build
```

6. **Démarrer le serveur**
```bash
symfony server:start
```

## 🚀 Utilisation

### 👤 Gestion des utilisateurs
- Inscription/Connexion via une interface simple.
- Rôles utilisateurs : `ROLE_USER` pour les sportifs, `ROLE_ADMIN` pour les gestionnaires.

### 🏓 Fonctionnalités
- Création et participation aux événements et matchs.
- Consultation du calendrier sportif.
- Enregistrement et suivi alimentaire.
- Accès à la boutique en ligne.
- Envoi et gestion des réclamations.

## 🖥️ Technologies utilisées

- **Back-end** : Symfony 6, PHP 8.x
- **Front-end** : Twig, HTML5, CSS3 (design turquoise & orangé)
- **Base de données** : MySQL
- **ORM** : Doctrine
- **Gestion des assets** : Webpack Encore
- **Sécurité** : Gestion des rôles, encodage des mots de passe, CSRF
- **Webpack Encore / Vite**

## 📂 Utilisation

Une fois installé, rendez-vous sur [http://localhost:8000](http://localhost:8000)

- Inscrivez-vous en tant qu’utilisateur
- Accédez aux différentes sections via le menu principal
- L’admin peut se connecter via des identifiants spéciaux pour gérer l’ensemble du système

---

## 🤝 Contribution

Les contributions sont les bienvenues !

1. Forkez ce dépôt
2. Créez une branche :
```bash
git checkout -b feature/NouvelleFonctionnalité
```
3. Faites vos modifications
4. Commitez :
```bash
git commit -m "Ajout d'une nouvelle fonctionnalité"
```
5. Poussez :
```bash
git push origin feature/NouvelleFonctionnalité
```
6. Ouvrez une Pull Request

---

## 📄 Licence

Ce projet est sous licence **MIT** – voir le fichier `LICENSE` pour plus d'informations.

---

Merci de votre intérêt pour MatchMate ! 🎾
