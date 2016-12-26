create table tb_db
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   ht varchar(50) comment '主机',
   pt int comment '端口',
   name varchar(128) comment '数据库名',
   account varchar(128) comment '账号',
   pwd varchar(128) comment '密码',
   create_time datetime comment '创建时间',
   update_time datetime comment '更新时间',
   PRIMARY KEY (id),
   KEY ht (ht),
   KEY name (name)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '数据库表';

create table sys_resource
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
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '资源表';

insert into sys_resource values(1,'DB管理','',0,1,1,'');
insert into sys_resource values(2,'DB列表','/db/dbView.do',1,1,1,'');
insert into sys_resource values(3,'数据查询','',0,1,1,'');
insert into sys_resource values(4,'数据查询','/db/query.do',3,1,1,'');




