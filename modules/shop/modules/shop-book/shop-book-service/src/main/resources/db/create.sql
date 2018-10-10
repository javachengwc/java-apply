create table shop_book (
   id bigint auto_increment,
   name varchar(64) default '' comment '书名',
   sub_title varchar(128) default '副标题',
   info varchar(256) default '' comment '简介',
   statu int default 0 comment '状态 0--初始化 1--上架 2--下架',
   author varchar(128) default '' comment '作者',
   price int default 0 comment '价格,分',
   orgl_price int default 0 comment '原始价格,分',
   top_type int default 0 comment '一级分类',
   top_type_name varchar(32) default '' comment '一级分类名称',
   second_type int default 0 comment '二级分类',
   second_type_name varchar(32) default '' comment '二级分类名称',
   store_id bigint default 0 comment '书店id',
   store_name varchar(64) default '书店名称',
   publish_time date default "0000-00-00" comment '出版时间',
   publisher_id bigint default 0 comment '出版社id',
   publisher_name varchar(64) default '' comment '出版社名称',
   label varchar(64) default '' comment '标签',
   sale_cnt int default 0 comment '销量',
   create_time datetime comment '创建时间',
   shelf_time datetime comment '上架时间',
   down_time datetime comment '下架时间',
   modified_time datetime comment '修改时间',
   primary key (id),
   key idx_type(top_type,second_type),
   key idx_create(create_time),
   key idx_downtime(down_time),
   key idx_name(name)
) engine INNODB default charset utf8 comment '书籍表';

create table qa (
   id bigint auto_increment,
   question varchar(255) default '' comment '问题',
   type int comment '问题分类',
   answer varchar(2000) default '' comment '答案',
   create_time datetime comment '创建时间',
   is_show int default 0 comment '是否展示 0--否 1--是',
   sort int default 0 comment '顺序值',
   modified_time datetime comment '修改时间',
   primary key (id)
) engine INNODB default charset utf8 comment 'qa表';

create table advert (
    id bigint auto_increment,
    title varchar(64) default '' comment '标题',
    content varchar(256) default '' comment '广告内容',
    statu int default 0 comment '状态 0--初始 1--启用 2--停用',
    position_code varchar(64) default '' comment '广告位置code',
    position_name varchar(64) default '' comment '广告位置名称',
    img_url varchar(128) default '' comment '广告图片url',
    start_time datetime comment '开始时间',
    end_time datetime comment '结束时间',
    sort int default 0 comment '排序值',
    go_type int default 0 comment '跳转类型 0--url 1--搜索页',
    go_value varchar(1024) default '' comment '跳转传参,跳转搜索页用到,json格式',
    create_time datetime comment '创建时间',
    modified_time datetime comment '修改时间',
    primary key(id),
    key idx_title(title),
    key idx_start_end(start_time,end_time)
) engine INNODB default charset utf8 comment '广告表';

create table dict (
   id bigint auto_increment,
   dict_key varchar(64) default '' comment '字典key',
   dict_label varchar(128) default '' comment '字典标签',
   dict_value varchar(256) default '' comment '字典值',
   dict_ext varchar(256) default '' comment '字典扩展信息',
   dict_type int comment '字典类型',
   dict_type_name varchar(64) default '' comment '字典类型名称',
   is_use int default 1 comment '是否启用 0--否 1--是',
   sort int default 0 comment '顺序',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   primary key (id),
   key idx_dict_key(dict_key),
   key idx_dict_type(dict_type)
) engine INNODB default charset utf8 comment '字典表';