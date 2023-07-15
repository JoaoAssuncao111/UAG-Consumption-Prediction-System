create table prediction
(
    id              int not null primary key,
    prediction_type varchar(50),
    prediction_agent varchar(50)
);

create table location
(
    id          serial primary key,
    observation varchar(2000)      not null,
    name        varchar(50) unique not null,
    distance    float,
    latitude    double precision,
    longitude   double precision,
    training json

);

create table company
(
    id           serial primary key,
    name         varchar(50) unique not null,
    market_share float              not null,
    daily_fleet  int                not null

);

create table temperature
(
    id            serial primary key,
    date_hour     date,
    location      integer not null references Location (id),
    prediction_id integer not null references prediction (id),
    min_value     float   not null,
    max_value     float   not null,
    CONSTRAINT unique_temperature_entry UNIQUE (date_hour, location, prediction_id, min_value,max_value)

);

create table humidity
(
    id            serial primary key,
    date_hour     timestamp,
    location      integer          not null references Location (id),
    prediction_id integer          not null references prediction (id),
    value         double precision not null,
    CONSTRAINT unique_humidity_entry UNIQUE (date_hour, location, prediction_id, value)
);

create table level
(
    id             serial primary key,
    date_hour      date,
    prediction_id  integer not null references prediction (id),
    gas_level      float   not null,
    location       integer not null references Location (id),
    deposit_number integer not null,
    counter        bigint  not null,
    consumption    double precision,
    CONSTRAINT unique_level_entry UNIQUE (date_hour, prediction_id, gas_level, location, deposit_number, counter,
                                          consumption)
);

create table delivery
(
    id          serial primary key,
    company     integer not null,
    load_amount float   not null,
    location_id integer not null references Location (id),
    time_of_day varchar(10) check ( time_of_day in ('M', 'T', 'M/T', '')),
    date_hour   date    not null,
    is_planned boolean,
    CONSTRAINT unique_delivery_entry UNIQUE (company, load_amount, location_id, time_of_day, date_hour)
);

insert into prediction
values ('99', 'dummy'),
       ('0', 'Real'),
       ('1', '1 Day IPMA Prediction'),
       ('2', '2 Day IPMA Prediction'),
       ('3', '3 Day IPMA Prediction'),
       ('4', '4 Day IPMA Prediction'),
       ('5', '5 Day IPMA Prediction'),
       ('6', '6 Day IPMA Prediction'),
       ('7', '7 Day IPMA Prediction'),
       ('11','1 Day Algorithm Prediction'),
       ('12','2 Day Algorithm Prediction'),
       ('13','3 Day Algorithm Prediction'),
       ('14','4 Day Algorithm Prediction'),
       ('15','5 Day Algorithm Prediction'),
       ('16','6 Day Algorithm Prediction'),
       ('17','7 Day Algorithm Prediction')

