-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : mar. 04 mars 2025 à 13:04
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
-- Base de données : `db3a8`
--

-- --------------------------------------------------------

--
-- Structure de la table `commande`
--

CREATE TABLE `commande` (
  `idCommande` int(11) NOT NULL,
  `dateCommande` datetime NOT NULL DEFAULT current_timestamp(),
  `quantiteCommande` int(11) NOT NULL,
  `idProduit` int(11) NOT NULL,
  `statusCommande` enum('EN_ATTENTE','VALIDÉE','ANNULÉE') NOT NULL DEFAULT 'EN_ATTENTE',
  `idUser` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `commande`
--

INSERT INTO `commande` (`idCommande`, `dateCommande`, `quantiteCommande`, `idProduit`, `statusCommande`, `idUser`) VALUES
(37, '2025-02-22 23:40:30', 4, 45, 'VALIDÉE', 0),
(38, '2025-02-22 23:51:15', 11, 43, 'VALIDÉE', 0),
(39, '2025-02-22 23:53:38', 8, 45, 'VALIDÉE', 0),
(40, '2025-02-23 23:23:33', 1, 41, 'VALIDÉE', 0),
(41, '2025-02-24 13:18:05', 20, 41, 'VALIDÉE', 0),
(46, '2025-02-25 21:30:39', 5, 41, 'VALIDÉE', 0),
(47, '2025-02-25 22:34:42', 1, 48, 'VALIDÉE', 0),
(48, '2025-02-25 23:19:20', 3, 41, 'VALIDÉE', 0),
(49, '2025-02-25 23:32:10', 3, 45, 'VALIDÉE', 0),
(50, '2025-02-25 23:38:28', 5, 40, 'VALIDÉE', 0),
(51, '2025-02-25 23:52:00', 1, 41, 'VALIDÉE', 0),
(52, '2025-02-26 00:40:51', 3, 40, 'VALIDÉE', 0),
(53, '2025-02-26 00:55:41', 2, 40, 'VALIDÉE', 0),
(54, '2025-02-26 10:57:12', 2, 41, 'VALIDÉE', 0),
(55, '2025-02-26 10:59:38', 1, 45, 'VALIDÉE', 0),
(56, '2025-02-26 11:02:06', 1, 48, 'VALIDÉE', 0),
(57, '2025-02-26 11:03:42', 3, 40, 'VALIDÉE', 0),
(58, '2025-02-26 11:45:41', 4, 40, 'VALIDÉE', 0),
(59, '2025-02-26 11:53:32', 3, 40, 'VALIDÉE', 0),
(60, '2025-03-01 15:14:11', 3, 49, 'VALIDÉE', 0),
(61, '2025-03-01 15:19:13', 1, 48, 'VALIDÉE', 0),
(62, '2025-03-02 01:05:02', 4, 40, 'VALIDÉE', 0),
(63, '2025-03-02 01:15:28', 3, 40, 'VALIDÉE', 0),
(64, '2025-03-02 01:16:28', 5, 41, 'VALIDÉE', 0),
(65, '2025-03-03 00:36:31', 3, 49, 'VALIDÉE', 0),
(66, '2025-03-03 00:39:38', 1, 51, 'VALIDÉE', 0),
(67, '2025-03-03 02:34:05', 2, 50, 'VALIDÉE', 0),
(68, '2025-03-03 02:36:37', 1, 49, 'VALIDÉE', 0),
(69, '2025-03-03 14:19:49', 1, 45, 'VALIDÉE', 0),
(70, '2025-03-03 22:34:41', 2, 40, 'VALIDÉE', 0),
(71, '2025-03-03 20:47:57', 4, 51, 'VALIDÉE', 0),
(72, '2025-03-03 20:49:06', 3, 48, 'VALIDÉE', 0),
(73, '2025-03-03 21:56:03', 2, 50, 'VALIDÉE', 0),
(75, '2025-03-03 23:05:12', 3, 49, 'VALIDÉE', 0),
(76, '2025-03-03 23:18:55', 2, 50, 'VALIDÉE', 0),
(77, '2025-03-03 23:19:50', 3, 50, 'VALIDÉE', 0),
(78, '2025-03-03 23:22:32', 3, 50, 'VALIDÉE', 0),
(79, '2025-03-03 23:24:46', 1, 40, 'VALIDÉE', 0),
(80, '2025-03-04 12:13:35', 1, 45, 'VALIDÉE', 0),
(81, '2025-03-04 12:15:50', 3, 49, 'VALIDÉE', 0),
(82, '2025-03-04 12:16:23', 1, 49, 'VALIDÉE', 0),
(83, '2025-03-04 12:25:14', 2, 45, 'VALIDÉE', 0),
(84, '2025-03-04 12:29:52', 2, 49, 'VALIDÉE', 0),
(85, '2025-03-04 12:31:17', 2, 41, 'VALIDÉE', 0),
(86, '2025-03-04 12:32:36', 15, 41, 'VALIDÉE', 0),
(87, '2025-03-04 12:37:03', 1, 52, 'VALIDÉE', 19),
(88, '2025-03-04 12:48:51', 2, 41, 'VALIDÉE', 19),
(89, '2025-03-04 12:53:49', 2, 41, 'VALIDÉE', 19);

-- --------------------------------------------------------

--
-- Structure de la table `produit`
--

CREATE TABLE `produit` (
  `idProduit` int(11) NOT NULL,
  `nomProduit` varchar(110) NOT NULL,
  `descriptionProduit` varchar(110) NOT NULL,
  `prixProduit` int(11) NOT NULL,
  `quantiteProduit` int(11) NOT NULL,
  `imageProduit` varchar(255) NOT NULL,
  `refProduit` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `produit`
--

INSERT INTO `produit` (`idProduit`, `nomProduit`, `descriptionProduit`, `prixProduit`, `quantiteProduit`, `imageProduit`, `refProduit`) VALUES
(40, 'Raquette Ping Pong', 'Pratique', 120, 0, 'file:/C:/Users/MSI/Desktop/MatchMate/RaquettePingPongRouge.jpeg', '8483-RAQ-6306'),
(41, 'tenue femme', '2 piéces : jupe et top couleur blanche', 200, 42, 'file:/C:/Users/MSI/Desktop/MatchMate/TenuNoir.png', '3385-TEN-8662'),
(43, 'Balles de tennis', 'Lots de 4 balles', 70, 0, 'file:/C:/Users/MSI/Desktop/MatchMate/BallesTennis.jpeg', '7793-BAL-7391'),
(45, 'Tenue homme', 'T-SHIRT && SHORT NOIR', 170, 92, 'file:/C:/Users/MSI/Desktop/MatchMate/TenuHomme.jpeg', '1694-TEN-1539'),
(48, 'raquette', 'HHHHHHHHHHHH', 200, -3, 'file:/C:/Users/MSI/Desktop/MatchMate/RaquettePadel.jpeg', '7884-RAQ-9643'),
(49, 'table PingPong', '444444', 334, 28, 'file:/C:/Users/MSI/Desktop/MatchMate/TableTennis.jpg', '4093-TAB-1119'),
(50, 'Pure whey ', 'Jusqu\'à 79% de protéines', 126, 0, 'file:/C:/Users/MSI/Desktop/MatchMate/prot.jpg', '9719-PUR-1714'),
(51, 'Raquette', 'SPORT LIFE', 130, 0, 'file:/C:/Users/MSI/Desktop/MatchMate/RaquettePadelNoire.jpg', '6775-RAQ-6477'),
(52, 'cb', 'HHHHHHH VETEMNENT', 448, 6, 'file:/C:/Users/MSI/Desktop/MatchMate/BallesDepadel.jpg', '3547-CB-6434');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `commande`
--
ALTER TABLE `commande`
  ADD PRIMARY KEY (`idCommande`),
  ADD KEY `idProduit` (`idProduit`);

--
-- Index pour la table `produit`
--
ALTER TABLE `produit`
  ADD PRIMARY KEY (`idProduit`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `commande`
--
ALTER TABLE `commande`
  MODIFY `idCommande` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=90;

--
-- AUTO_INCREMENT pour la table `produit`
--
ALTER TABLE `produit`
  MODIFY `idProduit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `commande`
--
ALTER TABLE `commande`
  ADD CONSTRAINT `commande_ibfk_1` FOREIGN KEY (`idProduit`) REFERENCES `produit` (`idProduit`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
