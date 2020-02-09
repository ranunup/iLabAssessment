-- MySQL dump 10.13  Distrib 5.7.17, for osx10.12 (x86_64)
--
-- Host: localhost    Database: iLab
-- ------------------------------------------------------
-- Server version	8.0.3-rc-log

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
-- Table structure for table `test_case`
--

CREATE Database iLab;

DROP TABLE IF EXISTS `test_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_case` (
  `seq_id` int(11) NOT NULL,
  `tc_command` varchar(25) NOT NULL,
  `tc_value` varchar(65) NOT NULL,
  `tc_id` varchar(25) NOT NULL,
  PRIMARY KEY (`seq_id`,`tc_id`),
  KEY `tc_id_fk_idx` (`tc_id`),
  CONSTRAINT `tc_id_fk` FOREIGN KEY (`tc_id`) REFERENCES `test_suite` (`ts_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case`
--

LOCK TABLES `test_case` WRITE;
/*!40000 ALTER TABLE `test_case` DISABLE KEYS */;
INSERT INTO `test_case` VALUES (1,'navigateTo','https://www.ilabquality.com/careers/','RegisterOnlineTest_01'),(2,'clickByHrefText','South Africa','RegisterOnlineTest_01'),(3,'clickHrefAfterText','CURRENT OPENINGS','RegisterOnlineTest_01'),(4,'clickButton','Apply Online','RegisterOnlineTest_01'),(5,'insertTextById','applicant_name/Phumlani','RegisterOnlineTest_01'),(6,'insertTextById','email/automationAssessment@iLABQuality.com','RegisterOnlineTest_01'),(7,'insertCellNoById','phone','RegisterOnlineTest_01'),(8,'clickInputByValue','Send Application','RegisterOnlineTest_01'),(9,'validateErrorText','You need to upload at least one file.','RegisterOnlineTest_01');
/*!40000 ALTER TABLE `test_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_suite`
--

DROP TABLE IF EXISTS `test_suite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test_suite` (
  `ts_id` varchar(25) NOT NULL,
  `ts_description` varchar(45) NOT NULL,
  `exec_flag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ts_id`),
  UNIQUE KEY `ts_id_UNIQUE` (`ts_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_suite`
--

LOCK TABLES `test_suite` WRITE;
/*!40000 ALTER TABLE `test_suite` DISABLE KEYS */;
INSERT INTO `test_suite` VALUES ('RegisterOnlineTest_01','Perform Online Registration Process',1);
/*!40000 ALTER TABLE `test_suite` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-02-08 13:20:14
