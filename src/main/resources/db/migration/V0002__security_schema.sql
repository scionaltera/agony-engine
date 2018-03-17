CREATE EXTENSION IF NOT EXISTS "citext";

create table users(
  username citext not null primary key,
  password citext not null,
  enabled boolean not null
);

create table authorities (
  username citext not null,
  authority citext not null,
  constraint fk_authorities_users foreign key(username) references users(username)
);

create unique index ix_auth_username on authorities (username, authority);