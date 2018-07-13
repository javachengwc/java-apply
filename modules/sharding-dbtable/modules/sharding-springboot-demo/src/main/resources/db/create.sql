
create database demo_ds_0;
create database demo_ds_1;
###分别在数据库 demo_ds_0, demo_ds_1执行建表语句,t_order_0,t_order_1,t_order_item_0,t_order_item_1
create table if not exists t_order_0(
  order_id bigint auto_increment,
  user_id bigint comment '用户id',
  user_name varchar(32) default '' comment '用户名称',
  statu int default 0 comment '状态',
  price bigint default 0 comment '订单总价',
  shop_id bigint comment '店铺id',
  shop_name varchar(64) comment '店铺名称',
  create_time datetime comment '创建时间',
  modified_time datetime comment '修改时间',
  primary key(order_id),
  key idx_user_order(user_id,order_id),
  key idx_create_order(create_time,order_id),
  key idx_shop_order(shop_id,order_id)
) engine INNODB default charset utf8 comment '订单表分表0';

create table if not exists t_order_1(
  order_id bigint auto_increment,
  user_id bigint comment '用户id',
  user_name varchar(32) default '' comment '用户名称',
  statu int default 0 comment '状态',
  price bigint default 0 comment '订单总价',
  shop_id bigint comment '店铺id',
  shop_name varchar(64) comment '店铺名称',
  create_time datetime comment '创建时间',
  modified_time datetime comment '修改时间',
  primary key(order_id),
  key idx_user_order(user_id,order_id),
  key idx_create_order(create_time,order_id),
  key idx_shop_order(shop_id,order_id)
) engine INNODB default charset utf8 comment '订单表分表1';

create table t_order_item_0 (
  order_item_id bigint auto_increment,
  order_id bigint comment '订单id',
  user_id bigint comment '用户id',
  user_name varchar(32) default '' comment '用户名称',
  item_price bigint default 0 comment '详单项总价',
  shop_id bigint comment '店铺id',
  shop_name varchar(64) comment '店铺名称',
  product_id bigint comment '产品id',
  product_name varchar(64) comment '产品名称',
  sku_num varchar(64) comment 'sku编码',
  sku_name varchar(64) comment 'sku名称',
  count int default 0 comment '数量',
  create_time datetime comment '创建时间',
  modified_time datetime comment '修改时间',
  primary key(order_item_id),
  key idx_order_item(order_id,order_item_id),
  key idx_product_create(product_id,create_time),
  key idx_sku_order(sku_num,product_id,order_id),
  key idx_create_user(create_time,user_id)
) engine INNODB default charset utf8 comment '订单详单分表0';

create table t_order_item_1 (
  order_item_id bigint auto_increment,
  order_id bigint comment '订单id',
  user_id bigint comment '用户id',
  user_name varchar(32) default '' comment '用户名称',
  item_price bigint default 0 comment '详单项总价',
  shop_id bigint comment '店铺id',
  shop_name varchar(64) comment '店铺名称',
  product_id bigint comment '产品id',
  product_name varchar(64) comment '产品名称',
  sku_num varchar(64) comment 'sku编码',
  sku_name varchar(64) comment 'sku名称',
  count int default 0 comment '数量',
  create_time datetime comment '创建时间',
  modified_time datetime comment '修改时间',
  primary key(order_item_id),
  key idx_order_item(order_id,order_item_id),
  key idx_product_create(product_id,create_time),
  key idx_sku_order(sku_num,product_id,order_id),
  key idx_create_user(create_time,user_id)
) engine INNODB default charset utf8 comment '订单详单分表1';