create view vcpu_time as
SELECT 
    COALESCE(wait_event_type, 'CPU / CPU Waiting') AS tipo_espera,
    COUNT(*) AS total_sesiones
FROM pg_stat_activity
WHERE state = 'active'
  AND pid != pg_backend_pid()
GROUP BY COALESCE(wait_event_type, 'CPU / CPU Waiting')
ORDER BY total_sesiones DESC;

/*
INSERT INTO ash_history (snapshot_time, pid, wait_event_type, wait_event, query_id)
SELECT 
    clock_timestamp(),
    pid,
    COALESCE(wait_event_type, 'CPU'),
    wait_event,
    query_id
FROM pg_stat_activity
WHERE state = 'active';
*/

create view vwait_events as
SELECT 
    pid,
    usename AS usuario,
    datname AS base_datos,
    state AS estado,
    wait_event_type,
    wait_event,
    query AS consulta_actual,
    now() - query_start AS duracion
FROM pg_stat_activity
WHERE state != 'idle' 
  AND pid != pg_backend_pid();
