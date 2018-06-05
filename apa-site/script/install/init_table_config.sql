CREATE DATABASE IF NOT EXISTS apa_site DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

use apa_site;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `config_domain_sensitive_word`
-- ----------------------------
DROP TABLE IF EXISTS `config_domain_sensitive_word`;
CREATE TABLE `config_domain_sensitive_word` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '名称',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `word` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '敏感词内容',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '启用标记(0-启用 1-停用)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='域名敏感词表';


-- ----------------------------
--  Table structure for `config_domain_regular`
-- ----------------------------
DROP TABLE IF EXISTS `config_domain_regular`;
CREATE TABLE `config_domain_regular` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '表达式名称',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '表达式描述',
  `expression` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '表达式内容',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '启用标记(0-启用 1-停用)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='域名正则表达式表';

-- ----------------------------
--  Table structure for `config_threat_actor`
-- ----------------------------
DROP TABLE IF EXISTS `config_threat_actor`;
CREATE TABLE `config_threat_actor` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `ip_address` varchar(256) COLLATE utf8mb4_bin NOT NULL COMMENT 'IP地址',
  `registrant_email` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '注册人Email',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '启用标记(0-启用 1-停用)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='TA黑名单表';

-- ----------------------------
--  Table structure for `config_clue_url_template`
-- ----------------------------
DROP TABLE IF EXISTS `config_clue_url_template`;
CREATE TABLE `config_clue_url_template` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `content` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '内容',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='URL构造模板表';

-- ----------------------------
--  Table structure for `config_trust_domain`
-- ----------------------------
DROP TABLE IF EXISTS `config_trust_domain`;
CREATE TABLE `config_trust_domain` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '可信域名名称',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `domain_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '域名',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '启用标记(0-启用 1-停用)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='合法可信域名表';

-- ----------------------------
--  Table structure for `config_poison_domain`
-- ----------------------------
DROP TABLE IF EXISTS `config_poison_domain`;
CREATE TABLE `config_poison_domain` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '毒丸域名名称',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `domain_name` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '域名',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '启用标记(0-启用 1-停用)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='毒丸域名表';


-- ----------------------------
--  Table structure for `config_tag`
-- ----------------------------
DROP TABLE IF EXISTS `config_tag`;
CREATE TABLE `config_tag` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `name` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标签名',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `status` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '启用标记(0-启用 1-停用)',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='标签Tag表';

-- ----------------------------
--  Table structure for `config_tag_rule`
-- ----------------------------
DROP TABLE IF EXISTS `config_tag_rule`;
CREATE TABLE `config_tag_rule` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `tag_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '标签名',
  `rule` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '规则',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '规则描述',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='标签Tag规则表';


-- ----------------------------
--  Table structure for `config_brand`
-- ----------------------------
DROP TABLE IF EXISTS `config_brand`;
CREATE TABLE `config_brand` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `name` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '品牌名称',
  `category` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '是否是客户(0-是 1-非 2-潜在客户)',
  `description` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '描述',
  `creator_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '添加人ID',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='客户品牌表';

