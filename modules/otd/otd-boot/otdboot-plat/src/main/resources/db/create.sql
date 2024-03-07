CREATE TABLE factory (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  factory_no varchar(50) NOT NULL DEFAULT '' COMMENT '工厂编号',
  factory_name varchar(50) NOT NULL DEFAULT '' COMMENT '工厂名称',
  factory_address varchar(200) DEFAULT '' COMMENT '工厂地址',
  legal_person VARCHAR(30) DEFAULT '' COMMENT '法人',
  create_time datetime comment '创建时间',
  create_by varchar(30) default '' comment '创建人',
  update_time datetime comment '修改时间',
  update_by varchar(30) comment '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_factory_no` (`factory_no`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '工厂表';

CREATE TABLE warehouse (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  warehouse_code varchar(50) NOT NULL DEFAULT '' COMMENT '仓库编码',
  warehouse_name VARCHAR(50) NOT NULL DEFAULT '' COMMENT '仓库名称',
  is_enable INT(2) DEFAULT '1' COMMENT '启用状态，1-启用，0-禁用',
  address varchar(200) DEFAULT '' comment '仓库地址',
  contact_person varchar(50) DEFAULT '' comment '联系人',
  contact_tel varchar(50) DEFAULT '' COMMENT '联系电话',
  contact_email varchar(50) DEFAULT '' COMMENT '联系邮箱',
  logistics_provider_no VARCHAR(30) DEFAULT '' COMMENT '物流商编号',
  diy_capacity int(2) DEFAULT '0' COMMENT '是否有私定能力 0:否 1 是',
  vkorgs varchar(50) DEFAULT '' COMMENT '支持的销售组织,多个以逗号分隔',
  line_body_qps int(11) DEFAULT '0' COMMENT '线体qps',
  work_start_time varchar(20) DEFAULT '' COMMENT '工作开始时间,格式HH:mm:ss',
  work_end_time varchar(20) DEFAULT '' COMMENT '工作结束时间,格式HH:mm:ss',
  in_warehouse_time decimal(6,1) COMMENT '仓内小时数',
  legal_person VARCHAR(30) DEFAULT '' COMMENT '法人',
  create_time datetime comment '创建时间',
  create_by varchar(30) default '' comment '创建人',
  update_time datetime comment '修改时间',
  update_by varchar(30) comment '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '仓库表';

CREATE TABLE storage (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  storage_code varchar(50) NOT NULL DEFAULT '' COMMENT '库存地code',
  storage_name varchar(50) NOT NULL DEFAULT '' COMMENT '库存地名称',
  storage_type INT(4) DEFAULT 1 COMMENT '库存地类型 1-正向库存地 2-前置仓 3-反向良品库存地 4-反向残品库存地 5-其他',
  storage_manager VARCHAR(30) DEFAULT '' COMMENT '库存地管理者',
  warehouse_code varchar(50) DEFAULT '' COMMENT '仓库编码',
  contact_tel varchar(50) DEFAULT '' COMMENT '联系电话',
  province_code VARCHAR(20) DEFAULT '' COMMENT '省份编码',
  province_name VARCHAR(50) DEFAULT '' COMMENT '省份名称',
  city_code VARCHAR(20) DEFAULT '' COMMENT '市编码',
  city_name varchar(50) DEFAULT '' COMMENT '市名称',
  county_code VARCHAR(20) DEFAULT '' COMMENT '区县编码',
  county_name VARCHAR(50) DEFAULT '' COMMENT '区县名称',
  address varchar(128) DEFAULT '' COMMENT '详细地址',
  remark varchar(200) DEFAULT '' COMMENT '备注',
  create_time datetime comment '创建时间',
  create_by varchar(30) default '' comment '创建人',
  update_time datetime comment '修改时间',
  update_by varchar(30) comment '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `udx_storage_code` (`storage_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '库存地表';

CREATE TABLE logistics_provider (
   id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   logistics_provider_no VARCHAR(30) not null DEFAULT '' COMMENT '物流商编号',
   logistics_provider_name varchar(100) NOT NULL DEFAULT '' COMMENT '物流商名称',
   express_type varchar(40) NOT NULL DEFAULT '' COMMENT '快递标识',
   first_weight decimal(10,2) DEFAULT '1.00' COMMENT '首重',
   create_time datetime comment '创建时间',
   create_by varchar(30) default '' comment '创建人',
   update_time datetime comment '修改时间',
   update_by varchar(30) comment '修改人',
   PRIMARY KEY (`id`),
   KEY `udx_logistics_provider` (`logistics_provider_no`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8 COMMENT '物流商表';




