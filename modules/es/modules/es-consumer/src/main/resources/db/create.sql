create table t_test (
  id bigint AUTO_INCREMENT,
  name varchar(64) default '' comment '名称',
  create_time datetime comment '创建时间',
  modify_time datetime comment '更新时间',
  primary key (id)
) ENGINE=InnoDB comment='test表';