create table sys_resource
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '名称',
   path varchar(250) comment '路径',
   parent_id int comment '父id',
   is_show int default 1 comment '是否显示 0--不显示 1--显示',
   is_menu int comment '是否菜单 0--不是 1--是',
   tag varchar(50) comment '权限标签',
   PRIMARY KEY (id),
   KEY name (name),
   KEY parent_id (parent_id),
   key tag(tag)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '资源表';


insert into sys_resource values(1,'商品管理','',0,1,1,'');
insert into sys_resource values(2,'商品列表','/shop/product/product.do',1,1,1,'');
insert into sys_resource values(3,'商品SKU','/shop/product/productSku.do',1,1,1,'');

create table pd_product
(
   id  bigint auto_increment comment '自增长id',
   product_id varchar(50) comment '产品id',
   seller_id int comment  '卖家ID',
   name varchar(128) comment '产品名称',
   title varchar(128) comment '产品标题',
   direct_cat_id int comment '直接分类id',
   post_type int default 1 comment '包邮 0--不限,1--包邮,2--不包邮,3--部分地区不包邮',
   no_free varchar(256) comment '不包邮地区ID串',
   imgs varchar(512) comment '图片地址串，依逗号分隔',
   max_buy int default 0 comment '单次购买限制 0--不限制',
   send_type int default 2 comment '发货类型 1--自动发货,2--物流发货',
   send_address varchar(256) comment '发货地址',
   send_province_id int comment '发货省ID',
   send_city_id int comment '发货市ID',
   state int default 0 comment '商品状态 0--初始,1--待审核,2--审核通过,3--审核未通过,4--出售中,5--下架',
   update_time timestamp comment '修改时间',
   updater int comment '更新人ID',
   create_time timestamp comment '创建时间',
   creater int comment  '创建人ID',
   note varchar(256) comment '备注',
   type int default 1 comment  '商品类型 1--普通商品,2--虚拟商品',
   send_time_limit int comment '发货时限小时,超过此时间再发货则发货超时',
   delive_desc varchar(512) comment '配送说明',
   is_customized int default 0 comment  '是否定制商品 0--否,1--是',
   audit_type int comment '商品审核类型 1--小审核 2--大审核',
   audit_time datetime comment '商品审核时间',
   sale_time datetime comment '商品开始销售时间',
   offline_reason varchar(256) comment '下架原因',
   offline_time datetime  comment '商品下架时间',
   primary key (id),
   key product(product_id),
   key update_time(update_time),
   key create_time(create_time),
   key seller_product(seller_id,product_id),
   key state(state,audit_type),
   key cat_product(direct_cat_id,product_id)
)ENGINE=InnoDB default charset=utf8 comment '商品表';

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





