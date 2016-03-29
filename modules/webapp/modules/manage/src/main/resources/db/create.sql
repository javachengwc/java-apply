--创建数据库-- 
create database db_admin;
--创建模块表--
CREATE TABLE admin_module (
  id int AUTO_INCREMENT,  
  name varchar(100),  
  url varchar(200),  
  parentid int,  
  PRIMARY key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--创建角色表---
CREATE TABLE admin_role (
  id int AUTO_INCREMENT,  
  name varchar(100),  
  introduct varchar(300), 
  PRIMARY key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--创建资源表---
CREATE TABLE admin_resource (
  id int AUTO_INCREMENT,  
  name varchar(100),  
  introduct varchar(300),
  path varchar(300),
  moduleid int,
  matchmdid int,
  PRIMARY key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--创建账号表---
CREATE TABLE admin_adminor (
  id int AUTO_INCREMENT,  
  name varchar(100),  
  nickname varchar(100),
  password varchar(200), 
  type int,  
  LAST_LOGIN_TIME date,  
  LOGIN_TIME date,
  PRIMARY key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--创建账号角色关联表--
CREATE TABLE ADMIN_USER_ROLE (
 USER_ID int, 
 ROLE_ID int
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--创建角色资源关联表---
CREATE TABLE ADMIN_ROLE_RESOURCE (
 ROLE_ID int, 
 RESOURCE_ID INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--基本模块数据录入脚本--
insert into admin_module(id,name,url) values(1,"公告管理",null);
insert into admin_module(id,name,url) values(2,"物资管理",null);
insert into admin_module(id,name,url) values(3,"用户管理",null);
insert into admin_module(id,name,url) values(4,"公会管理",null);
insert into admin_module(id,name,url) values(5,"游戏日志",null);

insert into admin_module(id,name,url) values(6,"后台账号管理",null);
insert into admin_module(id,name,url) values(7,"后台权限管理",null);
insert into admin_module(id,name,url) values(8,"后台操作日志",null);

insert into admin_module values(9,"冲值日志","/user/user.action",5);
insert into admin_module values(10,"商城消费日志","/user/user.action",5);
insert into admin_module values(11,"登陆日志","/user/user.action",5);
insert into admin_module values(12,"主公日志","/user/user.action",5);
insert into admin_module values(13,"物品日志","/user/user.action",5);
insert into admin_module values(14,"金钱日志","/user/user.action",5);
insert into admin_module values(15,"科技日志","/user/user.action",5);
insert into admin_module values(16,"副本日志","/user/user.action",5);
insert into admin_module values(17,"任务日志","/user/user.action",5);

insert into admin_module values(18,"公告管理","/user/user.action",1);
insert into admin_module values(19,"公告发布","/user/user.action",1);

insert into admin_module values(20,"物品管理","/user/user.action",2);
insert into admin_module values(21,"产品管理","/user/user.action",2);

insert into admin_module values(22,"用户管理","/user/user.action",3);
insert into admin_module values(23,"用户物品","/user/user.action",3);
insert into admin_module values(24,"封号管理","/user/user.action",3);
insert into admin_module values(25,"主公管理","/user/user.action",3);
insert into admin_module values(26,"科技树管理","/user/user.action",3);
insert into admin_module values(27,"好友管理","/user/user.action",3);
insert into admin_module values(28,"任务管理","/user/user.action",3);

insert into admin_module values(29,"公会信息","/user/user.action",4);
insert into admin_module values(30,"公会成员","/user/user.action",4);

insert into admin_module values(31,"账号管理","/user/user.action",6);

insert into admin_module values(32,"权限管理","/user/user.action",7);
insert into admin_module values(33,"资源管理","/user/user.action",7);
insert into admin_module values(35,"模块管理","/rbac/module.action",7);

insert into admin_module values(34,"操作日志","/user/user.action",8);

---2012.10.29--
alter table admin_resource change path value varchar(200);

create table admin_resource_permission
( id int  AUTO_INCREMENT,
  roleid int,  
  resourceId int,  
  flag int,
  primary key(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into admin_role values(1,'物资管理员','负责物资的录入');
insert into admin_role values(2,'游戏币管理员','负责游戏币的统计');
insert into admin_role values(3,'公告管理员','负责发布公告');
insert into admin_role values(4,'用户管理员','负责用户相关管理');
insert into admin_role values(5,'X1管理员','X1管理员');
insert into admin_role values(6,'X2管理员','X2管理员');
insert into admin_role values(7,'X3管理员','X3管理员');
insert into admin_role values(8,'X4管理员','X4管理员');
insert into admin_role values(9,'X5管理员','X5管理员');
insert into admin_role values(10,'X6管理员','X6管理员');
insert into admin_role values(11,'X7管理员','X7管理员');

insert into admin_resource(id,value,introduct) values(1,'com.manage.rbac.resource','资源管理');
insert into admin_resource(id,value,introduct) values(2,'com.manage.rbac.role','角色管理');
insert into admin_resource(id,value,introduct) values(3,'com.manage.rbac.adminor','管理员列表');

update admin_module set url="/rbac/resource.action" where name="资源管理";
update admin_module set url="/rbac/role.action",name="角色管理" where id=32;
update admin_module set url="/rbac/adminor.action" where name="账号管理";

----2012.11.02---
update admin_module set name='用户列表',url='/main/player.action' where id=22;
update admin_module set url='/main/material-ext.action' where id=20;

----2012.11.03---
delete from admin_module where id =21;
update admin_module set url='/main/user-material.action' where id=23;

update admin_module set name='用户武将',url='/main/user-captain.action' where name='封号管理';
----2012.11.08---
update  admin_module set name='用户金钱',url='/main/player.action?viewtype=1' where name='主公管理';

---2012.11.19---

delete from admin_module where name ='模块管理';
update admin_module set url='/rbac/resource!main.action' where name ='资源管理';

---2012.11.20---
alter table admin_module add sort int;

---2012.11.21---
alter table admin_adminor add CREATE_TIME date;
alter table admin_adminor modify create_time timestamp null default null ;
alter table admin_adminor modify LAST_LOGIN_TIME timestamp null default null ;
alter table admin_adminor modify LOGIN_TIME timestamp null default null;

----2012.11.26---
insert into admin_resource values(13,null,'模块管理','com.manage.rbac.module',33,null);

insert into admin_adminor(id,name,nickname,password,type,create_time) values(1,'admin','admin','96e79218965eb72c92a549dd5a330112',1,now());






