
create table shop_user
(
   id  bigint AUTO_INCREMENT comment '自增长id',
   name varchar(20) comment '用户姓名',
   nick_name varchar(20) comment '用户昵称',
   sex int comment '性别 0--女 1--男',
   mobile varchar(11) comment '用户手机号',
   statu int default 0 comment '用户状态 0--正常 1--禁用',
   reg_comefrom int comment '注册来源 1--pc 2--app',
   reg_time datetime comment '注册时间',
   last_login_id bigint default 0 comment '最后登录的登录记录id' ,
   last_login_time  datetime comment '最后登陆时间',
   token varchar(32) comment '登陆token',
   token_expire_time datetime comment 'token过期时间',
   app varchar(32) default '' comment '使用的app',
   app_version varchar(32) default '' comment '使用的app版本',
   device_os varchar(64) default '' comment '设备系统',
   device_os_version varchar(64) default '' comment '设备系统版本',
   device_token varchar(128) default '' comment '设备标识',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key name (name),
   key mobile(mobile),
   key reg_time(reg_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户表';


create table user_login (
   id  bigint AUTO_INCREMENT comment '自增长id',
   user_id bigint comment '用户id',
   login_from int comment '登录来源 1--pc 2--app',
   login_tag int default 0 comment '登录或登出 0--登录 1--登出',
   ip varchar(32) default '' comment '登录ip',
   login_time datetime comment '登录时间',
   logout_time datetime comment '登出时间',
   app varchar(32) default '' comment '使用的app',
   app_version varchar(32) default '' comment '使用的app版本',
   device_os varchar(64) default '' comment '设备系统',
   device_os_version varchar(64) default '' comment '设备系统版本',
   device_token varchar(128) default '' comment '设备标识',
   create_time datetime comment '创建时间',
   modified_time datetime comment'修改时间',
   PRIMARY KEY (id),
   key idx_user_login(user_id,login_time),
   key idx_logout(logout_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录登出记录表';

-- user_forbin