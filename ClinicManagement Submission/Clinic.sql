CREATE DATABASE  IF NOT EXISTS `hospital` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `hospital`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: hospital
-- ------------------------------------------------------
-- Server version	9.7.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '6ded7dbe-485d-11f1-ace2-005056c00001:1-391';

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `StaffID` int NOT NULL,
  `SpecificRole` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`StaffID`),
  CONSTRAINT `fk_admin_staff_id` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (5,'Hospital Administrator'),(14,'Receptinist');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `AppointmentID` int NOT NULL,
  `AppointmentDate` date DEFAULT NULL,
  `AppointmentTime` time DEFAULT NULL,
  `Status` varchar(20) DEFAULT NULL,
  `PatientID` int DEFAULT NULL,
  `DoctorID` int DEFAULT NULL,
  `RoomNumber` int DEFAULT NULL,
  PRIMARY KEY (`AppointmentID`),
  KEY `PatientID` (`PatientID`),
  KEY `DoctorID` (`DoctorID`),
  KEY `RoomNumber` (`RoomNumber`),
  CONSTRAINT `Appointment_ibfk_1` FOREIGN KEY (`PatientID`) REFERENCES `patient` (`PatientID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Appointment_ibfk_2` FOREIGN KEY (`DoctorID`) REFERENCES `doctor` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Appointment_ibfk_3` FOREIGN KEY (`RoomNumber`) REFERENCES `room` (`RoomNumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (1001,'2026-05-15','09:00:00','Scheduled',7,1,101),(1002,'2026-05-15','10:30:00','Completed',8,2,102),(1003,'2026-05-16','11:00:00','Completed',9,4,103),(1004,'2026-05-16','14:00:00','Scheduled',10,6,104),(1005,'2026-05-17','08:30:00','Cancelled',11,1,105),(1006,'2026-05-17','15:30:00','Scheduled',12,4,106),(1007,'2026-05-07','09:00:00','Completed',10,1,104),(1008,'2026-05-08','10:00:00','Completed',11,4,104),(1009,'2026-05-08','19:00:00','Completed',8,4,103),(1010,'2026-05-10','09:00:00','Scheduled',10,2,103),(1011,'2026-05-10','09:00:00','Completed',13,2,103),(1012,'2026-05-11','09:00:00','Scheduled',7,4,104),(1013,'2026-05-11','09:00:00','Scheduled',8,1,101),(1014,'2026-05-11','09:00:00','Completed',11,1,101),(1015,'2026-05-11','09:00:00','Completed',7,1,101),(1016,'2026-05-11','09:00:00','Scheduled',7,4,101),(1017,'2026-05-11','09:00:00','Scheduled',7,6,101),(1018,'2026-05-11','09:00:00','Scheduled',7,1,101);
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_appointment_insert_check` BEFORE INSERT ON `appointment` FOR EACH ROW BEGIN
  DECLARE patient_exists INT;
  SELECT COUNT(*) INTO patient_exists FROM Patient WHERE PatientID = NEW.PatientID;
  IF patient_exists = 0 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot book appointment: Patient does not exist.';
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `billing`
--

DROP TABLE IF EXISTS `billing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billing` (
  `BillingID` int NOT NULL AUTO_INCREMENT,
  `BillingDate` date DEFAULT NULL,
  `TotalAmount` decimal(10,2) DEFAULT NULL,
  `AppointmentID` int DEFAULT NULL,
  `PaymentStatus` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`BillingID`),
  KEY `AppointmentID` (`AppointmentID`),
  CONSTRAINT `Billing_ibfk_1` FOREIGN KEY (`AppointmentID`) REFERENCES `appointment` (`AppointmentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5010 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing`
--

LOCK TABLES `billing` WRITE;
/*!40000 ALTER TABLE `billing` DISABLE KEYS */;
INSERT INTO `billing` VALUES (5001,'2026-05-15',850.00,1001,'Paid'),(5002,'2026-05-15',950.00,1002,'Paid'),(5003,'2026-05-16',650.00,1003,'Paid'),(5004,'2026-05-16',800.00,1004,'Paid'),(5005,'2026-05-17',550.00,1005,'Paid'),(5006,'2026-05-17',750.00,1006,'Paid'),(5008,'2026-05-10',2500.00,1007,'Paid'),(5009,'2026-05-10',5000.00,1009,'Paid');
/*!40000 ALTER TABLE `billing` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_billing_set_date` BEFORE INSERT ON `billing` FOR EACH ROW BEGIN
  IF NEW.BillingDate IS NULL THEN
    SET NEW.BillingDate = CURDATE();
  END IF;
  IF NEW.PaymentStatus IS NULL THEN
    SET NEW.PaymentStatus = 'Unpaid';
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card` (
  `PaymentID` int NOT NULL,
  `CardNumber` varchar(20) DEFAULT NULL,
  `CardType` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`PaymentID`),
  CONSTRAINT `Card_ibfk_1` FOREIGN KEY (`PaymentID`) REFERENCES `payment` (`PaymentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES (9001,'4111111111111111','Visa'),(9003,'5555555555554444','Mastercard'),(9005,'378282246310005','American Express'),(9013,'1213212213231','Visa'),(9015,'992881829','master');
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cash`
--

DROP TABLE IF EXISTS `cash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash` (
  `PaymentID` int NOT NULL,
  PRIMARY KEY (`PaymentID`),
  CONSTRAINT `Cash_ibfk_1` FOREIGN KEY (`PaymentID`) REFERENCES `payment` (`PaymentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cash`
--

LOCK TABLES `cash` WRITE;
/*!40000 ALTER TABLE `cash` DISABLE KEYS */;
INSERT INTO `cash` VALUES (9002),(9004);
/*!40000 ALTER TABLE `cash` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `DepartmentID` int NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Location` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DepartmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'Cardiology','3rd Floor, East Wing'),(2,'Neurology','2nd Floor, West Wing'),(3,'Pediatrics','1st Floor, North Wing'),(4,'Orthopedics','Ground Floor, South Wing'),(5,'General Medicine','1st Floor, East Wing'),(6,'Psychiatry','4th Floor, West Wing');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosis`
--

DROP TABLE IF EXISTS `diagnosis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diagnosis` (
  `DocID` int DEFAULT NULL,
  `PatientID` int DEFAULT NULL,
  `StaffID` int DEFAULT NULL,
  KEY `diagnosisKey` (`DocID`),
  KEY `fk_diag_patient` (`PatientID`),
  KEY `fk_diag_staff` (`StaffID`),
  CONSTRAINT `diagnosisKey` FOREIGN KEY (`DocID`) REFERENCES `medicaldocument` (`DocID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_diag_patient` FOREIGN KEY (`PatientID`) REFERENCES `patient` (`PatientID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_diag_staff` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosis`
--

LOCK TABLES `diagnosis` WRITE;
/*!40000 ALTER TABLE `diagnosis` DISABLE KEYS */;
INSERT INTO `diagnosis` VALUES (2006,12,4),(2007,13,1),(2008,7,2),(2009,8,4),(2010,9,1);
/*!40000 ALTER TABLE `diagnosis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `StaffID` int NOT NULL,
  `LicenseNumber` varchar(30) NOT NULL,
  PRIMARY KEY (`StaffID`),
  CONSTRAINT `fk_doctor_staff_id` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES (1,'LIC001234'),(2,'LIC002345'),(4,'LIC003456'),(6,'LIC004567');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_doctor_delete_check` BEFORE DELETE ON `doctor` FOR EACH ROW BEGIN
  DECLARE pending INT;
  SELECT COUNT(*) INTO pending FROM Appointment
  WHERE DoctorID = OLD.StaffID AND Status = 'Scheduled';
  IF pending > 0 THEN
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Cannot delete Doctor: They have pending appointments.';
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `insurance`
--

DROP TABLE IF EXISTS `insurance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `insurance` (
  `PaymentID` int NOT NULL,
  `InsuranceProvider` varchar(50) DEFAULT NULL,
  `PolicyNumber` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`PaymentID`),
  CONSTRAINT `Insurance_ibfk_1` FOREIGN KEY (`PaymentID`) REFERENCES `payment` (`PaymentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insurance`
--

LOCK TABLES `insurance` WRITE;
/*!40000 ALTER TABLE `insurance` DISABLE KEYS */;
INSERT INTO `insurance` VALUES (9006,'Discovery Health','POL00123456');
/*!40000 ALTER TABLE `insurance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `labtest`
--

DROP TABLE IF EXISTS `labtest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `labtest` (
  `DocID` int NOT NULL,
  `TestType` varchar(50) DEFAULT NULL,
  `PatientID` int DEFAULT NULL,
  `DoctorID` int DEFAULT NULL,
  PRIMARY KEY (`DocID`),
  KEY `PatientID` (`PatientID`),
  KEY `DoctorID` (`DoctorID`),
  CONSTRAINT `fk_lab_patient` FOREIGN KEY (`PatientID`) REFERENCES `patient` (`PatientID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `LabTest_ibfk_2` FOREIGN KEY (`DoctorID`) REFERENCES `doctor` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `labtestID_fk` FOREIGN KEY (`DocID`) REFERENCES `medicaldocument` (`DocID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `labtest`
--

LOCK TABLES `labtest` WRITE;
/*!40000 ALTER TABLE `labtest` DISABLE KEYS */;
INSERT INTO `labtest` VALUES (2001,'Full Blood Count',7,1),(2002,'Glucose Test',8,2),(2003,'X-Ray',9,4),(2004,'Sputum Culture',10,1),(2005,'Vitamin Panel',11,2),(2018,'Blood test',15,4);
/*!40000 ALTER TABLE `labtest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicaldocument`
--

DROP TABLE IF EXISTS `medicaldocument`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicaldocument` (
  `DocID` int NOT NULL AUTO_INCREMENT,
  `Date` date DEFAULT NULL,
  `Notes` text,
  `StaffID` int DEFAULT NULL,
  `PatientID` int DEFAULT NULL,
  PRIMARY KEY (`DocID`),
  KEY `fk_meddoc_patient` (`PatientID`),
  KEY `fk_meddoc_staff` (`StaffID`),
  CONSTRAINT `fk_meddoc_patient` FOREIGN KEY (`PatientID`) REFERENCES `patient` (`PatientID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_meddoc_staff` FOREIGN KEY (`StaffID`) REFERENCES `doctor` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2021 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicaldocument`
--

LOCK TABLES `medicaldocument` WRITE;
/*!40000 ALTER TABLE `medicaldocument` DISABLE KEYS */;
INSERT INTO `medicaldocument` VALUES (2001,'2026-02-10','Result: WBC count elevated (12.4 x10^9/L). Possible infection.',1,7),(2002,'2026-02-12','Result: Fasting glucose 7.1 mmol/L. Consistent with pre-diabetes.',2,8),(2003,'2026-02-15','Result: No significant abnormalities in lumbar spine X-ray.',4,9),(2004,'2026-02-20','Result: Sputum culture positive for Streptococcus.',1,10),(2005,'2026-02-28','Result: Vitamin B12 levels low (140 pg/mL).',2,11),(2006,'2026-03-01','Observation: Patient reports chronic migraines and light sensitivity.',4,12),(2007,'2026-03-05','Observation: BP reading 150/95. Heart rate 88 bpm.',1,13),(2008,'2026-03-10','Observation: Significant swelling in the right ankle joint.',2,7),(2009,'2026-03-15','Observation: Productive cough with yellowish phlegm.',4,8),(2010,'2026-03-20','Observation: Visual signs of fatigue and pallor.',1,9),(2011,'2026-04-10','Instr: Take 1 capsule on an empty stomach every morning.',2,10),(2012,'2026-04-12','Instr: Take 1 tablet twice daily. Avoid alcohol during course.',4,11),(2013,'2026-04-15','Instr: Apply a thin layer to the affected area before bed.',1,12),(2014,'2026-04-20','Instr: Take with food to avoid stomach irritation.',2,13),(2015,'2026-04-25','Instr: Take strictly every 8 hours for 5 full days.',4,7),(2018,'2026-05-10','Positive',4,15),(2019,'2026-05-11','500mg',1,15),(2020,'2026-05-11','DIAGNOSIS:  | Observation: Very SIck',1,15);
/*!40000 ALTER TABLE `medicaldocument` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_medicalrecord_update` AFTER UPDATE ON `medicaldocument` FOR EACH ROW BEGIN
    INSERT INTO MedicalRecord_Log (DocID, OldNotes, NewNotes, OldDoctorID, NewDoctorID, ChangedAt)
    VALUES (
        OLD.DocID,
        OLD.Notes,
        NEW.Notes,
        OLD.DoctorID,
        NEW.DoctorID,
        NOW()
    );
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `medicalrecord_log`
--

DROP TABLE IF EXISTS `medicalrecord_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicalrecord_log` (
  `LogID` int NOT NULL AUTO_INCREMENT,
  `DocID` int NOT NULL,
  `OldNotes` text,
  `NewNotes` text,
  `OldDoctorID` int DEFAULT NULL,
  `NewDoctorID` int DEFAULT NULL,
  `ChangedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`LogID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicalrecord_log`
--

LOCK TABLES `medicalrecord_log` WRITE;
/*!40000 ALTER TABLE `medicalrecord_log` DISABLE KEYS */;
INSERT INTO `medicalrecord_log` VALUES (1,2001,'High blood pressure diagnosed. Prescribed medication.','High blood pressure diagnosed. Prescribed medication.',101,1,'2026-05-07 13:02:11'),(2,2005,'Sports injury. Physiotherapy required.','Sports injury. Physiotherapy required.',101,1,'2026-05-07 13:02:11'),(3,2002,'Migraine treatment. Rest recommended.','Migraine treatment. Rest recommended.',102,2,'2026-05-07 13:02:11'),(4,2006,'Respiratory infection. Antibiotics prescribed.','Respiratory infection. Antibiotics prescribed.',102,2,'2026-05-07 13:02:11'),(5,2003,'Routine checkup. All vitals normal.','Routine checkup. All vitals normal.',104,4,'2026-05-07 13:02:11'),(6,2004,'Stress-related anxiety. Counselling scheduled.','Stress-related anxiety. Counselling scheduled.',106,6,'2026-05-07 13:02:11'),(7,2001,'High blood pressure diagnosed. Prescribed medication.','High blood pressure diagnosed. Prescribed medication.',1,1,'2026-05-07 13:15:25'),(8,2002,'Migraine treatment. Rest recommended.','Migraine treatment. Rest recommended.',2,2,'2026-05-07 13:15:25'),(9,2003,'Routine checkup. All vitals normal.','Routine checkup. All vitals normal.',4,4,'2026-05-07 13:15:25'),(10,2004,'Stress-related anxiety. Counselling scheduled.','Stress-related anxiety. Counselling scheduled.',6,6,'2026-05-07 13:15:25'),(11,2005,'Sports injury. Physiotherapy required.','Sports injury. Physiotherapy required.',1,1,'2026-05-07 13:15:25'),(12,2006,'Respiratory infection. Antibiotics prescribed.','Respiratory infection. Antibiotics prescribed.',2,2,'2026-05-07 13:15:25');
/*!40000 ALTER TABLE `medicalrecord_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medication`
--

DROP TABLE IF EXISTS `medication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medication` (
  `Name` varchar(100) NOT NULL,
  `SideEffects` text,
  PRIMARY KEY (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medication`
--

LOCK TABLES `medication` WRITE;
/*!40000 ALTER TABLE `medication` DISABLE KEYS */;
INSERT INTO `medication` VALUES ('Amoxicillin','Diarrhea, rash, nausea'),('Atenolol','Fatigue, cold hands/feet, dizziness'),('Ibuprofen','Stomach pain, heartburn, dizziness'),('Insulin','Low blood sugar, weight gain, injection site reactions'),('Lipitor','Muscle pain, joint pain, diarrhea'),('Metformin','Stomach upset, gas, metallic taste in mouth'),('Nexium','Headache, abdominal pain, constipation'),('Panado','Nausea, allergic reactions, skin rash'),('Penado','N/A'),('Test','none'),('Ventolin','Shakiness, headache, fast heartbeat');
/*!40000 ALTER TABLE `medication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nurse`
--

DROP TABLE IF EXISTS `nurse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nurse` (
  `StaffID` int NOT NULL,
  `ShiftDetails` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`StaffID`),
  CONSTRAINT `fk_nurse_staff_id` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nurse`
--

LOCK TABLES `nurse` WRITE;
/*!40000 ALTER TABLE `nurse` DISABLE KEYS */;
INSERT INTO `nurse` VALUES (3,'Night Shift 8pm-8am');
/*!40000 ALTER TABLE `nurse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `PatientID` int NOT NULL,
  `MedicalAidNumber` varchar(20) DEFAULT NULL,
  `BloodType` varchar(5) DEFAULT NULL,
  `Allergies` varchar(100) DEFAULT NULL,
  `EmergencyContactInfo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`PatientID`),
  CONSTRAINT `fk_patient_person_id` FOREIGN KEY (`PatientID`) REFERENCES `person` (`PersonID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (7,'MA123456','A+','Pollen','Presidency: 012-1234567'),(8,'MA234567','O+','None','Family: 035-1234567'),(9,'MA345678','B+','Penicillin','Office: 011-1234567'),(10,'MA456789','AB+','Shellfish','DA Office: 021-1234567'),(11,'MA567890','O-','None','EFF Office: 011-7654321'),(12,'MA678901','A-','Dust','DA Office: 031-1234567'),(13,'54A34DKSMNS','AB+','None','0835550328'),(15,'113132232131','O-','Hayfever','09873729822');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_patient_insert` BEFORE INSERT ON `patient` FOR EACH ROW BEGIN
  IF NEW.PatientID IS NULL THEN
    SET NEW.PatientID = NEW.PatientID;  -- PatientID is the PK, can't be NULL
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_patient_delete` BEFORE DELETE ON `patient` FOR EACH ROW BEGIN
  DELETE FROM Appointment WHERE PatientID = OLD.PatientID;
  DELETE FROM MedicalRecord WHERE PatientID = OLD.PatientID;
  DELETE FROM LabTest WHERE PatientID = OLD.PatientID;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `PaymentID` int NOT NULL AUTO_INCREMENT,
  `Amount` decimal(10,2) DEFAULT NULL,
  `PaymentDate` date DEFAULT NULL,
  `BillingID` int DEFAULT NULL,
  PRIMARY KEY (`PaymentID`),
  KEY `BillingID` (`BillingID`),
  CONSTRAINT `Payment_ibfk_1` FOREIGN KEY (`BillingID`) REFERENCES `billing` (`BillingID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9016 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (9001,850.00,'2026-05-16',5002),(9002,650.00,'2026-05-17',5003),(9003,450.00,'2026-05-18',5004),(9004,850.00,'2026-05-19',5001),(9005,550.00,'2026-05-20',5006),(9006,750.00,'2026-05-21',5005),(9007,500.00,'2026-05-10',5008),(9008,250.00,'2026-05-10',5008),(9009,400.00,'2026-05-10',5008),(9013,300.00,'2026-05-10',5008),(9015,300.00,'2026-05-10',5009);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_payment_insert` AFTER INSERT ON `payment` FOR EACH ROW BEGIN
    UPDATE Billing
    SET PaymentStatus = 'Paid'
    WHERE BillingID = NEW.BillingID;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_payment_delete_cascade` BEFORE DELETE ON `payment` FOR EACH ROW BEGIN
  DELETE FROM Cash WHERE PaymentID = OLD.PaymentID;
  DELETE FROM Card WHERE PaymentID = OLD.PaymentID;
  DELETE FROM Insurance WHERE PaymentID = OLD.PaymentID;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person` (
  `PersonID` int NOT NULL,
  `FirstName` varchar(25) NOT NULL,
  `LastName` varchar(25) NOT NULL,
  `Gender` char(1) NOT NULL,
  `ContactDetails` varchar(16) DEFAULT NULL,
  `Address` varchar(100) NOT NULL,
  `DateOfBirth` date NOT NULL,
  PRIMARY KEY (`PersonID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'Cyril','Ramaphosa','M','0821111111','1 Union Buildings, Pretoria','1952-11-17'),(2,'Jacob','Zuma','M','0822222222','2 Nkandla, KwaZulu-Natal','1942-04-12'),(3,'Thabo','Mbeki','M','0823333333','3 Johannesburg, Gauteng','1942-06-18'),(4,'Helen','Zille','M','0824444444','4 Cape Town, Western Cape','1951-03-09'),(5,'Julius','Malema','M','0825555555','5 Johannesburg, Gauteng','1981-03-03'),(6,'John','Steenhuisen','M','0826666666','6 Durban, KwaZulu-Natal','1976-03-25'),(7,'Thabo','Mbeki','M','011-222-3333','789 Govan Mbeki Ave','1952-06-18'),(8,'Zanele','Dlamini','F','021-333-4444','12 Sea Point Promenade','1965-11-04'),(9,'Sizwe','Kloof','M','031-444-5555','45 Musgrave Road','1990-02-14'),(10,'Lerato','Molefe','F','012-555-6666','102 Hatfield Square','1988-07-22'),(11,'Pieter','Botha','M','051-666-7777','21 Naval Hill','1975-03-30'),(12,'Nandi','Zulu','F','035-777-8888','5 Shaka Marine Way','1995-12-12'),(13,'Tana','Tagwira','F','0661271191','11 Davenport road','2005-12-11'),(14,'Test','1','M','09827783845','27 Westvllle rd','2000-12-04'),(15,'Test','1','F','0998722892','25 Davenport Square','2000-12-25');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_CheckDOB` BEFORE INSERT ON `person` FOR EACH ROW BEGIN

    IF NEW.DateOfBirth > CURDATE() THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
        'Date of birth cannot be in the future';

    END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_person_delete_archive` BEFORE DELETE ON `person` FOR EACH ROW BEGIN
  INSERT INTO PERSON_ARCHIVE(PersonID, FirstName, LastName, DeletedAt)
  VALUES (OLD.PersonID, OLD.FirstName, OLD.LastName, NOW());
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `person_archive`
--

DROP TABLE IF EXISTS `person_archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `person_archive` (
  `ArchiveID` int NOT NULL AUTO_INCREMENT,
  `PersonID` int NOT NULL,
  `FirstName` varchar(25) NOT NULL,
  `LastName` varchar(25) NOT NULL,
  `DeletedAt` datetime NOT NULL,
  PRIMARY KEY (`ArchiveID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person_archive`
--

LOCK TABLES `person_archive` WRITE;
/*!40000 ALTER TABLE `person_archive` DISABLE KEYS */;
INSERT INTO `person_archive` VALUES (1,1,'Ola','exe','2026-05-05 19:14:08'),(2,2,'Simbu','MK','2026-05-05 19:14:08'),(3,3,'Sanele','Bobby','2026-05-05 19:14:08'),(4,17,'Test','jr','2026-05-11 13:44:53'),(5,16,'Minenhle','Ngubane','2026-05-11 13:59:44'),(6,16,'Dekete','Test','2026-05-11 14:01:29');
/*!40000 ALTER TABLE `person_archive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription`
--

DROP TABLE IF EXISTS `prescription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription` (
  `DocID` int NOT NULL,
  `MedicationName` varchar(100) DEFAULT NULL,
  `Dosage` varchar(50) DEFAULT NULL,
  `PatientID` int DEFAULT NULL,
  `StaffID` int DEFAULT NULL,
  PRIMARY KEY (`DocID`),
  KEY `MedicationName` (`DocID`),
  KEY `fk_presc_patient` (`PatientID`),
  KEY `fk_presc_staff` (`StaffID`),
  CONSTRAINT `fk_presc_patient` FOREIGN KEY (`PatientID`) REFERENCES `patient` (`PatientID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_presc_staff` FOREIGN KEY (`StaffID`) REFERENCES `staff` (`StaffID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription`
--

LOCK TABLES `prescription` WRITE;
/*!40000 ALTER TABLE `prescription` DISABLE KEYS */;
INSERT INTO `prescription` VALUES (2011,'Nexium','40mg',10,2),(2012,'Metformin','500mg',11,4),(2013,'Ibuprofen','200mg',12,1),(2014,'Zoloft','50mg',13,2),(2015,'Amoxicillin','500mg',7,4),(2017,'Lipitor','100mg',9,4),(2019,'Amoxicillin','500mg',15,1);
/*!40000 ALTER TABLE `prescription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `RoomNumber` int NOT NULL,
  `Type` varchar(30) DEFAULT NULL,
  `DepartmentID` int DEFAULT NULL,
  PRIMARY KEY (`RoomNumber`),
  KEY `DepartmentID` (`DepartmentID`),
  CONSTRAINT `Room_ibfk_1` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (101,'Consultation Room',1),(102,'Examination Room',2),(103,'Treatment Room',3),(104,'ICU Bed',4),(105,'General Ward',5),(106,'Private Suite',6);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff` (
  `StaffID` int NOT NULL,
  `HireDate` date NOT NULL,
  `Salary` decimal(10,2) DEFAULT NULL,
  `DepartmentID` int DEFAULT NULL,
  PRIMARY KEY (`StaffID`),
  KEY `DepartmentID` (`DepartmentID`),
  CONSTRAINT `fk_staff_person_id` FOREIGN KEY (`StaffID`) REFERENCES `person` (`PersonID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Staff_ibfk_2` FOREIGN KEY (`DepartmentID`) REFERENCES `department` (`DepartmentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

LOCK TABLES `staff` WRITE;
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` VALUES (1,'2010-06-01',120000.00,1),(2,'2012-08-15',115000.00,2),(3,'2015-03-20',95000.00,3),(4,'2018-11-10',110000.00,4),(5,'2020-01-05',85000.00,5),(6,'2016-07-22',105000.00,6),(14,'2026-05-08',15000.00,4);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_staff_insert_audit` AFTER INSERT ON `staff` FOR EACH ROW BEGIN
  INSERT INTO STAFF_AUDIT(StaffID, Action, ActionDate)
  VALUES (NEW.StaffID, 'INSERTED', NOW());
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `staff_audit`
--

DROP TABLE IF EXISTS `staff_audit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_audit` (
  `AuditID` int NOT NULL AUTO_INCREMENT,
  `StaffID` int NOT NULL,
  `Action` varchar(20) NOT NULL,
  `ActionDate` datetime NOT NULL,
  PRIMARY KEY (`AuditID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_audit`
--

LOCK TABLES `staff_audit` WRITE;
/*!40000 ALTER TABLE `staff_audit` DISABLE KEYS */;
INSERT INTO `staff_audit` VALUES (1,101,'INSERTED','2026-05-05 19:17:41'),(2,102,'INSERTED','2026-05-05 19:17:41'),(3,103,'INSERTED','2026-05-05 19:17:41'),(4,104,'INSERTED','2026-05-05 19:17:41'),(5,105,'INSERTED','2026-05-05 19:17:41'),(6,106,'INSERTED','2026-05-05 19:17:41'),(7,14,'INSERTED','2026-05-08 09:15:36'),(8,17,'INSERTED','2026-05-11 12:39:06'),(9,16,'INSERTED','2026-05-11 14:01:21');
/*!40000 ALTER TABLE `staff_audit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'hospital'
--
/*!50003 DROP PROCEDURE IF EXISTS `AddMedicalDocument` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddMedicalDocument`(
    IN p_DoctorID INT,
    IN p_PatientID INT,
    IN p_DocumentType VARCHAR(50),
    IN p_Description TEXT
)
BEGIN
    DECLARE next_id INT;
    SELECT IFNULL(MAX(DocID), 0) + 1 INTO next_id FROM MedicalDocument;
    
    INSERT INTO MedicalDocument(DocID, DoctorID, PatientID, DocumentType, Description)
    VALUES (next_id, p_DoctorID, p_PatientID, p_DocumentType, p_Description);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AddPatient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddPatient`(
    IN p_FirstName VARCHAR(25),
    IN p_LastName VARCHAR(25),
    IN p_Gender CHAR(1),
    IN p_ContactDetails VARCHAR(16),
    IN p_Address VARCHAR(100),
    IN p_DateOfBirth DATE,
    IN p_MedicalAidNumber VARCHAR(20),
    IN p_BloodType VARCHAR(5),
    IN p_Allergies VARCHAR(100),
    IN p_EmergencyContactInfo VARCHAR(50)
)
BEGIN
    INSERT INTO Person (FirstName, LastName, Gender, ContactDetails, Address, DateOfBirth)
    VALUES (p_FirstName, p_LastName, p_Gender, p_ContactDetails, p_Address, p_DateOfBirth);
    
    INSERT INTO Patient (PatientID, MedicalAidNumber, BloodType, Allergies, EmergencyContactInfo)
    VALUES (LAST_INSERT_ID(), p_MedicalAidNumber, p_BloodType, p_Allergies, p_EmergencyContactInfo);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `AddPayment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddPayment`(
    IN p_Amount DECIMAL(10,2),
    IN p_BillingID INT
)
BEGIN
    INSERT INTO Payment (Amount, BillingID, PaymentDate)
    VALUES (p_Amount, p_BillingID, CURDATE());
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_book_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_book_appointment`(
    IN p_Date DATE,
    IN p_Time TIME,
    IN p_PatientID INT,
    IN p_DoctorID INT,
    IN p_RoomNumber INT
)
BEGIN
    DECLARE next_id INT;
    SELECT IFNULL(MAX(AppointmentID), 1000) + 1 INTO next_id FROM Appointment;
    
    INSERT INTO Appointment(AppointmentID, AppointmentDate, AppointmentTime, Status, PatientID, DoctorID, RoomNumber)
    VALUES (next_id, p_Date, p_Time, 'Scheduled', p_PatientID, p_DoctorID, p_RoomNumber);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_cancel_appointment` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_cancel_appointment`(IN p_AppointmentID INT)
BEGIN
    UPDATE Appointment SET Status = 'Cancelled'
    WHERE AppointmentID = p_AppointmentID;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_create_billing` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_create_billing`(
    IN p_AppointmentID INT,
    IN p_TotalAmount DECIMAL(10,2)
)
BEGIN
    DECLARE next_id INT;
    SELECT IFNULL(MAX(BillingID), 5000) + 1 INTO next_id FROM Billing;
    
    INSERT INTO Billing(BillingID, BillingDate, TotalAmount, PaymentStatus, AppointmentID)
    VALUES (next_id, CURDATE(), p_TotalAmount, 'Unpaid', p_AppointmentID);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_delete_patient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_delete_patient`(IN p_PatientID INT)
BEGIN
    DECLARE active_appt INT;
    
    SELECT COUNT(*) INTO active_appt FROM Appointment
    WHERE PatientID = p_PatientID AND Status = 'Scheduled';
    
    IF active_appt > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Patient has active appointments. Cancel them first.';
    ELSE
        DELETE FROM Patient WHERE PatientID = p_PatientID;
        DELETE FROM Person WHERE PersonID = p_PatientID;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `sp_insert_patient` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insert_patient`(
    IN p_FirstName VARCHAR(25),
    IN p_LastName VARCHAR(25),
    IN p_Gender CHAR(1),
    IN p_DOB DATE,
    IN p_Contact VARCHAR(16),
    IN p_Address VARCHAR(100),
    IN p_MedicalAidNum VARCHAR(20),
    IN p_BloodType VARCHAR(5),
    IN p_Allergies VARCHAR(100),
    IN p_EmergencyContact VARCHAR(50)
)
BEGIN
    DECLARE new_id INT;
    
    INSERT INTO Person(PersonID, FirstName, LastName, Gender, DateOfBirth, ContactDetails, Address)
    VALUES ((SELECT IFNULL(MAX(PersonID), 0) + 1 FROM Person), p_FirstName, p_LastName, p_Gender, p_DOB, p_Contact, p_Address);
    
    SET new_id = (SELECT MAX(PersonID) FROM Person);
    
    INSERT INTO Patient(PatientID, MedicalAidNumber, BloodType, Allergies, EmergencyContactInfo)
    VALUES (new_id, p_MedicalAidNum, p_BloodType, p_Allergies, p_EmergencyContact);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-11 16:15:12
