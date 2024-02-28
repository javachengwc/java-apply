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

