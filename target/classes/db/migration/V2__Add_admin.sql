insert into employee(first_name, second_name, role, username, password, active)
    values ('admin', 'admin', 'ADMIN', 'admin', 'pass', true);

create extension if not exists pgcrypto;

update employee set password = crypt(password, gen_salt('bf', 8));