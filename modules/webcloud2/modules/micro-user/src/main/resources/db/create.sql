create table user
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
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key name (name),
   key mobile(mobile),
   key reg_time(reg_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户表';

