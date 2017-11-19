create  table  sf_brand (
  id bigint auto_increment,
  name varchar(50) default '' comment '品牌名称',
  logo varchar(128) comment '品牌logo',
  level int default 1 comment '品牌级别',
  is_show int default 1 comment '是否显示 1--显示 0--不显示',
  sort int default 0 comment  '排序序号',
  create_time datetime comment '创建时间',
  update_time datetime comment '更新时间',
  note varchar(128) comment '备注',
  primary key(id),
  key idx_name(name)
) ENGINE=InnoDB default charset=utf8 comment '沙发品牌表';

insert into sf_brand values(1,'凯撒豪庭','',1,1,1,now(),now(),'');
insert into sf_brand values(2,'彼岸阳光','',1,1,1,now(),now(),'');

create  table  sf_mater (
  id bigint auto_increment,
  name varchar(50) default '' comment '材质名称',
  sort int default 0 comment  '排序序号',
  create_time datetime comment '创建时间',
  update_time datetime comment '更新时间',
  note varchar(128) comment '备注',
  primary key(id),
  key idx_name(name)
) ENGINE=InnoDB default charset=utf8 comment '沙发材质表';

insert into sf_mater values(1,'实木',1,now(),now(),'');
insert into sf_mater values(2,'皮艺',1,now(),now(),'');
insert into sf_mater values(3,'布艺',1,now(),now(),'');
insert into sf_mater values(4,'板木',1,now(),now(),'');
insert into sf_mater values(5,'红木',1,now(),now(),'');
insert into sf_mater values(6,'棉麻',1,now(),now(),'');
insert into sf_mater values(7,'其他',1,now(),now(),'');

create  table  sf_type (
  id bigint auto_increment,
  name varchar(50) default '' comment '类型名称',
  sort int default 0 comment  '排序序号',
  create_time datetime comment '创建时间',
  update_time datetime comment '更新时间',
  note varchar(128) comment '备注',
  primary key(id),
  key idx_name(name)
) ENGINE=InnoDB default charset=utf8 comment '沙发类型表';

insert into sf_type values(1,'沙发套装',1,now(),now(),'');
insert into sf_type values(2,'转角沙发',1,now(),now(),'');
insert into sf_type values(3,'沙发床',1,now(),now(),'');
insert into sf_type values(4,'功能沙发',1,now(),now(),'');
insert into sf_type values(5,'贵妃',1,now(),now(),'');
insert into sf_type values(6,'脚踏',1,now(),now(),'');
insert into sf_type values(7,'单人沙发',1,now(),now(),'');
insert into sf_type values(8,'双人沙发',1,now(),now(),'');
insert into sf_type values(9,'三人沙发',1,now(),now(),'');
insert into sf_type values(10,'四人沙发',1,now(),now(),'');

create  table  sf_style (
  id bigint auto_increment,
  name varchar(50) default '' comment '风格名称',
  sort int default 0 comment  '排序序号',
  create_time datetime comment '创建时间',
  update_time datetime comment '更新时间',
  note varchar(128) comment '备注',
  primary key(id),
  key idx_name(name)
) ENGINE=InnoDB default charset=utf8 comment '沙发风格表';

insert into sf_style values(1,'简美风格',1,now(),now(),'');
insert into sf_style values(2,'中式风格',1,now(),now(),'');
insert into sf_style values(3,'法式风格',1,now(),now(),'');
insert into sf_style values(4,'美式古典',1,now(),now(),'');
insert into sf_style values(5,'欧式古典',1,now(),now(),'');
insert into sf_style values(6,'北欧风格',1,now(),now(),'');
insert into sf_style values(7,'现代风格',1,now(),now(),'');
insert into sf_style values(8,'美式田园',1,now(),now(),'');
insert into sf_style values(9,'欧式田园',1,now(),now(),'');
insert into sf_style values(10,'地中海风格',1,now(),now(),'');
insert into sf_style values(11,'韩式田园',1,now(),now(),'');
insert into sf_style values(12,'英式风格',1,now(),now(),'');
insert into sf_style values(13,'现代简约',1,now(),now(),'');
insert into sf_style values(14,'传统中式',1,now(),now(),'');

create  table  sf_product (
  id bigint auto_increment,
  name varchar(50) default '' comment '产品名称',
  price bigint default 0 comment '价格',
  orgl_price bigint default 0 comment '原价',
  statu int comment '状态 0--初始,1--上架,2--下架',
  up_time datetime comment '上架时间',
  down_time datetime comment '下架时间',
  brand_id bigint comment '品牌Id',
  brand_name varchar(50) comment '品牌名称',
  mater_id bigint comment '材质Id',
  mater_name varchar(50) comment '材质名称',
  mater_note varchar(128) comment '材质说明',
  type_id bigint comment '类型id',
  type_name varchar(50) comment '类型名称',
  style_id bigint comment '风格id',
  stype_name varchar(50) comment '风格名称',
  pd_sn varchar(20) comment '产品编号',
  make_place varchar(20) comment '产地',
  pd_size varchar(10) comment '体积',
  pd_speci varchar(50) comment '产品规格',
  pd_feature varchar(128) comment '产品特点',
  pd_desc varchar(200) comment '产品描述',
  create_time datetime comment '创建时间',
  update_time datetime comment '更新时间',
  primary key(id),
  key idx_name(name),
  key idx_sn(pd_sn),
  key idx_create(create_time),
  key idx_brand_mater_type(brand_id,mater_id,type_id)
) ENGINE=InnoDB default charset=utf8 comment '沙发产品表';

insert into sf_product values(1,'白杨实木贵妃沙发','300000','500000',1,now(),null,2,'彼岸阳光',1,'实木','白杨木+硬质实木+樱桃木薄片',5,'贵妃',10,'地中海风格',
'SF-GH-BAYG-001','越南','1.36','长1950*宽1050*高860mm','','',now(),now());
insert into sf_product values(2,'霸气黄牛皮沙发套装','800000','1000000',1,now(),null,1,'凯撒豪庭',2,'皮艺','巴西进口头层黄牛皮+硬质实木+蛇簧+海绵+公仔棉',1,'沙发套装',7,'现代风格',
'SF-TZ-KSHT-001','广东深圳','4.25','长3880*宽1940*高840mm','五档调节靠枕，不同姿势，颈椎轻松无压力；二段式护腰枕，贴心呵护腰椎。','',now(),now());

