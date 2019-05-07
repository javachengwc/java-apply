create table user (
  id bigint AUTO_INCREMENT,
  name varchar(64) default '' comment '用户名',
  mobile varchar(11) default '' comment '手机号',
  status int default 0 comment '状态 0--正常 1--冻结',
  create_time datetime comment '记录创建时间',
  modified_time datetime comment '记录更新时间',
  primary key (id),
  unique key udx_mobile(mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='用户表';

create table user_role (
  user_id bigint not null comment '用户ID',
  role_id bigint not null comment '角色ID',
  primary key (user_id,role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='用户角色表';

create table role(
  id bigint AUTO_INCREMENT,
  name varchar(64) default '' comment '角色名称',
  code varchar(64) default '' comment '角色code',
  note varchar(255) default '' comment '备注',
  create_time datetime comment '记录创建时间',
  modified_time datetime comment '记录更新时间',
  primary key (id),
  unique key udx_name(name),
  unique key udx_code(code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='角色表';

create table menu (
  id bigint AUTO_INCREMENT,
  name varchar(128) default '' comment '名称',
  url varchar(128) default '' comment '相对路径',
  perms varchar(512) default '' comment '权限，ep: user:add,user:list',
  parent_id bigint comment '父菜单ID',
  type int default 0 comment '类型 0--目录，1--菜单，2--按钮',
  icon varchar(128) default '' comment '图标',
  sort int  default 0 comment '排序',
  nav int default 0 comment '是否显示在导航栏 0--否，1--是',
  create_time datetime comment '记录创建时间',
  modified_time datetime comment '记录更新时间',
  primary key (id),
  key idx_name(name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='菜单表';

create table role_menu (
  role_id bigint not null comment '角色Id',
  menu_id bigint not null comment '菜单ID',
  primary key (role_id,menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='角色菜单表';

INSERT INTO `user` (`id`, `name`, `mobile`, `status`, `create_time`, `modified_time`) VALUES ('1', 'admin', '11111111111', '0', '2018-11-30 15:16:30', '2018-11-30 15:16:32');

INSERT INTO `role` (`id`, `name`, `code`, `note`, `create_time`, `modified_time`) VALUES ('1', '管理员', 'admin', '管理员', '2018-11-14 16:04:51', '2018-11-14 16:04:53');
INSERT INTO `role` (`id`, `name`, `code`, `note`, `create_time`, `modified_time`) VALUES ('2', '运营员', 'practicer', '运营人员', '2018-11-14 16:04:51', '2018-11-14 16:04:53');


INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('1', '门店管理', 'storefront', '', '0', '0', 'home', '1', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('2', '门店列表', 'store', '', '1', '1', '', '10', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('3', '品牌管理', 'brand', '', '1', '1', '', '20', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('4', '行业管理', 'industry', '', '1', '1', '', '30', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('5', '权限管理', 'rdbc', '', '0', '0', 'setting', '2', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('6', '账户管理', 'user', 'user:list,user:query', '5', '1', '', '10', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('7', '新增', '/user/add', 'user:add', '6', '2', '', '10', '0', '2018-11-14 16:54:04', '2018-11-14 16:54:06');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('8', '修改', '/user/update', 'user:update', '6', '2', '', '20', '0', '2018-11-14 16:54:27', '2018-11-14 16:54:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('9', '删除', '/user/deletes', 'user:delete', '6', '2', '', '30', '0', '2018-11-14 16:54:48', '2018-11-14 16:54:52');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('10', '启用', '/user/enable', 'user:enable', '6', '2', '', '40', '0', '2018-11-19 08:53:02', '2018-11-19 08:53:04');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('11', '冻结', '/user/disable', 'user:disable', '6', '2', '', '50', '0', '2018-11-19 08:53:25', '2018-11-19 08:53:27');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('12', '角色管理', 'role', '', '5', '1', '', '10', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('13', '新增角色', '/role/add', 'role:add', '12', '2', '', '10', '0', '2018-11-14 16:54:04', '2018-11-14 16:54:06');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('14', '修改角色', '/role/update', 'role:update', '12', '2', '', '20', '0', '2018-11-14 16:54:27', '2018-11-14 16:54:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('15', '删除角色', '/role/batchDel', 'role:delete', '12', '2', '', '30', '0', '2018-11-14 16:54:48', '2018-11-14 16:54:52');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('16', '菜单管理', 'menu', '', '5', '1', '', '10', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('17', '新增菜单', '/menu/add', 'menu:add', '16', '2', '', '10', '0', '2018-11-14 16:54:04', '2018-11-14 16:54:06');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('18', '修改菜单', '/menu/update', 'menu:update', '16', '2', '', '20', '0', '2018-11-14 16:54:27', '2018-11-14 16:54:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('19', '删除菜单', '/menu/batchDel', 'menu:delete', '16', '2', '', '30', '0', '2018-11-14 16:54:48', '2018-11-14 16:54:52');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('1', '1');

INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '1');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '2');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '3');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '4');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '5');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '6');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '7');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '8');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '9');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '10');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '11');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '12');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '13');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '14');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '15');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '16');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '17');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '18');
INSERT INTO `role_menu` (`role_id`, `menu_id`) VALUES ('1', '19');

CREATE TABLE `industry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) DEFAULT '' COMMENT '行业编码',
  `name` varchar(64) DEFAULT '' COMMENT '行业名字',
  `level` int(11) DEFAULT '1' COMMENT '层级,默认1',
  `parent_code` varchar(64) DEFAULT '' COMMENT '上一级行业编码',
  `parent_name` varchar(64) DEFAULT '' COMMENT '上一级行业名称',
  `full_path` varchar(256) DEFAULT '' COMMENT '全code路径,|号分割',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_code` (`code`),
  KEY `idx_parent` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行业表';

CREATE TABLE `brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT '' COMMENT '品牌名字',
  `first_idstry_code` varchar(64) DEFAULT '' COMMENT '大行业code',
  `first_idstry_name` varchar(64) DEFAULT '' COMMENT '大行业名称',
  `direct_idstry_code` varchar(64) DEFAULT '' COMMENT '细行业code',
  `direct_idstry_name` varchar(64) DEFAULT '' COMMENT '细行业名称',
  `is_idstry_mark` int(11) DEFAULT '0' COMMENT '是否行业标杆品牌，0--否，1--是',
  `company_id` bigint(20) DEFAULT '0' COMMENT '所属公司',
  `is_jingying` int(11) DEFAULT '1' COMMENT '是否在经营,0--否 1--是',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='品牌表';

CREATE TABLE `company` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT '' COMMENT '公司名称',
  `info` varchar(256) DEFAULT '' COMMENT '简介',
  `uscc` varchar(32) DEFAULT '' COMMENT '统一社会信用代码,18位',
  `is_jingying` int(11) DEFAULT '1' COMMENT '是否经营 0--否 1--是',
  `start_time` date DEFAULT NULL COMMENT '成立时间',
  `website` varchar(64) DEFAULT '' COMMENT '官网',
  `province_code` varchar(32) DEFAULT '' COMMENT '省code',
  `province_name` varchar(32) DEFAULT '' COMMENT '省名',
  `city_code` varchar(32) DEFAULT '' COMMENT '市code',
  `city_name` varchar(32) DEFAULT '' COMMENT '市名',
  `area_code` varchar(32) DEFAULT '' COMMENT '区县code',
  `area_name` varchar(32) DEFAULT '' COMMENT '区县名',
  `detail_address` varchar(128) DEFAULT '' COMMENT '详细地址',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_name` (`name`),
  KEY `idx_area` (`province_code`,`city_code`,`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司表';

CREATE TABLE `project_jiameng` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT '' COMMENT '加盟项目',
  `brand_name` varchar(64) DEFAULT '' COMMENT '加盟品牌',
  `company_id` bigint(20) DEFAULT '0' COMMENT '加盟商id',
  `company_name` varchar(128) DEFAULT '' COMMENT '加盟商名称',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_brand` (`brand_name`),
  KEY `idx_company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='加盟项目表';

CREATE TABLE `store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT '' COMMENT '门店名称',
  `info` varchar(128) DEFAULT '' COMMENT '介绍',
  `is_business_mark` int(11) DEFAULT '0' COMMENT '是否商圈标杆店铺，0--否，1--是',
  `first_idstry_code` varchar(64) DEFAULT '' COMMENT '大行业code',
  `first_idstry_name` varchar(64) DEFAULT '' COMMENT '大行业名称',
  `direct_idstry_code` varchar(64) DEFAULT '' COMMENT '细行业code',
  `direct_idstry_name` varchar(64) DEFAULT '' COMMENT '细行业名称',
  `is_jingying` int(11) DEFAULT '1' COMMENT '是否经营',
  `is_jiameng` int(11) DEFAULT '0' COMMENT '是否加盟',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(64) DEFAULT '' COMMENT '品牌名称',
  `start_time` date DEFAULT NULL COMMENT '成立时间',
  `end_time` date DEFAULT NULL COMMENT '结束时间',
  `province_code` varchar(32) DEFAULT '' COMMENT '省code',
  `province_name` varchar(32) DEFAULT '' COMMENT '省名',
  `city_code` varchar(32) DEFAULT '' COMMENT '市code',
  `city_name` varchar(32) DEFAULT '' COMMENT '市名',
  `area_code` varchar(32) DEFAULT '' COMMENT '区县code',
  `area_name` varchar(32) DEFAULT '' COMMENT '区县名',
  `position_name` varchar(32) DEFAULT '' COMMENT '小区/楼盘/商场/大厦名称',
  `position_type` int(11) DEFAULT '0' COMMENT '标识位置类型 0--小区,1--楼盘,2--商场,3--大厦,4--学校,5--步行街',
  `street_name` varchar(32) DEFAULT '' COMMENT '街道名称',
  `detail_address` varchar(32) DEFAULT '' COMMENT '详细地址',
  `create_time` datetime DEFAULT NULL COMMENT '记录创建时间',
  `modified_time` datetime DEFAULT NULL COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_first_idstry` (`first_idstry_code`),
  KEY `idx_direct_idstry` (`direct_idstry_code`),
  KEY `idx_position` (`position_name`),
  KEY `idx_street` (`street_name`),
  KEY `idx_area` (`province_code`,`city_code`,`area_code`),
  KEY `idx_brand` (`brand_id`,`brand_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门店表';


