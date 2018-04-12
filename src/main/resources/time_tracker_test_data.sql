truncate table users;
truncate table tokens;
truncate table marks;

insert into users (login, pass, gmt) values ('test_user', '4ac1b63dca561d274c6055ebf3ed97db', '+3');
insert into users (login, pass, gmt) values ('test_user2', 'a0f88cc778f5b4582da52b18d76ed5d8', '-2');
insert into users (login, pass, gmt) values ('test_user3', '0d275885fea21ff4263bcba2fb75fffe', '+0');

insert into tokens (user_id, token, wday) values (2, 'test_token_user_2', '2018-04-12');

insert into marks (user_id, wday, mark, state) values (1, '2018-04-07', '2018-04-07 05:02:00', true);
insert into marks (user_id, wday, mark, state) values (1, '2018-04-07', '2018-04-07 09:03:00', false);
insert into marks (user_id, wday, mark, state) values (1, '2018-04-07', '2018-04-07 09:58:00', true);
insert into marks (user_id, wday, mark, state) values (1, '2018-04-07', '2018-04-07 14:05:00', false);

insert into marks (user_id, wday, mark, state) values (2, '2018-04-12', '2018-04-12 12:02:00', true);
insert into marks (user_id, wday, mark, state) values (2, '2018-04-12', '2018-04-12 16:03:00', false);
insert into marks (user_id, wday, mark, state) values (2, '2018-04-12', '2018-04-12 16:58:00', true);
insert into marks (user_id, wday, mark, state) values (2, '2018-04-12', '2018-04-12 21:05:00', false);