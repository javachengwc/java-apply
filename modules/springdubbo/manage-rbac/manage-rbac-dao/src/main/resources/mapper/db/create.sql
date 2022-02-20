CREATE TABLE `t_system` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `url` varchar(128) DEFAULT '' COMMENT '访问地址',
  `state` tinyint(2) DEFAULT '0' COMMENT '状态(0--上线 1--下线)',
  `remark` varchar(256) DEFAULT '' COMMENT '备注',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人名称',
  `disable` tinyint(1) DEFAULT '0' COMMENT '逻辑删除（1不可用，0可用）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统表';

CREATE TABLE `t_organization` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '机构id',
  `name` varchar(64) DEFAULT '' COMMENT '机构名称',
  `level` int(11) DEFAULT NULL COMMENT '机构层级',
  `state` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `parent_id` int(11) DEFAULT NULL COMMENT '父机构id',
  `parent_name` varchar(64) DEFAULT '' COMMENT '父机构名称',
  `path` varchar(128) DEFAULT '' COMMENT '机构全路径path',
  `path_name` varchar(512) DEFAULT '' COMMENT '机构全路径名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人名称',
  PRIMARY KEY (`id`),
  KEY `idx_parent` (`parent_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '账号id',
  `uid` bigint(20) DEFAULT NULL COMMENT '平台账号id',
  `account` varchar(64) DEFAULT '' COMMENT '平台账号',
  `mobile` varchar(11) DEFAULT '' COMMENT '手机号',
  `name` varchar(64) DEFAULT '' COMMENT '姓名',
  `nickname` varchar(64) DEFAULT '' COMMENT '网名',
  `passwd` varchar(64) DEFAULT '' COMMENT '密码',
  `state` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `superior_id` int(11) DEFAULT NULL COMMENT '上级id',
  `superior_name` varchar(64) DEFAULT '' COMMENT '上级姓名',
  `superior_nickname` varchar(64) DEFAULT '' COMMENT '上级网名',
  `org_id` int(11) DEFAULT NULL COMMENT '机构id',
  `org_name` varchar(64) DEFAULT '' COMMENT '机构名称',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人网名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人网名',
  `disable` tinyint(1) DEFAULT '0' COMMENT '逻辑删除（1删除，0不删除）',
  PRIMARY KEY (`id`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账号表';

CREATE TABLE `t_crowd` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户组id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `state` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组表';

CREATE TABLE `t_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '岗位id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `code` varchar(64) DEFAULT '' COMMENT '编码',
  `state` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `code` varchar(64) DEFAULT '' COMMENT '编码',
  `state` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `type` int(11) DEFAULT '1' COMMENT '系统角色类型(1--业务角色 2--管理角色)',
  `sys_role` int(2) DEFAULT '0' COMMENT '是否系统角色(0--否 1--是)',
  `remark` varchar(128) DEFAULT '' COMMENT '备注',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `system_id` int(11) DEFAULT NULL COMMENT '系统id',
  `parent_id` int(11) DEFAULT '0' COMMENT '父菜单id',
  `state` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `level` int(11) DEFAULT '1' COMMENT '层级',
  `tag` varchar(128) DEFAULT '' COMMENT '菜单或按钮标识',
  `perms` varchar(256) DEFAULT '' COMMENT '权限',
  `icon` varchar(64) DEFAULT '' COMMENT '图标',
  `type` int(11) DEFAULT '0' COMMENT '类型(0--目录、1--菜单、2--按钮)',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_nickname` varchar(64) DEFAULT '' COMMENT '操作人名称',
  PRIMARY KEY (`id`),
  KEY `idx_system` (`system_id`),
  KEY `idx_parent` (`parent_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

CREATE TABLE `t_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `menu_id` int(11) DEFAULT NULL COMMENT '菜单id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_menu` (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单表';

CREATE TABLE `t_org_crowd` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `org_id` int(11) DEFAULT NULL COMMENT '机构id',
  `crowd_id` int(11) DEFAULT NULL COMMENT '用户组id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_org_crowd` (`org_id`,`crowd_id`),
  KEY `idx_crowd` (`crowd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构下用户组表';

CREATE TABLE `t_org_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `org_id` int(11) DEFAULT NULL COMMENT '机构id',
  `post_id` int(11) DEFAULT NULL COMMENT '岗位id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_org_post` (`org_id`,`post_id`),
  KEY `idx_post` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构下岗位表';

CREATE TABLE `t_crowd_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `crowd_id` int(11) DEFAULT NULL COMMENT '用户组id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_crowd_role` (`crowd_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户组角色表';

CREATE TABLE `t_post_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `post_id` int(11) DEFAULT NULL COMMENT '岗位id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_post_role` (`post_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位角色表';

CREATE TABLE `t_user_crowd` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_nickname` varchar(64) DEFAULT '' COMMENT '用户网名',
  `crowd_id` int(11) DEFAULT NULL COMMENT '用户组id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_crowd` (`user_id`,`crowd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户所属用户组表';

CREATE TABLE `t_user_post` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关联id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_nickname` varchar(64) DEFAULT '' COMMENT '用户网名',
  `post_id` int(11) DEFAULT NULL COMMENT '岗位id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_post` (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户岗位表';

CREATE TABLE `t_operate_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(64) DEFAULT '' COMMENT '动作',
  `system_id` int(11) DEFAULT NULL COMMENT '系统id',
  `system_name` varchar(64) DEFAULT '' COMMENT '系统名称',
  `module` varchar(64) DEFAULT '' COMMENT '功能模块',
  `detail` varchar(256) DEFAULT '' COMMENT '详情',
  `creater_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creater_nickname` varchar(64) DEFAULT '' COMMENT '创建人网名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_system_module` (`system_name`,`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

INSERT INTO `t_crowd` VALUES ('1', '管理员组', '0', '1', '21', '2021-12-03 19:27:39', '2021-12-10 11:07:23', '1', '21');
INSERT INTO `t_crowd_role` VALUES ('1', '1', '1', '2021-12-07 18:46:27', '2021-12-07 18:46:29');
INSERT INTO `t_menu` VALUES ('1', '角色管理', '1', '0', '0', '1', 'R', '', '', '1', '0', '1', '21', '2021-12-04 11:09:53', '2021-12-04 11:09:53', '1', '21');
INSERT INTO `t_menu` VALUES ('2', '权限管理', '1', '1', '0', '2', 'A', '', '', '2', '0', '1', '21', '2021-12-06 11:18:12', '2021-12-10 19:46:52', '1', '21');
INSERT INTO `t_menu` VALUES ('3', '禁用', '1', '1', '0', '2', 'D', '', '', '2', '0', '1', '21', '2021-12-06 11:18:24', '2021-12-10 14:14:46', '1', '21');
INSERT INTO `t_menu` VALUES ('4', '新增', '1', '1', '0', '2', 'A', '', '', '2', '0', '1', '21', '2021-12-06 11:18:39', '2021-12-06 11:18:39', '1', '21');
INSERT INTO `t_menu` VALUES ('5', '机构管理', '1', '0', '0', '1', 'O', '', '', '1', '0', '1', '21', '2021-12-06 11:18:56', '2021-12-06 11:18:56', '1', '21');
INSERT INTO `t_menu` VALUES ('6', '用户组管理', '1', '0', '0', '1', 'C', '', '', '1', '0', '1', '21', '2021-12-06 11:19:20', '2021-12-06 11:19:20', '1', '21');
INSERT INTO `t_menu` VALUES ('7', '岗位管理', '1', '0', '0', '1', 'P', '', '', '1', '0', '1', '21', '2021-12-06 11:19:29', '2021-12-07 18:51:06', '1', '21');
INSERT INTO `t_menu` VALUES ('8', '人员管理', '1', '0', '0', '1', 'U', '', '', '1', '0', '1', '21', '2021-12-06 11:19:40', '2021-12-07 18:51:09', '1', '21');
INSERT INTO `t_organization` VALUES ('1', 'XX公司', '1', '0', '0', '', '/1', '/XX公司', '0', '1', '21', '2021-12-03 17:25:53', '2021-12-09 15:03:00', '1', '21');
INSERT INTO `t_org_crowd` VALUES ('1', '1', '1', '2021-12-09 15:34:46', '2021-12-09 15:34:46');
INSERT INTO `t_org_post` VALUES ('1', '1', '1', '2021-01-02 17:53:28', '2021-01-02 17:53:28');
INSERT INTO `t_post` VALUES ('1', 'CEO', '001', '0', '1', '21', '2021-01-02 17:53:28', '2021-01-11 09:23:24', '1', '21');
INSERT INTO `t_post_role` VALUES ('1', '1', '1', '2021-01-02 17:53:28', '2021-01-02 17:53:28');
INSERT INTO `t_role` VALUES ('1', '管理员角色', 'admin', '0', '2', '1', '超级管理员', '1', '21', '2021-12-03 19:37:23', '2021-12-03 19:42:55', '1', '21');
INSERT INTO `t_role_menu` VALUES ('1', '1', '1', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('2', '1', '2', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('3', '1', '3', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('4', '1', '4', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('5', '1', '5', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('6', '1', '6', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('7', '1', '7', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_role_menu` VALUES ('8', '1', '8', '2021-01-16 16:36:08', '2021-01-16 16:36:08');
INSERT INTO `t_system` VALUES ('1', '权限系统', '', '0', '权限系统', '0', '1', '21', '2021-12-03 17:16:16', '2021-12-03 17:16:18', '1', '21', '0');
INSERT INTO `t_user` VALUES ('1', '1', 'n1', '11111111111', '21', '21', null, '0', '1', '21', '21', '1', 'XX公司', '1', '21', '2021-12-06 21:05:00', '2021-01-17 17:56:09', '1', '21', '0');
INSERT INTO `t_user_crowd` VALUES ('1', '1', '21', '1', '2021-01-02 14:16:39', '2021-01-02 14:16:41');
INSERT INTO `t_user_post` VALUES ('1', '1', '21', '1', '2021-01-11 09:24:36', '2021-01-11 09:24:36');




