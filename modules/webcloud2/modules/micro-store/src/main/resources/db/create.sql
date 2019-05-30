create table goods
(
   id  bigint AUTO_INCREMENT comment '自增长id',
   name varchar(32) comment '商品名称',
   price bigint comment '单价',
   stock_count int default 0 comment '库存数量',
   lock_stock_cnt int default 0 comment '锁定的库存',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '商品表';


