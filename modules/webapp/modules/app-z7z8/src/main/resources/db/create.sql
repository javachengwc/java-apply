create table monit_process
(
    id int AUTO_INCREMENT comment '自增长id',
    user_name varchar(50) comment '用户名',
    machine_name varchar(50) comment '电脑名',
    record_time TIMESTAMP  comment '记录时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '用户电脑表';

create table monit_process_item
(
    id int AUTO_INCREMENT comment '自增长id',
    process_id int  comment '用户电脑id',
    machine_name varchar(50) comment '电脑名',
    name varchar(50) comment '名称',
    title varchar(100) comment '标题',
    file_name varchar(100) comment '文件名',
    PRIMARY KEY (id),
    key process_id(process_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table monit_image
(
    id int AUTO_INCREMENT comment '自增长id',
    user_name varchar(50) comment '用户名',
    machine_name varchar(50) comment '电脑名',
    record_time TIMESTAMP  comment '记录时间',
    file_name varchar(50)  comment '文件名',
    path   varchar(100) comment '文件路径',
    PRIMARY KEY (id),
    key user_name(user_name),
    key machine_name(machine_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment '监控截图记录表';

