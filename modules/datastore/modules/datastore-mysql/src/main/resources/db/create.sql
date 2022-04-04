CREATE TABLE `sys_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `parent_id` int(11) DEFAULT '0' COMMENT '父菜单id',
  `is_show` int(11) DEFAULT '1' COMMENT '是否显示, 0--否,1--是, 默认值:1',
  `tag` varchar(128) DEFAULT '' COMMENT '标识',
  `path` varchar(256) DEFAULT '' COMMENT '路径',
  `icon` varchar(64) DEFAULT '' COMMENT '图标',
  `type` int(11) DEFAULT '0' COMMENT '类型(0--目录、1--菜单、2--按钮)',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_name` (`name`),
  KEY `idx_parent` (`parent_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

insert into sys_resource values (1,'用户管理',0,1,'','','',0,1,now(),now());
insert into sys_resource values (2,'角色管理',0,1,'','','',0,2,now(),now());
insert into sys_resource values (3,'菜单管理',0,1,'','','',0,3,now(),now());
insert into sys_resource values (4,'部门管理',0,1,'','','',0,4,now(),now());
insert into sys_resource values (5,'业务组管理',0,1,'','','',0,5,now(),now());