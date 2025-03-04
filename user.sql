-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 04 mars 2025 à 01:29
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `g_user`
--

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id_user` varchar(11) NOT NULL,
  `email_user` varchar(255) NOT NULL,
  `password_user` varchar(255) NOT NULL,
  `nom_user` varchar(255) DEFAULT NULL,
  `prenom_user` varchar(255) DEFAULT NULL,
  `dateNaissance_user` date NOT NULL,
  `sexe_user` enum('F','M') DEFAULT NULL,
  `telephone_user` varchar(15) DEFAULT NULL,
  `description_user` varchar(255) DEFAULT NULL,
  `maxDistance_user` int(11) DEFAULT NULL,
  `adresse_user` varchar(255) DEFAULT NULL,
  `role` enum('PLAYER','ADMIN','NUTRITIONIST') DEFAULT NULL,
  `experience` enum('ONE_YEAR','TWO_YEARS','THREE_YEARS','FOUR_YEARS','MORE_THAN_FIVE_YEARS') DEFAULT NULL,
  `salaire` float DEFAULT NULL,
  `is_premium` tinyint(1) DEFAULT NULL,
  `niveau_joueur` enum('Debutant','Intermediaire','Expert') DEFAULT NULL,
  `photo_user` varchar(255) DEFAULT NULL,
  `piece_jointe` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT 'PENDING',
  `reset_token` varchar(255) DEFAULT NULL,
  `token_expiration` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id_user`, `email_user`, `password_user`, `nom_user`, `prenom_user`, `dateNaissance_user`, `sexe_user`, `telephone_user`, `description_user`, `maxDistance_user`, `adresse_user`, `role`, `experience`, `salaire`, `is_premium`, `niveau_joueur`, `photo_user`, `piece_jointe`, `status`, `reset_token`, `token_expiration`) VALUES
('414JMT3789', 'hbib@esprit.tn', '$2a$10$h7INBhx0MsSrKJX9aY51OOFy0tOT8SWCvdnkkNOZYMk8BKUw/cyX2', 'hbib', 'bensalem', '1997-03-16', 'M', '26934999', 'hbib desc', 35, 'ariana', 'PLAYER', NULL, 0, 0, 'Expert', 'file:/C:/Users/hbibb/Downloads/profile-pic.png', NULL, 'PENDING', NULL, NULL),
('912NUT6784', 'hbibbensalem20@gmail.com', '$2a$10$j5gXOhoW.B.S4CFQKTKSpuD2zgVyNkYhN0PzDw3lcfG.xEAF600Fi', 'hbib', 'bensalem', '1995-03-08', 'M', '26934999', NULL, 0, 'شارع غانا, تونس, تونس', 'NUTRITIONIST', 'THREE_YEARS', 0, 0, NULL, 'file:/C:/Users/hbibb/Downloads/utilisateur.png', 'file:/C:/Users/hbibb/Downloads/sssssss.jpg', 'VALIDATED', NULL, NULL),
('921NUT3185', 'hbib@gmail.com', '$2a$10$vht7xJCBoA1lz055CclUpe9S01mdwISsyEUtHq89i.axFgK4PDDvC', 'hbib', 'bensalem', '1985-03-02', 'M', '26934999', NULL, 0, 'ariana', 'NUTRITIONIST', 'TWO_YEARS', 0, 0, NULL, 'file:/C:/Users/hbibb/AppData/Local/Temp/captured_image18040729452580185336.png', 'file:/C:/Users/hbibb/Downloads/700-506963-Diplome-d-Universite-de-Dietetique-et-Nutrition-Humaine.jpg', 'PENDING', NULL, NULL);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
