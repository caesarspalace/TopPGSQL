drop view v_lock_recursive;

create or replace view v_lock_recursive as
WITH RECURSIVE lock_tree AS (
-- Nodos raíz: Procesos bloqueadores iniciales
SELECT
blocking.pid AS blocking_pid,
blocked.pid AS blocked_pid,
1 AS depth,
ARRAY[blocking.pid, blocked.pid] AS path
FROM pg_locks blocked_locks
JOIN pg_stat_activity blocked ON blocked.pid = blocked_locks.pid
JOIN pg_locks blocking_locks
ON blocking_locks.locktype = blocked_locks.locktype
AND blocking_locks.database IS NOT DISTINCT FROM blocked_locks.database
AND blocking_locks.relation IS NOT DISTINCT FROM blocked_locks.relation
AND blocking_locks.page IS NOT DISTINCT FROM blocked_locks.page
AND blocking_locks.tuple IS NOT DISTINCT FROM blocked_locks.tuple
AND blocking_locks.transactionid IS NOT DISTINCT FROM blocked_locks.transactionid
AND blocking_locks.pid != blocked_locks.pid
JOIN pg_stat_activity blocking ON blocking.pid = blocking_locks.pid
WHERE NOT blocked_locks.granted AND blocking_locks.granted
UNION ALL
-- Recursión: Encadenar bloqueos en cascada
SELECT
lt.blocked_pid AS blocking_pid,
blocked.pid AS blocked_pid,
lt.depth + 1 AS depth,
lt.path || blocked.pid
FROM lock_tree lt
JOIN pg_locks blocked_locks ON blocked_locks.pid != lt.blocked_pid
JOIN pg_stat_activity blocked ON blocked.pid = blocked_locks.pid
JOIN pg_locks blocking_locks
ON blocking_locks.pid = lt.blocked_pid
AND blocking_locks.locktype = blocked_locks.locktype
AND blocking_locks.granted
WHERE NOT blocked_locks.granted AND NOT (blocked.pid = ANY(lt.path))
)
SELECT
REPEAT(' └─ ', lt.depth - 1) || lt.blocked_pid::text AS visual_tree,
lt.blocking_pid, lt.blocked_pid,
COALESCE(blocker_act.usename::text, 'desconocido') AS usuario_bloqueador, -- Casteo explícito a String/Text
COALESCE(blocked_act.usename::text, 'desconocido') AS usuario_bloqueado,
blocked_act.wait_event_type, blocked_act.wait_event,
ROUND(EXTRACT(EPOCH FROM (clock_timestamp() - blocked_act.state_change))::numeric, 2) AS tiempo_espera_seg,
blocker_act.query AS consulta_bloqueadora,
blocked_act.query AS consulta_bloqueada
FROM lock_tree lt
JOIN pg_stat_activity blocker_act ON blocker_act.pid = lt.blocking_pid
JOIN pg_stat_activity blocked_act ON blocked_act.pid = lt.blocked_pid
