create table course
(
   id  bigint AUTO_INCREMENT comment '自增长id',
   course_item_id bigint comment '科目id',
   course_name varchar(20) comment '课程名称',
   teacher_id bigint comment '老师id',
   teacher_name varchar(20) comment '老师名称',
   statu int default 0 comment '状态 0--正常 1--停课 2--结束',
   begin_time datetime comment '开始时间',
   end_time datetime comment '结束时间',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key idx_course_item(course_item_id),
   key idx_course_name(course_name),
   key idx_teacher(teacher_id),
   key idx_begin_end(begin_time,end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '课程表';

create table user_course
(
   id  bigint AUTO_INCREMENT comment '自增长id',
   user_id bigint comment '用户id',
   user_name varchar(20) comment '用户名称',
   course_id bigint comment '课程id',
   course_name varchar(20) comment '课程名称',
   course_item_id bigint comment '科目id',
   create_time datetime comment '创建时间',
   modified_time datetime comment '修改时间',
   PRIMARY KEY (id),
   key idx_user(user_id),
   key idx_course_item(course_item_id),
   key idx_course(course_id,course_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户课程表';

