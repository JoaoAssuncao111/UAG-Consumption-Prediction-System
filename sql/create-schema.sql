create table prediction
(
    id              int not null primary key,
    prediction_type varchar(50)
);

create table location
(
    id          serial primary key,
    observation varchar(2000)      not null,
    name        varchar(50) unique not null,
    distance    float,
    latitude    double precision,
    longitude   double precision

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
    max_value     float   not null

);

create table humidity
(
    date_hour     timestamp primary key,
    location      integer          not null references Location (id),
    prediction_id integer          not null references prediction (id),
    value         double precision not null

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
    CONSTRAINT unique_level_entry UNIQUE (date_hour, prediction_id, gas_level, location, deposit_number, counter,consumption)
);

create table planned_delivery
(
    id          serial primary key,
    company     integer not null,
    load_amount float   not null,
    location_id integer not null references Location (id),
    time_of_day varchar(10) check ( time_of_day in ('M', 'T', 'M/T', ''))
);

create table delivery
(
    id          serial primary key,
    company     integer not null,
    load_amount float   not null,
    location_id integer not null references Location (id),
    time_of_day varchar(10) check ( time_of_day in ('M', 'T', 'M/T', '')),
    date_hour   date    not null,
    CONSTRAINT unique_delivery_entry UNIQUE (company, load_amount, location_id, time_of_day, date_hour)
);

insert into prediction
values ('99', 'dummy'),
       ('0', 'Real'),
       ('1', '1 Day Prediction'),
       ('2', '2 Day Prediction'),
       ('3', '3 Day Prediction'),
       ('4', '4 Day Prediction'),
       ('5', '5 Day Prediction'),
       ('6', '6 Day Prediction'),
       ('7', '7 Day Prediction')

