create table car
(
    id serial primary key,
    name varchar not null unique,
	engine_id int,
	foreign key (engine_id) references engine(id)
);