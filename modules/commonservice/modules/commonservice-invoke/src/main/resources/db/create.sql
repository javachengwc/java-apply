create table t_access_resource (
  id bigint(20) AUTO_INCREMENT,
  name varchar(50) DEFAULT '' COMMENT '接口名称',
  note varchar(200) DEFAULT '' COMMENT '备注',
  sys_id int COMMENT '所属系统id',
  cate_id int COMMENT '类目id',
  http_method varchar(20) DEFAULT 'GET' COMMENT '接口httpMethod,GET,POST,PUT,DELETE,HEAD,PATCH',
  content_type varchar(100) DEFAULT '' COMMENT '请求数据类型Content-Type',
  resource_link varchar(200) DEFAULT '' COMMENT '接口链接',
  header varchar(1000) DEFAULT '' COMMENT '请求头header,逗号分隔',
  req_demo varchar(1000) DEFAULT '' COMMENT '请求参数例子',
  resp_demo varchar(1000) DEFAULT '' COMMENT '响应结果例子',
  create_time datetime,
  modify_time datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访问接口资源表';

