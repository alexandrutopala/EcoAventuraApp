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
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitate`
--

LOCK TABLES `activitate` WRITE;
/*!40000 ALTER TABLE `activitate` DISABLE KEYS */;
INSERT INTO `activitate` VALUES (1,'*anim_01, *anim_04, ','17/04/2017',6,3,' $dimineata$','',9),(2,'*anim_01, *anim_04, ','17/04/2017',7,3,' $dimineata$','',9),(3,'*anim_01, ','17/04/2017',2,1,'locatie_01 $dimineata$','',9),(4,'*anim_01, ','17/04/2017',1,1,'locatie_01 $dimineata$','',9),(5,'*anim_01, ','17/04/2017',3,1,'locatie_01 $dimineata$','',9),(6,'*anim_01, ','17/04/2017',4,1,'locatie_01 $dimineata$','',9),(7,'*anim_01, *anim_04, ','17/04/2017',5,3,' $dimineata$','',9),(8,'*anim_04, *anim_05, ','17/04/2017',2,5,' $seara$','post_05',9),(9,'*anim_04, *anim_05, ','17/04/2017',3,5,' $seara$','post_05',9),(10,'*anim_04, *anim_05, ','17/04/2017',4,5,' $seara$','post_05',9),(11,'*anim_04, *anim_05, ','17/04/2017',6,5,' $seara$','post_05',9),(12,'*anim_04, *anim_05, ','17/04/2017',1,5,' $seara$','post_05',9),(13,'*anim_04, *anim_05, ','17/04/2017',7,5,' $seara$','post_05',9),(14,'*anim_04, *anim_05, ','17/04/2017',5,5,' $seara$','post_05',9),(15,'*anim_04, ','17/04/2017',5,2,' $amiaza$','',9),(16,'*anim_04, ','17/04/2017',7,2,' $amiaza$','',9),(17,'*anim_04, ','17/04/2017',6,2,' $amiaza$','',9),(18,'*anca, ','18/04/2017',9,1,' $dimineata$','',11),(19,'*anim_01, ','19/04/2017',8,1,' $dimineata$','',12),(20,'*anim_01, ','19/04/2017',10,1,' $dimineata$','',12),(21,'*anim_01, ','19/04/2017',11,1,' $dimineata$','',12),(22,'*anim_01, ','19/04/2017',12,1,' $dimineata$','',12),(23,'*anim_01, ','19/04/2017',9,1,' $dimineata$','',12),(24,'*anim_01, ','19/04/2017',8,2,' $seara$','',12),(25,'*anim_01, ','19/04/2017',9,2,' $seara$','',12),(26,'*anim_01, ','19/04/2017',11,2,' $seara$','',12),(27,'*anim_01, ','19/04/2017',10,2,' $seara$','',12),(28,'*anim_01, ','19/04/2017',12,2,' $seara$','',12),(29,'*anim_01, ','19/04/2017',8,5,' $seara$','',12),(30,'*anim_01, ','19/04/2017',10,5,' $seara$','',12),(31,'*anim_01, ','19/04/2017',11,5,' $seara$','',12),(32,'*anim_01, ','19/04/2017',9,5,' $seara$','',12),(33,'*anim_01, ','19/04/2017',12,5,' $seara$','',12),(34,'*anim_01, ','19/04/2017',9,4,' $seara$','',12),(35,'*anim_01, ','19/04/2017',8,4,' $seara$','',12),(36,'*anim_01, ','19/04/2017',12,4,' $seara$','',12),(37,'*anim_01, ','19/04/2017',11,4,' $seara$','',12),(38,'*anim_01, ','19/04/2017',10,4,' $seara$','',12),(39,'*anim_01, ','19/04/2017',8,3,' $amiaza$','',12),(40,'*anim_01, ','19/04/2017',10,3,' $amiaza$','',12),(41,'*anim_01, ','19/04/2017',11,3,' $amiaza$','',12),(42,'*anim_01, ','19/04/2017',12,3,' $amiaza$','',12),(43,'*anim_01, ','19/04/2017',9,3,' $amiaza$','',12),(44,'*anim_01, ','19/04/2017',8,4,' $dimineata$','',12),(45,'*anim_01, ','19/04/2017',9,4,' $dimineata$','',12),(46,'*anim_01, ','19/04/2017',11,4,' $dimineata$','',12),(47,'*anim_01, ','19/04/2017',12,4,' $dimineata$','',12),(48,'*anim_01, ','19/04/2017',10,4,' $dimineata$','',12),(49,'*anim_01, ','19/04/2017',8,2,' $dimineata$','',12),(50,'*anim_01, ','19/04/2017',10,2,' $dimineata$','',12),(51,'*anim_01, ','19/04/2017',9,2,' $dimineata$','',12),(52,'*anim_01, ','19/04/2017',11,2,' $dimineata$','',12),(53,'*anim_01, ','19/04/2017',12,2,' $dimineata$','',12),(54,'*anim_01, ','19/04/2017',9,1,' $amiaza$','',12),(55,'*anim_01, ','19/04/2017',10,1,' $amiaza$','',12),(56,'*anim_01, ','19/04/2017',11,1,' $amiaza$','',12),(57,'*anim_01, ','19/04/2017',8,1,' $amiaza$','',12),(58,'*anim_01, ','19/04/2017',12,1,' $amiaza$','',12),(59,'*anim_01, ','19/04/2017',8,3,' $seara$','',12),(60,'*anim_01, ','19/04/2017',10,3,' $seara$','',12),(61,'*anim_01, ','19/04/2017',9,3,' $seara$','',12),(62,'*anim_01, ','19/04/2017',11,3,' $seara$','',12),(63,'*anim_01, ','19/04/2017',12,3,' $seara$','',12),(64,'*anim_01, ','19/04/2017',8,1,' $seara$','',12),(65,'*anim_01, ','19/04/2017',9,1,' $seara$','',12),(66,'*anim_01, ','19/04/2017',10,1,' $seara$','',12),(67,'*anim_01, ','19/04/2017',11,1,' $seara$','',12),(68,'*anim_01, ','19/04/2017',12,1,' $seara$','',12),(69,'*anim_01, ','19/04/2017',8,5,' $amiaza$','',12),(70,'*anim_01, ','19/04/2017',9,5,' $amiaza$','',12),(71,'*anim_01, ','19/04/2017',10,5,' $amiaza$','',12),(72,'*anim_01, ','19/04/2017',11,5,' $amiaza$','',12),(73,'*anim_01, ','19/04/2017',12,5,' $amiaza$','',12),(74,'*anim_01, ','19/04/2017',8,5,' $dimineata$','',12),(75,'*anim_01, ','19/04/2017',9,5,' $dimineata$','',12),(76,'*anim_01, ','19/04/2017',10,5,' $dimineata$','',12),(77,'*anim_01, ','19/04/2017',11,5,' $dimineata$','',12),(78,'*anim_01, ','19/04/2017',12,5,' $dimineata$','',12),(79,'*anim_01, ','19/04/2017',8,3,' $dimineata$','',12),(80,'*anim_01, ','19/04/2017',9,3,' $dimineata$','',12),(81,'*anim_01, ','19/04/2017',10,3,' $dimineata$','',12),(82,'*anim_01, ','19/04/2017',12,3,' $dimineata$','',12),(83,'*anim_01, ','19/04/2017',11,3,' $dimineata$','',12),(84,'*anim_01, ','19/04/2017',8,2,' $amiaza$','',12),(85,'*anim_01, ','19/04/2017',10,2,' $amiaza$','',12),(86,'*anim_01, ','19/04/2017',11,2,' $amiaza$','',12),(87,'*anim_01, ','19/04/2017',12,2,' $amiaza$','',12),(88,'*anim_01, ','19/04/2017',9,2,' $amiaza$','',12),(89,'*anim_01, ','19/04/2017',8,4,' $amiaza$','',12),(90,'*anim_01, ','19/04/2017',9,4,' $amiaza$','',12),(91,'*anim_01, ','19/04/2017',11,4,' $amiaza$','',12),(92,'*anim_01, ','19/04/2017',12,4,' $amiaza$','',12),(93,'*anim_01, ','19/04/2017',10,4,' $amiaza$','',12),(94,'*anim_01, ','20/04/2017',8,1,' $dimineata$','',13),(95,'*anim_01, ','20/04/2017',9,1,' $dimineata$','',13),(96,'*anim_01, ','20/04/2017',10,1,' $dimineata$','',13),(97,'*anim_01, ','20/04/2017',11,1,' $dimineata$','',13),(98,'*anim_01, ','20/04/2017',12,1,' $dimineata$','',13),(99,'*anim_01, ','20/04/2017',9,1,' $seara$','',13),(100,'*anim_01, ','20/04/2017',10,1,' $seara$','',13),(101,'*anim_01, ','20/04/2017',8,4,' $dimineata$','',13),(102,'*anim_01, ','20/04/2017',9,4,' $dimineata$','',13),(103,'*anim_01, ','20/04/2017',10,4,' $dimineata$','',13),(104,'*anim_01, ','20/04/2017',11,4,' $dimineata$','',13),(105,'*anim_01, ','20/04/2017',12,4,' $dimineata$','',13),(106,'*anim_01, ','20/04/2017',8,5,' $dimineata$','',13),(107,'*anim_01, ','20/04/2017',9,5,' $dimineata$','',13),(108,'*anim_01, ','20/04/2017',10,5,' $dimineata$','',13),(109,'*anim_01, ','20/04/2017',11,5,' $dimineata$','',13),(110,'*anim_01, ','20/04/2017',12,5,' $dimineata$','',13),(111,'*anim_01, ','20/04/2017',8,2,' $dimineata$','',13),(112,'*anim_01, ','20/04/2017',10,2,' $dimineata$','',13),(113,'*anim_01, ','20/04/2017',11,2,' $dimineata$','',13),(114,'*anim_01, ','20/04/2017',12,2,' $dimineata$','',13),(115,'*anim_01, ','20/04/2017',9,2,' $dimineata$','',13),(116,'*anim_01, ','20/04/2017',8,2,' $amiaza$','',13),(117,'*anim_01, ','20/04/2017',9,2,' $amiaza$','',13),(118,'*anim_01, ','20/04/2017',11,2,' $amiaza$','',13),(119,'*anim_01, ','20/04/2017',12,2,' $amiaza$','',13),(120,'*anim_01, ','20/04/2017',10,2,' $amiaza$','',13),(121,'*anim_01, ','20/04/2017',8,4,' $amiaza$','',13),(122,'*anim_01, ','20/04/2017',10,4,' $amiaza$','',13),(123,'*anim_01, ','20/04/2017',9,4,' $amiaza$','',13),(124,'*anim_01, ','20/04/2017',11,4,' $amiaza$','',13),(125,'*anim_01, ','20/04/2017',12,4,' $amiaza$','',13),(126,'*anim_01, ','20/04/2017',8,3,' $dimineata$','',13),(127,'*anim_01, ','20/04/2017',9,3,' $dimineata$','',13),(128,'*anim_01, ','20/04/2017',10,3,' $dimineata$','',13),(129,'*anim_01, ','20/04/2017',11,3,' $dimineata$','',13),(130,'*anim_01, ','20/04/2017',12,3,' $dimineata$','',13),(131,'*anim_01, ','20/04/2017',8,1,' $amiaza$','',13),(132,'*anim_01, ','20/04/2017',9,1,' $amiaza$','',13),(133,'*anim_01, ','20/04/2017',10,1,' $amiaza$','',13),(134,'*anim_01, ','20/04/2017',11,1,' $amiaza$','',13),(135,'*anim_01, ','20/04/2017',12,1,' $amiaza$','',13),(136,'*anim_01, ','20/04/2017',8,3,' $amiaza$','',13),(137,'*anim_01, ','20/04/2017',9,3,' $amiaza$','',13),(138,'*anim_01, ','20/04/2017',10,3,' $amiaza$','',13),(139,'*anim_01, ','20/04/2017',11,3,' $amiaza$','',13),(140,'*anim_01, ','20/04/2017',12,3,' $amiaza$','',13),(141,'*anim_01, ','20/04/2017',8,5,' $amiaza$','',13),(142,'*anim_01, ','20/04/2017',9,5,' $amiaza$','',13),(143,'*anim_01, ','20/04/2017',10,5,' $amiaza$','',13),(144,'*anim_01, ','20/04/2017',11,5,' $amiaza$','',13),(145,'*anim_01, ','20/04/2017',12,5,' $amiaza$','',13),(146,'*anim_01, ','20/04/2017',8,1,' $seara$','',13),(147,'*anim_01, ','20/04/2017',11,1,' $seara$','',13),(148,'*anim_01, ','20/04/2017',12,1,' $seara$','',13),(149,'*anim_01, ','20/04/2017',8,2,' $seara$','',13),(150,'*anim_01, ','20/04/2017',9,2,' $seara$','',13),(151,'*anim_01, ','20/04/2017',10,2,' $seara$','',13),(152,'*anim_01, ','20/04/2017',11,2,' $seara$','',13),(153,'*anim_01, ','20/04/2017',12,2,' $seara$','',13),(154,'*anim_01, ','20/04/2017',8,3,' $seara$','',13),(155,'*anim_01, ','20/04/2017',9,3,' $seara$','',13),(156,'*anim_01, ','20/04/2017',10,3,' $seara$','',13),(157,'*anim_01, ','20/04/2017',11,3,' $seara$','',13),(158,'*anim_01, ','20/04/2017',12,3,' $seara$','',13),(159,'*anim_01, ','20/04/2017',8,4,' $seara$','',13),(160,'*anim_01, ','20/04/2017',9,4,' $seara$','',13),(161,'*anim_01, ','20/04/2017',10,4,' $seara$','',13),(162,'*anim_01, ','20/04/2017',11,4,' $seara$','',13),(163,'*anim_01, ','20/04/2017',12,4,' $seara$','',13),(164,'*anim_01, ','20/04/2017',8,5,' $seara$','',13),(165,'*anim_01, ','20/04/2017',9,5,' $seara$','',13),(166,'*anim_01, ','20/04/2017',10,5,' $seara$','',13),(167,'*anim_01, ','20/04/2017',11,5,' $seara$','',13),(168,'*anim_01, ','20/04/2017',12,5,' $seara$','',13),(169,'*anca, ','22/04/2017',13,1,' $dimineata$','',15),(170,'*anca, ','22/04/2017',13,2,' $dimineata$','',15);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitategenerala`
--

LOCK TABLES `activitategenerala` WRITE;
/*!40000 ALTER TABLE `activitategenerala` DISABLE KEYS */;
INSERT INTO `activitategenerala` VALUES (1,'activ_01'),(2,'activ_02'),(3,'activ_03'),(4,'activ_04'),(5,'activ_05');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animator`
--

LOCK TABLES `animator` WRITE;
/*!40000 ALTER TABLE `animator` DISABLE KEYS */;
INSERT INTO `animator` VALUES (1,'anim_01',1),(2,'anim_02',1),(3,'anim_03',0),(4,'anim_04',1),(5,'anim_05',1),(6,'anca',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `echipa`
--

LOCK TABLES `echipa` WRITE;
/*!40000 ALTER TABLE `echipa` DISABLE KEYS */;
INSERT INTO `echipa` VALUES (1,'1','maro','1','1',1),(2,'2','albastru','2','2',1),(3,'3','galben','3','3',1),(4,'4','verde','4','4',1),(5,'5','rosu','5','5',1),(6,'6','portocaliu','6','6',1),(7,'7','indigo','7','7',1),(8,'1','maro','1','1',2),(9,'2','albastru','2','2',2),(10,'3','galben','3','3',2),(11,'4','verde','4','4',2),(12,'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa','rosu','22222222222222222222222222222222222222222222','33333333333333333333333333333333333333333333',2),(13,'1','maro','1','1',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joc`
--

LOCK TABLES `joc` WRITE;
/*!40000 ALTER TABLE `joc` DISABLE KEYS */;
INSERT INTO `joc` VALUES (1,'*anim_01, *anim_02, *anim_05, ',150,'17/04/2017',1,4,' $dimineata$','',0,9),(2,'*anim_01, *anim_02, *anim_05, ',170,'17/04/2017',2,4,' $dimineata$','',0,9),(3,'*anim_01, *anim_02, *anim_05, ',160,'17/04/2017',3,4,' $dimineata$','',0,9),(4,'*anim_01, *anim_02, *anim_05, ',153,'17/04/2017',4,4,' $dimineata$','',0,9),(5,'*anim_02, ',97,'17/04/2017',5,7,' $dimineata$','',0,9),(6,'*anim_02, ',185,'17/04/2017',7,7,' $dimineata$','',0,9),(7,'*anim_02, ',181,'17/04/2017',6,7,' $dimineata$','',0,9),(8,'*anim_02, ',28250,'17/04/2017',1,8,' $amiaza$','',0,9),(9,'*anim_02, ',0,'17/04/2017',3,8,' $amiaza$','',1,9),(10,'*anim_02, ',3933,'17/04/2017',4,8,' $amiaza$','',0,9),(11,'*anim_02, ',6633,'17/04/2017',2,8,' $amiaza$','',0,9),(12,'*anim_02, ',4083,'17/04/2017',5,8,' $amiaza$','',0,9),(13,'*anim_02, ',0,'17/04/2017',6,8,' $amiaza$','',1,9),(14,'*anim_02, ',7875,'17/04/2017',7,8,' $amiaza$','',0,9),(38,'anca, ',1,'18/04/2017',8,1,'1','',0,0),(39,'*anca, ',1433,'18/04/2017',8,8,'in sufragerie $dimineata$','1',0,11),(40,'*anca, ',2800,'18/04/2017',9,8,'in sufragerie $dimineata$','1',0,11),(41,'anim_01, ',100,'19/04/2017',9,1,'100','',0,0),(42,'anim_01, ',100,'19/04/2017',9,1,'100','',0,0),(43,'*anim_01, ',0,'19/04/2017',10,8,' $dimineata$','',0,12),(44,'*anim_01, ',0,'19/04/2017',11,8,' $dimineata$','',0,12),(45,'*anim_01, ',4500,'19/04/2017',12,8,' $dimineata$','',0,12),(46,'*anim_01, ',95,'19/04/2017',9,7,' $dimineata$','',0,12),(47,'*anim_01, ',4,'19/04/2017',10,7,' $dimineata$','',1,12),(48,'*anim_01, ',57,'19/04/2017',11,7,' $dimineata$','',0,12),(49,'*anim_01, ',48,'19/04/2017',12,7,' $dimineata$','',0,12),(50,'*anim_01, ',42,'19/04/2017',8,7,' $dimineata$','',0,12),(51,'*anim_01, ',6,'19/04/2017',8,9,' $amiaza$','',0,12),(52,'*anim_01, ',20,'19/04/2017',10,9,' $amiaza$','',0,12),(53,'*anim_01, ',10,'19/04/2017',12,9,' $amiaza$','',0,12),(54,'*anim_01, ',5,'19/04/2017',11,9,' $amiaza$','',0,12),(55,'*anim_01, ',3,'19/04/2017',9,9,' $amiaza$','',0,12),(56,'*anim_01, ',2,'19/04/2017',8,10,' $amiaza$','',0,12),(57,'*anim_01, ',10,'19/04/2017',10,10,' $amiaza$','',0,12),(58,'*anim_01, ',20,'19/04/2017',9,10,' $amiaza$','',0,12),(59,'*anim_01, ',14,'19/04/2017',12,10,' $amiaza$','',0,12),(60,'*anim_01, ',15,'19/04/2017',11,10,' $amiaza$','',0,12),(61,'anim_01, ',10,'20/04/2017',12,1,'10','',0,0),(62,'*anim_01, ',3625,'21/04/2017',11,8,' $dimineata$','',0,13),(63,'anim_01, ',2,'21/04/2017',11,1,'2','',0,0),(64,'anca, ',12,'22/04/2017',13,1,'12','',0,0),(65,'*anca, ',325,'22/04/2017',13,8,' $amiaza$','',0,15),(66,'*anca, ',460,'22/04/2017',13,7,' $dimineata$','',0,15),(67,'*anca, ',255,'22/04/2017',13,11,' $dimineata$','',0,15);
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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jocgeneral`
--

LOCK TABLES `jocgeneral` WRITE;
/*!40000 ALTER TABLE `jocgeneral` DISABLE KEYS */;
INSERT INTO `jocgeneral` VALUES (1,'penalizare','penalizare'),(4,'joc_01','Descrierea jocului joc_01'),(5,'joc_02','lfmldfv'),(6,'joc_03',''),(7,'joc_04',''),(8,'joc_05','joc care depinde de numarul de membri'),(9,'joc_gradi','gradi'),(10,'joc_generala','pt generala'),(11,'tras cu arcu','iei o sageata si vezi ce facu cu ea');
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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membruechipa`
--

LOCK TABLES `membruechipa` WRITE;
/*!40000 ALTER TABLE `membruechipa` DISABLE KEYS */;
INSERT INTO `membruechipa` VALUES (1,'1_1',1),(2,'1_2',1),(3,'1_3',1),(4,'1_4',1),(5,'2_1',2),(6,'2_2',2),(7,'2_3',2),(8,'2_4',2),(9,'2_5',2),(10,'3_1',3),(11,'3_2',3),(12,'3_3',3),(13,'3_4',3),(14,'4_1',4),(15,'4_2',4),(16,'4_3',4),(17,'5_1',5),(18,'5_2',5),(19,'5_3',5),(20,'5_4',5),(21,'5_5',5),(22,'5_6',5),(23,'6_1',6),(24,'6_2',6),(25,'6_3',6),(26,'7_1',7),(27,'7_2',7),(28,'7_3',7),(29,'7_4',7),(30,'1_1',8),(31,'1_2',8),(32,'1_3',8),(33,'2_1',9),(34,'2_2',9),(35,'2_3',9),(36,'3_1',10),(37,'3_2',10),(38,'3_3',10),(39,'3_4',10),(40,'4_1',11),(41,'4_2',11),(42,'4_3',11),(43,'4_4',11),(44,'mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm',12),(45,'1_4',8),(46,'1_1',13),(47,'1_2',13),(48,'1_3',13),(49,'1_4',13),(50,'1_5',13),(51,'1_6',13),(52,'1_7',13),(53,'1_8',13);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serie`
--

LOCK TABLES `serie` WRITE;
/*!40000 ALTER TABLE `serie` DISABLE KEYS */;
INSERT INTO `serie` VALUES (1,1,'16/04/2017'),(2,2,'17/04/2017'),(3,3,'21/04/2017');
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Topi','Wanted2014',3),(2,'cornel','1234',2),(3,'anim_01','',1),(4,'anim_02','',1),(5,'anim_04','',1),(6,'anim_05','',1),(8,'anim_03','',1),(9,'anca','anca',1);
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

-- Dump completed on 2017-04-22 19:06:43
