CREATE DATABASE IF NOT EXISTS apa_site DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

use apa_site;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ld_raw_domain`
-- ----------------------------
DROP TABLE IF EXISTS `ld_raw_domain`;
CREATE TABLE `ld_raw_domain` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `domain_name` varchar(192) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册域名',
  `registration_date` datetime DEFAULT NULL,
  `is_sensitive` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '是否是敏感域名(0-不是 1-是)',
  `registrant_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人名',
  `registrant_email` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人Email',
  `registrant_phone` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人电话',
  `domain_registrar_id` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '域名提供商ID',
  `record_json` TEXT COMMENT '域名注册全部信息json',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='原始域名表';

DROP TABLE IF EXISTS `v_ld_raw_domain`;
CREATE TABLE `v_ld_raw_domain` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `domain_name` varchar(192) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册域名',
  `registration_date` datetime DEFAULT NULL,
  `is_sensitive` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '是否是敏感域名(0-不是 1-是)',
  `registrant_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人名',
  `registrant_email` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人Email',
  `registrant_phone` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人电话',
  `domain_registrar_id` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '域名提供商ID',
  `record_json` TEXT COMMENT '域名注册全部信息json',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MERGE UNION(ld_raw_domain,ld_raw_domain_201712) INSERT_METHOD=LAST COMMENT='原始域名表';
ALTER table v_ld_raw_domain ENGINE=MERGE UNION(ld_raw_domain,ld_raw_domain_201712,ld_raw_domain_201801,ld_raw_domain_201802,ld_raw_domain_201803,ld_raw_domain_201804) INSERT_METHOD=LAST COMMENT='虚拟原始域名表';
-- ----------------------------
--  Table structure for `ld_sensitive_domain`
-- ----------------------------
DROP TABLE IF EXISTS `ld_sensitive_domain`;
CREATE TABLE `ld_sensitive_domain` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `category` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '类别(0-机器学习 1-人工添加 2-原始域名添加)',
  `domain_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册域名',
  `sensitive_word_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '敏感词名称',
  `sensitive_word` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '敏感词',
  `lcs_length` int COLLATE utf8mb4_bin NOT NULL DEFAULT 0 COMMENT 'lcs长度',
  `registration_date` datetime DEFAULT NULL,
  `whois_reg_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册商名称 registrar',
  `whois_reg_email` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人Email registrant',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='敏感域名表';


-- ----------------------------
--  Table structure for `sd_suspect_domain`
-- ----------------------------
DROP TABLE IF EXISTS `sd_suspect_domain`;
CREATE TABLE `sd_suspect_domain` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `domain_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册域名',
  `suspect_match` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '识别种类',
  `suspect_match_obj_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '嫌疑域名识别ID',
  `suspect_match_obj_text` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '嫌疑域名识别规则',
  `raw_domain_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '原始域名记录ID',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='域名匹配嫌疑域名表';

-- ----------------------------
--  Table structure for `su_clue_url`
-- ----------------------------
DROP TABLE IF EXISTS `su_clue_url`;
CREATE TABLE `su_clue_url` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '线索域名',
  `progress` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '当前进展(0:初始 1:访问失败 2:访问成功 3:复查开始)',
  `page_outlink_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '外链数量',
  `page_outsitelink_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '非本站外链数量',
  `page_trustlink_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '可信外链数量',
  `page_image_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '图片数量',
  `page_should_visit_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '抓取外链数量',
  `page_visit_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '抓取成功HTML数量',
  `page_visit_size` int(11) DEFAULT NULL DEFAULT 0 COMMENT '抓取成功HTML大小',
  `nohtml_visit_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '抓取成功非HTML数量',
  `nohtml_visit_size` int(11) DEFAULT NULL DEFAULT 0 COMMENT '抓取成功非HTML大小',
  `redirect_num` int(11) DEFAULT NULL DEFAULT 0 COMMENT '重定向发生次数',
  `raw_domain_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '原始域名记录ID',
  `suspect_domain_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '嫌疑域名ID',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='线索URL表';


-- ----------------------------
--  Table structure for `su_suspect_url`
-- ----------------------------
DROP TABLE IF EXISTS `su_suspect_url`;
CREATE TABLE `su_suspect_url` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '线索域名',
  `verify` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '核实状态(0:未确认 1:确实是 2:确认否 3:重新分析)',
  `tag_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标签ID',
  `tag_rule` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '匹配标签规则',
  `tag_rule_score` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '匹配标签规则评分',
  `raw_domain_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '原始域名记录ID',
  `suspect_domain_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '嫌疑域名ID',
  `clue_url_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '线索URLID',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='嫌疑URL表';

-- ----------------------------
--  Table structure for `pu_inspect_url`
-- ----------------------------
DROP TABLE IF EXISTS `pu_inspect_url`;
CREATE TABLE `pu_inspect_url` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `category` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '类别(0-爬虫确认 1-人工添加)',
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检URL',
  `ip_address` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT 'IP地址',
  `inspect_level` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '巡检级别(0:停止巡检 1:例行巡检)',
  `inspect_keyword` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字',
  `inspect_keyword_score` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字评分',
  `inspect_status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '在线状态(0:未检查 1:在线 2:内容改变 3:离线)',
  `inspect_message` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '巡检信息',
  `inspect_time` datetime DEFAULT NULL,
  `inspect_next_time` datetime DEFAULT NULL,
  `inspect_times` int(11) DEFAULT NULL DEFAULT 0 COMMENT '检查次数',
  `active_duration` int(11) DEFAULT NULL DEFAULT 0 COMMENT '在线持续时间（单位：秒）',
  `brand_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '品牌ID',
  `incident_no` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '工单号',
  `whois_reg_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册商名称 registrar',
  `whois_reg_email` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人Email registrant',
  `whois_webhost` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '托管主机',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='巡检钓鱼URL表';

-- ----------------------------
--  Table structure for `pu_inspect_event`
-- ----------------------------
DROP TABLE IF EXISTS `pu_inspect_event`;
CREATE TABLE `pu_inspect_event` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `category` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '类别(0-状态切换 1-内容变化 2-配置修改)',
  `url_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '巡检URLID',
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检URL',
  `keyword_from` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字',
  `keyword_to` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字',
  `keyword_score_from` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字评分',
  `keyword_score_to` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字评分',
  `message_from` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '巡检信息',
  `message_to` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '巡检信息',
  `status_from` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '在线状态(0:未检查 1:在线 2:内容改变 3:离线)',
  `status_to` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '在线状态(0:未检查 1:在线 2:内容改变 3:离线)',
  `time_from` datetime DEFAULT NULL,
  `time_to` datetime DEFAULT NULL,
  `progress` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '事件状态(0:未处理 1:已处理 2:无需处理)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='巡检钓鱼URL事件表';


-- ----------------------------
--  Table structure for `pu_inspect_trace`
-- ----------------------------
DROP TABLE IF EXISTS `pu_inspect_trace`;
CREATE TABLE `pu_inspect_trace` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `url_id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '巡检URLID',
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检URL',
  `ip_address` varchar(256) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'IP地址',
  `inspect_level` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '巡检级别(0:停止巡检 1:例行巡检)',
  `inspect_keyword` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字',
  `inspect_keyword_score` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '巡检内容关键字评分',
  `inspect_message` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '巡检信息',
  `inspect_status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '在线状态(0:未检查 1:在线 2:内容改变 3:离线)',
  `inspect_time` datetime DEFAULT NULL,
  `inspect_next_time` datetime DEFAULT NULL,
  `inspect_times` int(11) DEFAULT NULL DEFAULT 0 COMMENT '检查次数',
  `active_duration` int(11) DEFAULT NULL DEFAULT 0 COMMENT '在线持续时间（单位：秒）',
  `http_status_code` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'http返回码',
  `http_headers` LONGTEXT COMMENT 'http headers',
  `html` LONGTEXT COMMENT 'HTML内容',
  `text` LONGTEXT COMMENT 'TEXT内容',
  `text_tokens` LONGTEXT COMMENT 'TEXT切词的结果',
  `text_other` LONGTEXT COMMENT 'TEXT之外的内容',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='巡检日志表';
