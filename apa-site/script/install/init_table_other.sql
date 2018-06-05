CREATE DATABASE IF NOT EXISTS apa_site DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

use apa_site;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
--  Table structure for `phishtank_valid_url`
-- ----------------------------
DROP TABLE IF EXISTS `phishtank_valid_url`;
CREATE TABLE `phishtank_valid_url` (
  `id` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '标示',
  `phish_id` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank id',
  `url` varchar(8192) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'URL',
  `phish_detail_url` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank 存储的URL',
  `submission_time` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank 提交时间',
  `verified` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank 是否已确认',
  `verification_time` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank 确认时间',
  `online` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank 是否在线',
  `target` varchar(512) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'phishtank 钓鱼仿冒对象',
  `record_json` TEXT COMMENT '全部信息json',
  `deleted` char(1) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '删除标记(0-正常 1-删除)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='phishtank钓鱼网站确认表';

