create sequence todo_id_seq start with 1 increment by 1;
create sequence msg_id_seq start with 1 increment by 1;

create table todos
(
    id         bigint default todo_id_seq.nextval,
    content    varchar(1024) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table messages
(
    id         bigint default msg_id_seq.nextval,
    content    varchar(1024) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
