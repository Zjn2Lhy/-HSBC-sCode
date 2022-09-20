/*
 Navicat Premium Data Transfer

 Source Server         : zjn
 Source Server Type    : MySQL
 Source Server Version : 50540
 Source Host           : localhost:3306
 Source Schema         : airflow_db

 Target Server Type    : MySQL
 Target Server Version : 50540
 File Encoding         : 65001

 Date: 19/09/2022 15:34:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for salary
-- ----------------------------
DROP TABLE IF EXISTS `salary`;
CREATE TABLE `salary`  (
  `employee_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `salary` int(11) NULL DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of salary
-- ----------------------------
INSERT INTO `salary` VALUES ('001', 10000, '2022-01-20 00:00:00');
INSERT INTO `salary` VALUES ('001', 10010, '2022-02-20 00:00:00');
INSERT INTO `salary` VALUES ('002', 10005, '2022-01-20 00:00:00');
INSERT INTO `salary` VALUES ('002', 10000, '2022-02-20 00:00:00');
INSERT INTO `salary` VALUES ('003', 10020, '2022-01-20 00:00:00');
INSERT INTO `salary` VALUES ('003', 10020, '2022-02-20 00:00:00');
INSERT INTO `salary` VALUES ('006', 20000, '2022-01-20 00:00:00');
INSERT INTO `salary` VALUES ('006', 19000, '2022-02-20 00:00:00');

SET FOREIGN_KEY_CHECKS = 1;
