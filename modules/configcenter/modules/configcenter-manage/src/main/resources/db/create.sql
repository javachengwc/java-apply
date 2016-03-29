create table simple_user
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '昵称',
   account varchar(50) comment '账号',
   password varchar(50) comment '密码',
   useable int default 1 comment '可用状态 1--可用 0--不可用',
   PRIMARY KEY (id),
   KEY name (name),
   KEY account (account)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '简单账号表';

create table simple_role
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '角色代号',
   name_ch varchar(50) comment '角色名',
   PRIMARY KEY (id),
   KEY name (name),
   KEY name_ch (name_ch)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '简单角色表';

create table simple_user_role
(
   user_id int comment '账号id',
   role_id int comment '角色id',
   KEY user_role(user_id,role_id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '简单用户角色关联表';

create table simple_resource
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '名称',
   path varchar(250) comment '路径',
   parent_id int comment '父id',
   is_show int default 1 comment '是否显示 0--不显示 1--显示',
   is_menu int comment '是否菜单 0--不是 1--是',
   tag varchar(50) comment '权限标签',
   PRIMARY KEY (id),
   KEY name (name),
   KEY parent_id (parent_id),
   key tag(tag)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '简单资源表';

create table simple_role_resource
(
   role_id int comment '角色id',
   resource_id int comment '资源id',
   KEY user_resource(role_id,resource_id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '简单角色资源关联表';

create table simple_tag_user_or_role
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   tag varchar(50) comment '权限标签',
   rela_id int comment '关联的用户或角色id',
   belong int comment '归属用户或角色 0---用户  1---角色',
   PRIMARY KEY (id),
   KEY tag_rela(tag,rela_id,belong),
   key rela(rela_id,belong)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '简单权限标签关联表';

create table cc_app
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '应用名',
   name_ch varchar(50) comment '应用中文名',
   note varchar(255) comment '备注',
   PRIMARY KEY (id),
   KEY name (name),
   KEY name_ch (name_ch)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '应用表';

create table cc_config_item
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   app_id int comment '应用id',
   conf_key varchar(50) comment '配置项',
   conf_value varchar(500) comment '配置值',
   update_at datetime comment '更新时间',
   PRIMARY KEY (id),
   KEY app_id (app_id),
   KEY conf_key (conf_key),
   key app_conf(app_id,conf_key)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '配置项表';

create table cc_operate_record
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   operate_time datetime comment '操作时间',
   operator_id  int comment '操作人id',
   operator_name varchar(50) comment '操作人名称',
   operation varchar(255) comment '操作描述',
   PRIMARY KEY (id),
   KEY operate_time (operate_time),
   KEY operator_id (operator_id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '操作日志表';

insert into simple_user(name,account,password,useable) values('管理员','admin','admin',1);
insert into simple_role(name,name_ch) values('admin','管理员');
insert into simple_user_role(user_id,role_id) values(1,1);
insert into simple_resource values(1,'系统管理','',0,1,1,'');
insert into simple_resource values(2,'用户管理','/rbac/userView.do',1,1,1,'');
insert into simple_resource values(3,'角色管理','/rbac/roleView.do',1,1,1,'');
insert into simple_resource values(4,'资源管理','/rbac/resourceView.do',1,1,1,'');
insert into simple_resource values(5,'权限标签','/rbac/tagView.do',1,1,1,'');
insert into simple_resource values(6,'配置管理','',0,1,1,'');
insert into simple_resource values(7,'应用管理','/appView.do',6,1,1,'');
insert into simple_resource values(8,'配置项管理','/configItemView.do',6,1,1,'');
insert into simple_resource values(9,'日志管理','',0,1,1,'');
insert into simple_resource values(10,'日志列表','/operateView.do',9,1,1,'');
insert into simple_role_resource values(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10);
insert into cc_config_item values(1,1,'task_work_ip','127.0.0,1',now()),(2,1,'task_work_port','1001',now());


