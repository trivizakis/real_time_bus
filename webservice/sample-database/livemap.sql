-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 15, 2014 at 10:33 PM
-- Server version: 5.6.11
-- PHP Version: 5.5.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `livemap`
--
CREATE DATABASE IF NOT EXISTS `livemap` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `livemap`;

-- --------------------------------------------------------

--
-- Table structure for table `buses`
--

CREATE TABLE IF NOT EXISTS `buses` (
  `bus_id` int(11) NOT NULL AUTO_INCREMENT,
  `bus_name` varchar(20) NOT NULL,
  PRIMARY KEY (`bus_id`),
  UNIQUE KEY `bus_id` (`bus_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `buses`
--

INSERT INTO `buses` (`bus_id`, `bus_name`) VALUES
(1, '101'),
(2, '110');

-- --------------------------------------------------------

--
-- Table structure for table `livebus`
--

CREATE TABLE IF NOT EXISTS `livebus` (
  `bus_id` int(11) NOT NULL,
  `route_id` int(11) NOT NULL,
  `lat` float(10,6) NOT NULL,
  `lng` float(10,6) NOT NULL,
  PRIMARY KEY (`route_id`),
  UNIQUE KEY `bus_id` (`bus_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `livebus`
--

INSERT INTO `livebus` (`bus_id`, `route_id`, `lat`, `lng`) VALUES
(1, 1, 35.335457, 25.088612),
(2, 2, 35.338100, 25.120188);

-- --------------------------------------------------------

--
-- Table structure for table `routes`
--

CREATE TABLE IF NOT EXISTS `routes` (
  `route_id` int(11) NOT NULL AUTO_INCREMENT,
  `route` varchar(20) NOT NULL,
  PRIMARY KEY (`route_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `routes`
--

INSERT INTO `routes` (`route_id`, `route`) VALUES
(1, 'paralia'),
(2, 'petalo');

-- --------------------------------------------------------

--
-- Table structure for table `stops`
--

CREATE TABLE IF NOT EXISTS `stops` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(80) NOT NULL,
  `lat` float(10,6) NOT NULL,
  `lng` float(10,6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=33 ;

--
-- Dumping data for table `stops`
--

INSERT INTO `stops` (`id`, `address`, `lat`, `lng`) VALUES
(1, 'χανιώπορτα', 35.336441, 25.124273),
(2, 'γιόφυρο', 35.330376, 25.106956),
(3, 'ξεροπόταμος', 35.332703, 25.100969),
(4, 'λεβήνου', 35.334873, 25.116590),
(5, 'μαρινόπουλος', 35.333893, 25.095434),
(6, 'κούλε', 35.342918, 25.133739),
(7, 'λολ1', 35.342812, 25.131765),
(8, '', 35.341274, 25.129234),
(9, '', 35.341621, 25.126875),
(10, '', 35.341274, 25.125114),
(11, '', 35.341484, 25.123055),
(12, '', 35.340855, 25.120438),
(13, '', 35.340466, 25.117046),
(14, '', 35.339523, 25.113743),
(15, '', 35.338085, 25.111855),
(16, '', 35.337070, 25.109579),
(17, '', 35.335217, 25.107904),
(18, '', 35.331364, 25.106918),
(19, '', 35.333012, 25.107220),
(20, '', 35.329613, 25.106575),
(21, 'λιμάνι', 35.341442, 25.142521),
(22, '', 35.335278, 25.141577),
(23, '', 35.330517, 25.139174),
(24, '', 35.328419, 25.137886),
(25, '', 35.327648, 25.134796),
(26, '', 35.326805, 25.132221),
(27, '', 35.327507, 25.127329),
(28, '', 35.330238, 25.124754),
(29, '', 35.333038, 25.121407);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
