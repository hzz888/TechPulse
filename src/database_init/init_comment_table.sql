USE `community`;
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`(
                          `id` INT(11) NOT NULL AUTO_INCREMENT,
                          `user_id` INT(11) NOT NULL ,
                          `entity_type` INT(11) DEFAULT 1,
                          `entity_id` INT(11),
                          `target_id` INT(11) DEFAULT 0,
                          `content` TEXT,
                          `status` INT(11) DEFAULT 0,
                          `create_time` TIMESTAMP DEFAULT NULL,
                          PRIMARY KEY(`id`),
                          FOREIGN KEY `fk_user_id`(`user_id`) REFERENCES `user`(`id`),
                          KEY `index_user_id`(`user_id`),
                          CHECK ( STATUS IN (0, 1, 2) )
)ENGINE = INNODB DEFAULT CHARSET = utf8