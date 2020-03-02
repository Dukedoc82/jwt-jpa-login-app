IF EXISTS(select * from sys.views WHERE NAME = 'current_status_orders_view')
    DROP VIEW current_status_orders_view;
GO;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_user_seq' AND TYPE='SO')
    DROP SEQUENCE tp_user_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_role_seq' AND TYPE='SO')
    DROP SEQUENCE tp_role_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_user_role_seq' AND TYPE='SO')
    DROP SEQUENCE tp_user_role_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_order_seq' AND TYPE='SO')
    DROP SEQUENCE tp_order_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_status_seq' AND TYPE='SO')
    DROP SEQUENCE tp_status_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_order_history_seq' AND TYPE='SO')
    DROP SEQUENCE tp_order_history_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_token_blacklist_seq' AND TYPE='SO')
    DROP SEQUENCE tp_token_blacklist_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_activation_token_seq' AND TYPE='SO')
    DROP SEQUENCE tp_activation_token_seq;
IF EXISTS (SELECT * FROM sys.sequences WHERE NAME = N'tp_user_mail_settings_seq' AND TYPE='SO')
    DROP SEQUENCE tp_user_mail_settings_seq;

if OBJECT_ID('dbo.TP_USER_MAIL_SETTINGS', 'U') is not null
    drop table TP_USER_MAIL_SETTINGS;
if OBJECT_ID('dbo.TP_ACTIVATION_TOKEN', 'U') is not null
    drop table TP_ACTIVATION_TOKEN;
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
    USER_ID             BIGINT not null,
    USER_NAME           VARCHAR(50) not null,
    FIRST_NAME          VARCHAR(128) not null,
    LAST_NAME           VARCHAR(128) not null,
    PHONE_NUMBER        VARCHAR(20) not null,
    ENCRYTED_PASSWORD   VARCHAR(128) not null,
    ENABLED             BIT not null
);
alter table TP_USER
    add constraint TP_USER_PK primary key (USER_ID);
alter table TP_USER
    add constraint TP_USER_UK unique (USER_NAME);

create sequence tp_user_seq START WITH 1 INCREMENT BY 1;

create table TP_ROLE
(
    ROLE_ID   BIGINT not null,
    ROLE_NAME VARCHAR(30) not null
) ;
alter table TP_ROLE
    add constraint APP_ROLE_PK primary key (ROLE_ID);
alter table TP_ROLE
    add constraint APP_ROLE_UK unique (ROLE_NAME);

create sequence tp_role_seq START WITH 1 INCREMENT BY 1;

create table USER_ROLE
(
    ID      BIGINT not null,
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

create sequence tp_user_role_seq START WITH 1 INCREMENT BY 1;

create table TP_ORDER(
    ID                       BIGINT not null,
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

create sequence tp_order_seq START WITH 1 INCREMENT BY 1;

create table TP_STATUS(
    ID            BIGINT not null,
    TITLE_KEY     VARCHAR(50)
);

alter table TP_STATUS
    add constraint TP_STATUS_PK primary key (ID);

create sequence tp_status_seq START WITH 1 INCREMENT BY 1;

create table TP_ORDER_HISTORY(
     ID                 BIGINT not null,
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

create sequence tp_order_history_seq START WITH 1 INCREMENT BY 1;

create table TP_TOKEN_BLACKLIST(
    ID                  BIGINT not null,
    TOKEN_VALUE         VARCHAR(max),
    EXPIRE_DATETIME     DATETIME not null
);

alter table TP_TOKEN_BLACKLIST
    add constraint TP_TOKEN_BLACKLIST_PK primary key (ID);

go;

create sequence tp_token_blacklist_seq START WITH 1 INCREMENT BY 1;

create table TP_ACTIVATION_TOKEN(
    ID                  BIGINT not null,
    USER_ID             BIGINT not null,
    TOKEN_VALUE         VARCHAR(max),
    EXPIRE_DATETIME     DATETIME not null
);

alter table TP_ACTIVATION_TOKEN
    add constraint TP_ACTIVATION_TOKEN_PK primary key (ID);
alter table TP_ACTIVATION_TOKEN
    add constraint TP_ACTIVATION_TOKEN_USER_FK foreign key (USER_ID)
        references TP_USER(USER_ID);

create sequence tp_activation_token_seq START WITH 1 INCREMENT BY 1;

create table TP_USER_MAIL_SETTINGS(
    ID BIGINT not null,
    USER_ID BIGINT not null,
    NEW_ORDER BIT not null,
    ASSIGN_ORDER BIT not null,
    CANCEL_ORDER BIT not null,
    COMPLETE_ORDER BIT not null,
    REFUSE_ORDER BIT not null
);

alter table TP_USER_MAIL_SETTINGS
    add constraint TP_USER_MAIL_SETTINGS_PK primary key (ID);
alter table TP_USER_MAIL_SETTINGS
    add constraint TP_USER_MAIL_SETTINGS_USER_FK foreign key (USER_ID)
        references TP_USER(USER_ID);

create sequence tp_user_mail_settings_seq START WITH 1 INCREMENT BY 1;

insert into Tp_User (USER_ID, USER_NAME, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ENCRYTED_PASSWORD, ENABLED)
values (NEXT VALUE FOR tp_user_seq, 'dbuser1', 'DB', 'USER', '', '$2a$10$UVjqooXsjxyWoqDDTRBQF.fCbNNNbMYMqVdVVhizoKWaa1aKHPfeW', 1);

insert into Tp_User (USER_ID, USER_NAME, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ENCRYTED_PASSWORD, ENABLED)
values (NEXT VALUE FOR tp_user_seq, 'dbadmin1', 'DBA', 'ADMIN', '', '$2a$10$UVjqooXsjxyWoqDDTRBQF.fCbNNNbMYMqVdVVhizoKWaa1aKHPfeW', 1);

---

insert into tp_role (ROLE_ID, ROLE_NAME)
values (NEXT VALUE FOR tp_role_seq, 'ROLE_ADMIN');

insert into tp_role (ROLE_ID, ROLE_NAME)
values (NEXT VALUE FOR tp_role_seq, 'ROLE_USER');

insert into tp_role (ROLE_ID, ROLE_NAME)
values (NEXT VALUE FOR tp_role_seq, 'ROLE_DRIVER');

---

insert into user_role (ID, USER_ID, ROLE_ID)
values (NEXT VALUE FOR tp_user_role_seq, 1, 2);

insert into user_role (ID, USER_ID, ROLE_ID)
values (NEXT VALUE FOR tp_user_role_seq, 2, 1);

INSERT INTO tp_status VALUES (NEXT VALUE FOR tp_status_seq, 'tp.status.opened');
INSERT INTO tp_status VALUES (NEXT VALUE FOR tp_status_seq, 'tp.status.assigned');
INSERT INTO tp_status VALUES (NEXT VALUE FOR tp_status_seq, 'tp.status.cancelled');
INSERT INTO tp_status VALUES (NEXT VALUE FOR tp_status_seq, 'tp.status.completed');

go;

INSERT INTO tp_user_mail_settings VALUES (NEXT VALUE FOR tp_user_mail_settings_seq, 1, 0, 0, 0, 0, 0);
INSERT INTO tp_user_mail_settings VALUES (NEXT VALUE FOR tp_user_mail_settings_seq, 2, 0, 0, 0, 0, 0);

go;