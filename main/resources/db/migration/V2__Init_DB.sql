create table company(id bigint not null primary key default (nextval('s1')), name varchar(255) not null,
                     username varchar(255) not null unique, password varchar(255) not null);