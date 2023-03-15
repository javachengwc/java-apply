create table t_entity
(
   id  int not null AUTO_INCREMENT comment '自增长id',
   name varchar(50) comment '名称',
   PRIMARY KEY (id)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '实体表';