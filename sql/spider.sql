/*
Navicat MySQL Data Transfer

Source Server         : dy5-test
Source Server Version : 50551
Source Host           : localhost:3306
Source Database       : spider

Target Server Type    : MYSQL
Target Server Version : 50551
File Encoding         : 65001

Date: 2018-02-09 13:23:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for s_catalog
-- ----------------------------
DROP TABLE IF EXISTS `s_catalog`;
CREATE TABLE `s_catalog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `catalog_name` varchar(255) NOT NULL,
  `catalog_num` varchar(255) NOT NULL COMMENT '0-非叶子节点 1-叶子节点',
  `child_node` tinyint(4) NOT NULL,
  `parent_num` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_catalog_num` (`catalog_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=477 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for s_commodity
-- ----------------------------
DROP TABLE IF EXISTS `s_commodity`;
CREATE TABLE `s_commodity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comm_name` varchar(255) NOT NULL COMMENT '书名',
  `comm_price` varchar(255) NOT NULL COMMENT '加格 单位：元',
  `author` varchar(255) NOT NULL,
  `press` varchar(255) DEFAULT NULL COMMENT '出版社',
  `pub_data` varchar(255) DEFAULT NULL COMMENT '出版日期',
  `isbn` varchar(255) DEFAULT NULL,
  `comm_label` varchar(255) DEFAULT NULL COMMENT '标签',
  `comm_catalog` text COMMENT '书本目录',
  `preface` text COMMENT '序言',
  `picture_name` varchar(255) NOT NULL,
  `score` varchar(255) DEFAULT NULL,
  `parent_catalog_num` varchar(255) NOT NULL,
  `child_catalog_num` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21518 DEFAULT CHARSET=utf8;
