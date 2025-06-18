
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_name` varchar(50) DEFAULT '' COMMENT '公司名称',
  `province_name` varchar(30) DEFAULT '' COMMENT '省份名称',
  `stock_names` varchar(100) DEFAULT '' COMMENT '股票名字,多个以,号分隔',
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
