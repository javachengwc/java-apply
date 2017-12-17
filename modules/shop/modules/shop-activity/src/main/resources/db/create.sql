create table user_daily
(
	 id  bigint AUTO_INCREMENT comment '自增长id',
   user_id bigint comment '用户id',
   mobile varchar(11) comment '手机号',
   score int default 0 comment '积分',
   day_date date comment '日期',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key user_day (user_id,day_date),
   key mobile(mobile),
   key day_mobile(day_date,mobile)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户每天数据表';
