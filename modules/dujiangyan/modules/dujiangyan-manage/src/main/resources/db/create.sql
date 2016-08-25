create table st_resource
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

insert into st_resource values(1,'服务管理','',0,1,1,'');
insert into st_resource values(2,'应用维度','/djy/serve/application.do',1,1,1,'');
insert into st_resource values(3,'服务维度','/djy/serve/service.do',1,1,1,'');
insert into st_resource values(4,'机器维度','/djy/serve/machine.do',1,1,1,'');
insert into st_resource values(5,'提供者维度','/djy/serve/provider.do',1,1,1,'');
insert into st_resource values(6,'消费者维度','/djy/serve/consumer.do',1,1,1,'');


