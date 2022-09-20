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

 Date: 19/09/2022 15:34:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `employee_id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `employee_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `manager_id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('001', 'Tom', '');
INSERT INTO `employee` VALUES ('002', 'Alex', '001');
INSERT INTO `employee` VALUES ('003', 'Sophia', '001');
INSERT INTO `employee` VALUES ('004', 'William', '002');
INSERT INTO `employee` VALUES ('005', 'Amelia', '');
INSERT INTO `employee` VALUES ('006', 'Aimi', NULL);

SET FOREIGN_KEY_CHECKS = 1;
