-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 04 mars 2025 à 13:03
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `matchmate`
--

-- --------------------------------------------------------

--
-- Structure de la table `event`
--

CREATE TABLE `event` (
  `idevent` int(11) NOT NULL,
  `titre` varchar(255) NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `lieu` varchar(255) NOT NULL,
  `imageUrl` varchar(255) NOT NULL,
  `description` varchar(10000) NOT NULL,
  `iduser` int(11) NOT NULL,
  `capacite` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `event`
--

INSERT INTO `event` (`idevent`, `titre`, `date`, `lieu`, `imageUrl`, `description`, `iduser`, `capacite`) VALUES
(138, 'Event 7', '2025-03-08 23:00:00', 'blocG', 'file:/C:/Users/dhaou/Downloads/Matchmate/images/a7e839de-0099-4ee6-9821-da22f49af40a.jpg', 'Hello', 2, 5),
(149, 'eya', '2025-04-04 23:00:00', 'Ghazela', 'file:/C:/Users/dhaou/Downloads/Matchmate/images/a7e839de-0099-4ee6-9821-da22f49af40a.jpg', 'Un événement de sport de raquette intitulé : eya. L\'événement aura lieu le 2025-04-05 à Ghazela. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette.', 1, 1),
(150, 'hey', '2025-03-27 23:00:00', 'Mégrine', 'file:/C:/Gestion_event/src/main/resources/Images/image1.png', 'Un événement de sport de raquette intitulé : hey. L\'événement aura lieu le 2025-03-28 à Mégrine. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette.', 1, 12),
(151, 'hello', '2025-04-05 23:00:00', 'Megrine', 'file:/C:/Gestion_event/src/main/resources/Images/image1.png', 'Un événement de sport de raquette intitulé : hello. L\'événement aura lieu le 2025-04-06 à Megrine. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette.', 1, 13),
(152, 'hbib', '2025-03-28 23:00:00', 'Manzah', 'file:/C:/Gestion_event/src/main/resources/Images/image1.png', 'Un événement de sport de raquette intitulé : hbib. L\'événement aura lieu le 2025-03-29 à Manzah. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette.', 12, 12),
(157, 'eya', '2025-03-13 23:00:00', 'Rue Hédi Nouira, Tunis, Tunisie', 'file:/C:/Gestion_event/src/main/resources/Images/image1%20-%20Copie.png', 'Un événement de sport de raquette intitulé : eya. L\'événement aura lieu le 2025-03-14 à Rue Hédi Nouira, Tunis, Tunisie. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette.', 1, 12),
(158, 'eya', '2025-03-21 23:00:00', 'Rue de Libye, Tunis, Tunisie', 'file:/C:/Gestion_event/src/main/resources/Images/image.png', 'Un événement de sport de raquette intitulé : eya. L\'événement aura lieu le 2025-03-22 à Rue de Libye, Tunis, Tunisie. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette.', 1, 12),
(159, 'eya', '2025-03-28 23:00:00', 'Autoroute Tunis - Sousse - Sfax, Tunis, Tunisie', 'file:/C:/Gestion_event/src/main/resources/Images/image.png', 'Un événement de sport de raquette intitulé : eya. L\'événement aura lieu le 2025-03-29 à Autoroute Tunis - Sousse - Sfax, Tunis, Tunisie. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette. Inscrivez-vous dès maintenant pour garantir votre place ! Confrontation à contenait l\'expertment à seasonnelle surveillance avant dans les issues d\'articlealeurs: la réjudice qui no à navigation en criminé et asse de funding par les bas et d\'alli mate partées au nouvellement dans ce semainage d\'orbitement : avec le two sur le game, d\'assignée autresques était une release faire les problemés technicalement dans les recreatione entre les area types et ou de forwardown ordre du Prospectus, à son créat erguuët son Grimaud ambiagesin en Sport des Raqu Consulting Gatincourt, News The Bundesliga Offen has enjoyed a \"successful start\" from last year\'s double-bee campaign in Morocco.', 1, 2),
(160, 'eya', '2025-03-13 23:00:00', 'Avenue du Ghana, Tunis, Tunisie', 'file:/C:/Gestion_event/src/main/resources/Images/image.png', 'Un événement de sport de raquette intitulé : eya. L\'événement aura lieu le 2025-03-14 à Avenue du Ghana, Tunis, Tunisie. Des raquettes et des balles seront fournies sur place. L\'événement est organisé par MatchMate, une application dédiée aux amateurs de sports de raquette. Appliquée plus vas nous êtes rien- êtes pour le nouvel accoutre ? Planus rien- au sports qui lui castenir, présil en est de art, con la droits importantīnt, la sports du sports therapy. Charlie earned a reputation as a winner, and her debut at antipunctivs in 2003 came in 9-ous Quartent sold nous événements.', 1, 12);

-- --------------------------------------------------------

--
-- Structure de la table `participation`
--

CREATE TABLE `participation` (
  `idparticipation` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `idevent` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `participation`
--

INSERT INTO `participation` (`idparticipation`, `iduser`, `idevent`) VALUES
(584, 96, 138),
(585, 24, 138),
(586, 43, 138),
(626, 33, 149),
(627, 89, 150),
(628, 6, 150),
(629, 19, 150),
(630, 21, 150);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `event`
--
ALTER TABLE `event`
  ADD PRIMARY KEY (`idevent`);

--
-- Index pour la table `participation`
--
ALTER TABLE `participation`
  ADD PRIMARY KEY (`idparticipation`),
  ADD KEY `fk_idevent` (`idevent`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `event`
--
ALTER TABLE `event`
  MODIFY `idevent` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=161;

--
-- AUTO_INCREMENT pour la table `participation`
--
ALTER TABLE `participation`
  MODIFY `idparticipation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=631;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `participation`
--
ALTER TABLE `participation`
  ADD CONSTRAINT `fk_idevent` FOREIGN KEY (`idevent`) REFERENCES `event` (`idevent`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
