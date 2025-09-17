
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_name` varchar(50) DEFAULT '' COMMENT '公司名称',
  `province_name` varchar(30) DEFAULT '' COMMENT '省份名称',
  `stock_names` varchar(100) DEFAULT '' COMMENT '股票名字,多个以,号分隔',
   introduce varchar(2000) default '' comment '简介' ,
  `note` varchar(200) DEFAULT '' COMMENT '备注',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_company_name`(`company_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '公司表';

DROP TABLE IF EXISTS `t_company_stock`;
CREATE TABLE `t_company_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) comment '公司ID',
  `stock_name` varchar(30) DEFAULT '' COMMENT '股票名称',
  `stock_code` varchar(20) DEFAULT '' COMMENT '股票代码',
  `stock_market_code` varchar(20) DEFAULT '' COMMENT '股票交易所code',
  `care_value` int(11) DEFAULT 0 COMMENT '关注值,0-10',
  `industry` varchar(20) DEFAULT '' COMMENT '行业',
  `have_data` int(11) DEFAULT 0 COMMENT '是否有数据 0-否,1-是',
  `public_time` date COMMENT '上市时间',
  `tags` varchar(100) DEFAULT '' COMMENT '标签，逗号分隔',
  `record_day` varchar(30) DEFAULT '' COMMENT '股权登记日',
  `divvy_day` varchar(30) DEFAULT '' COMMENT '分红日',
  `create_time` datetime,
  `modify_time` datetime,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `udx_stock_code`(`stock_code`),
  INDEX `idx_stock_name`(`stock_name`),
  INDEX `idx_company`(`company_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '公司股票表';

DROP TABLE IF EXISTS `t_stock_year`;
CREATE TABLE `t_stock_year`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stock_name` varchar(30) DEFAULT '' COMMENT '股票名称',
  `stock_code` varchar(20) DEFAULT '' COMMENT '股票代码',
  `stat_year` int(11) COMMENT '年',
  `highlight` int(11) NULL DEFAULT 0 COMMENT '高光程度',
  `min_price` decimal(10, 2) COMMENT '最低股价',
  `max_price` decimal(10, 2) COMMENT '最高股价',
  `begin_price` decimal(10, 2) COMMENT '年初股价',
  `end_price` decimal(10, 2) COMMENT '年底股价',
  `market_value` bigint(20) COMMENT '市值(亿)',
  `pe` decimal(10, 2) COMMENT '市盈率',
  `gmv` decimal(20, 2) COMMENT '营收(亿)',
  `profit` decimal(20, 2) COMMENT '利润(亿)',
  `dividend` decimal(20, 2) COMMENT '分红(亿)',
  `note` varchar(100) DEFAULT '' COMMENT '备注',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_stock_code`(`stock_code`),
  INDEX `idx_stock_name_year`(`stock_name`, `stat_year`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '股票年表';

DROP TABLE IF EXISTS `t_stock_month`;
CREATE TABLE `t_stock_month` (
  `id` bigint(20) AUTO_INCREMENT,
  `hign_flag` int(11) DEFAULT '0' COMMENT '大涨大跌标识 1-大涨 -1-大跌',
  `stock_name` varchar(30) DEFAULT '' COMMENT '股票名称',
  `stock_code` varchar(20) DEFAULT '' COMMENT '股票代码',
  `month_date` date COMMENT '月期',
  `begin_price` decimal(10,2) COMMENT '当月开始股价',
  `end_price` decimal(10,2) COMMENT '当月结束股价',
  `min_price` decimal(10,2) COMMENT '最低价',
  `max_price` decimal(10,2) COMMENT '最高价',
  `increase_rate` decimal(10,2) COMMENT '涨幅',
  `note` varchar(100) DEFAULT '' COMMENT '备注',
  `turnover_rate` decimal(10,2) COMMENT '换手率',
  `turnover_amount` decimal(20,2) COMMENT '成交额(亿)',
  `change_rate` decimal(10,2) COMMENT '振幅',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  KEY `idx_stock_code` (`stock_code`),
  KEY `idx_stock_name` (`stock_name`),
  KEY `idx_month` (`month_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='股票每月表';

DROP TABLE IF EXISTS `t_stock_week`;
CREATE TABLE `t_stock_week`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hign_flag` int(11) NULL DEFAULT 0 COMMENT '大涨大跌标识 1-大涨 -1-大跌',
  `stock_name` varchar(30) DEFAULT '' COMMENT '股票名称',
  `stock_code` varchar(20) DEFAULT '' COMMENT '股票代码',
  `week_date` date COMMENT '周日期',
  `begin_price` decimal(10, 2) COMMENT '当周开始股价',
  `end_price` decimal(10, 2) COMMENT '当周结束股价',
  `min_price` decimal(10, 2) COMMENT '最低价',
  `max_price` decimal(10, 2) COMMENT '最高价',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '涨幅',
  `note` varchar(100) DEFAULT '' COMMENT '备注',
  `turnover_rate` varchar(10) DEFAULT '' COMMENT '换手率',
  `turnover_amount` decimal(20, 2) COMMENT '成交额(亿)',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_stock_code`(`stock_code`),
  INDEX `idx_stock_name`(`stock_name`),
  INDEX `idx_week`(`week_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '股票周表';

DROP TABLE IF EXISTS `t_stock_day`;
CREATE TABLE `t_stock_day`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `care_flag` int(11) COMMENT '关注标识 1-关注',
  `stock_name` varchar(30) DEFAULT '' COMMENT '股票名称',
  `stock_code` varchar(20) DEFAULT '' COMMENT '股票代码',
  `day_date` date COMMENT '日期',
  `begin_price` decimal(10, 2) COMMENT '当天开始股价',
  `end_price` decimal(10, 2) COMMENT '当天结束股价',
  `min_price` decimal(10, 2) COMMENT '最低价',
  `max_price` decimal(10, 2) COMMENT '最高价',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '涨幅',
  `note` varchar(500) DEFAULT '' COMMENT '备注',
  `turnover_rate` varchar(10) DEFAULT '' COMMENT '换手率',
  `turnover_amount` decimal(20, 2) COMMENT '成交额(亿)',
  `order_rate` varchar(10) DEFAULT '' COMMENT '委比,(委买手数-委卖手数)/(委买手数+委卖手数)×100%',
  `main_in_amount` decimal(20, 2) COMMENT '主力流入资金(亿)',
  `main_trans_amount` decimal(20, 2) COMMENT '主力净流入(亿)',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_stock_code`(`stock_code`),
  INDEX `idx_stock_name`(`stock_name`),
  INDEX `idx_day`(`day_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '股票每天表';

DROP TABLE IF EXISTS `t_fund`;
CREATE TABLE `t_fund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fund_name` varchar(30) DEFAULT '' COMMENT '基金名称',
  `fund_code` varchar(20) DEFAULT '' COMMENT '基金编码',
  `trade_mode` varchar(20) DEFAULT '' COMMENT '交易方式 T+0,T+1',
  `note` varchar(200) DEFAULT '' COMMENT '备注',
  `create_time` datetime,
  `modify_time` datetime,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `udx_fund_code`(`fund_code`),
  INDEX `idx_fund_name`(`fund_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '基金表';

DROP TABLE IF EXISTS `t_fund_month`;
CREATE TABLE `t_fund_month`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fund_name` varchar(30) DEFAULT '' COMMENT '基金名称',
  `fund_code` varchar(20) DEFAULT '' COMMENT '基金编码',
  `month_date` date COMMENT '月份',
  `begin_price` decimal(10, 3) COMMENT '开盘价',
  `end_price` decimal(10, 3) COMMENT '收盘价',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '涨幅',
  `note` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_month`(`month_date`),
  INDEX `idx_code_month`(`fund_code`, `month_date`),
  INDEX `idx_name_month`(`fund_name`, `month_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '基金每月数据表';

DROP TABLE IF EXISTS `t_fund_week`;
CREATE TABLE `t_fund_week`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fund_name` varchar(30) DEFAULT '' COMMENT '基金名称',
  `fund_code` varchar(20) DEFAULT '' COMMENT '基金编码',
  `week_date` date COMMENT '周期',
  `begin_price` decimal(10, 3) COMMENT '开盘价',
  `end_price` decimal(10, 3) COMMENT '收盘价',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '涨幅',
  `note` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_week`(`week_date`),
  INDEX `idx_code_week`(`fund_code`, `week_date`),
  INDEX `idx_name_week`(`fund_name`, `week_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '基金每周数据表';

DROP TABLE IF EXISTS `t_fund_day`;
CREATE TABLE `t_fund_day`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fund_name` varchar(30) DEFAULT '' COMMENT '基金名称',
  `fund_code` varchar(20) DEFAULT '' COMMENT '基金编码',
  `day_date` date COMMENT '日期',
  `begin_price` decimal(10, 3) COMMENT '开盘价',
  `end_price` decimal(10, 3) COMMENT '收盘价',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '涨幅',
  `note` varchar(500) DEFAULT '' COMMENT '备注',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_day`(`day_date`),
  INDEX `idx_code_date`(`fund_code`, `day_date`),
  INDEX `idx_name_date`(`fund_name`, `day_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '基金每天数据表';

DROP TABLE IF EXISTS `t_bourse_month`;
CREATE TABLE `t_bourse_month`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hign_flag` int(11) COMMENT '变幅大标识 -1-大跌 1-大涨',
  `bourse_name` varchar(30) DEFAULT '' COMMENT '证券交易所',
  `bourse_code` varchar(20) DEFAULT '' COMMENT '证券交易所编码',
  `month_date` date COMMENT '月期',
  `begin_point` decimal(10, 2) COMMENT '当月开始指数',
  `end_point` decimal(10, 2) COMMENT '当月结束指数',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '变幅百分比',
  `note` varchar(100) DEFAULT '' COMMENT '备注',
  `turnover_rate` varchar(10) DEFAULT '' COMMENT '换手率',
  `turnover_amount` decimal(20, 2) COMMENT '成交额(万亿)',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_month`(`month_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '证券交易所每月股票指数表';

DROP TABLE IF EXISTS `t_bourse_week`;
CREATE TABLE `t_bourse_week`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hign_flag` int(11) COMMENT '变幅大标识 -1-大跌 1-大涨',
  `bourse_name` varchar(30) DEFAULT '' COMMENT '证券交易所',
  `bourse_code` varchar(20) DEFAULT '' COMMENT '证券交易所编码',
  `week_date` date COMMENT '周期',
  `begin_point` decimal(10, 2) COMMENT '当周开始指数',
  `end_point` decimal(10, 2) COMMENT '当周结束指数',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '变幅百分比',
  `note` varchar(500) DEFAULT '' COMMENT '备注',
  `turnover_rate` varchar(10) DEFAULT '' COMMENT '换手率',
  `turnover_amount` decimal(20, 2) COMMENT '成交额(亿)',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_week`(`week_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '证券交易所每周股票指数表';

DROP TABLE IF EXISTS `t_bourse_day`;
CREATE TABLE `t_bourse_day`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hign_flag` int(11) COMMENT '变幅大标识 -1-大跌 1-大涨',
  `bourse_name` varchar(30) DEFAULT '' COMMENT '证券交易所',
  `bourse_code` varchar(20) DEFAULT '' COMMENT '证券交易所编码',
  `day_date` DATE COMMENT '日期',
  `begin_point` decimal(10, 2) COMMENT '当天开始指数',
  `end_point` decimal(10, 2) COMMENT '当天结束指数',
  `increase_rate` varchar(10) DEFAULT '' COMMENT '变幅百分比',
  `note` varchar(500) DEFAULT '' COMMENT '备注',
  `turnover_rate` varchar(10) DEFAULT '' COMMENT '换手率',
  `turnover_amount` decimal(20, 2) COMMENT '成交额(亿)',
  `create_time` datetime,
  PRIMARY KEY (`id`),
  INDEX `idx_day`(`day_date`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COMMENT = '证券交易所每天股票指数表';

