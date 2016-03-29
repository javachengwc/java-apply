create table user_act_note
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '动作名称',
   name_ch varchar(50) comment '中文名称',
   PRIMARY KEY (id),
   KEY name (name),
   KEY name_ch (name_ch)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户操作名称与中文名称字典表';

create table user_distribute_report
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   creator_id int comment '创建者Id',
   creator_name varchar(20) comment '创建者名',
   create_date timestamp  comment  '创建时间',
   data_path varchar(200) comment '文件路径',
   condit  varchar(300) comment '查询条件',
   note varchar(100) comment '备注',
   PRIMARY KEY (id),
   KEY creator_id (creator_id),
   KEY create_date (create_date)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户分布报告';

create table user_daily_stat
(
    id int AUTO_INCREMENT comment '自增长id',
    source_type int comment '来源',
    plat int comment '平台',
    utma varchar(100) comment '用户utma',
    user_id varchar(50) comment '用户user_id',
    pv int comment 'pv',
    out_count int comment 'out数',
    order_count int comment '订单数',
    order_amount int comment '订单额',
    sign_count int comment '签到数',
    create_time TIMESTAMP  comment '创建时间',
    partitiontime varchar(50) comment '分区日期',
    PRIMARY KEY (id),
    key partitiontime_idx(partitiontime,utma,user_id,source_type,plat)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户每天数据';


insert into user_daily_stat(source_type,plat,utma,user_id,pv,out_count,order_count,order_amount,sign_count,create_time,partitiontime) VALUES
(0,0,'9999988111436264687','',97,25,0,0,0,'2015-07-21 10:00:00','20150721'),
(0,0,'9999976551437493811','1821',60,13,1,2000,1,'2015-07-21 10:00:00','20150721'),
(0,0,'9999952511418979072','',72,6,0,0,0,'2015-07-21 10:00:00','20150721');

create table ty_entity
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '名称',
   table_name varchar(50) comment '表名',
   query_sql varchar(2000) comment '查询语句',
   page_size int default 0 comment '每页条数',
   PRIMARY KEY (id),
   KEY name (name),
   KEY table_name(table_name)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '通用列表实体表';

create table ty_entity_item
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   entity_id int not null comment '实体id',
   item_name varchar(20) comment '列项名',
   item_col  varchar(50) comment '列项对应的列名',
   sort int default 0 comment '序号',
   format varchar(200) comment '列数据格式化',
   PRIMARY KEY (id),
   KEY entity_id (entity_id),
   KEY item_name(item_name)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '通用列表实体列项表';

create table ty_entity_cdn
(
   id  int(11) not null AUTO_INCREMENT comment '自增长id',
   entity_id int not null comment '实体id',
   cdn_name varchar(20) comment '条件名',
   cdn_col  varchar(50) comment '条件对应的列名',
   cdn_type int default 0 comment '条件类型 0--int 1--string',
   is_show int default 1 comment '是否显示 0--不显示  1--显示',
   sort int default 0 comment '序号',
   PRIMARY KEY (id),
   KEY entity_id (entity_id),
   KEY cdn_name(cdn_name),
   Key cdn_col(cdn_col)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '通用列表实体查询条件表';

insert into ty_entity(name,table_name,query_sql,page_size) values('文件列表','file_record','',2);
insert into ty_entity(name,table_name,query_sql,page_size) values('用户行为','user_act_note','',10);
insert into ty_entity(name,table_name,query_sql,page_size) values('用户每天数据','user_daily_stat','select id,partitiontime,source_type,plat,user_id,pv,out_count,order_amount,sign_count from user_daily_stat where 1=1 #if($partitiontime && $partitiontime!='''') and partitiontime=''${partitiontime}'' #end #if($user_id && $user_id!='''') and user_id=$user_id #end',10);

insert into ty_entity_cdn(entity_id,cdn_name,cdn_col,cdn_type,is_show,sort) values
(2,'行为名称','name',1,1,1),
(2,'行为标识','name_ch',1,1,2);
insert into ty_entity_cdn(entity_id,cdn_name,cdn_col,cdn_type,is_show,sort)
values(3,'统计日期','partitiontime',1,1,1),(3,'用户ID','user_id',0,1,2);

insert into ty_entity_item(entity_id,item_name,item_col,sort,format)
values(2,'ID','id',1,''),(2,'行为名称','name',2,''),(2,'行为标识','name_ch',3,'');
