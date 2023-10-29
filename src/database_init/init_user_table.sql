USE `community`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`(
                       `id` INT(11) NOT NULL AUTO_INCREMENT,
                       `username` VARCHAR(50) NOT NULL,
                       `password` VARCHAR(50) NOT NULL ,
                       `salt` VARCHAR(50) DEFAULT 'hashSalt',
                       `email` VARCHAR(100) NOT NULL,
                       `type` INT(11) DEFAULT 0 COMMENT '0: normal user; 1: admin; 2: moderator',
                       `status` INT(11) DEFAULT 0 COMMENT '0: unverified; 1: verified',
                       `activation_code` VARCHAR(100),
                       `header_url` VARCHAR(200) DEFAULT NULL,
                       `create_time` TIMESTAMP NULL DEFAULT NULL,
                       PRIMARY KEY (`id`),
                       KEY `index_username` (`username`(20)),
                       KEY `index_email` (`email`(20)),
                       CHECK ( type IN (0,1,2) ),
                       CHECK ( status IN (0,1) )
)ENGINE = INNODB DEFAULT CHARSET = utf8;