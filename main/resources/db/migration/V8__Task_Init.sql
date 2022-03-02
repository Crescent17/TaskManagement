create table task
(
    task_id     bigint not null primary key default (nextval('task_sequence')),
    employee_id bigint,
    explanation varchar(255),
    foreign key (employee_id) references employee (employee_id)
);