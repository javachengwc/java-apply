
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

create table job_info
(
    id int auto_increment,
    parent_id int comment '父任务id',
    job_name varchar(128) comment '任务名',
    job_status int comment '任务状态',
    expression varchar(32) comment '任务表达式(crontab)',
    creater varchar(32) comment '创建者',
    create_time int comment '创建时间(秒)',
    type varchar(32) comment '类型',
    exe_url varchar(128) comment '执行url',
    run_status varchar(32) comment '执行状态',
    ip varchar(32) comment '执行应用的ip',
    run_time int comment '心跳时间',
    runer varchar(32) comment '执行者',
    code varchar(32),
    relator varchar(32) comment '任务联系人',
    is_call_back int comment '任务是否有回调 0--无  1--有',
    plan_cost_time int comment '预计执行时间(秒)',
    lasted_monit_time int comment '监控时间',
    monit_result int comment '监控结果',
    primary key (id),
    key parent_id(parent_id),
    key job_name(job_name)
) engine Innodb default charset utf8 comment '任务表';

create table job_drive
(
    id int auto_increment,
    job_id int comment '任务id',
    record_time int comment '记录时间',
    opt_flag int comment '操作标记,0--任务驱动开始,1--任务结束',
    result int comment '结果 0--成功,1--失败',
    note varchar(128) comment '备注',
    driver varchar(32) comment '操作人',
    primary key (id),
    key job_id(job_id),
    key record_time(record_time)
) engine Innodb default charset utf8 comment '任务驱动表';

create table job_execute
(
    id int auto_increment,
    job_id int comment '任务id',
    job_name varchar(128) comment '任务名',
    start_time int comment '开始时间',
    end_time int comment '结束时间',
    state int comment '状态 0--开始,1---完成',
    expression varchar(32) comment '任务表达式',
    operator varchar(32) comment '操作者',
    ip varchar(32) comment '执行应用的ip',
    note varchar(128) comment '备注',
    result int comment '执行结果',
    primary key (id),
    key job_id(job_id),
    key job_name(job_name),
    key start_time(start_time)
) engine Innodb default charset utf8 comment '任务执行表';

create table job_monit
(
    id int auto_increment,
    job_id int comment '任务id',
    record_time int comment '记录时间',
    result int comment '结果 0--正常,1---未正常执行 2---正常执行了但未执行成功',
    note varchar(128) comment '备注',
    primary key (id),
    key job_id(job_id),
    key record_time(record_time)
) engine Innodb default charset utf8 comment '任务监控表';

insert into ty_entity(name,table_name,query_sql,page_size) values('任务列表','job_info','',10);
insert into ty_entity(name,table_name,query_sql,page_size) values('任务驱动信息','job_drive','',10);
insert into ty_entity(name,table_name,query_sql,page_size) values('任务执行记录','job_execute','',10);
insert into ty_entity(name,table_name,query_sql,page_size) values('任务监控','job_monit','',10);

insert into ty_entity_cdn(entity_id,cdn_name,cdn_col,cdn_type,is_show,sort) values
(4,'ID','id',0,1,1),
(4,'名称','job_name',1,1,2);
insert into ty_entity_cdn(entity_id,cdn_name,cdn_col,cdn_type,is_show,sort) values
(5,'任务ID','job_id',0,1,1);
insert into ty_entity_cdn(entity_id,cdn_name,cdn_col,cdn_type,is_show,sort) values
(6,'任务ID','job_id',0,1,1),
(6,'任务名称','job_name',1,1,2);
insert into ty_entity_cdn(entity_id,cdn_name,cdn_col,cdn_type,is_show,sort) values
(7,'任务ID','job_id',0,1,1);

create table ty_import_record
(
    id int auto_increment,
    import_no varchar(20) comment '导入编号',
    operator varchar(20) comment '操作人',
    operate_time datetime comment '操作时间',
    state int comment '状态 0--开始，1--完成，2--异常,3--停止',
    is_tmp int comment '是否导入为临时表',
    table_name varchar(50) comment '导入的表名称',
    finish_time datetime comment '完成时间',
    data_format varchar(20) comment '来源数据格式',
    has_title int comment '是否有标题 0--无，1--有',
    title_col int comment '来源数据标题是否当成列名 0--不当作列名，1--当作列名',
    from_line int comment '导入开始行,如果第1行是标题，数据不应该包含标题，就是从第2行开始导入',
    line_separ varchar(50) comment '行分隔符',
    col_separ varchar(50) comment '列分隔符',
    note varchar(128) comment '备注',
    primary key (id),
    key import_no(import_no),
    key operate_time(operate_time),
    key table_name(table_name)
) engine Innodb default charset utf8 comment '通用导入记录表';

create table ty_export_record
(
    id int auto_increment,
    export_no varchar(20) comment '导出编号',
    operator varchar(20) comment '操作人',
    operate_time datetime comment '操作时间',
    state int comment '状态 0--开始，1--完成，2--异常',
    finish_time datetime comment '完成时间',
    table_name varchar(50) comment '如果是单表导出，导出的表名称',
    export_sql varchar(1024) comment '导出sql',
    export_format varchar(20) comment '导出格式，支持excel,cvs,json,xml',
    need_title int comment '是否需要标题 0--不需要，1--需要',
    file_name varchar(50) comment '导出文件名',
    file_path varchar(256) comment '导出数据文件存储路径',
    note varchar(256) comment '备注',
    primary key (id),
    key export_no(export_no),
    key operate_time(operate_time),
    key file_name(file_name),
    key table_name(table_name)
) engine Innodb default charset utf8 comment '通用导出记录表';

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

