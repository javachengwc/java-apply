create table shop_order
(
   id  bigint AUTO_INCREMENT comment '自增长id',
   order_sn varchar(20) comment '订单sn',
   buyer_id bigint comment '买家id',
   buyer_name varchar(32) default '' comment '买家名称',
   buyer_mobile varchar(11) default '' comment '买家手机号',
   statu int default 0 comment '订单状态',
   goods_id bigint comment '商品id',
   goods_name varchar(32) default '' comment '商品名称',
   goods_count int default 0 comment '商品数量',
   total_amount bigint default 0 comment '总金额',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key idx_order_sn (order_sn),
   key idx_buyer(buyer_id),
   key idx_goods(goods_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '订单表';

