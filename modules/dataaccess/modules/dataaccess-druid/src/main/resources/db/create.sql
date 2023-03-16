create table t_entity
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '名称',
   crete_time DATETIME COMMENT '创建时间',
   update_time DATETIME COMMENT '修改时间',
   PRIMARY KEY (id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '实体表';

create table t_entity_ext
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   entity_id INT COMMENT '实体id',
   ext VARCHAR(200) DEFAULT '' comment '扩展信息',
   crete_time DATETIME COMMENT '创建时间',
   update_time DATETIME COMMENT '修改时间',
   PRIMARY KEY (id),
   KEY idx_entity(entity_id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '实体扩展表';

create table t_log
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   biz VARCHAR(20) DEFAULT '' COMMENT '业务模块',
   rela_key VARCHAR(50) DEFAULT '' COMMENT '关联值',
   content VARCHAR(200) DEFAULT '' COMMENT '内容',
   crete_time DATETIME COMMENT '创建时间',
   update_time DATETIME COMMENT '修改时间',
   PRIMARY KEY (id),
   KEY idx_rela(rela_key)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '日志表';
