CREATE TABLE t_cache (
   id bigint AUTO_INCREMENT,
   cache_name varchar(255) NOT NULL COMMENT '缓存名称',
   sql_text varchar(2000) NOT NULL COMMENT 'sql 标准模式为 select t.a as key ,t.b as value from where condition',
   sys varchar(50) DEFAULT '' COMMENT '系统',
	flag int(2) DEFAULT 1 COMMENT '是否启用,1--启用，0--不启用',
   remark varchar(255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (id),
  UNIQUE KEY udx_cache(cache_name)
) ENGINE InnoDB DEFAULT CHARSET UTF8 COMMENT '缓存表';

CREATE TABLE t_data (
   id bigint AUTO_INCREMENT,
   type INT COMMENT '分类',
   data VARCHAR(2000) DEFAULT '' COMMENT '数据',
   fmt VARCHAR(50) DEFAULT 'text' COMMENT '格式: json/xml/text',
   remark varchar(255) DEFAULT '' COMMENT '备注',
   create_time DATETIME COMMENT '创建时间',
   create_by VARCHAR(50) DEFAULT '' COMMENT '创建人',
   update_time DATETIME COMMENT '修改时间',
   update_by VARCHAR(50) DEFAULT '' COMMENT '修改人',
  PRIMARY KEY (id),
  KEY idx_create_time(create_time)
) ENGINE InnoDB DEFAULT CHARSET UTF8 COMMENT '数据表';

CREATE TABLE `cachetable` (
  `cacheName` varchar(255) NOT NULL COMMENT '缓存名称',
  `sqlText` varchar(2000) NOT NULL COMMENT 'sql 标准模式为 select t.a as key ,t.b as value from where condition',
  `className` varchar(255) DEFAULT '' COMMENT '类名',
  `system` varchar(20) NOT NULL DEFAULT '' COMMENT '系统' ,
  `flag` int(1) DEFAULT 1 COMMENT '是否启用,1--启用，0--不启用',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`cacheName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT '缓存表2';
