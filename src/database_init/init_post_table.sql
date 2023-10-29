USE `community`;
DROP TABLE IF EXISTS `discuss_post`;
CREATE TABLE `discuss_post`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `user_id` INT(11) NOT NULL ,
    `title` VARCHAR(100) DEFAULT 'title',
    `content` TEXT,
    `type` INT(11) DEFAULT 0 COMMENT '0-normal, 1-top',
    `status` INT(11) DEFAULT 0 COMMENT '0-normal, 1-good, 2-banned',
    `create_time` TIMESTAMP DEFAULT NULL,
    `comment_count` INT(11) DEFAULT 0,
    `score` DOUBLE DEFAULT 0,
    PRIMARY KEY(`id`),
    FOREIGN KEY `fk_user_id`(`user_id`) REFERENCES `user`(`id`),
    KEY `index_user_id`(`user_id`),
    CHECK ( status IN (0, 1, 2) ),
    CHECK ( type IN (0, 1) )
)ENGINE = INNODB DEFAULT CHARSET = utf8