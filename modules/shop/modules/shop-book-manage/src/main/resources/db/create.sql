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
  nav int default comment '是否显示在导航栏 0--否，1--是',
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

INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('1', '公共模块', 'common', '', '0', '1', 'home', '1', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('2', '字典列表', 'dict', '', '1', '1', '', '10', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('3', '广告列表', 'advert', '', '1', '1', '', '20', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('4', '问答列表', 'qa', '', '1', '1', '', '30', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('5', '权限管理', 'rdbc', '', '0', '1', 'setting', '2', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('6', '账户管理', 'user', 'user:list,user:query', '5', '1', '', '10', '1', '2018-11-14 16:53:27', '2018-11-14 16:53:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('7', '新增', '/user/add', 'user:add', '6', '2', '', '10', '0', '2018-11-14 16:54:04', '2018-11-14 16:54:06');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('8', '修改', '/user/update', 'user:update', '6', '2', '', '20', '0', '2018-11-14 16:54:27', '2018-11-14 16:54:29');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('9', '删除', '/user/deletes', 'user:delete', '6', '2', '', '30', '0', '2018-11-14 16:54:48', '2018-11-14 16:54:52');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('10', '启用', '/user/enable', 'user:enable', '6', '2', '', '40', '0', '2018-11-19 08:53:02', '2018-11-19 08:53:04');
INSERT INTO `menu` (`id`, `name`, `url`, `perms`, `parent_id`, `type`, `icon`, `sort`, `nav`, `create_time`, `modified_time`) VALUES ('11', '冻结', '/user/disable', 'user:disable', '6', '2', '', '50', '0', '2018-11-19 08:53:25', '2018-11-19 08:53:27');

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

