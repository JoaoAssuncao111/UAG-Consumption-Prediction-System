create table prediction
(
    id              int not null primary key,
    prediction_type varchar(50)
);

create table location
(
    id          serial primary key,
    observation varchar(500)       not null,
    name        varchar(50) unique not null,
    distance    float

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
    date_hour     timestamp primary key,
    prediction_id integer not null,
    min_value     float   not null,
    max_value     float   not null

);

create table level
(
    id             serial primary key,
    date_hour      date,
    prediction_id  integer not null,
    gas_level      float   not null,
    location       integer not null references Location (id),
    deposit_number integer not null,
    counter        bigint  not null,
    CONSTRAINT unique_level_entry UNIQUE (date_hour, prediction_id,gas_level,location,deposit_number,counter)
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
        CONSTRAINT unique_delivery_entry UNIQUE (company, load_amount,location_id,time_of_day,date_hour)
    );

