-- Remove tables
if OBJECT_ID('dbo.APP_USER', 'U') is not null
    drop table APP_USER;
if OBJECT_ID('dbo.APP_ROLE', 'U') is not null
    drop table APP_ROLE;
if OBJECT_ID('dbo.USER_ROLE', 'U') is not null
    drop table USER_ROLE;
if OBJECT_ID('dbo.Persistent_Logins', 'U') is not null
    drop table Persistent_Logins;

go;

-- Create tables
create table APP_USER
(
    USER_ID             BIGINT not null IDENTITY(1, 1),
    USER_NAME           VARCHAR(50) not null,
    ENCRYTED_PASSWORD  VARCHAR(128) not null,
    ENABLED             BIT not null
);
alter table APP_USER
    add constraint APP_USER_PK primary key (USER_ID);
alter table APP_USER
    add constraint APP_USER_UK unique (USER_NAME);

create table APP_ROLE
(
    ROLE_ID   BIGINT not null,
    ROLE_NAME VARCHAR(30) not null
) ;
alter table APP_ROLE
    add constraint APP_ROLE_PK primary key (ROLE_ID);
alter table APP_ROLE
    add constraint APP_ROLE_UK unique (ROLE_NAME);

create table USER_ROLE
(
    ID      BIGINT not null IDENTITY (1, 1),
    USER_ID BIGINT not null,
    ROLE_ID BIGINT not null
);
alter table USER_ROLE
    add constraint USER_ROLE_PK primary key (ID);
alter table USER_ROLE
    add constraint USER_ROLE_UK unique (USER_ID, ROLE_ID);
alter table USER_ROLE
    add constraint USER_ROLE_FK1 foreign key (USER_ID)
        references APP_USER (USER_ID);
alter table USER_ROLE
    add constraint USER_ROLE_FK2 foreign key (ROLE_ID)
        references APP_ROLE (ROLE_ID);

-- Used by Spring Remember Me API.
CREATE TABLE Persistent_Logins (

                                   username varchar(64) not null,
                                   series varchar(64) not null,
                                   token varchar(64) not null,
                                   last_used Datetime not null,
                                   PRIMARY KEY (series)

);

insert into App_User (USER_ID, USER_NAME, ENCRYTED_PASSWORD, ENABLED)
values (2, 'dbuser1', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

insert into App_User (USER_ID, USER_NAME, ENCRYTED_PASSWORD, ENABLED)
values (1, 'dbadmin1', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

---

insert into app_role (ROLE_ID, ROLE_NAME)
values (1, 'ROLE_ADMIN');

insert into app_role (ROLE_ID, ROLE_NAME)
values (2, 'ROLE_USER');

---

insert into user_role (ID, USER_ID, ROLE_ID)
values (1, 1, 1);

insert into user_role (ID, USER_ID, ROLE_ID)
values (2, 1, 2);

insert into user_role (ID, USER_ID, ROLE_ID)
values (3, 2, 2);