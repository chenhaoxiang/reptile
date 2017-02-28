/*
SQLyog Ultimate v8.32 
MySQL - 5.7.14 : Database - reptile_baiduzhidao
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`reptile_baiduzhidao` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `reptile_baiduzhidao`;

/*Table structure for table `answer` */

DROP TABLE IF EXISTS `answer`;

CREATE TABLE `answer` (
  `id` varchar(32) NOT NULL,
  `problem_id` varchar(32) DEFAULT NULL COMMENT '问题id',
  `answer_time` varchar(100) DEFAULT NULL COMMENT '回答时间',
  `answer_anthor` varchar(100) DEFAULT NULL COMMENT '回答者',
  `anthor_grade` varchar(32) DEFAULT NULL COMMENT '回答者的等级',
  `point_praise` varchar(32) DEFAULT NULL COMMENT '点赞数',
  `contempt_number` varchar(32) DEFAULT NULL COMMENT '拍砖数',
  `answer` text COMMENT '回答内容',
  PRIMARY KEY (`id`),
  KEY `FK_answer` (`problem_id`),
  CONSTRAINT `FK_answer` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `answer` */

/*Table structure for table `keyword` */

DROP TABLE IF EXISTS `keyword`;

CREATE TABLE `keyword` (
  `id` varchar(32) NOT NULL,
  `keyword` varchar(32) DEFAULT NULL COMMENT '搜索的关键字',
  `search_time` datetime DEFAULT NULL COMMENT '搜索时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `keyword` */

/*Table structure for table `keyword_problem` */

DROP TABLE IF EXISTS `keyword_problem`;

CREATE TABLE `keyword_problem` (
  `keyword_id` varchar(32) NOT NULL COMMENT '关键字id',
  `problem_id` varchar(32) NOT NULL COMMENT '问题id',
  PRIMARY KEY (`keyword_id`,`problem_id`),
  KEY `FK_keyword_problem2` (`problem_id`),
  CONSTRAINT `FK_keyword_problem` FOREIGN KEY (`keyword_id`) REFERENCES `keyword` (`id`),
  CONSTRAINT `FK_keyword_problem2` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `keyword_problem` */

/*Table structure for table `problem` */

DROP TABLE IF EXISTS `problem`;

CREATE TABLE `problem` (
  `id` varchar(32) NOT NULL,
  `problem` varchar(500) DEFAULT NULL COMMENT '问题',
  `problem_describe` text COMMENT '问题描述',
  `problem_anthor` varchar(100) DEFAULT NULL COMMENT '提问者',
  `quiz_time` varchar(100) DEFAULT NULL COMMENT '提问时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `problem` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
