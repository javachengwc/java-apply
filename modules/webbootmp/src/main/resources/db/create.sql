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