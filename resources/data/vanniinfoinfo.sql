-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cas
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genre` (
  `genreID` int NOT NULL,
  `genreName` varchar(45) NOT NULL,
  PRIMARY KEY (`genreID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'Romance'),(2,'Love'),(3,'Pop'),(4,'Rock'),(5,'Soul'),(6,'Jazz'),(7,'Country'),(8,'Hip Hop'),(9,'Heavy Metal'),(10,'Folk'),(11,'Reggae'),(12,'Indie'),(13,'Techno'),(14,'Opera'),(15,'Christian'),(16,'Disco'),(17,'Classical'),(18,'R & B'),(19,'Punk');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lastwatched`
--

DROP TABLE IF EXISTS `lastwatched`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lastwatched` (
  `lastWatchedID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `liveSetID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`lastWatchedID`),
  KEY `user_index` (`userID`),
  KEY `liveSet_index` (`liveSetID`),
  CONSTRAINT `liveSet_index` FOREIGN KEY (`liveSetID`) REFERENCES `liveset` (`liveSetID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `user_index` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lastwatched`
--

LOCK TABLES `lastwatched` WRITE;
/*!40000 ALTER TABLE `lastwatched` DISABLE KEYS */;
/*!40000 ALTER TABLE `lastwatched` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liveset`
--

DROP TABLE IF EXISTS `liveset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liveset` (
  `liveSetID` varchar(20) NOT NULL,
  `status` varchar(10) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `thumbnail` longblob NOT NULL,
  `streamLinkURL` varchar(200) NOT NULL,
  `performerID` varchar(20) NOT NULL,
  `price` int NOT NULL,
  PRIMARY KEY (`liveSetID`),
  KEY `performerID_idx` (`performerID`),
  CONSTRAINT `performerID` FOREIGN KEY (`performerID`) REFERENCES `performer` (`performerID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liveset`
--

LOCK TABLES `liveset` WRITE;
/*!40000 ALTER TABLE `liveset` DISABLE KEYS */;
/*!40000 ALTER TABLE `liveset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loyaltycard`
--

DROP TABLE IF EXISTS `loyaltycard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loyaltycard` (
  `loyaltyCardID` varchar(20) NOT NULL,
  `userID` varchar(20) NOT NULL,
  PRIMARY KEY (`loyaltyCardID`),
  KEY `userID_idx` (`userID`),
  CONSTRAINT `userID` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loyaltycard`
--

LOCK TABLES `loyaltycard` WRITE;
/*!40000 ALTER TABLE `loyaltycard` DISABLE KEYS */;
/*!40000 ALTER TABLE `loyaltycard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performer`
--

DROP TABLE IF EXISTS `performer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performer` (
  `performerID` varchar(20) NOT NULL,
  `performerName` varchar(100) NOT NULL,
  `genre` int NOT NULL,
  `performerType` int NOT NULL,
  `description` varchar(1000) NOT NULL,
  `performerStatus` varchar(20) NOT NULL,
  PRIMARY KEY (`performerID`),
  KEY `genre_idx` (`genre`),
  KEY `performerType_idx` (`performerType`),
  CONSTRAINT `genre` FOREIGN KEY (`genre`) REFERENCES `genre` (`genreID`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `performerType` FOREIGN KEY (`performerType`) REFERENCES `performertype` (`performerTypeID`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performer`
--

LOCK TABLES `performer` WRITE;
/*!40000 ALTER TABLE `performer` DISABLE KEYS */;
INSERT INTO `performer` VALUES ('6ee9c836-a8cb-4618','sdvsdvsdvsdv',1,1,'acasca','Active'),('pf001','Dilaw',1,1,'A Filipino rock band formed in Baguio, Benguet. YES SIR','Inactive'),('pf002','Ben and Ben',2,4,'Award-winning nine piece folk-pop band in Manila.','Active'),('pf003','Kamikaze',3,4,'Filipino rock band formed in 2000.','Active'),('pf004','The Animals',4,4,'British rock-band from Great Britain that started in 1963.','Active'),('pf005','Ed Sheeran',2,1,'Grammy-winning English singer-songwriter.','Active'),('pf006','Hev Abi',4,1,'Rising star in hip hop and alternative hip hop genres.','Active'),('pf007','Rob Deniel',2,1,'Filipino singer-songwriter under Viva Records.','Active'),('pf008','NIKI',1,1,'Indonesian singer-songwriter based in Los Angeles.','Active'),('pf009','Jason Dhakal',1,1,'Contemporary queer R&B singer-songwriter.','Active'),('pf010','Lola Amour',2,4,'Filipino rock band formed in Muntinlupa, Metro Manila, Philippines in 2013.','Active'),('sdvsdvsdv','Giovanni',3,1,'Giovanni','Active');
/*!40000 ALTER TABLE `performer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `performertype`
--

DROP TABLE IF EXISTS `performertype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performertype` (
  `performerTypeID` int NOT NULL,
  `pTypes` varchar(20) NOT NULL,
  PRIMARY KEY (`performerTypeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performertype`
--

LOCK TABLES `performertype` WRITE;
/*!40000 ALTER TABLE `performertype` DISABLE KEYS */;
INSERT INTO `performertype` VALUES (1,'Solo'),(2,'Duo'),(3,'Trio'),(4,'Band');
/*!40000 ALTER TABLE `performertype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchased`
--

DROP TABLE IF EXISTS `purchased`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchased` (
  `purchasedID` varchar(20) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `buyerID` varchar(20) NOT NULL,
  `ticketID` varchar(20) NOT NULL,
  PRIMARY KEY (`purchasedID`),
  KEY `buyerID_idx` (`buyerID`),
  KEY `ticketID_idx` (`ticketID`),
  CONSTRAINT `buyerID` FOREIGN KEY (`buyerID`) REFERENCES `user` (`userID`) ON UPDATE CASCADE,
  CONSTRAINT `ticketID` FOREIGN KEY (`ticketID`) REFERENCES `ticket` (`ticketID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchased`
--

LOCK TABLES `purchased` WRITE;
/*!40000 ALTER TABLE `purchased` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchased` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ticket` (
  `ticketID` varchar(20) NOT NULL,
  `liveSetID` varchar(20) NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `userID` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ticketID`),
  KEY `liveSetID_idx` (`liveSetID`),
  KEY `userID_idx` (`userID`),
  CONSTRAINT `fk_userID` FOREIGN KEY (`userID`) REFERENCES `user` (`userID`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `liveSetID` FOREIGN KEY (`liveSetID`) REFERENCES `liveset` (`liveSetID`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ticket`
--

LOCK TABLES `ticket` WRITE;
/*!40000 ALTER TABLE `ticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `userID` varchar(20) NOT NULL,
  `firstName` varchar(30) NOT NULL,
  `lastName` varchar(30) NOT NULL,
  `userName` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(20) NOT NULL,
  `watchedConsecShows` int NOT NULL DEFAULT '0',
  `userStatus` varchar(20) NOT NULL DEFAULT 'Active',
  `loyaltyCardID` varchar(20) DEFAULT '0',
  `userType` varchar(45) NOT NULL DEFAULT 'Client',
  PRIMARY KEY (`userID`),
  KEY `asc_idx` (`loyaltyCardID`),
  CONSTRAINT `asc` FOREIGN KEY (`loyaltyCardID`) REFERENCES `loyaltycard` (`loyaltyCardID`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('uID001','Giovanni','Leo','1','giovannileo100@gmail.com','1',0,'Active',NULL,'Admin'),('uID002','Alfred Christian Emmanuel','Ngaosi','ace123','acengaosi100@gmail.com','acavadvd',0,'Active',NULL,'Client'),('uID003','Judrey','Padsuyan','juduruu','judrey100@gmail.com','ahzdfbddjt',0,'Deleted',NULL,'Client'),('uID004','Eugene Kyle','Patano','yujin123','eugenekyle100@gmail.com','Sfjtdzzsd',0,'Active',NULL,'Client'),('uID005','Harry','Dominguez','HD93','harrydomin100@gmail.com','bzdjzdzfbs',0,'Active',NULL,'Client'),('uID006','Jelai','Cahanap','jelai_29','jelai100@gmail.com','djzfrehhea',0,'Active',NULL,'Client'),('uID007','Sean Justin','Aromin','sean','seanjustin100@gmail.com','sean',0,'Deleted',NULL,'Client'),('uID008','Arvy','Agabao','agaaga07','arvyarvy100@gmail.com','ejtzdgher',0,'Active',NULL,'Client'),('uID009','Jude Angelo','Ilumi','D_Howl','judeangelo100@gmail.com','SDheatjtet',0,'Active',NULL,'Client'),('uID010','Navia','Ayadna','aiban','naviaN100@gmail.com','aiban',0,'Active',NULL,'Client');
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

-- Dump completed on 2024-04-05 21:07:25
