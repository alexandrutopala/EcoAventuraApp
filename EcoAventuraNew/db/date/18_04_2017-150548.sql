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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activitate`
--

LOCK TABLES `activitate` WRITE;
/*!40000 ALTER TABLE `activitate` DISABLE KEYS */;
INSERT INTO `activitate` VALUES (1,'*anim_01, *anim_04, ','17/04/2017',6,3,' $dimineata$','',9),(2,'*anim_01, *anim_04, ','17/04/2017',7,3,' $dimineata$','',9),(3,'*anim_01, ','17/04/2017',2,1,'locatie_01 $dimineata$','',9),(4,'*anim_01, ','17/04/2017',1,1,'locatie_01 $dimineata$','',9),(5,'*anim_01, ','17/04/2017',3,1,'locatie_01 $dimineata$','',9),(6,'*anim_01, ','17/04/2017',4,1,'locatie_01 $dimineata$','',9),(7,'*anim_01, *anim_04, ','17/04/2017',5,3,' $dimineata$','',9),(8,'*anim_04, *anim_05, ','17/04/2017',2,5,' $seara$','post_05',9),(9,'*anim_04, *anim_05, ','17/04/2017',3,5,' $seara$','post_05',9),(10,'*anim_04, *anim_05, ','17/04/2017',4,5,' $seara$','post_05',9),(11,'*anim_04, *anim_05, ','17/04/2017',6,5,' $seara$','post_05',9),(12,'*anim_04, *anim_05, ','17/04/2017',1,5,' $seara$','post_05',9),(13,'*anim_04, *anim_05, ','17/04/2017',7,5,' $seara$','post_05',9),(14,'*anim_04, *anim_05, ','17/04/2017',5,5,' $seara$','post_05',9),(15,'*anim_04, ','17/04/2017',5,2,' $amiaza$','',9),(16,'*anim_04, ','17/04/2017',7,2,' $amiaza$','',9),(17,'*anim_04, ','17/04/2017',6,2,' $amiaza$','',9),(18,'*anim_01, *anim_05, ','18/04/2017',12,2,' $dimineata$','',10),(19,'*anim_01, *anim_05, ','18/04/2017',13,2,' $dimineata$','',10),(20,'*anim_01, *anim_05, ','18/04/2017',15,2,' $dimineata$','',10),(21,'*anim_01, *anim_05, ','18/04/2017',14,2,' $dimineata$','',10),(22,'*anim_01, *anim_02, *anim_05, ','18/04/2017',13,3,' $amiaza$','',10),(23,'*anim_01, *anim_02, *anim_05, ','18/04/2017',14,3,' $amiaza$','',10),(24,'*anim_01, *anim_02, *anim_05, ','18/04/2017',12,3,' $amiaza$','',10),(25,'*anim_01, *anim_02, *anim_05, ','18/04/2017',15,3,' $amiaza$','',10),(26,'*anim_01, *anim_05, ','18/04/2017',12,1,' $dimineata$','',10),(27,'*anim_01, *anim_05, ','18/04/2017',13,1,' $dimineata$','',10),(28,'*anim_01, *anim_05, ','18/04/2017',15,1,' $dimineata$','',10),(29,'*anim_01, *anim_05, ','18/04/2017',14,1,' $dimineata$','',10),(30,'*anim_04, *anim_05, ','18/04/2017',12,5,' $seara$','',10),(31,'*anim_04, *anim_05, ','18/04/2017',14,5,' $seara$','',10),(32,'*anim_04, *anim_05, ','18/04/2017',13,5,' $seara$','',10),(33,'*anim_04, *anim_05, ','18/04/2017',15,5,' $seara$','',10),(34,'*anim_04, *anim_05, ','18/04/2017',12,4,' $seara$','',10),(35,'*anim_04, *anim_05, ','18/04/2017',13,4,' $seara$','',10),(36,'*anim_04, *anim_05, ','18/04/2017',14,4,' $seara$','',10),(37,'*anim_04, *anim_05, ','18/04/2017',15,4,' $seara$','',10);
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animator`
--

LOCK TABLES `animator` WRITE;
/*!40000 ALTER TABLE `animator` DISABLE KEYS */;
INSERT INTO `animator` VALUES (1,'anim_01',1),(2,'anim_02',1),(3,'anim_03',0),(4,'anim_04',1),(5,'anim_05',1);
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `echipa`
--

LOCK TABLES `echipa` WRITE;
/*!40000 ALTER TABLE `echipa` DISABLE KEYS */;
INSERT INTO `echipa` VALUES (1,'1','maro','1','1',1),(2,'2','albastru','2','2',1),(3,'3','galben','3','3',1),(4,'4','verde','4','4',1),(5,'5','rosu','5','5',1),(6,'6','portocaliu','6','6',1),(7,'7','indigo','7','7',1),(8,'1','maro','1','1',2),(9,'2','albastru','2','2',2),(10,'3','galben','3','3',2),(11,'4','verde','4','4',2),(12,'1','maro','1','1',3),(13,'2','albastru','2','2',3),(14,'3','galben','3','3',3),(15,'4','verde','4','4',3);
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
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `joc`
--

LOCK TABLES `joc` WRITE;
/*!40000 ALTER TABLE `joc` DISABLE KEYS */;
INSERT INTO `joc` VALUES (1,'*anim_01, *anim_02, *anim_05, ',150,'17/04/2017',1,4,' $dimineata$','',0,9),(2,'*anim_01, *anim_02, *anim_05, ',170,'17/04/2017',2,4,' $dimineata$','',0,9),(3,'*anim_01, *anim_02, *anim_05, ',160,'17/04/2017',3,4,' $dimineata$','',0,9),(4,'*anim_01, *anim_02, *anim_05, ',153,'17/04/2017',4,4,' $dimineata$','',0,9),(5,'*anim_02, ',97,'17/04/2017',5,7,' $dimineata$','',0,9),(6,'*anim_02, ',185,'17/04/2017',7,7,' $dimineata$','',0,9),(7,'*anim_02, ',181,'17/04/2017',6,7,' $dimineata$','',0,9),(8,'*anim_02, ',28250,'17/04/2017',1,8,' $amiaza$','',0,9),(9,'*anim_02, ',0,'17/04/2017',3,8,' $amiaza$','',1,9),(10,'*anim_02, ',3933,'17/04/2017',4,8,' $amiaza$','',0,9),(11,'*anim_02, ',6633,'17/04/2017',2,8,' $amiaza$','',0,9),(12,'*anim_02, ',4083,'17/04/2017',5,8,' $amiaza$','',0,9),(13,'*anim_02, ',0,'17/04/2017',6,8,' $amiaza$','',1,9),(14,'*anim_02, ',7875,'17/04/2017',7,8,' $amiaza$','',0,9),(15,'*anim_01, ',0,'18/04/2017',12,6,' $dimineata$','',0,10),(16,'*anim_01, ',25,'18/04/2017',14,6,' $dimineata$','',0,10),(17,'*anim_01, ',13,'18/04/2017',13,6,' $dimineata$','',0,10),(18,'*anim_01, ',69,'18/04/2017',15,6,' $dimineata$','',0,10),(19,'*anim_02, ',50,'18/04/2017',12,4,' $amiaza$','',0,10),(20,'*anim_02, ',58,'18/04/2017',14,4,' $amiaza$','',0,10),(21,'*anim_02, ',80,'18/04/2017',15,4,' $amiaza$','',0,10),(22,'*anim_02, ',25,'18/04/2017',13,4,' $amiaza$','',0,10),(23,'*anim_02, ',7000,'18/04/2017',13,8,' $amiaza$','',0,10),(24,'*anim_02, ',3200,'18/04/2017',14,8,' $amiaza$','',0,10),(25,'anim_02, ',100,'18/04/2017',14,1,'100','',0,0),(26,'*anim_04, ',319,'18/04/2017',12,7,' $seara$','',0,10),(27,'*anim_04, ',229,'18/04/2017',14,7,' $seara$','',0,10),(28,'*anim_04, ',108,'18/04/2017',15,7,' $seara$','',0,10),(29,'*anim_04, ',365,'18/04/2017',13,7,' $seara$','',0,10),(30,'*anim_02, ',3333,'18/04/2017',12,8,' $amiaza$','',0,10),(31,'*anim_02, ',2750,'18/04/2017',15,8,' $amiaza$','',0,10);
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jocgeneral`
--

LOCK TABLES `jocgeneral` WRITE;
/*!40000 ALTER TABLE `jocgeneral` DISABLE KEYS */;
INSERT INTO `jocgeneral` VALUES (1,'penalizare','penalizare'),(4,'joc_01','Descrierea jocului joc_01'),(5,'joc_02','lfmldfv'),(6,'joc_03',''),(7,'joc_04',''),(8,'joc_05','joc care depinde de numarul de membri');
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
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membruechipa`
--

LOCK TABLES `membruechipa` WRITE;
/*!40000 ALTER TABLE `membruechipa` DISABLE KEYS */;
INSERT INTO `membruechipa` VALUES (1,'1_1',1),(2,'1_2',1),(3,'1_3',1),(4,'1_4',1),(5,'2_1',2),(6,'2_2',2),(7,'2_3',2),(8,'2_4',2),(9,'2_5',2),(10,'3_1',3),(11,'3_2',3),(12,'3_3',3),(13,'3_4',3),(14,'4_1',4),(15,'4_2',4),(16,'4_3',4),(17,'5_1',5),(18,'5_2',5),(19,'5_3',5),(20,'5_4',5),(21,'5_5',5),(22,'5_6',5),(23,'6_1',6),(24,'6_2',6),(25,'6_3',6),(26,'7_1',7),(27,'7_2',7),(28,'7_3',7),(29,'7_4',7),(30,'1_1',8),(31,'1_2',8),(32,'1_3',8),(33,'2_1',9),(34,'2_2',9),(35,'2_3',9),(36,'3_1',10),(37,'3_2',10),(38,'3_3',10),(39,'3_4',10),(40,'4_1',11),(41,'4_2',11),(42,'4_3',11),(43,'4_4',11),(44,'1_1',12),(45,'1_2',12),(46,'1_3',12),(47,'2_1',13),(48,'2_2',13),(49,'2_3',13),(50,'3_1',14),(51,'3_2',14),(52,'3_3',14),(53,'3_4',14),(54,'4_1',15),(55,'4_2',15),(56,'4_3',15),(57,'4_4',15);
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
INSERT INTO `serie` VALUES (1,1,'16/04/2017'),(2,2,'17/04/2017'),(3,3,'18/04/2017');
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Topi','Wanted2014',3),(2,'cornel','1234',2),(3,'anim_01','',1),(4,'anim_02','',1),(5,'anim_04','',1),(6,'anim_05','',1),(7,'anim_03','',1);
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

-- Dump completed on 2017-04-18 15:05:50
