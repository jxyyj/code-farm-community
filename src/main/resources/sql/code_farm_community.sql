/*
 Navicat Premium Data Transfer

 Source Server         : mysql-8.0
 Source Server Type    : MySQL
 Source Server Version : 80012 (8.0.12)
 Source Host           : localhost:3306
 Source Schema         : code_farm_community

 Target Server Type    : MySQL
 Target Server Version : 80012 (8.0.12)
 File Encoding         : 65001

 Date: 20/03/2025 11:52:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

create database if not exists code_farm_community character set utf8mb4 collate utf8mb4_unicode_ci;
use code_farm_community;


-- ----------------------------
-- Table structure for sys_auth_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_permission`;
CREATE TABLE `sys_auth_permission`
(
    `id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '权限名称',
    `parent_id`      bigint(20)                                                    NULL DEFAULT NULL COMMENT '父菜单ID',
    `order_num`      int(11)                                                       NULL DEFAULT NULL COMMENT '显示顺序',
    `type`           tinyint(4)                                                    NULL DEFAULT NULL COMMENT '权限类型 0菜单 1操作',
    `menu_url`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单路由',
    `component`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
    `status`         tinyint(2)                                                    NULL DEFAULT NULL COMMENT '状态 0启用 1禁用',
    `show`           tinyint(2)                                                    NULL DEFAULT NULL COMMENT '展示状态 0展示 1隐藏',
    `icon`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图标',
    `permission_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '权限唯一标识',
    `created_by`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `created_time`   datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `update_time`    datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`     int(11)                                                       NULL DEFAULT 0 COMMENT '是否被删除 0为删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '权限表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_auth_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_role`;
CREATE TABLE `sys_auth_role`
(
    `id`           bigint(20)                                                   NOT NULL AUTO_INCREMENT,
    `role_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色名称',
    `role_key`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色唯一标识',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `created_time` datetime                                                     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`   int(11)                                                      NULL DEFAULT 0 COMMENT '是否被删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_auth_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_role_permission`;
CREATE TABLE `sys_auth_role_permission`
(
    `id`            bigint(20)                                                   NOT NULL AUTO_INCREMENT,
    `role_id`       bigint(20)                                                   NULL DEFAULT NULL COMMENT '角色id',
    `permission_id` bigint(20)                                                   NULL DEFAULT NULL COMMENT '权限id',
    `created_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `created_time`  datetime                                                     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`    int(11)                                                      NULL DEFAULT 0 COMMENT '是否被删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限关联表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_user`;
CREATE TABLE `sys_auth_user`
(
    `id`           bigint(20)                                                    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '用户名称/账号',
    `nick_name`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '昵称',
    `email`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '邮箱',
    `phone`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '手机号',
    `password`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '密码',
    `sex`          tinyint(2)                                                    NULL DEFAULT NULL COMMENT '性别',
    `avatar`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
    `status`       tinyint(2)                                                    NULL DEFAULT NULL COMMENT '状态 0启用 1禁用',
    `introduce`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '个人介绍',
    `ext_json`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '特殊字段',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '创建人',
    `created_time` datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NULL DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime                                                      NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`   int(11)                                                       NULL DEFAULT 0 COMMENT '是否被删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_auth_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_user_role`;
CREATE TABLE `sys_auth_user_role`
(
    `id`           bigint(20)                                                   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint(20)                                                   NULL DEFAULT NULL COMMENT '用户id',
    `role_id`      bigint(20)                                                   NULL DEFAULT NULL COMMENT '角色id',
    `created_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
    `created_time` datetime                                                     NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime                                                     NULL DEFAULT NULL COMMENT '更新时间',
    `is_deleted`   int(11)                                                      NULL DEFAULT 0 COMMENT '是否被删除 0未删除 1已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- 基础角色数据
-- ----------------------------
INSERT INTO `sys_auth_role` (`role_name`, `role_key`, `created_by`, `created_time`, `is_deleted`)
VALUES
('管理员', 'ADMIN', 'system', NOW(), 0),
('普通用户', 'USER', 'system', NOW(), 0);

-- ----------------------------
-- 基础权限数据
-- ----------------------------
INSERT INTO `sys_auth_permission` (`name`, `parent_id`, `order_num`, `type`, `menu_url`, `component`, `status`, `show`, `icon`, `permission_key`, `created_by`, `created_time`, `is_deleted`)
VALUES
-- 系统管理
('系统管理', 0, 1, 0, '/system', 'system/index', 0, 0, 'system', 'system:manage', 'system', NOW(), 0),
-- 用户管理
('用户管理', 1, 1, 0, '/system/user', 'system/user/index', 0, 0, 'user', 'system:user:manage', 'system', NOW(), 0),
('用户列表', 2, 1, 1, '', '', 0, 0, '', 'system:user:list', 'system', NOW(), 0),
('用户添加', 2, 2, 1, '', '', 0, 0, '', 'system:user:add', 'system', NOW(), 0),
('用户编辑', 2, 3, 1, '', '', 0, 0, '', 'system:user:edit', 'system', NOW(), 0),
('用户删除', 2, 4, 1, '', '', 0, 0, '', 'system:user:delete', 'system', NOW(), 0),
-- 角色管理
('角色管理', 1, 2, 0, '/system/role', 'system/role/index', 0, 0, 'role', 'system:role:manage', 'system', NOW(), 0),
('角色列表', 7, 1, 1, '', '', 0, 0, '', 'system:role:list', 'system', NOW(), 0),
('角色添加', 7, 2, 1, '', '', 0, 0, '', 'system:role:add', 'system', NOW(), 0),
('角色编辑', 7, 3, 1, '', '', 0, 0, '', 'system:role:edit', 'system', NOW(), 0),
('角色删除', 7, 4, 1, '', '', 0, 0, '', 'system:role:delete', 'system', NOW(), 0),
-- 权限管理
('权限管理', 1, 3, 0, '/system/permission', 'system/permission/index', 0, 0, 'permission', 'system:permission:manage', 'system', NOW(), 0),
('权限列表', 12, 1, 1, '', '', 0, 0, '', 'system:permission:list', 'system', NOW(), 0),
('权限添加', 12, 2, 1, '', '', 0, 0, '', 'system:permission:add', 'system', NOW(), 0),
('权限编辑', 12, 3, 1, '', '', 0, 0, '', 'system:permission:edit', 'system', NOW(), 0),
('权限删除', 12, 4, 1, '', '', 0, 0, '', 'system:permission:delete', 'system', NOW(), 0);

-- ----------------------------
-- 角色权限关联数据
-- ----------------------------
-- 管理员角色拥有所有权限
INSERT INTO `sys_auth_role_permission` (`role_id`, `permission_id`, `created_by`, `created_time`, `is_deleted`)
SELECT 1, id, 'system', NOW(), 0 FROM `sys_auth_permission`;

-- 普通用户角色拥有基础权限
INSERT INTO `sys_auth_role_permission` (`role_id`, `permission_id`, `created_by`, `created_time`, `is_deleted`)
VALUES
(2, 1, 'system', NOW(), 0), -- 系统管理
(2, 2, 'system', NOW(), 0), -- 用户管理
(2, 3, 'system', NOW(), 0); -- 用户列表

-- ----------------------------
-- 基础用户数据
-- ----------------------------
-- 管理员用户 (密码: admin123)
INSERT INTO `sys_auth_user` (`user_name`, `nick_name`, `email`, `phone`, `password`, `status`, `created_by`, `created_time`, `is_deleted`)
VALUES
('admin', '管理员', 'admin@codefarm.com', '13800138000', '$2a$10$E6zqOQfYI7mQvX8t6T7I0e6Y9u9u9u9u9u9u9u9u9u9u9u9u9u9', 0, 'system', NOW(), 0);

-- 普通用户 (密码: user123)
INSERT INTO `sys_auth_user` (`user_name`, `nick_name`, `email`, `phone`, `password`, `status`, `created_by`, `created_time`, `is_deleted`)
VALUES
('user', '普通用户', 'user@codefarm.com', '13800138001', '$2a$10$E6zqOQfYI7mQvX8t6T7I0e6Y9u9u9u9u9u9u9u9u9u9u9u9u9', 0, 'system', NOW(), 0);

-- ----------------------------
-- 用户角色关联数据
-- ----------------------------
-- 管理员用户关联管理员角色
INSERT INTO `sys_auth_user_role` (`user_id`, `role_id`, `created_by`, `created_time`, `is_deleted`)
VALUES
(1, 1, 'system', NOW(), 0);

-- 普通用户关联普通用户角色
INSERT INTO `sys_auth_user_role` (`user_id`, `role_id`, `created_by`, `created_time`, `is_deleted`)
VALUES
(2, 2, 'system', NOW(), 0);

SET FOREIGN_KEY_CHECKS = 1;