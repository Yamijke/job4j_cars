CREATE TABLE history_owners (
   id serial PRIMARY KEY,
   car_id int not null REFERENCES car(id),
   owner_id int not null REFERENCES owners(id),
   history_id int NOT NULL REFERENCES history(id),
   UNIQUE (car_id, owner_id, history_id)
);