
create table sms (
   id bigint auto_increment,
   mobile varchar(11) default '' comment '手机号',
   content varchar(255) default '' comment '发送内容',
   captcha varchar(20) default '' comment '验证码',
   send_time datetime comment '发送时间',
   send_result int default 0 comment '发送结果 0--初始 1--成功 2--失败',
   rt_code varchar(64) default '' comment '短信发送接口返回code码',
   rt_message varchar(128) default '' comment '短信发送接口返回信息',
   biz_id varchar(64) default '' comment '短信发送接口返回的bizId,对方系统中短信记录的标识',
   type int comment '发送类型,1--验证码,2-文本',
   fail_count int default 0 comment '失败次数',
   verify_count int default 0 comment '验证次数',
   is_use int default 0 comment '是否使用 0--否 1--是',
   expire_time datetime comment '过期时间',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
  primary key (id),
  key idx_mobile_send(mobile,send_time)
) engine InnoDB default charset utf8 comment '短信记录';

create table sms_template (
   id bigint auto_increment,
   code varchar(64) default '' comment '模板code',
   content varchar(255) default '' comment '模板内容',
   type int comment '模板类型,1--验证码,2-文本',
   channel int default 0 comment '模板渠道，0--自身 1--ali',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   primary key (id),
   key idx_code(code)
) engine InnoDB default charset utf8 comment '短信模板表'
