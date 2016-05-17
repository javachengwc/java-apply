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
insert into sys_resource values(4,'订单管理','',0,1,1,'');
insert into sys_resource values(5,'订单列表','/shop/order/order.do',4,1,1,'');
insert into sys_resource values(6,'订单详单','/shop/order/orderProduct.do',4,1,1,'');
insert into sys_resource values(7,'发货物流管理','',0,1,1,'');
insert into sys_resource values(8,'售后管理','',0,1,1,'');
insert into sys_resource values(9,'店铺管理','',0,1,1,'');
insert into sys_resource values(10,'发货管理','',7,1,1,'');
insert into sys_resource values(11,'物流公司','',7,1,1,'');
insert into sys_resource values(12,'物流异常','',7,1,1,'');
insert into sys_resource values(13,'售后管理','',8,1,1,'');
insert into sys_resource values(14,'维权管理','',8,1,1,'');
insert into sys_resource values(15,'投诉管理','',8,1,1,'');
insert into sys_resource values(16,'退款管理','',8,1,1,'');
insert into sys_resource values(17,'退货管理','',8,1,1,'');
insert into sys_resource values(18,'店铺列表','',9,1,1,'');
insert into sys_resource values(19,'卖家资产','',9,1,1,'');
insert into sys_resource values(20,'店铺折扣','/shop/shop/shopDiscount.do',9,1,1,'');
insert into sys_resource values(21,'营销管理','',0,1,1,'');
insert into sys_resource values(22,'商城活动','',21,1,1,'');

create  table  pd_tag (
  id int auto_increment comment '分类ID',
  name varchar(50) default '' comment '分类名称',
  pid int default 0 comment '上级分类id',
  level int default 1 comment '级数',
  state int default 1 comment '状态 1--新增 2--作废',
  sort int default 0 comment  '排序序号',
  is_leaf int default 0 comment '是否叶子节点 0--否,1--是',
  create_time datetime comment '创建时间',
  creator int comment '创建人ID',
  update_time datetime comment '更新时间',
  updater int comment '更新人Id',
  note varchar(128) comment '备注',
  primary key(id),
  key pid(pid)
) ENGINE=InnoDB default charset=utf8 comment '产品分类表'

create table pd_product
(
   id  bigint auto_increment comment '自增长id',
   product_id varchar(50) comment '产品id',
   shop_id bigint comment  '商店ID',
   name varchar(128) comment '产品名称',
   title varchar(128) comment '产品标题',
   direct_tag_id int comment '直接分类id',
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
   creator int comment  '创建人ID',
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
   key shop_product(shop_id,product_id),
   key state(state,audit_type),
   key tag_product(direct_tag_id,product_id)
)ENGINE=InnoDB default charset=utf8 comment '商品表';

create table pd_sku_stock
(
   id  bigint auto_increment comment '自增长id',
   shop_id bigint comment  '商店ID',
   product_id varchar(50) comment '产品ID',
   sku_num varchar(64) comment 'sku编码',
   sku_name varchar(128) comment 'sku编码名称',
   cur_price bigint comment '商品售价',
   org_price bigint comment '商品原价',
   stock_cnt int default 0 comment  '商品库存数',
   update_time timestamp comment '修改时间',
   updater int comment '更新人ID',
   create_time timestamp comment '创建时间',
   creator int comment '创建人ID',
   pic varchar(256) comment 'sku图片',
   note varchar(256) comment '备注',
  primary key (id),
  unique key product_sku(product_id,sku_num),
  KEY shop_product (shop_id,product_id)
) ENGINE=InnoDB default charset=utf8 comment '商品sku库存表';

create table od_order (
   id bigint auto_increment,
   order_id varchar(20) comment '订单ID',
   trade_code varchar(5) comment'订单渠道, Z0001:实体交易 Z0002:话费充值 Z0003:积分兑换类 Z0004:货到付款',
   from_source int default 1 comment '订单来源: 1--PC,2--无线',
   order_state int default 0 comment '订单状态: 0:初始 1:待支付 2:待发货 3:已发货 5:交易成功 7:交易关闭',
   user_id bigint comment '买家ID',
   user_name varchar(128) comment '买家昵称',
   user_comment varchar(256) comment '卖家备注',
   shop_id bigint comment '商店ID',
   shop_name varchar(128) comment '商店名称',
   shop_comment varchar(256) comment '商店备注',
   create_time datetime comment '订单生成时间',
   is_overbuy int default 0 comment '是否超卖: 0,正常 1,超卖',
   is_cancel int default 0 comment '是否取消 0--否,1--是',
   cancel_time datetime comment '订单取消时间',
   cancel_type int comment '订单取消类型: 1,系统自动取消  2,买家手工取消  3,售后取消  4,维权取消',
   cancel_reason int comment '订单取消原因: 1,我不想买了 2,信息填写错误 3:重新拍 4:卖家缺货 5:同城见面交易 6:其他原因',
   update_time timestamp comment '更新时间',
   updater int comment '更新人id',
   price_amount bigint default 0 comment '订单价格',
   postage bigint default 0 comment '邮费',
   exchange_score int default 0 comment '兑换的积分数量',
   exchange_cash  bigint default 0  comment '兑换的金额',
   coupon_no varchar(32) comment '优惠券编码',
   coupon_price bigint default 0 comment '优惠券金额',
   discount_id int comment '活动id',
   discount_type int comment '活动类型',
   discount_desc varchar(128) comment '活动描述',
   discount_rule varchar(128) comment '满足的活动规则',
   discount_price bigint comment  '活动优惠的价格',
   pay_state int default 0 comment '支付状态: 0:未支付 1:支付成功 2:支付失败',
   pay_time datetime comment '买家付款时间',
   pay_account varchar(64) comment '支付账号',
   trade_pay_id varchar(64) comment '交易系统支付ID',
   pay_channel int default 0 comment '支付渠道 0:无 1:支付宝 2:微信支付 3:网银',
   pay_id varchar(64) comment '支付交易代码',
   receiver_province_id int comment '收货人所属省ID',
   receiver_province_name varchar(36) comment '收货人所属省名称',
   receiver_city_id int comment  '收货人所属城市ID',
   receiver_city_name varchar(36) comment  '收货人所属城市名称',
   receiver_area_id int comment  '收货人所属区县ID',
   receiver_area_name varchar(36) comment '收货人所属区县名称',
   receiver_address varchar(256) comment '收货人地址',
   receiver_name varchar(64) comment '收货人姓名',
   receiver_mobile varchar(20) comment '收货人手机号',
   receiver_tele varchar(20) comment '收货人电话',
   receiver_address_id int comment '收货人地址ID',
   receive_time timestamp comment '收货时间',
   deliver_time timestamp comment '卖家发货时间',
   deliver_time_limit int comment '发货时限小时,超过此时间再发货则发货超时',
   remind_deliver_count int default 0 comment '买家提醒卖家发货的次数',
   express_id int comment '快递公司编码',
   express_name varchar(128) comment '快递公司名称',
   express_no varchar(128) comment '货运单号',
  primary key(id),
  unique key (order_id),
  key trade_pay_id(trade_pay_id),
  key create_order(create_time,order_id),
  key shop_order(shop_id,order_id),
  key user_order(user_id,order_id),
  key state_order(order_state,order_id),
  key paytime_order(pay_time,order_id),
  key receive_time_order(receive_time,order_id),
  key receiver_name_order (receiver_name,order_id),
  key receiver_mobile(receiver_mobile,order_id),
  key deliver_time(deliver_time,order_id),
  key express_order (express_no,express_id,order_id)
) ENGINE=InnoDB default charset=utf8 comment '订单表';

create table sh_shop
(
   id bigint auto_increment,
   name varchar(50) comment '店铺名称',
	 state int comment '店铺状态',
   create_time datetime comment '创建时间',
   grade int default 1 comment '店铺等级',
   shop_desc varchar(256) comment '店铺描述',
   pic varchar(128) comment '店铺图',
   verify_status int comment '认证状态',
   verify_time datetime comment '认证时间',
   is_enterprise int default 0 comment '是否企业店铺 0--否 1--是',
   ent_name varchar(128) comment '企业名称',
   owner_id bigint comment '拥有者id',
   owner_name varchar(50) comment '拥有者名',
   primary key (id),
   key shop_name(name),
   key create_shop (create_time,name),
   key shop_owner (owner_id,id)
) engine=InnoDB default charset =utf8 comment '商店表';

create table sh_discount (
   id bigint auto_increment,
   shop_id bigint default 0 comment '商家id',
   discount_name varchar(128) comment '折扣名称',
   begin_time timestamp comment '折扣开始时间',
   end_time timestamp  comment '折扣结束时间',
   discount_state int default 0 comment '促销状态 0--正常, 1--删除, 2--终止',
   discount_range int comment '折扣范围 1--全店商品, 2--指定商品',
   discount_products varchar(256) comment '指定商品串，依,号分割',
   discount_type int comment '折扣类型：1--满额减额,2--满件打折,3--满件包邮,4--满额包邮',
   discount_rule varchar(128) comment '折扣规则',
   create_time timestamp comment '创建时间',
   update_time timestamp comment '更新时间',
   primary key (id),
   key shop_discount (shop_id,discount_name),
   key time_shop (begin_time,end_time,shop_id,discount_state)
) engine=InnoDB default charset =utf8 comment '商店折扣表';


create table od_saleafter
(
    id bigint auto_increment,
    order_id varchar(50) not null comment '订单id',
    order_amount bigint comment '订单金额',
    cost_amount bigint comment '扣款金额',
    product_id varchar(20) comment '商品id',
		product_name varchar(128) comment '商品名称',
    sku_num varchar(128) comment 'sku编码',
    apply_time datetime comment '申请售后时间',
    user_id bigint comment '买家id',
    user_name varchar(50) comment '买家名称',
    shop_id bigint comment '商店id',
    shop_name varchar(128) comment '商店名称',
    refund_type int comment '退款类型 1--只退款, 2--退货退款',
    refund_reason varchar(256) comment '退货退款原因',
    saleafter_state int comment '售后状态 1--等待商家处理, 2--退款退货处理中 3--拒绝退款退货 4--售后完成',
    deal_time datetime comment '处理时间',
    complete_time datetime comment '完成时间',
    complain_id bigint comment '维权id,买家因售后处理不合意而发起维权对应的维权id',
    primary key(id),
    key order_id(order_id),
    key product(product_id,sku_num,order_id),
    key apply_time(apply_time),
    key user_apply(user_id,apply_time),
    key shop_apply(shop_id,apply_time)
) engine=InnoDB default charset=utf8 comment '售后表';





