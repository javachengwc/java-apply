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
insert into st_resource values(17,'维权统计','/shopstat/safeguard/safeguard.do',6,1,1,'');
insert into st_resource values(18,'投诉统计','/shopstat/complain/complain.do',7,1,1,'');
insert into st_resource values(19,'超时发货统计','/shopstat/deliver/deliverTimeout.do',8,1,1,'');
insert into st_resource values(20,'虚假发货统计','/shopstat/deliver/deliverException.do',8,1,1,'');
insert into st_resource values(21,'用户评论统计','/shopstat/comment/comment.do',9,1,1,'');



