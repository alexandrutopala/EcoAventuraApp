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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitate`
--

LOCK TABLES `activitate` WRITE;
/*!40000 ALTER TABLE `activitate` DISABLE KEYS */;
INSERT INTO `activitate` VALUES (1,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',2,7,' $dimineata$','',2),(2,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',3,7,' $dimineata$','',2),(3,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',4,7,' $dimineata$','',2),(4,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',5,7,' $dimineata$','',2),(5,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',6,7,' $dimineata$','',2),(6,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',7,7,' $dimineata$','',2),(7,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',8,7,' $dimineata$','',2),(8,'*maria, cosmina, sabina, ciolo, topi, voje, diana, mitica, edi, tranda, ','24/07/2017',9,7,' $dimineata$','',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitategenerala`
--

LOCK TABLES `activitategenerala` WRITE;
/*!40000 ALTER TABLE `activitategenerala` DISABLE KEYS */;
INSERT INTO `activitategenerala` VALUES (1,'tiroliana'),(2,'drumetie'),(3,'piscina'),(4,'supravietuire'),(5,'disco'),(6,'fotbal'),(7,'simulare'),(8,'excursie');
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animator`
--

LOCK TABLES `animator` WRITE;
/*!40000 ALTER TABLE `animator` DISABLE KEYS */;
INSERT INTO `animator` VALUES (1,'maria',1),(2,'cosmina',1),(3,'sabina',1),(4,'ciolo',1),(6,'voje',1),(7,'diana',1),(8,'mitica',1),(9,'edi',1),(10,'anca',1),(11,'tranda',1),(12,'topicel',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `echipa`
--

LOCK TABLES `echipa` WRITE;
/*!40000 ALTER TABLE `echipa` DISABLE KEYS */;
INSERT INTO `echipa` VALUES (2,'Aventurierii din apuseni','verde','Lucian Pavel','Buga Erna',1),(3,'Curiosii din Apuseni','albastru','Lucian Pavel, Oltenita','Buga Erna',1),(4,'Marea Unire','maro','Lucian Pavel, Oltenita','Samara Angelica',1),(5,'Soimii Curajosi','alb','Spiru Haret, Oltenita','Zamfir Liliana',1),(6,'Veteranii','violet','Gimnaziala nr. 163, Bucuresti','Pompilia Liana Bugan',1),(7,'LumberJacks','indigo','Gimnaziala nr. 163, Bucuresti','Pompilia Liana Bugan',1),(8,'Strong','portocaliu','Gimnaziala nr. 163, Bucuresti','Pompilia Liana Bugan',1),(9,'Campionii','negru','Gimnaziala Lucian Pavel, Oltenita','Samara Angelica',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joc`
--

LOCK TABLES `joc` WRITE;
/*!40000 ALTER TABLE `joc` DISABLE KEYS */;
INSERT INTO `joc` VALUES (1,'*maria, ',1828,'25/07/2017',5,17,'vila irina $amiaza$','',0,3),(2,'*maria, ',2208,'25/07/2017',2,17,'vila irina $amiaza$','',0,3),(3,'*maria, ',2207,'25/07/2017',3,17,'vila irina $amiaza$','',0,3),(4,'*maria, ',1952,'25/07/2017',6,17,'vila irina $amiaza$','',0,3),(5,'*maria, ',1998,'25/07/2017',7,17,'vila irina $amiaza$','',0,3),(6,'*maria, ',1747,'25/07/2017',8,17,'vila irina $amiaza$','',0,3),(7,'*maria, ',1765,'25/07/2017',9,17,'vila irina $amiaza$','',0,3),(8,'*maria, ',1577,'25/07/2017',4,17,'vila irina $amiaza$','',0,3),(9,'*topicel, topi, ',1666,'25/07/2017',7,16,'vila irina $amiaza$','',0,3),(10,'*topicel, topi, ',1665,'25/07/2017',6,16,'vila irina $amiaza$','',0,3),(11,'*topicel, topi, ',1497,'25/07/2017',8,16,'vila irina $amiaza$','',0,3),(12,'*topicel, topi, ',1871,'25/07/2017',3,16,'vila irina $amiaza$','',0,3),(13,'*topicel, topi, ',1869,'25/07/2017',2,16,'vila irina $amiaza$','',0,3),(14,'*topicel, topi, ',1494,'25/07/2017',4,16,'vila irina $amiaza$','',0,3),(15,'*topicel, topi, ',1659,'25/07/2017',5,16,'vila irina $amiaza$','',0,3),(16,'*topicel, topi, ',1492,'25/07/2017',9,16,'vila irina $amiaza$','',0,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jocgeneral`
--

LOCK TABLES `jocgeneral` WRITE;
/*!40000 ALTER TABLE `jocgeneral` DISABLE KEYS */;
INSERT INTO `jocgeneral` VALUES (1,'penalizare','penalizare'),(2,'broquito','Broscutele trebuie sa ajunga de pe malul drept al lacului Fantanele pe malul stang, prin sarituri de broscute, contra cronometru'),(3,'stafeta','Membrii echipei trebuie sa parcurga un traseu printre jaloane cu o minge ghidata de o matura. '),(4,'tras cu arcul','numarul de sageti per membru variaza in functie de echipe'),(5,'cerculetul mut','Fiecare membru al echipei are de parcurs un traseu secret, contra cronometru.'),(6,'baseball','fiecare membru arunca o data si prinde o data\n'),(7,'ursul suparat','copii trag cu mingea prin cauciuc, fara sa-i atinga marginile'),(8,'cerculetul voinic','trebuie sa arunce cercul pe bat\n'),(9,'ciocanul lui thor','fiecare membru va parcurge o stafeta tinand ciocanul lui thor in mana'),(10,'cara minereul','membrii echipei vor forma perechi de cate 2 (in functie de inaltime) si vor duce o minge, stand spate-n spate, pana ce ajung la ligheanul pus de animator, unde lasa mingea'),(12,'ghivece','copiii arunca cu mingea in ghivece'),(13,'mingiute saltarete','copii arunca 80 de mingi intr-o galeata, incercand sa nimereasca cat mai multe'),(14,'X si 0','animatorul joaca 3 meciuri de X si 0 cu echipa, si se noteaza numarul de victoriei ale echipei'),(15,'dus-intors','stand cu spatele pe podea, copii transporta mingea din picioare in picioare, dus-intors'),(16,'transportul','copii duc in pereche de cate 2 un bat pe varful piciorului'),(17,'patratelul magic','o echipa se imparte in 2 sau mai multe parti, si parcurg un traseu, mutand patratelul anterior pana ajung in partea cealalta'),(18,'plimba cercul','echipa face o hora, cu cercul pe brate, mutandu-l de la un membru la altul\n'),(19,'joc de proba','');
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
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membruechipa`
--

LOCK TABLES `membruechipa` WRITE;
/*!40000 ALTER TABLE `membruechipa` DISABLE KEYS */;
INSERT INTO `membruechipa` VALUES (2,'Mircea Horia',2),(3,'Penu Gabriel',2),(4,'Penu Diana',2),(5,'Vasile Maria',2),(6,'Vasile David',2),(7,'Gheorghe Theodor',2),(8,'Melica Mariuca',2),(9,'Maragit Robert',3),(10,'Decu Dayana',3),(11,'Vieru Maria',3),(12,'Oancea Rafael',3),(13,'Draghici Ana-Maria',3),(14,'Cristea David',3),(15,'Valciu Cezar',3),(16,'Madin Breatrice',3),(17,'Iris Iatan',4),(18,NULL,2),(19,'Daria Tene',4),(20,'Maria Dume',4),(21,'Teodora Ivan',4),(22,'Alexandra Mocanu',4),(23,'Cristiana Stoica',4),(24,'Alexia Tiliscan',4),(25,'Ariana Comnea',4),(26,'Irina Dume',4),(27,'Teodora Burtoi',4),(28,'Tabac Sofia',5),(29,'Danaila Denisa',5),(30,'Pitu Maria',5),(31,'Tudorache Denisa',5),(32,'Raiu Eric',5),(33,'Ciolacu David',5),(34,'Sturzu Darius',5),(35,'Militaru Daniel',5),(36,'Lungu Luca',5),(37,'Pascu Alexia',6),(38,'Tonovici Antonia',6),(39,'Constantin Andreea',6),(40,'Murica Ioana',6),(41,'Ungureanu Iulian',6),(42,'Stoican Ioana',6),(43,'Vasile Adriana',6),(44,'Flueras Denis',6),(45,'Ionita Florin',6),(46,'Safta Cristian',7),(47,'Vilcea Vlad',7),(48,'Milea Valentin',7),(49,'Sanda Daniel',7),(50,'Ciltu Aimee',7),(51,'Tanase Alexia',7),(52,'Scarlat Ionica',7),(53,'Madularu Cosmin',7),(54,'Vulcan Toni',7),(55,'Ionita Iuliana',8),(56,'Ciocoi Alina',8),(57,'Zurba Iuliana',8),(58,'Cojocaru Eduard',8),(59,'Voinescu Ana',8),(60,'Dumitrescu Flori ?',8),(61,'Danila Alexandru',8),(62,'Stan Eric',8),(63,'Aldea Ciprian',8),(64,'Ganea Bianca',8),(65,'Galbinasu Stefan',9),(66,'Ernard Sandoi',9),(67,'Andu Greceanu',9),(68,'Cezar Cindescu',9),(69,'Mihnea Tene',9),(70,'Radu Marin',9),(71,'Cristian Burtoi',9),(72,'Andreea Comnea',9),(73,'Ana Paun',9),(74,'Maia Bestea',9);
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
INSERT INTO `serie` VALUES (1,1,'23/07/2017');
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Topi','Wanted2014',3),(2,'coordonator','coordonator',2),(4,'anca','1980',1),(5,'voje','alexalex',1),(6,'maria','crocodildenil',1),(7,'tranda','cinderella',1),(8,'sabina','cascaval',1),(9,'ciolo','khalifa',1),(10,'topicel','',1);
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

-- Dump completed on 2017-07-30 12:01:33
