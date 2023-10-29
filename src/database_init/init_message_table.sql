USE `community`;
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `from_id` int DEFAULT NULL,
                           `to_id` int DEFAULT NULL,
                           `conversation_id` varchar(45) NOT NULL,
                           `content` text,
                           `status` int DEFAULT NULL COMMENT '0-Unread;1-Read;2-Delete;',
                           `create_time` timestamp NULL DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `index_from_id` (`from_id`),
                           KEY `index_to_id` (`to_id`),
                           KEY `index_conversation_id` (`conversation_id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;