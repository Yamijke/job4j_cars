alter table auto_post
add column car_id int,
add foreign key (car_id) REFERENCES car(id);