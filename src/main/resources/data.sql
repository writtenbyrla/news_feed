INSERT INTO user(username, pwd, email, phone) VALUES ('wldms' , 'wldms123!', 'je@gmail.com', '010-1111-1111');
INSERT INTO user(username, pwd, email, phone) VALUES ('wlrma' , 'wlrma123!', 'ww@gmail.com', '010-9999-9999');
INSERT INTO user(username, pwd, email, phone) VALUES ('wlehd' , 'wlehd123!', 'jee@gmail.com', '010-1881-7777');


INSERT INTO post(content, user_id) VALUES('봄', 1);
INSERT INTO post(content, user_id) VALUES('여름', 2);
INSERT INTO post(content, user_id) VALUES('가을', 3);
INSERT INTO post(content, user_id) VALUES('겨울', 3);

INSERT INTO comment(content, user_id, post_id) VALUES('봄 좋아', 3, 1);
INSERT INTO comment(content, user_id, post_id) VALUES('여름 싫어', 3, 2);
INSERT INTO comment(content, user_id, post_id) VALUES('가을 좋아', 2, 3);
INSERT INTO comment(content, user_id, post_id) VALUES('겨울 추워', 1, 4);