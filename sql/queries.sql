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


ALTER TABLE location
    ADD modulo_treino json;

update location set modulo_treino = :training where id = :id;

SELECT DISTINCT ON (date_hour) *
FROM temperature
WHERE date_hour >= '2023-05-01'::date
  AND date_hour <= '2023-07-07'::date + INTERVAL '1 day'
and location = 21
ORDER BY date_hour, prediction_id;