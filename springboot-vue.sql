/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : springboot-vue

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 17/01/2023 20:08:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出版社',
  `create_time` date NULL DEFAULT NULL COMMENT '出版时间',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '1' COMMENT '0：未归还 1：已归还',
  `borrownum` int NOT NULL DEFAULT 0 COMMENT '此书被借阅次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (9, '20211880115', '十万个为什么', 15.00, '苏格与', '温州大学出版社', '2022-12-05', '0', 18);
INSERT INTO `book` VALUES (10, '20211880119', '五万个为什么', 20.00, '聂家辉', '温州大学出版社', '2022-12-01', '0', 19);
INSERT INTO `book` VALUES (11, '20211880106', '一万个为什么', 16.00, '丁奕中', '温州大学出版社', '2022-12-02', '1', 13);
INSERT INTO `book` VALUES (12, '20211880114', '计算机操作系统', 32.00, '凌浩晨', '温州大学出版社', '2022-12-03', '1', 4);
INSERT INTO `book` VALUES (13, '20211880118', '伊索寓言', 25.00, '沈益威', '温州大学出版社', '2022-12-04', '1', 12);
INSERT INTO `book` VALUES (15, '20211880109', '格林童话', 20.00, '张超祥', '温州大学出版社', '2022-12-05', '1', 3);
INSERT INTO `book` VALUES (16, '20211880100', '图书管理系统', 188.00, '聂家辉', '温州大学出版社', '2022-08-10', '0', 16);
INSERT INTO `book` VALUES (17, '20211880128', '和另一个自己谈谈心', 12.00, '\r\n武志红', '中国友谊出版公司', '2021-01-01', '1', 12);
INSERT INTO `book` VALUES (18, '20211880111', '全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲', 12.00, '全国计算机技术与软件专业技术资格(水平)考试办公室', '清华大学出版社', '2004-05-22', '1', 1);
INSERT INTO `book` VALUES (19, '20211880145', '简单减肥餐, 好吃不反弹', 20.00, '\r\n萨巴蒂娜', '中国轻工业出版社', '2022-07-07', '1', 1);
INSERT INTO `book` VALUES (20, '20211880155', '其实她真的好喜欢你', 25.00, '\r\n莫妮打', '时代文艺出版社', '2022-02-02', '1', 0);
INSERT INTO `book` VALUES (21, '20211880156', '导游业务. 第7版', 24.00, '\r\n全国导游人员资格考试教材编写组', '旅游教育出版社', '2022-08-22', '1', 0);
INSERT INTO `book` VALUES (22, '20211880166', '法医秦明. 幸存者', 22.00, '\r\n法医秦明', '北京联合出版公司', '2022-02-22', '1', 0);
INSERT INTO `book` VALUES (23, '20211880177', '中国书法史', 24.00, '\r\n徐建融', '浙江人民美术出版社', '2021-11-01', '1', 0);
INSERT INTO `book` VALUES (24, '20211880199', '实用百草治百病', 22.00, '\r\n宋纬文', '福建科学技术出版社', '2021-09-22', '1', 0);
INSERT INTO `book` VALUES (25, '20211880176', '烈火芳菲', 20.00, '赵方新', '北京十月文艺出版社', '2022-01-22', '1', 0);

-- ----------------------------
-- Table structure for bookwithuser
-- ----------------------------
DROP TABLE IF EXISTS `bookwithuser`;
CREATE TABLE `bookwithuser`  (
  `id` bigint NOT NULL COMMENT '读者id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图书编号',
  `book_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书名',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '读者姓名',
  `lendtime` datetime NULL DEFAULT NULL COMMENT '借阅时间',
  `deadtime` datetime NULL DEFAULT NULL COMMENT '应归还时间',
  `prolong` int NULL DEFAULT NULL COMMENT '续借次数',
  PRIMARY KEY (`book_name`) USING BTREE,
  INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bookwithuser
-- ----------------------------
INSERT INTO `bookwithuser` VALUES (21, '20211880119', '五万个为什么', 'db', '2023-01-01 13:38:36', '2023-01-31 13:38:36', 1);
INSERT INTO `bookwithuser` VALUES (28, '20211880115', '十万个为什么', '123', '2022-12-29 09:22:08', '2023-02-27 09:22:08', 0);
INSERT INTO `bookwithuser` VALUES (27, '20211880100', '图书管理系统', '12', '2022-12-29 08:48:36', '2023-01-28 08:48:36', 2);

-- ----------------------------
-- Table structure for lend_record
-- ----------------------------
DROP TABLE IF EXISTS `lend_record`;
CREATE TABLE `lend_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reader_id` bigint NOT NULL COMMENT '读者id',
  `isbn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图书编号',
  `bookname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图书名',
  `lend_time` datetime NULL DEFAULT NULL COMMENT '借书日期',
  `return_time` datetime NULL DEFAULT NULL COMMENT '还书日期',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '0：未归还 1：已归还',
  `borrownum` int NOT NULL COMMENT '此书被借阅次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2030043138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lend_record
-- ----------------------------
INSERT INTO `lend_record` VALUES (-2048151551, 21, '20211880119', '五万个为什么', '2023-01-01 13:38:36', NULL, '0', 19);
INSERT INTO `lend_record` VALUES (-1859469311, 21, '20211880114', '计算机操作系统', '2022-12-26 17:31:05', '2023-01-01 13:38:22', '1', 4);
INSERT INTO `lend_record` VALUES (-1851039742, 21, '20211880100', '图书管理系统', '2022-12-23 19:30:59', '2022-12-23 19:32:00', '1', 13);
INSERT INTO `lend_record` VALUES (-1691627519, 27, '20211880100', '图书管理系统', '2022-12-29 08:48:36', NULL, '0', 16);
INSERT INTO `lend_record` VALUES (-1230368766, 21, '20211880115', '十万个为什么', '2022-12-24 15:10:31', '2022-12-24 15:17:51', '1', 13);
INSERT INTO `lend_record` VALUES (-1171603454, 21, '20211880106', '一万个为什么', '2021-12-26 17:31:01', '2022-12-31 15:58:52', '1', 12);
INSERT INTO `lend_record` VALUES (-1007984639, 21, '20211880115', '十万个为什么', '2022-12-23 19:34:28', '2022-12-23 19:34:33', '1', 12);
INSERT INTO `lend_record` VALUES (-785686526, 21, '20211880115', '十万个为什么', '2022-12-23 19:31:28', '2022-12-23 19:32:05', '1', 11);
INSERT INTO `lend_record` VALUES (-705966078, 28, '20211880115', '十万个为什么', '2022-12-29 09:22:08', NULL, '0', 18);
INSERT INTO `lend_record` VALUES (-693452798, 21, '20211880100', '图书管理系统', '2022-12-26 17:31:07', '2022-12-27 13:16:54', '1', 15);
INSERT INTO `lend_record` VALUES (-630497279, 21, '20211880109', '格林童话', '2022-12-23 19:31:31', '2022-12-23 19:32:10', '1', 2);
INSERT INTO `lend_record` VALUES (-483696639, 21, '20211880118', '伊索寓言', '2022-12-23 19:31:29', '2022-12-23 19:32:11', '1', 9);
INSERT INTO `lend_record` VALUES (-391421951, 21, '20211880128', '和另一个自己谈谈心', '2022-12-23 19:35:41', '2022-12-23 19:35:46', '1', 11);
INSERT INTO `lend_record` VALUES (-177598462, 21, '20211880115', '十万个为什么', '2022-12-24 15:23:24', '2022-12-24 15:23:33', '1', 15);
INSERT INTO `lend_record` VALUES (-18128895, 21, '20211880111', '全国计算机技术与软件专业技术资格(水平)考试. 系统分析师考试大纲', '2022-12-23 19:31:35', '2022-12-23 19:32:15', '1', 1);
INSERT INTO `lend_record` VALUES (19632129, 21, '20211880106', '一万个为什么', '2022-12-31 16:05:24', '2023-01-01 13:38:29', '1', 13);
INSERT INTO `lend_record` VALUES (262901761, 21, '20211880109', '格林童话', '2022-12-31 15:58:56', '2023-01-01 13:38:24', '1', 3);
INSERT INTO `lend_record` VALUES (611016705, 21, '20211880119', '五万个为什么', '2022-12-23 19:31:28', '2022-12-23 19:31:57', '1', 18);
INSERT INTO `lend_record` VALUES (749342722, 21, '20211880115', '十万个为什么', '2022-12-24 15:26:33', '2022-12-24 15:29:49', '1', 16);
INSERT INTO `lend_record` VALUES (761970690, 21, '20211880118', '伊索寓言', '2022-12-26 17:31:02', '2023-01-01 13:38:28', '1', 12);
INSERT INTO `lend_record` VALUES (1001086978, 21, '20211880118', '伊索寓言', '2022-12-23 19:33:59', '2022-12-23 19:34:02', '1', 10);
INSERT INTO `lend_record` VALUES (1017778177, 21, '20211880115', '十万个为什么', '2022-12-24 15:19:35', '2022-12-24 15:19:50', '1', 14);
INSERT INTO `lend_record` VALUES (1172967426, 21, '20211880100', '图书管理系统', '2022-12-24 15:28:35', '2022-12-24 15:30:12', '1', 14);
INSERT INTO `lend_record` VALUES (1181442050, 21, '20211880106', '一万个为什么', '2022-12-23 19:31:29', '2022-12-23 19:32:06', '1', 11);
INSERT INTO `lend_record` VALUES (1290493954, 21, '20211880118', '伊索寓言', '2022-12-23 19:35:39', '2022-12-23 19:35:44', '1', 11);
INSERT INTO `lend_record` VALUES (1412087809, 21, '20211880128', '和另一个自己谈谈心', '2022-12-26 17:31:04', '2023-01-01 13:38:26', '1', 12);
INSERT INTO `lend_record` VALUES (1428905986, 21, '20211880114', '计算机操作系统', '2022-12-23 19:31:30', '2022-12-23 19:32:08', '1', 3);
INSERT INTO `lend_record` VALUES (1659592705, 21, '20211880119', '五万个为什么', '2022-12-23 19:30:59', '2022-12-23 19:31:18', '1', 17);
INSERT INTO `lend_record` VALUES (1735090177, 21, '20211880118', '和另一个自己谈谈心', '2022-12-23 19:33:57', '2022-12-23 19:34:03', '1', 2);
INSERT INTO `lend_record` VALUES (1756061697, 21, '20211880118', '和另一个自己谈谈心', '2022-12-23 19:31:33', '2022-12-23 19:32:13', '1', 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话号码',
  `sex` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `role` int NOT NULL COMMENT '角色、1：管理员 2：普通用户',
  `alow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否允许借阅',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'njh', '123', '聂家辉', '17326788058', '男', '温州大学南校区A12-417室', 1, '1');
INSERT INTO `user` VALUES (21, 'db', '123', '借阅者', '17326788058', '男', '温州大学计算机与人工智能学院', 2, '1');
INSERT INTO `user` VALUES (28, '123', '123', 'njh', '19802045416', '男', 'xxx', 2, '1');

SET FOREIGN_KEY_CHECKS = 1;
