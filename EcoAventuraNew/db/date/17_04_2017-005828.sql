-- MySQL dump 10.16  Distrib 10.1.19-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.1.19-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activitate`
--

DROP TABLE IF EXISTS `activitate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activitate` (
  `idActivitate` int(11) NOT NULL AUTO_INCREMENT,
  `organizator` varchar(150) DEFAULT NULL,
  `data` varchar(45) DEFAULT NULL,
  `Echipa_idEchipa` int(11) NOT NULL,
  `ActivitateGenerala_idActivitateGenerala` int(11) NOT NULL,
  `loactie` varchar(50) DEFAULT NULL,
  `post` varchar(50) DEFAULT NULL,
  `idProgram` int(20) DEFAULT NULL,
  PRIMARY KEY (`idActivitate`),
  KEY `fk_Activitate_Echipa1_idx` (`Echipa_idEchipa`),
  KEY `fk_Activitate_ActivitateGenerala1_idx` (`ActivitateGenerala_idActivitateGenerala`),
  CONSTRAINT `fk_Activitate_ActivitateGenerala1` FOREIGN KEY (`ActivitateGenerala_idActivitateGenerala`) REFERENCES `activitategenerala` (`idActivitateGenerala`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Activitate_Echipa1` FOREIGN KEY (`Echipa_idEchipa`) REFERENCES `echipa` (`idEchipa`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitate`
--

LOCK TABLES `activitate` WRITE;
/*!40000 ALTER TABLE `activitate` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activitategenerala`
--

DROP TABLE IF EXISTS `activitategenerala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activitategenerala` (
  `idActivitateGenerala` int(11) NOT NULL AUTO_INCREMENT,
  `numeActivitateGenerala` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idActivitateGenerala`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitategenerala`
--

LOCK TABLES `activitategenerala` WRITE;
/*!40000 ALTER TABLE `activitategenerala` DISABLE KEYS */;
/*!40000 ALTER TABLE `activitategenerala` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `animator`
--

DROP TABLE IF EXISTS `animator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `animator` (
  `idAnimator` int(11) NOT NULL AUTO_INCREMENT,
  `numeAnimator` varchar(45) DEFAULT NULL,
  `disponibilAnimator` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idAnimator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animator`
--

LOCK TABLES `animator` WRITE;
/*!40000 ALTER TABLE `animator` DISABLE KEYS */;
/*!40000 ALTER TABLE `animator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `echipa`
--

DROP TABLE IF EXISTS `echipa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `echipa` (
  `idEchipa` int(11) NOT NULL AUTO_INCREMENT,
  `numeEchipa` varchar(45) DEFAULT NULL,
  `culoareEchipa` varchar(45) DEFAULT NULL,
  `scoalaEchipa` varchar(45) DEFAULT NULL,
  `profEchipa` varchar(45) DEFAULT NULL,
  `Serie_idSerie` int(11) NOT NULL,
  PRIMARY KEY (`idEchipa`),
  KEY `fk_Echipa_Serie_idx` (`Serie_idSerie`),
  CONSTRAINT `fk_Echipa_Serie` FOREIGN KEY (`Serie_idSerie`) REFERENCES `serie` (`idSerie`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `echipa`
--

LOCK TABLES `echipa` WRITE;
/*!40000 ALTER TABLE `echipa` DISABLE KEYS */;
INSERT INTO `echipa` VALUES (1,'1','maro','1','1',1);
/*!40000 ALTER TABLE `echipa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `joc`
--

DROP TABLE IF EXISTS `joc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `joc` (
  `idJoc` int(11) NOT NULL AUTO_INCREMENT,
  `organizator` varchar(150) DEFAULT NULL,
  `punctaj` int(11) DEFAULT NULL,
  `data` varchar(45) DEFAULT NULL,
  `Echipa_idEchipa` int(11) NOT NULL,
  `JocGeneral_idJocGeneral` int(11) NOT NULL,
  `locatie` varchar(50) DEFAULT NULL,
  `post` varchar(50) DEFAULT NULL,
  `absent` tinyint(1) DEFAULT NULL,
  `idProgram` int(20) DEFAULT NULL,
  PRIMARY KEY (`idJoc`),
  KEY `fk_Joc_Echipa1_idx` (`Echipa_idEchipa`),
  KEY `fk_Joc_JocGeneral1_idx` (`JocGeneral_idJocGeneral`),
  CONSTRAINT `fk_Joc_Echipa1` FOREIGN KEY (`Echipa_idEchipa`) REFERENCES `echipa` (`idEchipa`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Joc_JocGeneral1` FOREIGN KEY (`JocGeneral_idJocGeneral`) REFERENCES `jocgeneral` (`idJocGeneral`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joc`
--

LOCK TABLES `joc` WRITE;
/*!40000 ALTER TABLE `joc` DISABLE KEYS */;
/*!40000 ALTER TABLE `joc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jocgeneral`
--

DROP TABLE IF EXISTS `jocgeneral`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jocgeneral` (
  `idJocGeneral` int(11) NOT NULL AUTO_INCREMENT,
  `numeJocGeneral` varchar(45) DEFAULT NULL,
  `descriereJoc` varchar(1010) DEFAULT NULL,
  PRIMARY KEY (`idJocGeneral`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jocgeneral`
--

LOCK TABLES `jocgeneral` WRITE;
/*!40000 ALTER TABLE `jocgeneral` DISABLE KEYS */;
INSERT INTO `jocgeneral` VALUES (1,'penalizare','penalizare');
/*!40000 ALTER TABLE `jocgeneral` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membruechipa`
--

DROP TABLE IF EXISTS `membruechipa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membruechipa` (
  `idMembruEchipa` int(11) NOT NULL AUTO_INCREMENT,
  `numeMembruEchipa` varchar(45) DEFAULT NULL,
  `Echipa_idEchipa` int(11) NOT NULL,
  PRIMARY KEY (`idMembruEchipa`),
  KEY `fk_MembruEchipa_Echipa1_idx` (`Echipa_idEchipa`),
  CONSTRAINT `fk_MembruEchipa_Echipa1` FOREIGN KEY (`Echipa_idEchipa`) REFERENCES `echipa` (`idEchipa`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membruechipa`
--

LOCK TABLES `membruechipa` WRITE;
/*!40000 ALTER TABLE `membruechipa` DISABLE KEYS */;
INSERT INTO `membruechipa` VALUES (1,'1_1',1);
/*!40000 ALTER TABLE `membruechipa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serie`
--

DROP TABLE IF EXISTS `serie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serie` (
  `idSerie` int(11) NOT NULL AUTO_INCREMENT,
  `numarSerie` int(11) DEFAULT NULL,
  `dataInceput` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idSerie`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serie`
--

LOCK TABLES `serie` WRITE;
/*!40000 ALTER TABLE `serie` DISABLE KEYS */;
INSERT INTO `serie` VALUES (1,1,'16/04/2017');
/*!40000 ALTER TABLE `serie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) DEFAULT NULL,
  `parola` varchar(45) DEFAULT NULL,
  `acces` int(11) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Topi','Wanted2014',3),(2,'cornel','1234',2);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-04-17  0:58:42
