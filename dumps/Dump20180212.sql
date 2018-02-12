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
-- Table structure for table `browse_history`
--

DROP TABLE IF EXISTS `browse_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `browse_history` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RID` int(11) NOT NULL COMMENT '资源编号',
  `CREATOR` int(11) NOT NULL COMMENT '浏览人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '浏览时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=624 DEFAULT CHARSET=utf8 COMMENT='浏览记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `browse_history`
--

LOCK TABLES `browse_history` WRITE;
/*!40000 ALTER TABLE `browse_history` DISABLE KEYS */;
INSERT INTO `browse_history` VALUES (128,1,2,'2018-02-01 10:47:16.041'),(129,1,2,'2018-02-01 10:53:53.018'),(130,8,2,'2018-02-01 10:55:02.22'),(131,8,2,'2018-02-01 10:55:14.173'),(137,9,2,'2018-02-01 11:09:57.692'),(138,9,2,'2018-02-01 11:10:42.093'),(139,9,2,'2018-02-01 11:11:07.665'),(156,10,2,'2018-02-01 13:47:38.923'),(157,10,2,'2018-02-01 13:48:45.863'),(194,12,2,'2018-02-01 20:28:58.877'),(195,12,2,'2018-02-01 20:28:58.929'),(196,9,2,'2018-02-01 20:29:29.253'),(197,9,2,'2018-02-01 20:29:29.323'),(198,7,2,'2018-02-01 20:30:13.717'),(199,7,2,'2018-02-01 20:30:13.788'),(200,8,2,'2018-02-01 20:30:29.351'),(201,8,2,'2018-02-01 20:30:29.404'),(226,7,2,'2018-02-01 21:02:13.51'),(227,7,2,'2018-02-01 21:02:13.565'),(228,4,2,'2018-02-01 21:02:25.574'),(229,4,2,'2018-02-01 21:02:25.648'),(230,3,2,'2018-02-01 21:02:30.316'),(231,3,2,'2018-02-01 21:02:30.364'),(232,3,2,'2018-02-01 21:04:34.199'),(233,3,2,'2018-02-01 21:04:34.278'),(234,8,2,'2018-02-01 21:04:49.209'),(235,8,2,'2018-02-01 21:04:49.276'),(236,3,2,'2018-02-01 21:05:15.629'),(237,3,2,'2018-02-01 21:05:15.687'),(238,8,2,'2018-02-01 21:07:14.822'),(239,8,2,'2018-02-01 21:07:14.911'),(240,3,2,'2018-02-01 21:07:57.686'),(241,3,2,'2018-02-01 21:07:57.734'),(244,3,2,'2018-02-01 21:09:06.427'),(245,3,2,'2018-02-01 21:09:06.504'),(246,3,2,'2018-02-01 21:09:21.908'),(247,3,2,'2018-02-01 21:09:21.971'),(248,8,2,'2018-02-01 21:09:32.299'),(249,8,2,'2018-02-01 21:09:32.355'),(258,8,2,'2018-02-02 08:51:42.693'),(259,8,2,'2018-02-02 08:51:42.756'),(260,7,2,'2018-02-02 08:55:08.811'),(261,7,2,'2018-02-02 08:55:08.972'),(264,9,2,'2018-02-02 08:56:30.91'),(265,9,2,'2018-02-02 08:56:30.986'),(266,7,2,'2018-02-02 08:56:40.226'),(267,7,2,'2018-02-02 08:56:40.32'),(270,8,2,'2018-02-02 09:10:39.05'),(271,8,2,'2018-02-02 09:10:39.118'),(272,8,2,'2018-02-02 09:13:12.159'),(273,8,2,'2018-02-02 09:13:12.243'),(274,8,2,'2018-02-02 09:21:04.045'),(275,8,2,'2018-02-02 09:21:04.127'),(276,8,2,'2018-02-02 09:21:51.284'),(277,8,2,'2018-02-02 09:21:51.351'),(278,8,2,'2018-02-02 09:23:14.002'),(279,8,2,'2018-02-02 09:23:14.12'),(280,8,2,'2018-02-02 09:27:55.068'),(281,8,2,'2018-02-02 09:27:55.191'),(282,8,2,'2018-02-02 09:30:10.714'),(283,8,2,'2018-02-02 09:30:10.771'),(284,8,2,'2018-02-02 09:35:21.868'),(285,8,2,'2018-02-02 09:35:21.919'),(286,8,2,'2018-02-02 09:43:36.904'),(287,8,2,'2018-02-02 09:43:36.955'),(288,8,2,'2018-02-02 09:44:37.052'),(289,8,2,'2018-02-02 09:44:37.12'),(290,8,2,'2018-02-02 09:49:58.948'),(291,8,2,'2018-02-02 09:49:58.985'),(292,8,2,'2018-02-02 09:53:28.213'),(293,8,2,'2018-02-02 09:53:28.262'),(294,8,2,'2018-02-02 09:57:21.493'),(295,8,2,'2018-02-02 09:57:21.568'),(296,8,2,'2018-02-02 09:59:22.903'),(297,8,2,'2018-02-02 09:59:22.946'),(298,8,2,'2018-02-02 10:07:10.119'),(299,8,2,'2018-02-02 10:07:10.165'),(300,8,2,'2018-02-02 10:07:54.162'),(301,8,2,'2018-02-02 10:07:54.31'),(302,8,2,'2018-02-02 10:09:45.061'),(303,8,2,'2018-02-02 10:09:45.114'),(304,8,2,'2018-02-02 10:20:48.827'),(305,8,2,'2018-02-02 10:20:48.918'),(306,8,2,'2018-02-02 10:21:23.335'),(307,8,2,'2018-02-02 10:21:23.474'),(308,8,2,'2018-02-02 10:21:54.711'),(309,8,2,'2018-02-02 10:21:54.781'),(310,8,2,'2018-02-02 10:23:08.075'),(311,8,2,'2018-02-02 10:23:08.151'),(312,8,2,'2018-02-02 10:23:18.708'),(313,8,2,'2018-02-02 10:23:18.768'),(316,14,1,'2018-02-02 10:28:41.343'),(317,13,1,'2018-02-02 10:28:58.119'),(318,9,1,'2018-02-02 10:33:44.068'),(319,9,1,'2018-02-02 10:33:59.771'),(320,8,2,'2018-02-02 10:35:57.293'),(321,8,2,'2018-02-02 10:35:57.366'),(322,7,2,'2018-02-02 10:37:16.424'),(323,7,2,'2018-02-02 10:37:16.494'),(324,8,2,'2018-02-02 10:48:33.28'),(325,8,2,'2018-02-02 10:48:33.362'),(326,8,2,'2018-02-02 10:49:49.484'),(327,8,2,'2018-02-02 10:49:49.524'),(328,8,2,'2018-02-02 10:50:41.449'),(329,8,2,'2018-02-02 10:50:41.508'),(330,7,2,'2018-02-02 10:50:52.389'),(331,7,2,'2018-02-02 10:50:52.43'),(332,7,2,'2018-02-02 10:51:16.489'),(333,7,2,'2018-02-02 10:51:16.585'),(334,7,2,'2018-02-02 10:52:12.482'),(335,7,2,'2018-02-02 10:52:12.563'),(336,7,2,'2018-02-02 10:52:44.231'),(337,7,2,'2018-02-02 10:52:44.277'),(338,8,2,'2018-02-02 10:53:55.556'),(339,8,2,'2018-02-02 10:53:55.612'),(340,8,2,'2018-02-02 10:54:34.504'),(341,8,2,'2018-02-02 10:54:34.532'),(342,8,2,'2018-02-02 10:55:20.235'),(343,8,2,'2018-02-02 10:55:20.311'),(344,9,1,'2018-02-02 11:05:27.799'),(345,9,1,'2018-02-02 11:06:21.753'),(346,9,1,'2018-02-02 11:11:02.546'),(347,9,1,'2018-02-02 11:13:07.5'),(348,9,1,'2018-02-02 11:13:35.967'),(349,9,1,'2018-02-02 11:14:59.966'),(350,9,1,'2018-02-02 11:21:40.563'),(351,9,1,'2018-02-02 11:22:37.397'),(352,9,1,'2018-02-02 11:22:44.849'),(353,9,1,'2018-02-02 11:23:48.763'),(354,9,1,'2018-02-02 11:23:56.14'),(355,9,1,'2018-02-02 11:30:34.107'),(356,9,1,'2018-02-02 11:30:42.362'),(357,9,1,'2018-02-02 11:33:02.218'),(358,9,1,'2018-02-02 11:34:17.339'),(359,9,1,'2018-02-02 11:34:25.523'),(360,9,1,'2018-02-02 11:39:19.939'),(361,9,1,'2018-02-02 11:39:45.763'),(362,9,1,'2018-02-02 11:46:12.291'),(363,9,1,'2018-02-02 11:46:20.731'),(364,9,1,'2018-02-02 11:52:47.316'),(365,9,1,'2018-02-02 11:53:40.772'),(366,9,1,'2018-02-02 11:53:49.075'),(367,9,1,'2018-02-02 11:57:11.94'),(368,9,1,'2018-02-02 11:57:23.476'),(369,9,1,'2018-02-02 11:57:31.075'),(370,9,1,'2018-02-02 11:57:48.587'),(371,9,1,'2018-02-02 12:00:00.723'),(372,9,1,'2018-02-02 12:01:34.395'),(373,9,1,'2018-02-02 13:20:36.609'),(374,9,1,'2018-02-02 13:26:56.003'),(375,9,1,'2018-02-02 13:27:38.793'),(376,9,1,'2018-02-02 13:27:50.917'),(377,9,1,'2018-02-02 13:34:00.258'),(378,9,1,'2018-02-02 13:35:08.108'),(379,9,1,'2018-02-02 13:38:24.868'),(380,9,1,'2018-02-02 13:39:10.683'),(381,9,1,'2018-02-02 13:41:35.379'),(382,9,1,'2018-02-02 13:43:14.45'),(383,9,1,'2018-02-02 13:48:27.165'),(384,9,1,'2018-02-02 13:52:24.715'),(385,9,1,'2018-02-02 13:59:30.346'),(386,9,1,'2018-02-02 14:02:05.731'),(387,9,1,'2018-02-02 14:07:49.123'),(388,9,1,'2018-02-02 14:09:06.876'),(389,9,1,'2018-02-02 14:24:31.14'),(390,9,1,'2018-02-02 14:25:23.332'),(391,9,1,'2018-02-02 14:26:44.316'),(392,9,1,'2018-02-02 14:27:59.476'),(393,9,1,'2018-02-02 14:28:20.476'),(394,9,1,'2018-02-02 14:29:28.804'),(395,9,1,'2018-02-02 14:31:42.803'),(396,9,1,'2018-02-02 14:36:50.5'),(397,9,1,'2018-02-02 14:42:27.395'),(398,9,1,'2018-02-02 14:43:18.524'),(399,9,1,'2018-02-02 14:44:20.031'),(400,9,1,'2018-02-02 14:47:27.121'),(401,9,1,'2018-02-02 14:48:12.579'),(402,9,1,'2018-02-02 14:55:17.876'),(403,9,1,'2018-02-02 14:56:51.381'),(404,9,1,'2018-02-02 15:00:23.756'),(405,9,1,'2018-02-02 15:01:23.147'),(406,9,1,'2018-02-02 15:02:27.788'),(407,9,1,'2018-02-02 15:05:00.396'),(408,9,1,'2018-02-02 15:05:37.724'),(409,9,1,'2018-02-02 15:08:59.701'),(410,8,2,'2018-02-02 15:10:38.001'),(411,8,2,'2018-02-02 15:10:38.084'),(412,9,1,'2018-02-02 15:11:14.972'),(413,9,1,'2018-02-02 15:14:35.541'),(414,9,1,'2018-02-02 15:16:29.406'),(415,9,1,'2018-02-02 15:17:21.948'),(416,9,1,'2018-02-02 15:24:30.189'),(417,9,1,'2018-02-02 15:24:58.389'),(418,9,1,'2018-02-02 15:25:33.9'),(419,9,1,'2018-02-02 15:27:21.901'),(420,9,1,'2018-02-02 15:30:46.549'),(421,9,1,'2018-02-02 15:35:14.782'),(422,9,1,'2018-02-02 15:36:16.062'),(423,9,1,'2018-02-02 15:36:33.397'),(424,9,1,'2018-02-02 15:40:33.742'),(425,9,1,'2018-02-02 15:41:39.23'),(426,9,1,'2018-02-02 15:42:55.534'),(427,9,1,'2018-02-02 15:45:28.302'),(428,9,1,'2018-02-02 15:47:26.046'),(429,9,1,'2018-02-02 15:51:24.319'),(430,9,1,'2018-02-02 15:52:11.416'),(431,9,1,'2018-02-02 15:53:53.079'),(432,9,1,'2018-02-02 15:54:39.83'),(433,9,1,'2018-02-02 15:56:42.982'),(434,9,1,'2018-02-02 15:58:21.023'),(435,9,1,'2018-02-02 15:59:09.856'),(436,9,1,'2018-02-02 16:01:17.591'),(437,9,1,'2018-02-02 16:03:12.007'),(438,9,1,'2018-02-02 16:04:35.503'),(439,9,1,'2018-02-02 16:08:26.576'),(440,9,1,'2018-02-02 16:14:06.035'),(441,9,1,'2018-02-02 16:14:53.124'),(442,9,1,'2018-02-02 16:15:27.122'),(443,9,1,'2018-02-02 16:19:21.174'),(444,9,1,'2018-02-02 16:19:44.125'),(445,9,1,'2018-02-02 16:23:07.557'),(446,9,1,'2018-02-02 16:25:38.475'),(447,9,1,'2018-02-02 16:27:29.845'),(448,9,1,'2018-02-02 16:28:34.829'),(449,9,1,'2018-02-02 16:30:20.02'),(450,9,1,'2018-02-02 16:31:56.293'),(451,9,1,'2018-02-02 16:32:31.203'),(452,9,1,'2018-02-02 16:32:54.654'),(453,9,1,'2018-02-02 16:33:05.842'),(454,9,1,'2018-02-02 16:33:28.058'),(455,7,2,'2018-02-02 16:33:36.137'),(456,7,2,'2018-02-02 16:33:36.217'),(457,7,2,'2018-02-02 16:36:11.499'),(458,7,2,'2018-02-02 16:36:11.56'),(459,8,2,'2018-02-02 16:37:25.849'),(460,8,2,'2018-02-02 16:37:25.901'),(461,9,1,'2018-02-02 16:38:21.756'),(462,9,1,'2018-02-02 16:39:33.682'),(463,9,1,'2018-02-02 16:39:52.667'),(464,7,2,'2018-02-02 16:40:05.797'),(465,7,2,'2018-02-02 16:40:05.836'),(466,9,1,'2018-02-02 16:41:30.932'),(467,9,1,'2018-02-02 16:41:49.26'),(468,9,1,'2018-02-02 16:43:09.082'),(469,7,2,'2018-02-02 16:43:31.92'),(470,7,2,'2018-02-02 16:43:31.953'),(471,9,1,'2018-02-02 16:44:03.467'),(472,7,2,'2018-02-02 16:44:23.935'),(473,7,2,'2018-02-02 16:44:23.971'),(474,9,1,'2018-02-02 16:45:16.571'),(475,3,2,'2018-02-02 16:46:06.784'),(476,3,2,'2018-02-02 16:46:06.868'),(477,9,1,'2018-02-02 16:47:17.97'),(478,8,2,'2018-02-02 16:48:56.507'),(479,8,2,'2018-02-02 16:48:56.588'),(480,9,1,'2018-02-02 16:49:03.626'),(481,9,1,'2018-02-02 16:49:35.891'),(482,8,2,'2018-02-02 16:50:56.045'),(483,8,2,'2018-02-02 16:50:56.109'),(484,9,1,'2018-02-02 16:51:51.573'),(485,7,2,'2018-02-02 16:51:56.278'),(486,7,2,'2018-02-02 16:51:56.323'),(487,8,2,'2018-02-02 16:52:25.414'),(488,8,2,'2018-02-02 16:52:25.453'),(489,8,2,'2018-02-02 16:53:02.98'),(490,8,2,'2018-02-02 16:53:03.064'),(491,8,2,'2018-02-02 16:54:35.613'),(492,8,2,'2018-02-02 16:54:35.694'),(493,9,1,'2018-02-02 16:56:48.066'),(494,9,1,'2018-02-02 16:58:01.516'),(495,8,2,'2018-02-02 16:59:43.702'),(496,8,2,'2018-02-02 16:59:43.741'),(497,8,2,'2018-02-02 17:06:37.005'),(498,8,2,'2018-02-02 17:06:37.088'),(499,7,2,'2018-02-02 17:06:43.814'),(500,7,2,'2018-02-02 17:06:43.878'),(501,9,1,'2018-02-02 17:09:59.931'),(502,9,1,'2018-02-02 17:11:17.272'),(503,8,2,'2018-02-02 17:12:37.855'),(504,8,2,'2018-02-02 17:12:37.943'),(505,9,1,'2018-02-02 17:13:00.423'),(506,8,2,'2018-02-02 17:13:38.432'),(507,8,2,'2018-02-02 17:13:38.583'),(508,9,1,'2018-02-02 17:15:13.391'),(509,8,2,'2018-02-02 17:15:59.615'),(510,8,2,'2018-02-02 17:15:59.76'),(511,7,2,'2018-02-02 17:17:43.184'),(512,7,2,'2018-02-02 17:17:43.224'),(513,9,1,'2018-02-02 17:33:32.131'),(514,9,1,'2018-02-02 17:35:10.571'),(515,9,1,'2018-02-02 17:37:20.482'),(516,9,1,'2018-02-02 17:38:35.231'),(517,9,1,'2018-02-02 17:39:19.679'),(518,3,2,'2018-02-02 17:40:20.2'),(519,3,2,'2018-02-02 17:40:20.238'),(520,8,2,'2018-02-02 17:40:33.249'),(521,8,2,'2018-02-02 17:40:33.297'),(522,6,2,'2018-02-02 17:40:58.063'),(523,6,2,'2018-02-02 17:40:58.147'),(524,9,1,'2018-02-02 17:41:24.928'),(525,14,2,'2018-02-02 17:42:32.602'),(526,14,2,'2018-02-02 17:42:32.658'),(527,9,1,'2018-02-02 17:44:50.342'),(528,9,1,'2018-02-02 17:46:05.574'),(529,9,1,'2018-02-02 17:47:49.335'),(530,9,1,'2018-02-02 17:49:26.807'),(531,9,1,'2018-02-02 17:50:19.814'),(532,9,1,'2018-02-02 17:51:24.072'),(533,9,1,'2018-02-02 17:52:19.36'),(534,9,1,'2018-02-02 18:05:37.207'),(535,9,1,'2018-02-02 18:11:15.486'),(536,9,1,'2018-02-02 18:19:41.48'),(537,9,1,'2018-02-02 18:29:39.104'),(538,9,1,'2018-02-02 18:30:43.688'),(539,9,1,'2018-02-02 18:31:26.919'),(540,9,1,'2018-02-02 18:32:22.496'),(541,9,1,'2018-02-02 18:33:30.079'),(542,9,1,'2018-02-02 18:33:39.056'),(543,9,1,'2018-02-02 18:34:15.06'),(544,9,1,'2018-02-02 18:35:24.468'),(545,9,1,'2018-02-02 18:35:38.395'),(546,9,1,'2018-02-02 18:35:56.831'),(547,9,1,'2018-02-02 18:36:23.364'),(548,9,1,'2018-02-02 18:36:48.859'),(549,9,1,'2018-02-02 18:37:31.277'),(550,9,1,'2018-02-02 18:39:02.907'),(551,9,1,'2018-02-02 18:40:12.475'),(552,9,1,'2018-02-02 18:40:41.644'),(553,9,1,'2018-02-02 18:41:26.971'),(554,9,1,'2018-02-02 18:41:54.06'),(555,9,1,'2018-02-02 18:44:19.42'),(556,9,1,'2018-02-02 18:44:43.395'),(557,9,1,'2018-02-02 18:45:44.859'),(558,9,1,'2018-02-02 18:46:44.476'),(559,9,1,'2018-02-02 18:49:46.868'),(560,9,1,'2018-02-02 18:50:34.09'),(561,9,1,'2018-02-02 18:51:46.366'),(562,9,1,'2018-02-02 18:55:19.409'),(563,9,1,'2018-02-02 18:55:58.31'),(564,9,1,'2018-02-02 18:57:31.894'),(565,9,1,'2018-02-02 18:58:31.76'),(566,9,1,'2018-02-02 19:00:55.564'),(567,9,1,'2018-02-02 19:01:33.514'),(568,9,1,'2018-02-02 19:02:46.442'),(569,9,1,'2018-02-03 09:04:44.909'),(570,9,1,'2018-02-03 09:06:30.944'),(571,9,1,'2018-02-03 09:09:42.083'),(572,9,1,'2018-02-03 09:10:58.607'),(573,9,1,'2018-02-03 09:15:06.421'),(574,9,1,'2018-02-03 09:15:57.325'),(575,9,1,'2018-02-03 09:16:52.788'),(576,9,1,'2018-02-03 09:18:55.012'),(577,9,1,'2018-02-03 09:21:04.669'),(578,9,1,'2018-02-03 09:25:51.284'),(579,16,1,'2018-02-03 09:27:05.251'),(580,9,1,'2018-02-03 09:32:22.668'),(581,9,1,'2018-02-03 09:47:05.294'),(582,9,1,'2018-02-03 09:51:48.815'),(583,9,1,'2018-02-03 09:51:55.548'),(584,3,2,'2018-02-03 11:17:53.961'),(585,15,2,'2018-02-03 11:23:45.537'),(586,1,2,'2018-02-03 11:23:57.959'),(587,2,2,'2018-02-03 11:24:02.676'),(588,8,2,'2018-02-03 13:24:25.378'),(589,9,5,'2018-02-05 11:07:43.547'),(590,9,5,'2018-02-05 11:11:02.773'),(591,9,5,'2018-02-05 11:12:42.606'),(592,9,5,'2018-02-05 11:13:24.821'),(593,9,5,'2018-02-05 11:15:02.67'),(594,9,5,'2018-02-05 11:16:22.336'),(595,1,2,'2018-02-06 09:19:38.844'),(596,3,2,'2018-02-06 09:19:48.555'),(597,1,2,'2018-02-06 09:19:57.629'),(598,14,2,'2018-02-06 09:20:08.457'),(599,3,2,'2018-02-06 09:20:16.975'),(600,8,2,'2018-02-06 09:20:23.039'),(601,7,2,'2018-02-06 09:21:02.104'),(602,12,2,'2018-02-06 09:22:11.494'),(603,9,1,'2018-02-07 17:23:01.863'),(604,16,2,'2018-02-08 18:19:09.556'),(605,7,2,'2018-02-08 18:19:17.523'),(606,9,2,'2018-02-09 10:12:13.082'),(607,16,2,'2018-02-09 10:12:20.477'),(608,14,2,'2018-02-09 10:37:57.013'),(609,16,2,'2018-02-09 10:38:57.486'),(610,5,2,'2018-02-09 10:39:20.422'),(611,10,2,'2018-02-09 10:39:27.763'),(612,12,2,'2018-02-09 10:39:31.543'),(613,15,2,'2018-02-09 10:39:47.871'),(614,5,2,'2018-02-09 10:39:57.417'),(615,16,2,'2018-02-09 10:45:19.935'),(616,5,2,'2018-02-09 10:45:31.723'),(617,5,2,'2018-02-09 10:47:45.11'),(618,5,2,'2018-02-09 10:50:25.227'),(619,6,2,'2018-02-09 10:50:43.877'),(620,6,2,'2018-02-09 10:52:22.356'),(621,6,2,'2018-02-09 10:56:04.893'),(622,5,2,'2018-02-09 10:56:17.464'),(623,3,2,'2018-02-09 10:56:38.43');
/*!40000 ALTER TABLE `browse_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catalog`
--

DROP TABLE IF EXISTS `catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalog` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `INDEX` float DEFAULT NULL COMMENT '排序（预留字段）',
  `NAME` varchar(45) NOT NULL COMMENT '目录名称',
  `LESSONS` int(11) NOT NULL COMMENT '课时',
  `RID` varchar(45) DEFAULT NULL COMMENT '关联到上级目录的ID',
  `CREATOR_ID` int(11) NOT NULL COMMENT '创建人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `TYPE` tinyint(1) DEFAULT NULL COMMENT '类型：0-课程、1-章、2-节、3-知识点',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='课程目录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalog`
--

LOCK TABLES `catalog` WRITE;
/*!40000 ALTER TABLE `catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `catalog` ENABLE KEYS */;
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
  `CREATOR` int(11) NOT NULL COMMENT '创建人',
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
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RID` int(11) NOT NULL COMMENT '资源编号',
  `CREATOR` int(11) NOT NULL COMMENT '收藏人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '收藏时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='收藏记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
INSERT INTO `collection` VALUES (1,4,2,'2018-01-28 19:50:32.401',0),(2,3,2,'2018-01-31 17:13:47.774',0),(3,7,2,'2018-02-01 10:22:01.763',1),(4,1,2,'2018-02-01 10:54:17.968',0),(5,12,1,'2018-02-01 11:51:06.656',1),(6,12,1,'2018-02-01 11:58:56.854',0),(7,16,1,'2018-02-01 18:45:33.698',0),(8,9,1,'2018-02-02 14:20:37.255',1),(9,9,1,'2018-02-02 14:20:54.461',1),(10,9,5,'2018-02-05 11:12:51.973',1),(11,9,5,'2018-02-05 11:13:05.477',1),(12,9,5,'2018-02-05 11:13:30.182',0);
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elec_comp`
--

DROP TABLE IF EXISTS `elec_comp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `elec_comp` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` varchar(45) NOT NULL COMMENT '类型名称,如:继电器(类)',
  `NAME` varchar(45) NOT NULL COMMENT '元器件名称',
  `MODEL` varchar(45) NOT NULL COMMENT '元器件型号，如：cj-100',
  `MDL_PATH` varchar(45) NOT NULL COMMENT '模型路径',
  `CFG_PATH` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `MODEL_UNIQUE` (`MODEL`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='元器件类别表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elec_comp`
--

LOCK TABLES `elec_comp` WRITE;
/*!40000 ALTER TABLE `elec_comp` DISABLE KEYS */;
INSERT INTO `elec_comp` VALUES (10,'接触器','交流接触器','CJX2-12','Model/CJX2-12/CJX2-12.j3o','configurations/Accontactor/CJX2-1210.xml'),(11,'继电器','热继电器','JR36-20','Model/JR36-20/JR36-20.j3o','configurations/Relay/JR36-20.xml'),(12,'电抗器','电抗器','电抗器','Model/JZ7-44/JZ7-44.j3o',NULL),(13,'断路器','自动空气断路器','DZ47-63C16','Model/NBE7/NBE7.j3o',NULL),(14,'断路器','自动空气断路器','DZ47-63C5','Model/NP4/NP4.j3o',NULL),(15,'断路器','断路器','DZ47LEC32','Model/NR2-25/NR2-25.j3o',NULL),(16,'接触器','接触器','LC1-D09M7C','Model/RT28N-32X/RT28N-32X.j3o',NULL),(17,'接触器','电磁接触器','CJX1-45','Model/YBLX-K1/YBLX-K1.j3o',NULL);
/*!40000 ALTER TABLE `elec_comp` ENABLE KEYS */;
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
  `TIME` int(3) DEFAULT '100' COMMENT '限定考核时间（单位：分钟）',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题库类型：\n0-模拟题\n1-真题\n2-教师个人题库',
  `TID` int(11) NOT NULL DEFAULT '0' COMMENT '所属教师',
  `NUM` int(6) NOT NULL DEFAULT '0' COMMENT '试题数量',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（教师/管理员）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='试题库';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library`
--

LOCK TABLES `library` WRITE;
/*!40000 ALTER TABLE `library` DISABLE KEYS */;
INSERT INTO `library` VALUES (1,'维修电工（初级）单选题（1）',100,0,0,20,1,'2018-02-12 11:15:52.776',NULL,NULL,0),(2,'维修电工（初级）单选题（2）',100,0,0,0,1,'2018-02-12 11:17:14.175',NULL,NULL,0),(3,'维修电工（初级）单选题（3）',100,0,0,0,1,'2018-02-12 11:17:32.934',NULL,NULL,0),(4,'维修电工（初级）单选题（4）',100,0,0,0,1,'2018-02-12 11:19:46.592',NULL,NULL,0),(5,'维修电工（初级）单选题（5）',100,0,0,0,1,'2018-02-12 11:21:43.6',NULL,NULL,0),(6,'维修电工（初级）单选题（6）',100,0,0,0,1,'2018-02-12 11:22:32.814',NULL,NULL,0),(7,'维修电工（初级）单选题（7）',100,0,0,0,1,'2018-02-12 11:22:58.864',NULL,NULL,0);
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
  `INDEX` tinyint(3) NOT NULL COMMENT '试题序号(保留字段)',
  `ANSWER` varchar(200) DEFAULT NULL COMMENT '学生答案',
  `QID` int(11) NOT NULL COMMENT '试题编号',
  `LRID` int(11) NOT NULL COMMENT '考核/练习记录编号',
  `CORRECTED` tinyint(1) DEFAULT '0' COMMENT '0-未更正(还是错的状态)\n1-已更正',
  `CORRECT_ANSWER` varchar(45) DEFAULT NULL COMMENT '用户最后一次更正后的答案',
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
  `AVERAGE` float(3,2) DEFAULT NULL COMMENT '平均分数',
  `CREATOR` int(11) NOT NULL COMMENT '发布人（学生/教师）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '发布时间',
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
-- Table structure for table `library_record`
--

DROP TABLE IF EXISTS `library_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `library_record` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '试题考核/练习记录编号',
  `PID` int(11) NOT NULL COMMENT '发布记录编号',
  `SCORE` int(3) NOT NULL COMMENT '得分',
  `COST` bigint(13) NOT NULL COMMENT '用时（单位：毫秒）',
  `CREATOR` int(11) NOT NULL,
  `CREATE_DATE` varchar(24) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='试题记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library_record`
--

LOCK TABLES `library_record` WRITE;
/*!40000 ALTER TABLE `library_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `library_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preparation`
--

DROP TABLE IF EXISTS `preparation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preparation` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CID` int(11) NOT NULL COMMENT '知识点编号',
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
-- Table structure for table `preparation_resource`
--

DROP TABLE IF EXISTS `preparation_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `preparation_resource` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '备课资源编号',
  `TYPE` tinyint(1) NOT NULL COMMENT '备课资源类型:\n0-资源库资源\n1-元器件认知\n2-典型案例\n',
  `PID` int(11) NOT NULL COMMENT '备课编号',
  `RID` varchar(45) NOT NULL COMMENT '资源编号\n认知编号\n案例编号',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（管理员/教师编号）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（管理员/教师编号）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='备课资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preparation_resource`
--

LOCK TABLES `preparation_resource` WRITE;
/*!40000 ALTER TABLE `preparation_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `preparation_resource` ENABLE KEYS */;
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
  `OPTIONS` varchar(250) DEFAULT NULL COMMENT '试题选项',
  `IMGS` varchar(100) DEFAULT NULL COMMENT '试题配图路径（预留字段）',
  `REFERENCE` varchar(250) DEFAULT NULL COMMENT '参考答案',
  `ANALYSIS` varchar(250) DEFAULT NULL COMMENT '题目解析',
  `TYPE` tinyint(1) NOT NULL DEFAULT '0' COMMENT '试题类型：\n0-选择题\n1-判断题\n2-填空题\n3-主观题\n',
  `RID` int(11) NOT NULL COMMENT '关联编号（题库编号/知识点编号）\n',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（教师/管理员）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='试题表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'根据表达信息的内容，电气图分为______种。','A、1|B、2|C、3|D、4',NULL,'B','难度：1\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(2,'电气图形符号的形式有______种。','A、1|B、2|C、3|D、4',NULL,'D','难度：1\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(3,'______是指用于图样或其他技术文件中，表示电气元件或电气设备性能的图形、标记或字符。','A、图形符号|B、文字符号|C、回路标号|D、明细符号',NULL,'A','难度：1\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(4,'一个完整的项目代号应由______部分组成。','A、1|B、2|C、3|D、4',NULL,'D','难度：1\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(5,'中性线的文字符号是______。','A、E|B、N|C、PE|D、PEN',NULL,'C','难度：2\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(6,'保护接地的文字符号是______。','A、E|B、N|C、PE|D、PEN',NULL,'C','难度：2\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(7,'保护接地线与中性线共用时，其文字符号是______。','A、E|B、N|C、PE|D、PEN',NULL,'D','难度：2\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(8,'在电气原理图中，主回路标号由______和数字标号两部分组成。','A、图形标号|B、文字标号|C、基本符号|D、一般符号',NULL,'B','难度：2\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(9,'在电气图上，一般电路或元器件是按功能布置，并按工作顺序______排列的。','A、从前向后，从左到右|B、从前向后，从小到大|C、从上向下，从小到大|D、从左到右，从上向下',NULL,'D','难度：3\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(10,'接线图中，一般需要提供项目的相对位置、项目代号、端子号和______。','A、导线号|B、单元号|C、接线图号|D、元器件号',NULL,'A','难度：3\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(11,'电气图包括电路图、功能表图、系统图与框图和______等。','A、装配图|B、位置图|C、部件图|D、元器件图',NULL,'B','难度：3\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(12,'隔离开关画断开位置时，在电气图属于______。','A、正常状态|B、不正常状态|C、断开状态|D、闭合状态',NULL,'A','难度：3\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(13,'安装图中的围框线用______表示。','A、折断线|B、细实线|C、粗实线|D、点划线',NULL,'D','难度：4\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(14,'电气图中的母线应采用______。','A、虚线|B、细实线|C、中实线|D、粗实线',NULL,'D','难度：4\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(15,'电气图中的尺寸线和尺寸界线应采用______。','A、虚线|B、细实线|C、中实线|D、粗实线',NULL,'B','难度：4\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(16,'电气图中的围框线用______表示。','A、虚线|B、细实线|C、粗实线|D、点划线',NULL,'C','难度：4\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(17,'接线表应与______相配合。','A、电路图|B、接线图|C、功能图|D、逻辑图',NULL,'B','难度：5\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(18,'电气图的图区编号的作用是______。','A、美观大方|B、便于装订|C、便于理解全电路的工作原理|D、便于检索图中电气线路或元件',NULL,'D','难度：5\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(19,'仅给出系统的功能布置，为进一步绘制电路图或其他简图提供依据的图是_____。','A、系统图|B、功能图|C、电路图|D、框图',NULL,'B','难度：5\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0),(20,'电路图的主要用途之一是______。','A、表示框图|B、表示功能图|C、提供安装位置|D、设计编制接线图的基础资料',NULL,'D','难度：5\r\n知识点：电气图的基本知识',0,1,1,'2018-02-12 11:24:48.068',NULL,NULL,0);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
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
  `TYPE` tinyint(1) NOT NULL COMMENT '资源类型：\n0-PNG/JPG/GIF\n1-SWF\n2-MP4/WMV/RMVB/FLV/AVI\n3-TXT\n4-Word\n5-PPT\n6-EXCEL\n7-PDF',
  `PATH` varchar(100) NOT NULL COMMENT '资源路径',
  `KEYWORD` varchar(100) DEFAULT NULL COMMENT '关键词（“|”分割）',
  `DETAIL` varchar(250) DEFAULT NULL COMMENT '资源描述',
  `CREATOR` int(11) NOT NULL COMMENT '创建人（教师/管理员）',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人（教师/管理员）',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` int(11) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `OPEN` tinyint(1) DEFAULT NULL,
  `BROWSED_TIMES` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `COLLECTED_TIMES` int(11) NOT NULL DEFAULT '0' COMMENT '收藏次数',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='资源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resource`
--

LOCK TABLES `resource` WRITE;
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
INSERT INTO `resource` VALUES (1,'1',0,'resources/1516757727944.png','111|222',NULL,1,'2018-01-24 09:35:28.24',NULL,NULL,0,0,41,0),(2,'主轴调试',2,'resources/1516792518694.wmv',NULL,NULL,1,'2018-01-24 19:15:25.053',NULL,NULL,0,0,12,0),(3,'机械传动操作手册',7,'resources/1517140173197.pdf','机械传动 操作手册',NULL,2,'2018-01-28 19:49:33.274',NULL,NULL,0,0,15,0),(4,'查看文件MD5码',3,'resources/1517140197602.txt','MD5',NULL,2,'2018-01-28 19:49:57.614',NULL,NULL,0,0,5,0),(5,'6140产品说明书1220',7,'resources/1517214085675.pdf','测试|02',NULL,1,'2018-01-29 16:21:25.787',NULL,NULL,0,0,75,0),(6,'teachResources',1,'resources/1517222986737.swf','测试|03',NULL,1,'2018-01-29 18:49:46.929',NULL,NULL,0,0,10,0),(7,'xajh',2,'resources/4dae3b67-1d55-4125-a577-4086585464c1.mp4','笑傲江湖',NULL,2,'2018-02-01 18:49:46.929',NULL,NULL,0,0,23,-1),(8,'Mux',2,'Test/Mux140928003405.avi','Mux',NULL,2,'2018-02-01 18:49:46.929',NULL,NULL,0,0,53,0),(9,'xajh',2,'resources/xajh.avi','xajh',NULL,1,'2018-02-01 18:49:46.929',NULL,NULL,0,0,238,0),(10,'工厂虚拟仿真操作说明书',4,'d4a88843-2f95-43f9-91c9-f84fbe90873d.docx','测试|19',NULL,1,'2018-02-01 11:23:59.84',NULL,NULL,0,0,7,0),(11,'3D电工仿真项目方案书2',4,'8ac656c7-9354-4e3e-8c7a-1d1d317acb6d.docx','测试|19',NULL,1,'2018-02-01 11:44:55.558',NULL,NULL,0,0,1,0),(12,'ZHE 是一个WPS 文件',4,'1d17005f-42cb-4c6d-be1e-8e29238521eb.docx','测试|19',NULL,1,'2018-02-01 11:47:38.176',NULL,NULL,0,0,7,1),(13,'添加PageOffice的jar包到本地Maven仓库',4,'85296419-9b52-4a58-9ad3-5de7798d15d5.doc','测试|19',NULL,1,'2018-02-01 13:32:02.676',NULL,NULL,0,0,1,0),(14,'XlsxEngine',6,'9d7d4189-22ec-4e56-b0fd-bb9c0d810583.xlsx',NULL,NULL,1,'2018-02-01 17:25:43.864',NULL,NULL,0,0,5,0),(15,'混合式课程建设的理论与实践',5,'79910c39-4402-4b3b-97f9-7116e45a1517.pptx',NULL,NULL,1,'2018-02-01 17:27:03.148',NULL,NULL,0,0,2,0),(16,'新建文本文档',3,'f51860aa-60e3-49f8-85a7-da28905a49d0.txt',NULL,NULL,1,'2018-02-01 17:27:54.642',NULL,NULL,0,0,8,1);
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='学生表（废弃）';
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
  `CREATOR` int(11) NOT NULL COMMENT '创建人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='教师表（废弃）';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES (1,'admin','管理员','123456',0,'2018-01-19 08:15:30',NULL,NULL,0),(4,'001','张老师','123',1,'2018-01-19 08:15:30',NULL,NULL,0),(5,'admin','管理员','123456',0,'2018-01-19 15:04:09',NULL,NULL,0),(6,'admin','管理员','123456',0,'2018-01-19 15:04:47',NULL,NULL,0);
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typical_case`
--

DROP TABLE IF EXISTS `typical_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typical_case` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `ARCHIVE_PATH` varchar(45) DEFAULT NULL COMMENT '存档路径',
  `CREATOR_ID` varchar(45) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typical_case`
--

LOCK TABLES `typical_case` WRITE;
/*!40000 ALTER TABLE `typical_case` DISABLE KEYS */;
INSERT INTO `typical_case` VALUES (1,'小车往返控制电路','archives\\Test.xml','2');
/*!40000 ALTER TABLE `typical_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CODE` varchar(10) NOT NULL COMMENT '登录账户',
  `NAME` varchar(20) NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(8) NOT NULL DEFAULT '123456' COMMENT '登录密码',
  `CID` int(11) DEFAULT NULL COMMENT '班级编号',
  `TID` int(11) DEFAULT NULL COMMENT '教师编号',
  `ROLE` int(11) NOT NULL COMMENT '角色\n0-管理员\n1-教师\n2-学生',
  `CREATOR` int(11) NOT NULL COMMENT '创建人',
  `CREATE_DATE` varchar(24) NOT NULL COMMENT '创建时间',
  `UPDATER` int(11) DEFAULT NULL COMMENT '修改人',
  `UPDATE_DATE` varchar(24) DEFAULT NULL COMMENT '修改时间',
  `DEL` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','管理员','123456',NULL,NULL,3,0,'2018-01-19 15:06:11',NULL,NULL,0),(2,'001','张老师','123456',0,NULL,2,1,'2018-01-19 15:06:11',NULL,NULL,0),(5,'0001','张三','123456',1,2,1,1,'2018-01-19 15:06:11',NULL,NULL,0);
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

-- Dump completed on 2018-02-12 14:16:47
