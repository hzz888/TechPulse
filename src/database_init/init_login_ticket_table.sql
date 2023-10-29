USE `community`;
DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `user_id` INT(11) NOT NULL,
    `ticket` VARCHAR(45) NOT NULL,
    `status` INT(11) DEFAULT 0 NOT NULL COMMENT '0-valid, 1-invalid',
    `expired` TIMESTAMP NOT NULL,
    PRIMARY KEY(`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
    KEY `index_ticket` (`ticket` (20))
)ENGINE = INNODB DEFAULT CHARSET = utf8;