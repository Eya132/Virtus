-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 04 mars 2025 à 12:32
-- Version du serveur : 10.4.28-MariaDB
-- Version de PHP : 8.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `db3a8`
--

-- --------------------------------------------------------

--
-- Structure de la table `regime`
--

CREATE TABLE `regime` (
  `regime_id` int(20) NOT NULL,
  `utilisateur_id` int(11) NOT NULL,
  `nutritionniste_id` int(11) NOT NULL,
  `objectif` enum('PERTE_DE_POIDS','PRISE_DE_MASSE','MAINTIEN_DU_POIDS') NOT NULL,
  `calories_journalières` int(11) DEFAULT NULL,
  `proteines` int(11) DEFAULT NULL,
  `glucides` int(11) DEFAULT NULL,
  `lipides` int(11) DEFAULT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `statut` varchar(50) DEFAULT 'En cours'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `regime`
--

INSERT INTO `regime` (`regime_id`, `utilisateur_id`, `nutritionniste_id`, `objectif`, `calories_journalières`, `proteines`, `glucides`, `lipides`, `date_debut`, `date_fin`, `statut`) VALUES
(5, 2, 0, 'PERTE_DE_POIDS', 2000, 150, 250, 50, '2025-02-01', '2025-04-01', 'En cours'),
(6, 2, 0, 'PERTE_DE_POIDS', 2000, 150, 250, 50, '2025-02-01', '2025-04-01', 'En cours'),
(7, 2, 0, 'PERTE_DE_POIDS', 2000, 150, 250, 50, '2025-02-01', '2025-04-01', 'En cours'),
(8, 2, 0, 'PERTE_DE_POIDS', 2000, 150, 250, 50, '2025-02-01', '2025-04-01', 'En cours'),
(10, 0, 0, 'PRISE_DE_MASSE', 12, 333, 444, 444, '2002-12-10', '2010-12-10', 'HHHHHHHHHHHHH'),
(11, 0, 0, 'PRISE_DE_MASSE', 90, 234, 54, 34, '2025-06-10', '2025-08-10', 'VHBJN.?'),
(19, 0, 0, 'PERTE_DE_POIDS', 20, 30, 40, 40, '2025-03-01', '2025-05-10', 'CDGVFDS'),
(22, 0, 0, 'MAINTIEN_DU_POIDS', 20, 30, 20, 40, '2025-03-06', '2025-03-28', 'integration'),
(23, 0, 0, 'MAINTIEN_DU_POIDS', 29, 20, 30, 37, '2026-03-01', '2026-04-01', 'KAMALNA'),
(24, 0, 0, 'MAINTIEN_DU_POIDS', 34, 34, 34, 34, '2025-02-28', '2025-04-11', 'VBFDBD'),
(25, 0, 0, 'MAINTIEN_DU_POIDS', 34, 34, 34, 56, '2025-02-28', '2025-05-11', 'BGFNG');

-- --------------------------------------------------------

--
-- Structure de la table `suivi`
--

CREATE TABLE `suivi` (
  `suivi_id` int(11) NOT NULL,
  `utilisateur_id` int(11) NOT NULL,
  `regime_id` int(11) NOT NULL,
  `poids` decimal(5,2) DEFAULT NULL,
  `tour_de_taille` decimal(5,2) DEFAULT NULL,
  `imc` decimal(5,2) DEFAULT NULL,
  `date_suivi` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `suivi`
--

INSERT INTO `suivi` (`suivi_id`, `utilisateur_id`, `regime_id`, `poids`, `tour_de_taille`, `imc`, `date_suivi`) VALUES
(6, 1, 5, 77.00, 15.00, 67.00, '2025-02-18'),
(7, 1, 7, 56.00, 19.00, 78.00, '2025-02-18'),
(10, 1, 6, 50.00, 40.00, 20.00, '2025-02-19'),
(11, 1, 6, 30.00, 50.00, 19.00, '2025-02-19'),
(12, 1, 6, 10.00, 40.00, 19.00, '2025-03-02');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `regime`
--
ALTER TABLE `regime`
  ADD PRIMARY KEY (`regime_id`);

--
-- Index pour la table `suivi`
--
ALTER TABLE `suivi`
  ADD PRIMARY KEY (`suivi_id`),
  ADD KEY `regime_id` (`regime_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `regime`
--
ALTER TABLE `regime`
  MODIFY `regime_id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT pour la table `suivi`
--
ALTER TABLE `suivi`
  MODIFY `suivi_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `suivi`
--
ALTER TABLE `suivi`
  ADD CONSTRAINT `suivi_ibfk_1` FOREIGN KEY (`regime_id`) REFERENCES `regime` (`regime_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
