USE newnewsfeed;

-- DROP TABLE 
DROP TABLE COMMENTLIKE;
DROP TABLE POSTLIKE;
DROP TABLE COMMENT;
DROP TABLE POST;
DROP TABLE FOLLOW;
DROP TABLE PWD_HISTORY;
DROP TABLE USER;



-- CREATE TABLE(PK, UNIQUE)
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `pwd` varchar(255) not null,
  `username` varchar(255) not null,
  `email` varchar(255) not null,
  `phone` varchar(255),
  `description` varchar(255) DEFAULT '자신을 한 줄에 담아 표현해보세요!',
  `profile_img` varchar(255),
  `role` varchar(255) DEFAULT 'user',
`created_at` datetime DEFAULT CURRENT_TIMESTAMP,
`updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(10) DEFAULT 'Y',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
);

CREATE TABLE `post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255),
  `multimedia` varchar(255),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` bigint,
  PRIMARY KEY (`post_id`)
);

CREATE TABLE `comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` bigint,
  `post_id` bigint,
  PRIMARY KEY (`comment_id`)
) ;

CREATE TABLE `postlike` (
  `postlike_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint,
  `post_id` bigint,
  PRIMARY KEY (`postlike_id`)
);

CREATE TABLE `commentlike` (
  `commentlike_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint,
  `comment_id` bigint,
  PRIMARY KEY (`commentlike_id`)
);

CREATE TABLE `follow` (
  `follow_id` bigint NOT NULL AUTO_INCREMENT,
  `following_id` bigint,
  `follower_id` bigint,
    `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`follow_id`)
);

CREATE TABLE `auth_history` (
  `history_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint,
  `old_pwd` varchar(255),
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`history_id`)
);

-- FOREIGN KEY(FK)
ALTER TABLE `post` ADD CONSTRAINT `post_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`);
ALTER TABLE `comment` ADD CONSTRAINT `comment_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`);
ALTER TABLE `comment` ADD CONSTRAINT `comment_post_fk` FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`) ON DELETE CASCADE;
ALTER TABLE `postlike` ADD CONSTRAINT `postlike_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`);
ALTER TABLE `postlike` ADD CONSTRAINT `postlike_post_fk` FOREIGN KEY (`post_id`) REFERENCES `post`(`post_id`) ON DELETE CASCADE;
ALTER TABLE `commentlike` ADD CONSTRAINT `commentlike_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`);
ALTER TABLE `commentlike` ADD CONSTRAINT `commentlike_comment_fk` FOREIGN KEY (`comment_id`) REFERENCES `comment`(`comment_id`) ON DELETE CASCADE;
ALTER TABLE `auth_history` ADD CONSTRAINT `auth_history_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`);
ALTER TABLE `follow` ADD CONSTRAINT `following_user_fk` FOREIGN KEY (`following_id`) REFERENCES `user`(`user_id`);
ALTER TABLE `follow` ADD CONSTRAINT `follower_user_fk` FOREIGN KEY (`follower_id`) REFERENCES `user`(`user_id`);
-- CREATE UNIQUE INDEX unique_postlike ON postlike (user_id, post_id);
-- CREATE UNIQUE INDEX unique_commentlike ON commentlike (user_id, comment_id);

-- SELECT
SELECT * FROM comment;
SELECT * FROM commentlike;
select * from auth_history;
select * from follow;
select * from post;
select * from postlike;
select * from user;
select * from multimedia;

use newnewsfeed;