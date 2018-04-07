truncate table users;
truncate table tokens;
truncate table marks;

insert into users (login, pass) values ('test_user', '4ac1b63dca561d274c6055ebf3ed97db');/* pass: test_pass */
/* Token is md5 from string: test_user2018-04-07 08:02:00 */
insert into tokens (user_id, token) values (1, '13fe018b338e5f605a7eb281ed3134dc');

insert into marks (user_id, token, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', true);
insert into marks (user_id, token, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', false);
insert into marks (user_id, token, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', true);
insert into marks (user_id, token, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', false);
/*
insert into marks (user_id, token, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07 08:02:00', true);
insert into marks (user_id, token, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07 12:03:00', false);
insert into marks (user_id, token, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07 12:59:00', true);
insert into marks (user_id, token, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07 17:05:00', false);

select * from marks where token = '13fe018b338e5f605a7eb281ed3134dc' order by mark;
update marks set mark = current_timestamp where token = '13fe018b338e5f605a7eb281ed3134dc' and mark = '2018-04-07 17:05:00';
*/