create table st_resource
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


insert into st_resource values(1,'销售信息','',0,1,1,'');
insert into st_resource values(2,'业务信息','',0,1,1,'');
insert into st_resource values(3,'用户统计','',0,1,1,'');
insert into st_resource values(4,'店铺统计','',0,1,1,'');
insert into st_resource values(5,'售后信息','',0,1,1,'');
insert into st_resource values(6,'维权信息','',0,1,1,'');
insert into st_resource values(7,'投诉信息','',0,1,1,'');
insert into st_resource values(8,'发货物流统计','',0,1,1,'');
insert into st_resource values(9,'评论信息','',0,1,1,'');

insert into st_resource values(10,'销售统计','/shopstat/sale/sale.do',1,1,1,'');
insert into st_resource values(11,'商品销售明细','/shopstat/sale/saleDetail.do',1,1,1,'');
insert into st_resource values(12,'购物流程','/shopstat/bus/buy.do',2,1,1,'');
insert into st_resource values(13,'支付信息','/shopstat/bus/pay.do',2,1,1,'');
insert into st_resource values(14,'用户访问','/shopstat/user/access.do',3,1,1,'');
insert into st_resource values(15,'店铺DSR','/shopstat/shop/dsr.do',4,1,1,'');
insert into st_resource values(16,'售后统计','/shopstat/aftersale/afterSale.do',5,1,1,'');
insert into st_resource values(17,'维权统计','/shopstat/aftersale/safeguardStat.do',6,1,1,'');
insert into st_resource values(18,'投诉统计','/shopstat/complain/complain.do',7,1,1,'');
insert into st_resource values(19,'超时发货统计','/shopstat/deliver/deliverTimeout.do',8,1,1,'');
insert into st_resource values(20,'虚假发货统计','/shopstat/deliver/deliverException.do',8,1,1,'');
insert into st_resource values(21,'用户评论统计','/shopstat/comment/comment.do',9,1,1,'');

create table lod_safeguard (
   id bigint auto_increment,
   stat_date date comment '统计日期',
   insert_time datetime comment '录入时间',
   safeguard_id bigint comment '维权id',
   order_id varchar(50) comment '订单id',
   from_source int default 1 comment '订单来源: 1--PC,2--无线',
   order_amount bigint comment '订单金额',
   product_id varchar(20) comment '商品id',
   tag_id int comment '一级分类',
   sub_id int comment  '二级分类',
   third_id int comment '三级分类',
   apply_time datetime comment '申请时间',
   user_id bigint comment '买家id',
   shop_id bigint comment '商店id',
   safeguard_state int comment '维权状态',
   safeguard_starter int comment '维权发起方,1--买家,2--商家',
   safeguard_reason int comment '维权原因',
   mistaker int comment '过错方,1--商家,2--买家,3--买卖双方,4--平台,5--不确定',
   over_time datetime comment '结束时间',
   primary key(id),
   unique key (safeguard_id),
   key stat_date(stat_date),
   key apply_time(apply_time),
   key over_time(over_time),
   key tag_date(tag_id,sub_id,third_id,apply_time)
) engine=InnoDB default charset=utf8 comment '维权细节统计表';

create table stat_safeguard (
   id bigint auto_increment,
   stat_date date comment '统计日期',
   insert_time datetime comment '录入时间',
   from_source int default 1 comment '订单来源: 1--PC,2--无线',
   tag_id int comment '一级分类',
   sub_id int comment  '二级分类',
   third_id int comment '三级分类',
   safeguard_starter int comment '维权发起方,1--买家,2--商家',
   inc_cnt int comment '当天新增数',
   shop_mistake_cnt int comment '当天新增中商家责任的维权数',
   user_mistake_cnt int comment '当天新增中买家责任的维权数',
   complete_cnt int comment '当天完成数',
   complete_over_week_cnt int comment '当天完成且维权周期超过7天的维权数',
   close_cnt int comment '当天关闭数',
   dealing_total_cnt int comment '累计处理中的维权数',
   primary key(id),
   key stat_date(stat_date),
   key tag_stat(tag_id,stat_date),
   key tagall_stat(tag_id,sub_id,third_id,stat_date)
) engine=InnoDB default charset=utf8 comment '维权统计表';



