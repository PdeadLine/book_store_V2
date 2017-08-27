/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.56 : Database - bookstore
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bookstore` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bookstore`;

/*Table structure for table `book` */

DROP TABLE IF EXISTS `book`;

CREATE TABLE `book` (
  `bid` char(32) NOT NULL,
  `bname` varchar(40) DEFAULT NULL,
  `price` double DEFAULT '0',
  `cid` char(40) DEFAULT NULL,
  `author` varchar(30) DEFAULT NULL,
  `image` varchar(200) DEFAULT NULL,
  `del` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`bid`),
  KEY `cid` (`cid`),
  CONSTRAINT `cid` FOREIGN KEY (`cid`) REFERENCES `category` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `book` */

insert  into `book`(`bid`,`bname`,`price`,`cid`,`author`,`image`,`del`) values ('084FA0A8B9A642AEA8AEC2DEA3B979E7','666',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','me','book_img/8E2753761B3F4B2685DAF711DB97EB16_1.jpg',0),('0A5F69FEA32C4CC7A3013C44031BA1F1','t3',40.7,'61DA05DB82B043D0A51A387221409D21','t3','book_img/9684C02DBB5D419FAD04D675023B5ADB_6.jpg',0),('25642DAC566148DCA6E23B2E5E623DDB','888888',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','open','book_img/E73C48A6D1C54DE58662E38C0190EAAD_4.jpg',0),('48AA0F0360DC41E88BD85B7B54F2C923','5555',50.1,'61DA05DB82B043D0A51A387221409D21','winter','book_img/1A24D75000F74124A0CA69C7521BF5C3_20.jpg',0),('4EF3D18B2B6F4964A5A6050B98FD34B5','444',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','1','book_img/4FFD19080C5C4F0EB10D1A957722F4E9_9.jpg',0),('54501EEB753847C88A6E67559255EF9A','three',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','tree','book_img/367C60D0F4774FF6A353D36FC77945CE_50.jpg',0),('606F120FF3AE48259F1FF8E68DEA08BB','333',40.7,'61DA05DB82B043D0A51A387221409D21','abc','book_img/FBB6599225F942A9B32579D2FB8EFC18_7.bmp',0),('683E01953C254AACB0458824CD39B7F6','1111111111111',59,'FB1E6BF05DCB4694A0AD48AAD45779FE','shit','book_img/39C677C3104649DAAFEC119B34ACE714_1.jpg',0),('8A780BD039BD4834946A99B1878CCEDD','22222222222222',99,'61DA05DB82B043D0A51A387221409D21','myself','book_img/D85B5C6234FD4B75924022B258E76651_6.jpg',0),('8BED7F9DD76F4D21AAE924ED466B6F5F','77777',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','lex','book_img/D8BB72B96B8E460BA572EBBC12324281_200.jpg',0),('A09FAC41C8264B75AEA9E968849E5ADA','t1',40.7,'61DA05DB82B043D0A51A387221409D21','t1','book_img/08F67B88807A4375AAE90094C3B53309_9.jpg',0),('B036DFE2B70B4EA2886971B3AFCD36C1','11111111',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','1111111111111','book_img/F4A39239284E452F8CA446B0C72E2BB9_4.jpg',0),('C36243A108E6476E8C29C091B79D3D15','10000000',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','github','book_img/A09E06AD308E439BA0F39457D745939C_100.jpg',0),('C84829AB108047C69766A88A74F5885D','t2',40.7,'61DA05DB82B043D0A51A387221409D21','t2','book_img/FE55CEFDE7704B77B8ADC8B96DAFDE1B_8.jpg',0),('DD9B948455D24C01A8E2259039102B64','2222222222222',40.7,'D8A9A3D1B4464CD08876774F73A5B8B7','2222222222222222222','book_img/F7427B43594D4F18BD7949E3642055A3_5.jpg',0),('E9DC87CEFF8D4B9E93DA9B9DBE143D2A','99999',40.7,'61DA05DB82B043D0A51A387221409D21','abc','book_img/EC682ABFA70F4CA1B7131DC9D15299FC_9.jpg',0);

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `cid` char(32) NOT NULL,
  `cname` varchar(100) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `category` */

insert  into `category`(`cid`,`cname`) values ('61DA05DB82B043D0A51A387221409D21','java'),('D8A9A3D1B4464CD08876774F73A5B8B7','c++'),('FB1E6BF05DCB4694A0AD48AAD45779FE','javaScript');

/*Table structure for table `orderitem` */

DROP TABLE IF EXISTS `orderitem`;

CREATE TABLE `orderitem` (
  `iid` char(32) NOT NULL,
  `count` int(11) DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `oid` char(32) DEFAULT NULL,
  `bid` char(32) DEFAULT NULL,
  PRIMARY KEY (`iid`),
  KEY `oid` (`oid`),
  KEY `bid` (`bid`),
  CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`oid`) REFERENCES `orders` (`oid`),
  CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`bid`) REFERENCES `book` (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `orderitem` */

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `oid` varchar(100) NOT NULL,
  `ordertime` timestamp NOT NULL DEFAULT '2016-01-01 00:00:00',
  `total` decimal(10,2) DEFAULT NULL,
  `sate` smallint(1) DEFAULT NULL,
  `uid` char(32) DEFAULT NULL,
  `receiveAddress` varchar(255) DEFAULT NULL,
  `receiveName` varchar(20) DEFAULT NULL,
  `reveicePhone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`oid`),
  KEY `uid` (`uid`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `t_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `orders` */

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `uid` char(32) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `code` char(64) NOT NULL,
  `state` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`uid`,`username`,`password`,`email`,`code`,`state`) values ('4E18D37D802E4493B1DE60379F043259','github','111','hacke594@163.com','C74B55BAF42E4A0287CB6326A6813071560863BDDA074C9EAB7FCAD24E7FC337',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;