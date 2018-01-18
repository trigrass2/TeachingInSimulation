CREATE DATABASE  IF NOT EXISTS `db_cas_electrical` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `db_cas_electrical`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 192.168.1.19    Database: db_cas_electrical
-- ------------------------------------------------------
-- Server version	5.6.36

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
-- Table structure for table `assets`
--

DROP TABLE IF EXISTS `assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assets` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PATH` varchar(45) NOT NULL COMMENT '资源路径，相对路径，通过FTP下载获取',
  `HASH` varchar(45) NOT NULL COMMENT '文件HASH(MD5)码,用于验证客户端资源是否被修改。',
  `CREATE_DATE` varchar(45) DEFAULT NULL,
  `CREATOR_ID` varchar(45) NOT NULL COMMENT '上传者',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `URL_UNIQUE` (`PATH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模型资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assets`
--

LOCK TABLES `assets` WRITE;
/*!40000 ALTER TABLE `assets` DISABLE KEYS */;
/*!40000 ALTER TABLE `assets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) NOT NULL COMMENT '班级名称',
  `TID` int(11) DEFAULT NULL COMMENT '负责教师编号',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='班级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class`
--

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `examination`
--

DROP TABLE IF EXISTS `examination`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `examination` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL COMMENT '考试名称',
  `QUESTION_IDS` text COMMENT '题目ID(题目类型:题目ID)',
  `CREATE_DATE` varchar(45) DEFAULT NULL COMMENT '创建时间',
  `CREATOR_ID` int(11) DEFAULT NULL COMMENT '创建人',
  `DEL` tinyint(1) DEFAULT NULL,
  `UPDATE_DATE` varchar(45) DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='考试记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `examination`
--

LOCK TABLES `examination` WRITE;
/*!40000 ALTER TABLE `examination` DISABLE KEYS */;
INSERT INTO `examination` VALUES (1,'测试试题-1',NULL,'2018-01-15 17:46:15.453',7,0,NULL),(2,'测试试题-1',NULL,'2018-01-15 17:53:14.944',7,0,NULL),(3,'测试试题-1',NULL,'2018-01-15 17:54:28.06',7,0,NULL),(4,'测试试题-1',NULL,'2018-01-15 17:56:58.266',7,0,NULL),(5,'测试试题-1',NULL,'2018-01-15 18:06:35.386',7,0,NULL),(6,'测试试题-1','[{\"questionId\":1,\"type\":1},{\"questionId\":3,\"type\":1},{\"questionId\":5,\"type\":1},{\"questionId\":6,\"type\":2}]','2018-01-15 18:36:19.333',7,0,NULL),(7,'测试试题-1','[{\"id\":1,\"type\":1},{\"id\":3,\"type\":1},{\"id\":5,\"type\":1},{\"id\":6,\"type\":2}]','2018-01-15 18:38:09.475',7,0,NULL),(8,'测试试题-1','[{\"i\":1,\"t\":1},{\"i\":3,\"t\":1},{\"i\":5,\"t\":1},{\"i\":6,\"t\":2}]','2018-01-15 18:40:23.042',7,0,NULL),(9,'测试试题-1','[{\"i\":1,\"t\":1},{\"i\":3,\"t\":1},{\"i\":5,\"t\":1},{\"i\":6,\"t\":2}]','2018-01-15 19:10:44.12',7,0,NULL),(10,'测试试题-1','[{\"i\":1,\"t\":1},{\"i\":3,\"t\":1},{\"i\":5,\"t\":1},{\"i\":6,\"t\":2}]','2018-01-15 19:11:13.717',7,0,NULL),(11,'测试试题-1','[{\"i\":1,\"t\":1},{\"i\":3,\"t\":1},{\"i\":5,\"t\":1},{\"i\":6,\"t\":2}]','2018-01-17 20:06:37.544',7,0,NULL);
/*!40000 ALTER TABLE `examination` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `examination_answer`
--

DROP TABLE IF EXISTS `examination_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `examination_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXAM_ID` int(11) NOT NULL COMMENT '考试名称',
  `ANSWERS` text COMMENT '我的答案(题目类型，题目ID，我的答案)',
  `SUBMIT_TIME` varchar(45) DEFAULT NULL COMMENT '提交时间',
  `CREATOR_ID` int(11) DEFAULT NULL COMMENT '创建人',
  `CLS_ID` int(11) DEFAULT NULL COMMENT '班级ID（脏数据，是为了提高查询效率，比如查询某个班级的平均成绩）',
  `DEL` tinyint(1) DEFAULT NULL,
  `SCORE` float DEFAULT NULL COMMENT '本次考试成绩',
  `EXAM_TIME` varchar(45) DEFAULT NULL COMMENT '考试开始时间',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-练习\n1-考核',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='考试答题';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `examination_answer`
--

LOCK TABLES `examination_answer` WRITE;
/*!40000 ALTER TABLE `examination_answer` DISABLE KEYS */;
INSERT INTO `examination_answer` VALUES (9,4,'[{\"a\":\"A\",\"i\":1,\"t\":1},{\"a\":\"1\",\"i\":3,\"t\":1},{\"a\":\"B\",\"i\":5,\"t\":1},{\"a\":\"你\",\"i\":6,\"t\":2}]','2018-01-15 19:10:16.647',7,NULL,0,NULL,NULL,0),(10,4,'[{\"a\":\"A\",\"i\":1,\"t\":1},{\"a\":\"1\",\"i\":3,\"t\":1},{\"a\":\"B\",\"i\":5,\"t\":1},{\"a\":\"你\",\"i\":6,\"t\":2}]','2018-01-17 20:06:36.546',7,NULL,0,NULL,NULL,0);
/*!40000 ALTER TABLE `examination_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson`
--

DROP TABLE IF EXISTS `lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lesson` (
  `ID` int(11) NOT NULL,
  `TID` int(11) NOT NULL,
  `SECTION_ID` int(11) NOT NULL,
  `CREATOR_ID` int(11) NOT NULL,
  `CREATE_DATE` varchar(45) NOT NULL,
  `UPDATE_DATE` varchar(45) DEFAULT NULL,
  `DEL` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson`
--

LOCK TABLES `lesson` WRITE;
/*!40000 ALTER TABLE `lesson` DISABLE KEYS */;
/*!40000 ALTER TABLE `lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lesson_resource`
--

DROP TABLE IF EXISTS `lesson_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lesson_resource` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '备课资源编号',
  `TID` int(11) NOT NULL COMMENT '备课资源类型:\n0-资源库资源\n1-元器件认知\n2-典型案例\n',
  `LESSON_ID` int(11) NOT NULL COMMENT '备课编号',
  `RESOURCE_ID` int(11) NOT NULL COMMENT '资源编号\n认知编号\n案例编号',
  `SORT` int(11) NOT NULL COMMENT '创建人（管理员/教师编号）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备课资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lesson_resource`
--

LOCK TABLES `lesson_resource` WRITE;
/*!40000 ALTER TABLE `lesson_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `lesson_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library`
--

DROP TABLE IF EXISTS `library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL COMMENT '试题库名称',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题库类型：\n0-模拟题\n1-真题\n2-教师个人题库',
  `TID` int(11) NOT NULL DEFAULT '0' COMMENT '所属教师',
  `NUM` tinyint(3) NOT NULL COMMENT '试题数量',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（教师/管理员）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题库';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library`
--

LOCK TABLES `library` WRITE;
/*!40000 ALTER TABLE `library` DISABLE KEYS */;
/*!40000 ALTER TABLE `library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library_answer`
--

DROP TABLE IF EXISTS `library_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `INDEX` tinyint(3) NOT NULL COMMENT '试题序号',
  `ANSWER` varchar(10) DEFAULT NULL COMMENT '学生答案',
  `QID` int(11) NOT NULL COMMENT '试题编号',
  `SID` int(11) NOT NULL COMMENT '学生编号',
  `LPID` int(11) NOT NULL COMMENT '发布记录编号',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题库答题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library_answer`
--

LOCK TABLES `library_answer` WRITE;
/*!40000 ALTER TABLE `library_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `library_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library_publish`
--

DROP TABLE IF EXISTS `library_publish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library_publish` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `LID` int(11) NOT NULL COMMENT '题库编号',
  `CID` int(11) NOT NULL COMMENT '班级编号',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发布记录类型\n0-个人练习\n1-教师考核',
  `PUBLISHER` int(11) NOT NULL COMMENT '发布人（学生/教师）',
  `PUBLISH_DATE` varchar(24) NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题发布记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library_publish`
--

LOCK TABLES `library_publish` WRITE;
/*!40000 ALTER TABLE `library_publish` DISABLE KEYS */;
/*!40000 ALTER TABLE `library_publish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preparation`
--

DROP TABLE IF EXISTS `preparation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preparation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PID` int(11) NOT NULL COMMENT '知识点编号',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（管理员/教师编号）\n',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（管理员/教师编号）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备课表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preparation`
--

LOCK TABLES `preparation` WRITE;
/*!40000 ALTER TABLE `preparation` DISABLE KEYS */;
/*!40000 ALTER TABLE `preparation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preparation_answer`
--

DROP TABLE IF EXISTS `preparation_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preparation_answer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PPID` int(11) NOT NULL COMMENT '备课发布编号',
  `SID` int(11) NOT NULL COMMENT '学生编号',
  `ANSWER` varchar(100) NOT NULL COMMENT '学生回答\n1、文字试题学生选项\n2、故障维修接线存档文档地址\n3、自由接线存档文件地址',
  `FEEDBACK` varchar(500) DEFAULT NULL COMMENT '教师文字反馈',
  `SCORE` tinyint(3) DEFAULT NULL COMMENT '得分',
  `ANSWER_DATE` varchar(24) DEFAULT NULL COMMENT '学生回答时间',
  `FEEDBACK_DATE` varchar(24) DEFAULT NULL COMMENT '教师反馈时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备课试题回答表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preparation_answer`
--

LOCK TABLES `preparation_answer` WRITE;
/*!40000 ALTER TABLE `preparation_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `preparation_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preparation_publish`
--

DROP TABLE IF EXISTS `preparation_publish`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preparation_publish` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PQID` int(11) NOT NULL COMMENT '备课试题编号',
  `CID` int(11) NOT NULL COMMENT '班级编号',
  `PUBLISHER` int(11) NOT NULL COMMENT '发布人',
  `PUBLISH_DATE` varchar(24) NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备课试题发布记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preparation_publish`
--

LOCK TABLES `preparation_publish` WRITE;
/*!40000 ALTER TABLE `preparation_publish` DISABLE KEYS */;
/*!40000 ALTER TABLE `preparation_publish` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preparation_quiz`
--

DROP TABLE IF EXISTS `preparation_quiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preparation_quiz` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` tinyint(1) NOT NULL COMMENT '备课试题类型：\n0-文字试题\n1-故障维修\n2-自由接线\n',
  `NAME` varchar(20) DEFAULT NULL COMMENT '自由接线名称',
  `DESC` varchar(250) DEFAULT NULL COMMENT '自由接线任务描述',
  `PID` int(11) NOT NULL COMMENT '备课编号',
  `RID` int(11) DEFAULT NULL COMMENT '关联编号（试题编号/故障维修编号）\n',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（管理员/教师编号）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（管理员/教师编号）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备课试题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preparation_quiz`
--

LOCK TABLES `preparation_quiz` WRITE;
/*!40000 ALTER TABLE `preparation_quiz` DISABLE KEYS */;
/*!40000 ALTER TABLE `preparation_quiz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `q_choice`
--

DROP TABLE IF EXISTS `q_choice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `q_choice` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(250) NOT NULL COMMENT '试题题干',
  `CHOICE_ITEMS` varchar(250) NOT NULL COMMENT '试题选项',
  `REFERENCE` varchar(10) NOT NULL COMMENT '参考答案',
  `ANALYSIS` varchar(100) DEFAULT NULL COMMENT '题目解析',
  `Q_TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题类型：\n0-题库试题\n1-备课试题\n',
  `LIBRARY_ID` int(11) NOT NULL COMMENT '关联编号（题库编号/知识点编号）\n',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `q_choice`
--

LOCK TABLES `q_choice` WRITE;
/*!40000 ALTER TABLE `q_choice` DISABLE KEYS */;
/*!40000 ALTER TABLE `q_choice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `q_completion`
--

DROP TABLE IF EXISTS `q_completion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `q_completion` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(250) NOT NULL COMMENT '试题题干',
  `REFERENCE` varchar(10) NOT NULL COMMENT '参考答案',
  `ANALYSIS` varchar(100) DEFAULT NULL COMMENT '题目解析',
  `Q_TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题类型：\n0-题库试题\n1-备课试题\n',
  `LIBRARY_ID` int(11) NOT NULL COMMENT '关联编号（题库编号/知识点编号）\n',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `q_completion`
--

LOCK TABLES `q_completion` WRITE;
/*!40000 ALTER TABLE `q_completion` DISABLE KEYS */;
/*!40000 ALTER TABLE `q_completion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `q_judgment`
--

DROP TABLE IF EXISTS `q_judgment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `q_judgment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(250) NOT NULL COMMENT '试题题干',
  `REFERENCE` varchar(10) NOT NULL COMMENT '参考答案',
  `ANALYSIS` varchar(100) DEFAULT NULL COMMENT '题目解析',
  `Q_TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题类型：\n0-题库试题\n1-备课试题\n',
  `LIBRARY_ID` int(11) NOT NULL COMMENT '关联编号（题库编号/知识点编号）\n',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `q_judgment`
--

LOCK TABLES `q_judgment` WRITE;
/*!40000 ALTER TABLE `q_judgment` DISABLE KEYS */;
/*!40000 ALTER TABLE `q_judgment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(250) NOT NULL COMMENT '试题题干',
  `IMGS` varchar(100) DEFAULT NULL COMMENT '试题配图路径（预留字段）',
  `ANALYSIS` varchar(100) DEFAULT NULL COMMENT '题目解析',
  `Q_TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题类型：\n0-题库试题\n1-备课试题\n',
  `LIBRARY_ID` int(11) NOT NULL COMMENT '关联编号（题库编号/知识点编号）\n',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `r_circuitry`
--

DROP TABLE IF EXISTS `r_circuitry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_circuitry` (
  `ID` int(11) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `PATH` varchar(45) NOT NULL,
  `DRAW_ID` int(11) DEFAULT NULL,
  `COMMENT` varchar(45) DEFAULT NULL,
  `CREATOR_ID` int(11) NOT NULL,
  `CREATOR_ROLE` int(11) NOT NULL,
  `CREATE_DATE` varchar(45) NOT NULL,
  `UPDATE_DATE` varchar(45) DEFAULT NULL,
  `DEL` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_circuitry`
--

LOCK TABLES `r_circuitry` WRITE;
/*!40000 ALTER TABLE `r_circuitry` DISABLE KEYS */;
/*!40000 ALTER TABLE `r_circuitry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `r_cognition`
--

DROP TABLE IF EXISTS `r_cognition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_cognition` (
  `ID` int(11) NOT NULL,
  `R_TYPE` int(11) NOT NULL,
  `NAME` varchar(45) NOT NULL,
  `PATH` varchar(45) NOT NULL,
  `KEYWORD` varchar(45) DEFAULT NULL,
  `COMMENT` varchar(45) DEFAULT NULL,
  `CREATOR_ID` int(11) NOT NULL,
  `CREATE_DATE` varchar(45) NOT NULL,
  `DEL` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_cognition`
--

LOCK TABLES `r_cognition` WRITE;
/*!40000 ALTER TABLE `r_cognition` DISABLE KEYS */;
/*!40000 ALTER TABLE `r_cognition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resource`
--

DROP TABLE IF EXISTS `resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resource` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL COMMENT '资源名称',
  `R_TYPE` tinyint(1) NOT NULL COMMENT '资源类型：\n0-Word/EXCEL/PPT\n1-PDF\n2-SWF\n3-PNG/JPG/GIF\n4-MP4/WMV/RMVB/FLV/AVI\n',
  `PATH` varchar(45) NOT NULL COMMENT '资源路径',
  `KEYWORD` varchar(100) DEFAULT NULL COMMENT '关键词（“|”分割）',
  `COMMENT` varchar(250) DEFAULT NULL COMMENT '资源描述',
  `PUBLIC` tinyint(1) NOT NULL COMMENT '资源公开',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'资源-0',0,'资源路径-0','关键字-0','资源描述-0',0,5,'2018-01-11 14:37:47.421',0),(2,'资源-1',1,'资源路径-1','关键字-1','资源描述-1',1,5,'2018-01-11 14:37:47.421',0),(3,'资源-2',1,'资源路径-2','关键字-2','资源描述-2',0,5,'2018-01-11 14:37:47.421',0),(4,'资源-3',1,'资源路径-3','关键字-3','资源描述-3',1,4,'2018-01-11 14:37:47.421',0),(5,'资源-4',4,'资源路径-4','关键字-4','资源描述-4',1,4,'2018-01-11 14:37:47.421',0),(7,'资源-6',6,'资源路径-6','关键字-6','资源描述-6',0,5,'2018-01-11 14:37:47.421',0),(15,'资源-0',0,'资源路径-0','关键字-0','资源描述-0',1,5,'2018-01-13 14:08:07.673',0),(16,'资源-1',1,'资源路径-1','关键字-1','资源描述-1',1,5,'2018-01-13 14:08:07.673',0),(17,'资源-2',2,'资源路径-2','关键字-2','资源描述-2',1,5,'2018-01-13 14:08:07.673',0),(18,'资源-3',3,'资源路径-3','关键字-3','资源描述-3',1,5,'2018-01-13 14:08:07.673',0),(19,'资源-4',4,'资源路径-4','关键字-4','资源描述-4',1,5,'2018-01-13 14:08:07.673',0),(20,'资源-5',5,'资源路径-5','关键字-5','资源描述-5',1,5,'2018-01-13 14:08:07.673',0),(21,'资源-6',6,'资源路径-6','关键字-6','资源描述-6',1,5,'2018-01-13 14:08:07.673',0),(22,'资源-0',0,'资源路径-0','关键字-0','资源描述-0',1,5,'2018-01-13 14:18:13.09',0),(23,'资源-1',1,'资源路径-1','关键字-1','资源描述-1',1,5,'2018-01-13 14:18:13.09',0),(24,'资源-2',2,'资源路径-2','关键字-2','资源描述-2',1,5,'2018-01-13 14:18:13.09',0),(25,'资源-3',3,'资源路径-3','关键字-3','资源描述-3',1,5,'2018-01-13 14:18:13.09',0),(26,'资源-4',4,'资源路径-4','关键字-4','资源描述-4',1,5,'2018-01-13 14:18:13.09',0),(27,'资源-5',5,'资源路径-5','关键字-5','资源描述-5',1,5,'2018-01-13 14:18:13.09',0),(28,'资源-6',6,'资源路径-6','关键字-6','资源描述-6',1,5,'2018-01-13 14:18:13.09',0);
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schematic`
--

DROP TABLE IF EXISTS `schematic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schematic` (
  `ID` int(11) NOT NULL,
  `PATH` varchar(45) DEFAULT NULL,
  `SUM` int(11) DEFAULT NULL,
  `CREATOR_ID` int(11) NOT NULL,
  `CREATOR_ROLE` int(11) NOT NULL,
  `CREATE_DATE` varchar(45) NOT NULL,
  `UPDATE_DATE` varchar(45) DEFAULT NULL,
  `DEL` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schematic`
--

LOCK TABLES `schematic` WRITE;
/*!40000 ALTER TABLE `schematic` DISABLE KEYS */;
/*!40000 ALTER TABLE `schematic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL COMMENT '知识点名称',
  `LVL` int(11) DEFAULT NULL,
  `UPPER_ID` int(11) NOT NULL COMMENT '所属任务编号',
  `SORT` int(11) DEFAULT '0' COMMENT '排序（预留字段）',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) DEFAULT '0' COMMENT '删除标记',
  `PERIOD` int(11) DEFAULT '0' COMMENT '学时',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COMMENT='课程-项目-任务-知识点';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (1,'课程1',0,0,0,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(2,'项目-0',1,1,0,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(3,'项目-1',1,1,1,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(4,'项目-2',1,1,2,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(5,'任务-0',2,4,0,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(6,'任务-1',2,4,1,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(7,'任务-2',2,4,2,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(8,'知识点-0',3,6,0,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(9,'知识点-1',3,6,1,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(10,'知识点-2',3,6,2,5,'2018-01-11 13:42:09.561',NULL,0,NULL),(11,'课程1',0,0,0,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(12,'项目-0',1,0,0,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(13,'项目-1',1,0,1,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(14,'项目-2',1,0,2,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(15,'任务-0',2,4,0,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(16,'任务-1',2,4,1,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(17,'任务-2',2,4,2,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(18,'知识点-0',3,7,0,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(19,'知识点-1',3,7,0,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(20,'知识点-2',3,7,0,5,'2018-01-11 14:01:08.059',NULL,0,NULL),(21,'课程1',0,0,0,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(22,'项目-0',1,0,0,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(23,'项目-1',1,0,1,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(24,'项目-2',1,0,2,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(25,'任务-0',2,4,0,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(26,'任务-1',2,4,1,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(27,'任务-2',2,4,2,5,'2018-01-11 17:22:47.217',NULL,0,NULL),(28,'知识点-0',3,7,0,7,'2018-01-11 17:22:47.217',NULL,0,NULL),(29,'知识点-1',3,7,1,7,'2018-01-11 17:22:47.217',NULL,0,NULL),(30,'知识点-2',3,7,2,7,'2018-01-11 17:22:47.217',NULL,0,NULL),(31,'课程1',0,0,0,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(32,'项目-0',1,0,0,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(33,'项目-1',1,0,1,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(34,'项目-2',1,0,2,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(35,'任务-0',2,4,0,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(36,'任务-1',2,4,1,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(37,'任务-2',2,4,2,5,'2018-01-13 14:08:07.534',NULL,0,NULL),(38,'知识点-0',3,7,0,7,'2018-01-13 14:08:07.534',NULL,0,NULL),(39,'知识点-1',3,7,1,7,'2018-01-13 14:08:07.534',NULL,0,NULL),(40,'知识点-2',3,7,2,7,'2018-01-13 14:08:07.534',NULL,0,NULL),(41,'课程1',0,0,0,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(42,'项目-0',1,0,0,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(43,'项目-1',1,0,1,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(44,'项目-2',1,0,2,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(45,'任务-0',2,4,0,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(46,'任务-1',2,4,1,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(47,'任务-2',2,4,2,5,'2018-01-13 14:18:12.868',NULL,0,NULL),(48,'知识点-0',3,7,0,7,'2018-01-13 14:18:12.868',NULL,0,NULL),(49,'知识点-1',3,7,1,7,'2018-01-13 14:18:12.868',NULL,0,NULL),(50,'知识点-2',3,7,2,7,'2018-01-13 14:18:12.868',NULL,0,NULL);
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(10) NOT NULL,
  `NAME` varchar(20) NOT NULL,
  `PASSWORD` varchar(8) NOT NULL DEFAULT '123456',
  `CID` int(11) NOT NULL,
  `CREATOR` int(11) NOT NULL,
  `CREATE_DATE` varchar(24) NOT NULL,
  `UPDATER` int(11) DEFAULT NULL,
  `UPDATE_DATE` varchar(24) DEFAULT NULL,
  `DEL` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teacher` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '教师编号',
  `CODE` varchar(10) NOT NULL COMMENT '教师工号',
  `NAME` varchar(10) NOT NULL COMMENT '教师名称',
  `PASSWORD` varchar(8) NOT NULL DEFAULT '123456' COMMENT '密码\n默认：123456',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  UNIQUE KEY `CODE_UNIQUE` (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 COMMENT='教师表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'admin','张老师','123456','2018-01-11 11:55:48.379',NULL,0),(4,'000','老师-0','123456','2018-01-11 11:55:48.379',NULL,0),(21,'1110','老师-1','123456','2018-01-11 17:42:21.04',NULL,0),(22,'1112','老师-1','123456','2018-01-11 18:10:39.973',NULL,0),(23,'1113','老师-1','123456','2018-01-11 18:15:34.564',NULL,0);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-18 11:35:59
