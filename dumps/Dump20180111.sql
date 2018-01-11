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
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

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
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='课程-项目-任务-知识点';
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='教师表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `wront_set`
--

DROP TABLE IF EXISTS `wront_set`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wront_set` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SID` int(11) NOT NULL COMMENT '学生编号',
  `QID` int(11) NOT NULL COMMENT '试题编号',
  `CORRECTED` int(11) NOT NULL DEFAULT '0' COMMENT '是否更正\n0-未更正\n1-已更正\n',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='错题集表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-11 15:16:48
