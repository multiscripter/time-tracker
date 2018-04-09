create table if not exists users (
    id int unsigned not null auto_increment primary key,
    login varchar(64) not null unique,
    pass char(32) not null unique
);

create table if not exists tokens (
    user_id int unsigned not null unique,
    token char(32) not null unique,
    wday date default null
);

create table if not exists marks (
    user_id int unsigned not null,
    token char(32) not null,
    wday date not null,
    mark datetime not null default current_timestamp,
    state boolean not null
);