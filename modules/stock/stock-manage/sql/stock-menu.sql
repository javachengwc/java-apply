
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (5, '股票管理', 0, 5, 'stk', NULL, '', '', 1, 0, 'M', '0', '0', '', '', 'admin', now(), 'admin', now(), '股票目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (6, '股票数据', 0, 6, 'sdata', NULL, '', '', 1, 0, 'M', '0', '0', '', '', 'admin', now(), 'admin', now(), '股票数据目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (7, '基金管理', 0, 7, 'fund', NULL, '', '', 1, 0, 'M', '0', '0', '', '', 'admin', now(), 'admin', now(), '基金目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (8, '基金数据', 0, 8, 'fdata', NULL, '', '', 1, 0, 'M', '0', '0', '', '', 'admin', now(), 'admin', now(), '基金数据目录');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (9, '大盘指数', 0, 9, 'disk', NULL, '', '', 1, 0, 'M', '0', '0', '', '', 'admin', now(), 'admin', now(), '大盘指数');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司管理', '5', '1', 'company', 'stock/company/index', 1, 0, 'C', '0', '0', 'stock:company:list', '#', 'admin', sysdate(), '', null, '公司菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:company:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:company:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:company:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:company:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:company:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司股票', '5', '1', 'stock', 'stock/stock/index', 1, 0, 'C', '0', '0', 'stock:stock:list', '#', 'admin', sysdate(), '', null, '公司股票菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司股票查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:stock:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司股票新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:stock:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司股票修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:stock:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司股票删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:stock:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('公司股票导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:stock:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票年数据', '6', '1', 'stockyear', 'stock/stockyear/index', 1, 0, 'C', '0', '0', 'stock:stockyear:list', '#', 'admin', sysdate(), '', null, '股票年数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票年数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockyear:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票年数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockyear:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票年数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockyear:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票年数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockyear:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票年数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockyear:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票月数据', '6', '1', 'stockmonth', 'stock/stockmonth/index', 1, 0, 'C', '0', '0', 'stock:stockmonth:list', '#', 'admin', sysdate(), '', null, '股票月数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票月数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockmonth:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票月数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockmonth:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票月数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockmonth:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票月数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockmonth:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票月数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockmonth:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票周数据', '6', '1', 'stockweek', 'stock/stockweek/index', 1, 0, 'C', '0', '0', 'stock:stockweek:list', '#', 'admin', sysdate(), '', null, '股票周数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票周数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockweek:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票周数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockweek:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票周数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockweek:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票周数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockweek:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票周数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockweek:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票天数据', '6', '1', 'stockday', 'stock/stockday/index', 1, 0, 'C', '0', '0', 'stock:stockday:list', '#', 'admin', sysdate(), '', null, '股票天数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票天数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockday:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票天数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockday:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票天数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockday:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票天数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockday:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('股票天数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:stockday:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金列表', '7', '1', 'fund', 'stock/fund/index', 1, 0, 'C', '0', '0', 'stock:fund:list', '#', 'admin', sysdate(), '', null, '基金菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:fund:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:fund:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:fund:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:fund:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:fund:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金月数据', '8', '1', 'fundmonth', 'stock/fundmonth/index', 1, 0, 'C', '0', '0', 'stock:fundmonth:list', '#', 'admin', sysdate(), '', null, '基金月数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金月数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundmonth:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金月数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundmonth:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金月数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundmonth:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金月数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundmonth:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金月数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundmonth:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金周数据', '8', '1', 'fundweek', 'stock/fundweek/index', 1, 0, 'C', '0', '0', 'stock:fundweek:list', '#', 'admin', sysdate(), '', null, '基金周数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金周数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundweek:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金周数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundweek:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金周数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundweek:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金周数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundweek:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金周数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundweek:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金天数据', '8', '1', 'fundday', 'stock/fundday/index', 1, 0, 'C', '0', '0', 'stock:fundday:list', '#', 'admin', sysdate(), '', null, '基金天数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金天数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundday:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金天数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundday:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金天数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundday:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金天数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundday:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('基金天数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:fundday:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数月数据', '9', '1', 'boursemonth', 'stock/boursemonth/index', 1, 0, 'C', '0', '0', 'stock:boursemonth:list', '#', 'admin', sysdate(), '', null, '证券指数月数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数月数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:boursemonth:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数月数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:boursemonth:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数月数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:boursemonth:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数月数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:boursemonth:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数月数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:boursemonth:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数周数据', '9', '1', 'bourseweek', 'stock/bourseweek/index', 1, 0, 'C', '0', '0', 'stock:bourseweek:list', '#', 'admin', sysdate(), '', null, '证券指数周数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数周数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseweek:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数周数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseweek:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数周数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseweek:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数周数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseweek:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数周数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseweek:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数天数据', '9', '1', 'bourseday', 'stock/bourseday/index', 1, 0, 'C', '0', '0', 'stock:bourseday:list', '#', 'admin', sysdate(), '', null, '证券指数天数据菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数天数据查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseday:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数天数据新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseday:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数天数据修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseday:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数天数据删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseday:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('证券指数天数据导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'stock:bourseday:export',       '#', 'admin', sysdate(), '', null, '');

