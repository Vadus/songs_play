# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table linked_account (
  id                            bigint not null,
  user_id                       bigint not null,
  provider_user_id              varchar(255),
  provider_key                  varchar(255),
  constraint pk_linked_account primary key (id)
);
create sequence linked_account_seq;

create table playlists (
  id                            bigint not null,
  name                          varchar(255),
  user_id                       bigint,
  created                       timestamp,
  constraint pk_playlists primary key (id)
);
create sequence playlists_seq;

create table playlist_songs (
  playlist_id                   bigint not null,
  song_id                       bigint not null,
  constraint pk_playlist_songs primary key (playlist_id,song_id)
);

create table playlist_tags (
  playlist_id                   bigint not null,
  tag_id                        bigint not null,
  constraint pk_playlist_tags primary key (playlist_id,tag_id)
);

create table security_role (
  id                            bigint not null,
  role_name                     varchar(255),
  constraint pk_security_role primary key (id)
);
create sequence security_role_seq;

create table songs (
  id                            bigint not null,
  pos                           integer,
  title                         varchar(255),
  url                           varchar(255),
  source_url                    varchar(255),
  source                        varchar(255),
  constraint pk_songs primary key (id)
);
create sequence songs_seq;

create table song_tags (
  song_id                       bigint not null,
  tag_id                        bigint not null,
  constraint pk_song_tags primary key (song_id,tag_id)
);

create table tags (
  id                            bigint not null,
  name                          varchar(255),
  constraint pk_tags primary key (id)
);
create sequence tags_seq;

create table token_action (
  id                            bigint not null,
  token                         varchar(255),
  target_user_id                bigint,
  type                          varchar(2),
  created                       timestamp,
  expires                       timestamp,
  constraint ck_token_action_type check (type in ('PR','EV')),
  constraint uq_token_action_token unique (token),
  constraint pk_token_action primary key (id)
);
create sequence token_action_seq;

create table users (
  id                            bigint not null,
  email                         varchar(255),
  name                          varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  last_login                    timestamp,
  active                        boolean,
  email_validated               boolean,
  constraint pk_users primary key (id)
);
create sequence users_seq;

create table users_security_role (
  users_id                      bigint not null,
  security_role_id              bigint not null,
  constraint pk_users_security_role primary key (users_id,security_role_id)
);

create table users_user_permission (
  users_id                      bigint not null,
  user_permission_id            bigint not null,
  constraint pk_users_user_permission primary key (users_id,user_permission_id)
);

create table user_permission (
  id                            bigint not null,
  value                         varchar(255),
  constraint pk_user_permission primary key (id)
);
create sequence user_permission_seq;

alter table linked_account add constraint fk_linked_account_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_linked_account_user_id on linked_account (user_id);

alter table playlists add constraint fk_playlists_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;
create index ix_playlists_user_id on playlists (user_id);

alter table playlist_songs add constraint fk_playlist_songs_playlists foreign key (playlist_id) references playlists (id) on delete restrict on update restrict;
create index ix_playlist_songs_playlists on playlist_songs (playlist_id);

alter table playlist_songs add constraint fk_playlist_songs_songs foreign key (song_id) references songs (id) on delete restrict on update restrict;
create index ix_playlist_songs_songs on playlist_songs (song_id);

alter table playlist_tags add constraint fk_playlist_tags_playlists foreign key (playlist_id) references playlists (id) on delete restrict on update restrict;
create index ix_playlist_tags_playlists on playlist_tags (playlist_id);

alter table playlist_tags add constraint fk_playlist_tags_tags foreign key (tag_id) references tags (id) on delete restrict on update restrict;
create index ix_playlist_tags_tags on playlist_tags (tag_id);

alter table song_tags add constraint fk_song_tags_songs foreign key (song_id) references songs (id) on delete restrict on update restrict;
create index ix_song_tags_songs on song_tags (song_id);

alter table song_tags add constraint fk_song_tags_tags foreign key (tag_id) references tags (id) on delete restrict on update restrict;
create index ix_song_tags_tags on song_tags (tag_id);

alter table token_action add constraint fk_token_action_target_user_id foreign key (target_user_id) references users (id) on delete restrict on update restrict;
create index ix_token_action_target_user_id on token_action (target_user_id);

alter table users_security_role add constraint fk_users_security_role_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_users_security_role_users on users_security_role (users_id);

alter table users_security_role add constraint fk_users_security_role_security_role foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;
create index ix_users_security_role_security_role on users_security_role (security_role_id);

alter table users_user_permission add constraint fk_users_user_permission_users foreign key (users_id) references users (id) on delete restrict on update restrict;
create index ix_users_user_permission_users on users_user_permission (users_id);

alter table users_user_permission add constraint fk_users_user_permission_user_permission foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;
create index ix_users_user_permission_user_permission on users_user_permission (user_permission_id);


# --- !Downs

alter table linked_account drop constraint if exists fk_linked_account_user_id;
drop index if exists ix_linked_account_user_id;

alter table playlists drop constraint if exists fk_playlists_user_id;
drop index if exists ix_playlists_user_id;

alter table playlist_songs drop constraint if exists fk_playlist_songs_playlists;
drop index if exists ix_playlist_songs_playlists;

alter table playlist_songs drop constraint if exists fk_playlist_songs_songs;
drop index if exists ix_playlist_songs_songs;

alter table playlist_tags drop constraint if exists fk_playlist_tags_playlists;
drop index if exists ix_playlist_tags_playlists;

alter table playlist_tags drop constraint if exists fk_playlist_tags_tags;
drop index if exists ix_playlist_tags_tags;

alter table song_tags drop constraint if exists fk_song_tags_songs;
drop index if exists ix_song_tags_songs;

alter table song_tags drop constraint if exists fk_song_tags_tags;
drop index if exists ix_song_tags_tags;

alter table token_action drop constraint if exists fk_token_action_target_user_id;
drop index if exists ix_token_action_target_user_id;

alter table users_security_role drop constraint if exists fk_users_security_role_users;
drop index if exists ix_users_security_role_users;

alter table users_security_role drop constraint if exists fk_users_security_role_security_role;
drop index if exists ix_users_security_role_security_role;

alter table users_user_permission drop constraint if exists fk_users_user_permission_users;
drop index if exists ix_users_user_permission_users;

alter table users_user_permission drop constraint if exists fk_users_user_permission_user_permission;
drop index if exists ix_users_user_permission_user_permission;

drop table if exists linked_account;
drop sequence if exists linked_account_seq;

drop table if exists playlists;
drop sequence if exists playlists_seq;

drop table if exists playlist_songs;

drop table if exists playlist_tags;

drop table if exists security_role;
drop sequence if exists security_role_seq;

drop table if exists songs;
drop sequence if exists songs_seq;

drop table if exists song_tags;

drop table if exists tags;
drop sequence if exists tags_seq;

drop table if exists token_action;
drop sequence if exists token_action_seq;

drop table if exists users;
drop sequence if exists users_seq;

drop table if exists users_security_role;

drop table if exists users_user_permission;

drop table if exists user_permission;
drop sequence if exists user_permission_seq;

