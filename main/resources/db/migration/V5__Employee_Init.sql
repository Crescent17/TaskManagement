create table employee
(
    employee_id bigint       not null primary key default (nextval('employee_sequence')),
    name        varchar(255) not null,
    last_Name   varchar(255),
    username    varchar(255) not null unique,
    password    varchar(255) not null,
    company_name varchar(255) not null,
    company_id bigint not null,
    task varchar(255),
    foreign key (company_id) references company(company_id)
);