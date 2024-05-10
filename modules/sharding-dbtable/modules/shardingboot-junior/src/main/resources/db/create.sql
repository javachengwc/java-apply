
create database db_user;
create table if not exists t_user(
  id bigint auto_increment,
  user_name varchar(32) default '' comment '用户名称',
  create_time datetime comment '创建时间',
  update_time datetime comment '修改时间',
  primary key(id),
  key idx_user_name(user_name)
) engine INNODB default charset utf8 comment '用户表';

create database db_order;
create table if not exists t_order(
  id bigint auto_increment,
  order_no varchar(36) default '' comment '订单编号',
  user_id bigint comment '用户id',
  user_name varchar(32) default '' comment '用户名称',
  statu int default 0 comment '状态',
  price bigint default 0 comment '订单总价',
  shop_id bigint comment '店铺id',
  shop_name varchar(64) comment '店铺名称',
  create_time datetime comment '创建时间',
  update_time datetime comment '修改时间',
  primary key(id),
  unique key udx_order_no(order_no),
  key idx_user_order(user_id,order_no),
  key idx_create_order(create_time,order_no),
  key idx_shop_order(shop_id,order_no)
) engine INNODB default charset utf8 comment '订单表';
