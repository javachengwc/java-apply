create table t_resource_system (
  id bigint(20) AUTO_INCREMENT,
  name varchar(50) default '' comment '名称',
  note varchar(200) default '' comment '备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口系统表';

create table t_resource_category (
  id bigint AUTO_INCREMENT,
  name varchar(50) default '' comment '名称',
  parent_id bigint default 0 comment '父类目',
  note varchar(50) default '' comment '备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  primary key (`id`),
  key idx_parent(parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口类目表';

create table t_access_resource (
  id bigint(20) AUTO_INCREMENT,
  name varchar(50) default '' comment '接口名称',
  note varchar(200) default '' comment '备注',
  sys_id int comment '所属系统id',
  cate_id int comment '类目id',
  http_method varchar(20) default 'GET' comment '接口httpMethod,GET,POST,PUT,DELETE,HEAD,PATCH',
  content_type varchar(100) default '' comment '请求数据类型Content-Type',
  resource_link varchar(200) default '' comment '接口链接',
  req_demo varchar(1000) default '' comment '请求参数例子',
  resp_demo varchar(1000) default '' comment '响应结果例子',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访问接口表';

create table t_resource_param (
  id bigint(20) AUTO_INCREMENT,
  resource_id bigint comment '接口id',
  name varchar(100) default '' comment '参数名称',
  type varchar(50) default '' comment '参数类型',
  is_mast int(2) default 0 comment '是否必填 0--否,1--是',
  default_value varchar(1000) default '' comment '默认值',
  note varchar(200) default '' comment '参数备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_resource(resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口参数表';

create table t_resource_header (
  id bigint(20) AUTO_INCREMENT,
  resource_id bigint comment '接口id',
  name varchar(50) default '' comment 'header名称',
  default_value varchar(200) default '' comment '默认值',
  note varchar(200) default '' comment 'header备注',
  sort int default 1 comment '排序',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_resource(resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口请求头表';

create table t_resource_invoke (
  id bigint(20) AUTO_INCREMENT,
  resource_id bigint comment '接口id',
  resource_name varchar(50) default '' comment '接口名称',
  resource_link varchar(200) default '' comment '接口链接',
  http_method varchar(20) default 'GET' comment '接口httpMethod,GET,POST,PUT,DELETE,HEAD,PATCH',
  content_type varchar(100) default '' comment '请求数据类型Content-Type',
  req_header varchar(500) default '' comment '请求头',
  req_data  varchar(1000) default '' comment '请求数据',
  resp_code int default 0 comment '响应code',
  resp_data varchar(2000) default '' comment '响应数据',
  is_success int(2) default 1 comment '是否成功,0--否,1--是',
  error_message varchar(200) default '' comment '错误信息',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`),
  key idx_resource(resource_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口调用表';


