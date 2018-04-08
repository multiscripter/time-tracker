insert into users (login, pass) values ('test_user', '4ac1b63dca561d274c6055ebf3ed97db');/* pass: test_pass */
/* Token is md5 from string: test_user2018-04-07 08:02:00 */
insert into tokens (user_id, token, wday) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07');
/*
select * from marks where token = '13fe018b338e5f605a7eb281ed3134dc' order by mark;
update marks set mark = '2018-04-07 18:05:00' where token = '13fe018b338e5f605a7eb281ed3134dc' and mark = '2018-04-07 17:05:00';
*/