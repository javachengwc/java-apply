create table user_act_note
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '动作名称',
   name_ch varchar(50) comment '中文名称',
   PRIMARY KEY (id),
   KEY name (name),
   KEY name_ch (name_ch)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户操作名称与中文名称字典表';

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