truncate table users;
truncate table tokens;
truncate table marks;

insert into users (login, pass) values ('test_user', '4ac1b63dca561d274c6055ebf3ed97db');

insert into tokens (user_id, token, wday) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07');

insert into marks (user_id, token, wday, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07', '2018-04-07 08:02:00', true);
insert into marks (user_id, token, wday, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07', '2018-04-07 12:03:00', false);
insert into marks (user_id, token, wday, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07', '2018-04-07 12:58:00', true);
insert into marks (user_id, token, wday, mark, state) values (1, '13fe018b338e5f605a7eb281ed3134dc', '2018-04-07', '2018-04-07 17:05:00', false);