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

create table user_daily
(
   id  bigint AUTO_INCREMENT comment '自增长id',
   user_id bigint comment '用户id',
   mobile varchar(11) comment '手机号',
   score int default 0 comment '积分',
   day_date date comment '日期',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   primary key (id),
   key user_day (user_id,day_date),
   key mobile(mobile),
   key day_mobile(day_date,mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户每天数据表';
