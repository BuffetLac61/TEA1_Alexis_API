-- phpMyAdmin SQL Dump
-- version 4.9.5deb2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : lun. 13 juin 2022 à 10:32
-- Version du serveur :  8.0.29-0ubuntu0.20.04.3
-- Version de PHP : 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `todo-api`
--

-- --------------------------------------------------------

--
-- Structure de la table `todo_items`
--

CREATE TABLE `todo_items` (
  `id` int NOT NULL,
  `idList` int NOT NULL,
  `label` varchar(100) NOT NULL,
  `checked` tinyint(1) NOT NULL DEFAULT '0',
  `url` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `todo_items`
--

INSERT INTO `todo_items` (`id`, `idList`, `label`, `checked`, `url`) VALUES
(1, 1, 'item1', 0, NULL),
(2, 1, 'item2', 1, NULL),
(3, 3, 'Item 1', 0, NULL),
(4, 3, 'Item 2', 0, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `todo_lists`
--

CREATE TABLE `todo_lists` (
  `id` int NOT NULL,
  `idUser` int NOT NULL,
  `label` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `todo_lists`
--

INSERT INTO `todo_lists` (`id`, `idUser`, `label`) VALUES
(1, 1, 'list1_tom'),
(2, 1, 'list2_tom'),
(3, 2, 'liste 1 (Isa)'),
(4, 2, 'Liste 2 (Isa)');

-- --------------------------------------------------------

--
-- Structure de la table `todo_users`
--

CREATE TABLE `todo_users` (
  `id` int NOT NULL,
  `pseudo` varchar(100) NOT NULL,
  `pass` varchar(100) NOT NULL,
  `hash` varchar(100) NOT NULL DEFAULT 'hash'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `todo_users`
--

INSERT INTO `todo_users` (`id`, `pseudo`, `pass`, `hash`) VALUES
(1, 'tom', 'web', '10bca641466d835d3db9be02ab6e1d08'),
(2, 'isa', 'bdd', 'b9edda3aacebbf26bdfb708540070c05');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `todo_items`
--
ALTER TABLE `todo_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idList` (`idList`);

--
-- Index pour la table `todo_lists`
--
ALTER TABLE `todo_lists`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idUser` (`idUser`);

--
-- Index pour la table `todo_users`
--
ALTER TABLE `todo_users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `pseudo` (`pseudo`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `todo_items`
--
ALTER TABLE `todo_items`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `todo_lists`
--
ALTER TABLE `todo_lists`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `todo_users`
--
ALTER TABLE `todo_users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `todo_items`
--
ALTER TABLE `todo_items`
  ADD CONSTRAINT `todo_items_ibfk_1` FOREIGN KEY (`idList`) REFERENCES `todo_lists` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `todo_lists`
--
ALTER TABLE `todo_lists`
  ADD CONSTRAINT `todo_lists_ibfk_1` FOREIGN KEY (`idUser`) REFERENCES `todo_users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
