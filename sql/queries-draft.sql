select * from temperature WHERE date_hour BETWEEN :startDate AND :endDate AND location = :location;


DELETE FROM level AS l1
WHERE consumption = 0
  AND EXISTS (
    SELECT 1
    FROM level AS l2
    WHERE l1.date_hour = l2.date_hour
      AND l2.consumption <> 0
);

delete  from level;
ALTER TABLE location RENAME COLUMN modulo_treino TO training;
insert into prediction
values ('11','1 Day Algorithm Prediction'),
       ('12','2 Day Algorithm Prediction'),
       ('13','3 Day Algorithm Prediction'),
       ('14','4 Day Algorithm Prediction'),
       ('15','5 Day Algorithm Prediction'),
       ('16','6 Day Algorithm Prediction'),
       ('17','7 Day Algorithm Prediction');



ALTER TABLE location
    ADD modulo_treino json;

ALTER TABLE delivery
    ADD is_planned boolean;

update location set modulo_treino = :training where id = :id;

SELECT DISTINCT ON (date_hour) *
FROM temperature
WHERE date_hour >= '2023-07-10'::date
  AND date_hour <= '2023-07-11'::date + INTERVAL '1 day'
and location = 21
ORDER BY date_hour, prediction_id;

DELETE FROM level
WHERE prediction_id BETWEEN 11 AND 17;

SELECT *
                FROM temperature
                WHERE date_hour >= '2023-07-10'::date
                  AND date_hour <= '2023-07-11'::date + INTERVAL '1 day'
                and location = :location
                ORDER BY date_hour, prediction_id;


SELECT DISTINCT ON (deposit_number) * from level where location = :location
