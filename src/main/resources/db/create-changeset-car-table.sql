CREATE TABLE cars
(
    id              UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    brand           VARCHAR(256)     NOT NULL,
    model           VARCHAR(256)     NOT NULL,
    body_type       VARCHAR(256)     NOT NULL,
    engine_capacity DOUBLE PRECISION NOT NULL,
    fuel_type       VARCHAR(256)     NOT NULL
);