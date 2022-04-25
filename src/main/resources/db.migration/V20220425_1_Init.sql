create table role(
    id serial primary key,
    role_name varchar(255) not null unique
);

create table users(
    id serial primary key,
    role_id bigint not null,
    email varchar(255) not null,
    password varchar(255) not null,
    name varchar(255) not null,
    surname varchar(255) not null,
    phone varchar(255) not null,
    is_active boolean default false,
    is_locked boolean default false,
    constraint fk_role_id foreign key (role_id) references role (id)
);

create table pin_code(
    id serial primary key,
    user_id bigint not null unique,
    pin_code int not null,
    created_date timestamp not null
);