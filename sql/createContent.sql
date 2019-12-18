IF EXISTS(select * from sys.views WHERE NAME = 'current_status_orders_view')
    DROP VIEW current_status_orders_view;
GO;

if OBJECT_ID('dbo.TP_TOKEN_BLACKLIST', 'U') is not null
    drop table TP_TOKEN_BLACKLIST;
if OBJECT_ID('dbo.TP_ORDER_HISTORY', 'U') is not null
    drop table TP_ORDER_HISTORY;
if OBJECT_ID('dbo.TP_STATUS', 'U') is not null
    drop table TP_STATUS;
if OBJECT_ID('dbo.TP_ORDER', 'U') is not null
    drop table TP_ORDER;
if OBJECT_ID('dbo.USER_ROLE', 'U') is not null
    drop table USER_ROLE;
if OBJECT_ID('dbo.TP_USER', 'U') is not null
    drop table TP_USER;
if OBJECT_ID('dbo.TP_ROLE', 'U') is not null
    drop table TP_ROLE;

--

go;

-- Create tables
create table TP_USER
(
    USER_ID             BIGINT not null IDENTITY(1, 1),
    USER_NAME           VARCHAR(50) not null,
    FIRST_NAME          VARCHAR(128) not null,
    LAST_NAME           VARCHAR(128) not null,
    PHONE_NUMBER        VARCHAR(12) not null,
    ENCRYTED_PASSWORD   VARCHAR(128) not null,
    ENABLED             BIT not null
);
alter table TP_USER
    add constraint TP_USER_PK primary key (USER_ID);
alter table TP_USER
    add constraint TP_USER_UK unique (USER_NAME);

create table TP_ROLE
(
    ROLE_ID   BIGINT not null,
    ROLE_NAME VARCHAR(30) not null
) ;
alter table TP_ROLE
    add constraint APP_ROLE_PK primary key (ROLE_ID);
alter table TP_ROLE
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
        references TP_USER (USER_ID);
alter table USER_ROLE
    add constraint USER_ROLE_FK2 foreign key (ROLE_ID)
        references TP_ROLE (ROLE_ID);

create table TP_ORDER(
    ID                       BIGINT not null IDENTITY (1, 1),
    CLIENT_ID                BIGINT not null,
    ADDRESS_FROM             VARCHAR(600) not null,
    ADDRESS_TO               VARCHAR(600) not null,
    MAP_COORDINATES          VARCHAR(30),
    APPOINTMENT_DATETIME     DATETIME not null,
    CLIENT_COMMENTS          VARCHAR(max)
);

alter table TP_ORDER
    add constraint TP_ORDER_PK primary key (ID);
alter table TP_ORDER
    add constraint TP_ORDER_CLIENT_FK foreign key (CLIENT_ID)
        references TP_USER(USER_ID);

create table TP_STATUS(
    ID            BIGINT not null,
    TITLE_KEY     VARCHAR(50)
);

alter table TP_STATUS
    add constraint TP_STATUS_PK primary key (ID);

create table TP_ORDER_HISTORY(
     ID                 BIGINT not null IDENTITY(1, 1),
     ORDER_ID           BIGINT not null,
     STATUS_ID          BIGINT not null,
     DRIVER_ID          BIGINT,
     UPDATE_DATETIME    DATETIME not null ,
     UPDATED_BY         BIGINT not null
);

alter table TP_ORDER_HISTORY
    add constraint TP_ORDER_HISTORY_PK primary key (ID);
alter table TP_ORDER_HISTORY
    add constraint TP_ORDER_HISTORY_ORDER_FK foreign key (ORDER_ID)
        references TP_ORDER(ID);
alter table TP_ORDER_HISTORY
    add constraint TP_ORDER_HISTORY_STATUS_FK foreign key (STATUS_ID)
        references TP_STATUS(ID);
alter table TP_ORDER_HISTORY
    add constraint TP_ORDER_HISTORY_DRIVER_FK foreign key (DRIVER_ID)
        references TP_USER(USER_ID);
alter table TP_ORDER_HISTORY
    add constraint TP_ORDER_HISTORY_UPDATED_FK foreign key (UPDATED_BY)
        references TP_USER(USER_ID);

create table TP_TOKEN_BLACKLIST(
    ID                  BIGINT not null IDENTITY(1, 1),
    TOKEN_VALUE         VARCHAR(max),
    EXPIRE_DATETIME     DATETIME not null
);

alter table TP_TOKEN_BLACKLIST
    add constraint TP_TOKEN_BLACKLIST_PK primary key (ID);

go;

SET IDENTITY_INSERT TP_User ON

insert into Tp_User (USER_ID, USER_NAME, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ENCRYTED_PASSWORD, ENABLED)
values (2, 'dbuser1', 'DB', 'USER', '', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

insert into Tp_User (USER_ID, USER_NAME, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ENCRYTED_PASSWORD, ENABLED)
values (1, 'dbadmin1', 'DBA', 'ADMIN', '', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

SET IDENTITY_INSERT Tp_User OFF

---

insert into tp_role (ROLE_ID, ROLE_NAME)
values (1, 'ROLE_ADMIN');

insert into tp_role (ROLE_ID, ROLE_NAME)
values (2, 'ROLE_USER');

insert into tp_role (ROLE_ID, ROLE_NAME)
values (3, 'ROLE_DRIVER');

---

SET IDENTITY_INSERT user_role ON

insert into user_role (ID, USER_ID, ROLE_ID)
values (1, 1, 1);

insert into user_role (ID, USER_ID, ROLE_ID)
values (2, 1, 2);

insert into user_role (ID, USER_ID, ROLE_ID)
values (3, 2, 2);

SET IDENTITY_INSERT user_role OFF

INSERT INTO tp_status VALUES (1, 'tp.status.opened');
INSERT INTO tp_status VALUES (2, 'tp.status.assigned');
INSERT INTO tp_status VALUES (3, 'tp.status.cancelled');
INSERT INTO tp_status VALUES (4, 'tp.status.completed');

go;