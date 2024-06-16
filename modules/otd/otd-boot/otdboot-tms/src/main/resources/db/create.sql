create table tms_delivery_plan (
  id varchar(36) NOT NULL,
  delivery_no varchar(20) default '' comment '发货单号',
  delivery_batch_number varchar(60) default '' comment '发货批次号',
  warehouse_code varchar(36) default '' comment '仓库code',
  dest_province varchar(10) default '' comment '目的地-省',
  dest_city varchar(10) default '' comment '目的地-市',
  dest_county  varchar(10) default '' comment '目的地-区县',
  route_id varchar(36) default '' comment '线路id',
  logistics_provider_code varchar(10) default '' comment '物流商code',
  logistics_type int default '1' comment '发货方式;1-快递,2-干线,3-自提',
  pick_mode int default 1 comment '拣货方式;1-按单拣货,2-货匹配单',
  tran_type int default 1 comment '运输方式;1-公路运输',
  print_flag int(2) default 0 comment '是否打印装箱单;0-否,1-是',
  print_time datetime comment '打印时间',
  turn_wms_flag int(2) default 0 comment '是否抛送wms;0-否,1-是',
  turn_wms_time datetime comment '抛送wms时间',
  create_time datetime comment '创建时间',
  create_by varchar(30) default '' comment '创建人',
  update_time datetime comment '修改时间',
  update_by varchar(30) comment '修改人',
  PRIMARY KEY (id),
  KEY idx_create_time (create_time),
  KEY idx_delivery_no (delivery_no),
  KEY idx_delivery_batch_number (delivery_batch_number),
  key idx_warehouse_dest(warehouse_code,dest_province,dest_city,dest_county)
)  ENGINE=InnoDB DEFAULT CHARSET=UTF8 comment '配送计划表';
