create table t_resource_system (
  id bigint(20) AUTO_INCREMENT,
  name varchar(50) default '' comment '名称',
  note varchar(200) default '' comment '备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口系统表';

create table t_resource_category (
  id bigint AUTO_INCREMENT,
  name varchar(50) default '' comment '名称',
  parent_id bigint default 0 comment '父类目',
  sys_id bigint comment '系统id',
  note varchar(50) default '' comment '备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  primary key (`id`),
  key idx_parent(parent_id,id),
  key idx_system(sys_id,id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口类目表';

create table t_access_resource (
  id bigint(20) AUTO_INCREMENT,
  name varchar(50) default '' comment '接口名称',
  path varchar(300) default '' comment '接口路径',
  note varchar(200) default '' comment '备注',
  type int default '1' comment '接口类型 1--http,2--dubbo,3--task,4--方法,5--代码片段',
  sys_id bigint comment '系统id',
  cate_id bigint comment '类目id',
  http_method varchar(20) default 'GET' comment '接口httpMethod,GET,POST,PUT,DELETE,HEAD,PATCH',
  content_type varchar(100) default '' comment '请求数据类型Content-Type',
  resource_link varchar(200) default '' comment '接口链接',
  req_demo varchar(1000) default '' comment '请求参数例子',
  resp_demo varchar(1000) default '' comment '响应结果例子',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访问接口表';

create table t_resource_param (
  id bigint(20) AUTO_INCREMENT,
  resource_id bigint comment '接口id',
  name varchar(100) default '' comment '参数名称',
  type varchar(50) default '' comment '参数类型',
  is_mast int(2) default 0 comment '是否必填 0--否,1--是',
  default_value varchar(1000) default '' comment '默认值',
  note varchar(200) default '' comment '参数备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_resource(resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口参数表';

create table t_resource_header (
  id bigint(20) AUTO_INCREMENT,
  resource_id bigint comment '接口id',
  name varchar(50) default '' comment 'header名称',
  type int default 0 comment '类型 0--header 1--cookie',
  default_value varchar(200) default '' comment '默认值',
  note varchar(200) default '' comment 'header备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_resource(resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口请求头表';

create table t_resource_invoke (
  id bigint(20) AUTO_INCREMENT,
  resource_id bigint comment '接口id',
  resource_name varchar(50) default '' comment '接口名称',
  resource_link varchar(200) default '' comment '接口链接',
  http_method varchar(20) default 'GET' comment '接口httpMethod,GET,POST,PUT,DELETE,HEAD,PATCH',
  content_type varchar(100) default '' comment '请求数据类型Content-Type',
  req_header varchar(500) default '' comment '请求头',
  req_data  varchar(1000) default '' comment '请求数据',
  resp_code int default 0 comment '响应code',
  resp_data varchar(2000) default '' comment '响应数据',
  is_success int(2) default 1 comment '是否成功,0--否,1--是',
  error_message varchar(200) default '' comment '错误信息',
  invoke_time datetime comment '调用时间',
  return_time` datetime comment '返回时间',
  cost bigint default 0 comment '耗时,毫秒',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_resource(resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口调用表';

create table t_access_log (
  id bigint(20) AUTO_INCREMENT,
  sys_id bigint comment '系统id',
  resource_id bigint comment '接口id',
  resource_path varchar(300) default '' comment '接口路径',
  invoke_time datetime comment '调用时间',
  return_time datetime comment '返回时间',
  cost bigint default 0 comment '耗时,毫秒',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_sys_res(sys_id,resource_id),
  key idx_invoke_res(invoke_time,resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口访问日志表';

CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `name` varchar(64) DEFAULT '' COMMENT '名称',
  `system_id` int(11) DEFAULT NULL COMMENT '系统id',
  `parent_id` int(11) DEFAULT '0' COMMENT '父菜单id',
  `status` int(11) DEFAULT '0' COMMENT '状态，0:正常、1:禁用，默认值:0',
  `level` int(11) DEFAULT '1' COMMENT '层级',
  `icon` varchar(64) DEFAULT '' COMMENT '图标',
  `visible` int(2) DEFAULT '1' COMMENT '是否显示(1--显示 0--隐藏)',
  `type` int(2) DEFAULT '0' COMMENT '类型(0--目录、1--菜单、2--按钮)',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT '' COMMENT '组件路径',
  `query` varchar(255) DEFAULT '' COMMENT '路由参数',
  `out_link` int(2) DEFAULT '0' COMMENT '是否为外链(0--否,1--是)',
  `perms` varchar(100) DEFAULT '' COMMENT '权限标识',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent` (`parent_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1, '系统管理', NULL, 0, 0, 1, 'system', 0, 0, 'system', NULL, '', 0, '', 1, '2022-04-28 03:06:50', '2022-04-28 03:06:50');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (2, '系统监控', NULL, 0, 0, 1, 'monitor', 0, 0, 'monitor', NULL, '', 0, '', 2, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (3, '系统工具', NULL, 0, 0, 1, 'tool', 0, 0, 'tool', NULL, '', 0, '', 3, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (100, '用户管理', NULL, 1, 0, 2, 'user', 1, 1, 'user', 'system/user/index', '', 0, 'system:user:list', 1, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (101, '角色管理', NULL, 1, 0, 2, 'peoples', 1, 1, 'role', 'system/role/index', '', 0, 'system:role:list', 2, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (102, '菜单管理', NULL, 1, 0, 2, 'tree-table', 1, 1, 'menu', 'system/menu/index', '', 0, 'system:menu:list', 3, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (103, '部门管理', NULL, 1, 0, 2, 'tree', 1, 1, 'dept', 'system/dept/index', '', 0, 'system:dept:list', 4, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (104, '岗位管理', NULL, 1, 0, 2, 'post', 1, 1, 'post', 'system/post/index', '', 0, 'system:post:list', 5, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (105, '字典管理', NULL, 1, 0, 2, 'dict', 1, 1, 'dict', 'system/dict/index', '', 0, 'system:dict:list', 6, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (106, '参数设置', NULL, 1, 0, 2, 'edit', 1, 1, 'config', 'system/config/index', '', 0, 'system:config:list', 7, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (107, '通知公告', NULL, 1, 0, 2, 'message', 1, 1, 'notice', 'system/notice/index', '', 0, 'system:notice:list', 8, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (108, '日志管理', NULL, 1, 0, 1, 'log', 1, 0, 'log', '', '', 0, '', 9, '2022-04-28 03:14:09', '2022-04-28 03:14:09');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (109, '在线用户', NULL, 2, 0, 2, 'online', 1, 1, 'online', 'monitor/online/index', '', 0, 'monitor:online:list', 1, '2022-04-28 03:14:09', '2022-04-28 03:14:09');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (110, '定时任务', NULL, 2, 0, 2, 'job', 1, 1, 'job', 'monitor/job/index', '', 0, 'monitor:job:list', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (111, '数据监控', NULL, 2, 0, 2, 'druid', 1, 1, 'druid', 'monitor/druid/index', '', 0, 'monitor:druid:list', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (112, '服务监控', NULL, 2, 0, 2, 'server', 1, 1, 'server', 'monitor/server/index', '', 0, 'monitor:server:list', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (113, '缓存监控', NULL, 2, 0, 2, 'redis', 1, 1, 'cache', 'monitor/cache/index', '', 0, 'monitor:cache:list', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (114, '表单构建', NULL, 3, 0, 2, 'build', 1, 1, 'build', 'tool/build/index', '', 0, 'tool:build:list', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (115, '代码生成', NULL, 3, 0, 2, 'code', 1, 1, 'gen', 'tool/gen/index', '', 0, 'tool:gen:list', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (116, '系统接口', NULL, 3, 0, 2, 'swagger', 1, 1, 'swagger', 'tool/swagger/index', '', 0, 'tool:swagger:list', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (500, '操作日志', NULL, 108, 0, 2, 'form', 1, 1, 'operlog', 'monitor/operlog/index', '', 0, 'monitor:operlog:list', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (501, '登录日志', NULL, 108, 0, 2, 'logininfor', 1, 1, 'logininfor', 'monitor/logininfor/index', '', 0, 'monitor:logininfor:list', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1001, '用户查询', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1002, '用户新增', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1003, '用户修改', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1004, '用户删除', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1005, '用户导出', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:export', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1006, '用户导入', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:import', 6, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1007, '重置密码', NULL, 100, 0, 3, '#', 1, 2, '', '', '', 0, 'system:user:resetPwd', 7, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1008, '角色查询', NULL, 101, 0, 3, '#', 1, 2, '', '', '', 0, 'system:role:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1009, '角色新增', NULL, 101, 0, 3, '#', 1, 2, '', '', '', 0, 'system:role:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1010, '角色修改', NULL, 101, 0, 3, '#', 1, 2, '', '', '', 0, 'system:role:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1011, '角色删除', NULL, 101, 0, 3, '#', 1, 2, '', '', '', 0, 'system:role:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1012, '角色导出', NULL, 101, 0, 3, '#', 1, 2, '', '', '', 0, 'system:role:export', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1013, '菜单查询', NULL, 102, 0, 3, '#', 1, 2, '', '', '', 0, 'system:menu:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1014, '菜单新增', NULL, 102, 0, 3, '#', 1, 2, '', '', '', 0, 'system:menu:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1015, '菜单修改', NULL, 102, 0, 3, '#', 1, 2, '', '', '', 0, 'system:menu:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1016, '菜单删除', NULL, 102, 0, 3, '#', 1, 2, '', '', '', 0, 'system:menu:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1017, '部门查询', NULL, 103, 0, 3, '#', 1, 2, '', '', '', 0, 'system:dept:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1018, '部门新增', NULL, 103, 0, 3, '#', 1, 2, '', '', '', 0, 'system:dept:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1019, '部门修改', NULL, 103, 0, 3, '#', 1, 2, '', '', '', 0, 'system:dept:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1020, '部门删除', NULL, 103, 0, 3, '#', 1, 2, '', '', '', 0, 'system:dept:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1021, '岗位查询', NULL, 104, 0, 3, '#', 1, 2, '', '', '', 0, 'system:post:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1022, '岗位新增', NULL, 104, 0, 3, '#', 1, 2, '', '', '', 0, 'system:post:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1023, '岗位修改', NULL, 104, 0, 3, '#', 1, 2, '', '', '', 0, 'system:post:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1024, '岗位删除', NULL, 104, 0, 3, '#', 1, 2, '', '', '', 0, 'system:post:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1025, '岗位导出', NULL, 104, 0, 3, '#', 1, 2, '', '', '', 0, 'system:post:export', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1026, '字典查询', NULL, 105, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:dict:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1027, '字典新增', NULL, 105, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:dict:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1028, '字典修改', NULL, 105, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:dict:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1029, '字典删除', NULL, 105, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:dict:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1030, '字典导出', NULL, 105, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:dict:export', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1031, '参数查询', NULL, 106, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:config:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1032, '参数新增', NULL, 106, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:config:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1033, '参数修改', NULL, 106, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:config:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1034, '参数删除', NULL, 106, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:config:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1035, '参数导出', NULL, 106, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:config:export', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1036, '公告查询', NULL, 107, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:notice:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1037, '公告新增', NULL, 107, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:notice:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1038, '公告修改', NULL, 107, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:notice:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1039, '公告删除', NULL, 107, 0, 3, '#', 1, 2, '#', '', '', 0, 'system:notice:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1040, '操作查询', NULL, 500, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:operlog:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1041, '操作删除', NULL, 500, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:operlog:remove', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1042, '日志导出', NULL, 500, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:operlog:export', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1043, '登录查询', NULL, 501, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:logininfor:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1044, '登录删除', NULL, 501, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:logininfor:remove', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1045, '日志导出', NULL, 501, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:logininfor:export', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1046, '在线查询', NULL, 109, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:online:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1047, '批量强退', NULL, 109, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:online:batchLogout', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1048, '单条强退', NULL, 109, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:online:forceLogout', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1049, '任务查询', NULL, 110, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:job:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1050, '任务新增', NULL, 110, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:job:add', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1051, '任务修改', NULL, 110, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:job:edit', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1052, '任务删除', NULL, 110, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:job:remove', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1053, '状态修改', NULL, 110, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:job:changeStatus', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1054, '任务导出', NULL, 110, 0, 3, '#', 1, 2, '#', '', '', 0, 'monitor:job:export', 7, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1055, '生成查询', NULL, 115, 0, 3, '#', 1, 2, '#', '', '', 0, 'tool:gen:query', 1, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1056, '生成修改', NULL, 115, 0, 3, '#', 1, 2, '#', '', '', 0, 'tool:gen:edit', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1057, '生成删除', NULL, 115, 0, 3, '#', 1, 2, '#', '', '', 0, 'tool:gen:remove', 3, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1058, '导入代码', NULL, 115, 0, 3, '#', 1, 2, '#', '', '', 0, 'tool:gen:import', 2, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1059, '预览代码', NULL, 115, 0, 3, '#', 1, 2, '#', '', '', 0, 'tool:gen:preview', 4, '2022-04-28 03:15:49', '2022-04-28 03:15:49');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (1060, '生成代码', NULL, 115, 0, 3, '#', 1, 2, '#', '', '', 0, 'tool:gen:code', 5, '2022-04-28 03:15:49', '2022-04-28 03:15:49');

INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (4, '接口管理', NULL, 0, 0, 1, 'component', 1, 0, 'resource', NULL, '', 0, '', 4, '2022-04-28 03:12:18', '2022-04-28 03:12:18');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (117, '接口列表', NULL, 4, 0, 2, 'icon', 1, 1, 'api-list', 'interapi/access/index', '', 0, '', 1, '2022-04-28 12:07:23', '2022-04-28 12:07:23');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (118, '接口调用记录', NULL, 4, 0, 2, 'list', 1, 1, 'api-invoke', 'interapi/invoke/index', '', 0, '', 2, '2022-04-28 12:07:23', '2022-04-28 12:07:23');
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (5, '接口日志', NULL, 0, 0, 1, 'component', 1, 0, 'resourcelog', NULL, '', 0, '', 5, now(), now());
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (119, '日志列表', NULL, 5, 0, 2, 'icon', 1, 1, 'api-log', 'interlog/log/index', '', 0, '', 1, now(), now());
INSERT INTO `t_menu` (`id`, `name`, `system_id`, `parent_id`, `status`, `level`, `icon`, `visible`, `type`, `path`, `component`, `query`, `out_link`, `perms`, `sort`, `create_time`, `modify_time`) VALUES (120, '日志分析', NULL, 5, 0, 2, 'list', 1, 1, 'api-loganalysis', 'interlog/loganalysis/index', '', 0, '', 2, now(), now());

