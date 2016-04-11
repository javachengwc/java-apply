
create table pd_sku_stock
(
   id  bigint auto_increment comment '自增长id',
   seller_id int comment  '卖家ID',
   product_id varchar(50) comment '产品ID',
   sku_num varchar(64) comment 'sku编码',
   sku_name varchar(128) comment 'sku编码名称',
   cur_price bigint comment '商品售价',
   org_price bigint comment '商品原价',
   stock_cnt int default 0 comment  '商品库存数',
   update_time timestamp comment '修改时间',
   updater int comment '更新人ID',
   create_time timestamp comment '创建时间',
   creater int comment '创建人ID',
   pic varchar(256) comment 'sku图片',
   note varchar(256) comment '备注',
  primary key (id),
  unique key product_sku(product_id,sku_num),
  KEY seller_product (seller_id,product_id)
) ENGINE=InnoDB default charset=utf8 comment '商品sku库存表';




