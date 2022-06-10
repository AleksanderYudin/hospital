insert into employee(first_name, second_name, role, qualification, username, password, active)
    values ('admin', 'admin', 'ADMIN', 'admin', 'admin', 'pass', true);


insert into employee(first_name, second_name, role, qualification, username, password, active)
    values ('Иван', 'Иванов', 'DOCTOR', 'терапевт', 'ivanov', 'pass', true);


insert into employee(first_name, second_name, role, qualification, username, password, active)
    values ('Пётр', 'Петров', 'DOCTOR', 'педиатр', 'petrov', 'pass', true);


insert into employee(first_name, second_name, role, qualification, username, password, active)
    values ('Семён', 'Семёнов', 'DOCTOR', 'хирург', 'semenov', 'pass', true);


create extension if not exists pgcrypto;
update employee set password = crypt(password, gen_salt('bf', 8));