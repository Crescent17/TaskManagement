create table company
(
    company_id bigint       not null primary key default (nextval('company_sequence')),
    name       varchar(255) not null,
    username   varchar(255) not null unique,
    password   varchar(255) not null
);